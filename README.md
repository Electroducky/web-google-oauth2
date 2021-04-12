# web-google-oauth2

Web app that supports authorization via Google Account.

Naive implementation of OAuth 2.0 protocol client.

## Configuration

To use Google API, service must be provided with a `client_secret` configuration file.

**If you already have configuration file** pointing to server you plan running 
application on (e.g. localhost) you can change `app.web.oauthcallbackPath` property to 
use already specified redirectUri path.

To acquire new configuration file:
1) visit the Google [API Console](https://console.developers.google.com/)
2) create new project or select existing one
3) create Application Consent screen, add People API scope, add yourself as tester 
in [credential management panel](https://console.cloud.google.com/apis/credentials/consent)
4) create new Oauth2 client ID for web application, provide valid redirectUri 
(by default `http://localhost:8080/oauthcallback` but it can be changed in `application.propeties`)
5) enable People API for your project in [google api library](https://console.cloud.google.com/apis/library/people.googleapis.com)
6) download file OAuth 2.0 Client IDs (**contains confidential client credentials**)
7) put the file into directory specified by `app.google.secret.path` property, by 
default `./secret`. 
8) *on start service will scan for json file with name starting with `client_secret` in specified folder and load it.
It will also validate presence of current oauth2 callback url in registered redirectUris*

You also might want to edit `app.web` properties to specify service self root uri. 
Default value is `http://localhost:8080`.

## How to run

The project contains a gradle wrapper, so you can use the command to build the jar file:

`./gradlew bootJar`

and to run the application:

`./gradlew bootRun`

You can also launch the application in other standard ways. Additional parameters are optional.

## Using

When you run the app, it outputs to console home url `http://localhost:8080/home`

You can use most popular browsers.

The service only requests your Google Account name and does not store any data.

### API

`http://localhost:8080/home` - home

`http://localhost:8080/signin` - sign in

`http://localhost:8080/googlesignin` - redirect uri to Google OAuth2 flow

`http://localhost:8080/signout` - sign out

## How it works

* Backend - Kotlin, Spring Boot, Spring Web
* HTTP client - Spring WebFlux
* Frontend - Thymeleaf
* Client state - default session. Persistent Spring Session may be used to save session state.
