# Bitbucket REST client

This plugin integrates Bitbucket Cloud with pull request branches analyses.

## Install
Download and plugin in ${SONAR_HOME}/extensions/plugins

## Setup

In administration panel you can setup credentials to use for bitbucket access.

![image](images/config_panel.png)

You can enable or not the approval of bitbucket pull request if the quality gate is passed.
The scanner recognise the pull request URL if run on Jenkins with [bitbucket plugin](https://github.com/jenkinsci/bitbucket-branch-source-plugin).
Otherwise you need to setup the following properties:

|Property|Default value|Since|Description|
|---|---|---|---|
|sonar.bitbucket.branch.pullrequest.url||1.0.0|The URL of pull request on bitbucket. https://bitbucket.org/{workspace}/{repo_slug}/pull-requests/{pull_request_id}|

Supported methods are:
- Bitbucket App Password

### App Password

Create an App Password from your personal settings after login with bitbucket account

`https://bitbucket.org/account/settings/app-passwords/`

Create one and put any label you want. This plugin requires write grant for pull request.

![image](images/apppassword_settings.png)

## Build
It is maven base so all you need is Java 8 and Maven >= 3.3 installed in you system.

`mvn clean verify`

## Maven reference
You can add scan maven central to get a download url put in a custom sonar update center JSON file
```xml
	<groupId>com.github.nfalco79</groupId>
	<artifactId>sonar-bitbucket-cloud</artifactId>
	<version>1.0.0</version>
```