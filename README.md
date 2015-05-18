The ECalculon project is for experimenting and learning continuous integration
concepts and techniques for Android software development. My goal with this
project is to use a combination of github, Travis-CI and Triple-T to deliver
application updates to the end user via google play.

The application is a simple calculator named after Calculon, a robot. It uses
the support library and is a fragment activity. There are several integration
tests using junit. Those that test the UI also use espresso 2.

This project uses Travis-Ci as the continuous integration service. Travis-CI has
integrated support for github. When pushes are made to the repo, the service is
triggered into building the project with a Webhook. The service uses the
.travis.yml file found under the root directory. This file is used to customize
the build including when to trigger builds.

The language key is an important part of the yml file. There is now support for
android via the line
    language: android
This simplified development, taking care of installing the Android
SDK and all the necessary components to assemble and build the android project
using the gradle wrapper. There were some complications but were not hard to
find solutions for. First, Travis needed to be able to execute the gradlew file.
Second, Travis had not been updated with the latest support libraries and needed
to be updated. Because the application uses espresso during tests, an emulator
needed to be started. Solutions can be found in the .travis.yml file.



// Some usefull links
https://developer.github.com/webhooks/
http://docs.travis-ci.com/user/languages/android/
http://rkistner.github.io/android/2013/02/05/android-builds-on-travis-ci/
http://stackoverflow.com/questions/29622597/is-there-a-way-to-start-android-emulator-in-travis-ci-build
https://github.com/travis-ci/travis-ci/issues/1395
http://www.kevinrschultz.com/blog/2014/05/31/how-to-use-travisci-for-android-testing/
