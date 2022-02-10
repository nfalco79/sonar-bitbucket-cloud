/*
 * Copyright (C) 2022 Falco Nikolas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package com.github.nfalco79.sonar.plugins.bitbucket.cloud;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.ce.posttask.Analysis;
import org.sonar.api.ce.posttask.Branch;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.ce.posttask.QualityGate.Status;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.github.nfalco79.bitbucket.client.BitbucketCloudClient;
import com.github.nfalco79.bitbucket.client.Credentials.CredentialsBuilder;

@ComputeEngineSide
public class PullRequestPostAnalysisTask implements PostProjectAnalysisTask {

    private static final Logger LOGGER = Loggers.get(PullRequestPostAnalysisTask.class);

    private final BitbucketCloudProperties config;

    public PullRequestPostAnalysisTask(Configuration configuration) {
        this.config = new BitbucketCloudProperties(configuration);
    }

    @Override
    public String getDescription() {
        return "Pull Request Decoration";
    }

    @Override
    public void finished(Context context) {
        if (!config.approve()) {
            LOGGER.trace("Skip pull request approval as per configuration.");
            return;
        }
        ProjectAnalysis projectAnalysis = context.getProjectAnalysis();
        Optional<Branch> optionalPullRequest = projectAnalysis.getBranch().filter(branch -> Branch.Type.PULL_REQUEST == branch.getType());
        if (!optionalPullRequest.isPresent()) {
            LOGGER.trace("Current analysis is not for a Pull Request. Task being skipped");
            return;
        }

        Optional<String> optionalBranchName = optionalPullRequest.get().getName();
        if (!optionalBranchName.isPresent()) {
            LOGGER.warn("No branch name has been submitted with the Pull Request. Approval will be skipped");
            return;
        }

        Optional<Analysis> optionalAnalysis = projectAnalysis.getAnalysis();
        if (!optionalAnalysis.isPresent()) {
            LOGGER.warn("No analysis results were created for this project analysis. This is likely to be due to an earlier failure");
            return;
        }

        QualityGate qualityGate = projectAnalysis.getQualityGate();
        if (null == qualityGate) {
            LOGGER.warn("No quality gate was found on the analysis, so no approvals are available");
            return;
        }

        Map<String, String> properties = projectAnalysis.getScannerContext().getProperties();
        String prURL = properties.get(BitbucketCloudProperties.PULL_REQUEST_BB_URL);
        if (prURL == null) {
            LOGGER.warn("No pull request URL has been submitted with the Pull Request. Approval will be skipped");
            return;
        }
        URL url;
        try {
            url = validateURL(prURL);
        } catch (MalformedURLException e) {
            LOGGER.warn("Pull request URL submitted is invalid: " + e.getMessage() + " Approval will be skipped");
            return;
        }

        int prId = Integer.valueOf(optionalBranchName.get());
        String[] segments = Arrays.stream(url.getPath().split("/")).filter(s -> s != null && !s.isEmpty()).toArray(String[]::new);
        String project = segments[0];
        String repoSlug = segments[1];

        // perform REST call for approval
        try (BitbucketCloudClient client = new BitbucketCloudClient(CredentialsBuilder.appPassword(config.username(), config.secret()))) {
            boolean isApproved = client.isPullRequestApproved(project, repoSlug, prId);
            boolean hasToApprove = Status.OK == qualityGate.getStatus();
            if (isApproved != hasToApprove) {
                client.setPullRequestApproval(project, repoSlug, prId, hasToApprove);
            }
        } catch (IOException e) {
            LOGGER.error("Fails to approve pull request " + prId, e);
        }
    }

    private URL validateURL(String pullRequestURL) throws MalformedURLException {
        URL url = new URL(pullRequestURL);
        if (!"bitbucket.org".equals(url.getHost())) {
            throw new MalformedURLException("Invalid host " + url.getHost());
        }
        if (url.getPath().split("/").length <= 2) {
            throw new MalformedURLException("Invalid paths, URL should be https://bitbucket.org/{project}/{slug}/...");
        }
        return url;
    }

}