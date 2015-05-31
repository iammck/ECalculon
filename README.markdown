###Get the latest [release](https://play.google.com/store/apps/details?id=com.mck.ecalculon) or see the latest [build](https://travis-ci.org/iammck/ECalculon).

#About
The ECalculon project is for experimenting and learning continuous integration concepts and techniques for Android software development. My goal with this project is to use a combination of Travis-CI and Google's AndroidPublisher Api to deliver application updates to testers and users via google play.

My secondary learning goals include

The application is a simple calculator named after Calculon, a famous robot. It uses the support library and is a fragment activity. There are several integration tests using junit. Those that test the UI also use espresso 2.

#Travis-ci
This project uses Travis-Ci as the continuous integration service. Travis-CI has integrated support for github. When pushes are made to the repo, the service is triggered into building the project with a Webhook. The service uses the .travis.yml file found under the root directory. This file is used to customize the build including when to trigger builds.

The language key is an important part of the yml file. There is now support for android via the line `language: android` This simplified development, taking care of installing the Android SDK and all the necessary components to assemble and build the android project using the gradle wrapper. There were some complications but were not hard to find solutions for. First, Travis needed to be able to execute the gradlew file. Second, Travis had not been updated with the latest support libraries and needed to be updated. Because the application uses espresso during tests, an emulator needed to be started. Solutions can be found in the .travis.yml file.

To provide the certs for signing the release apk produced by the ci service, the necessary information is stored using encrypted environment variables and the necessary file is stored using an encrypted file. Perhaps because I am using a windows machine, I was unable to use the `travis encrypt-file` command. I was able to locally encrypt the file and encrypt the password as an environment variable on travis, then pushed the .enc file. I also needed to declare the environment variable and add lines to decrypt the file before install in `.travis.yml`. To do all this I needed to install travis cli and openssl. There are links below for help with installing openssl and travis client on windows. See the .travis.yml file and Travis-ci online documentation for more info about actually encrypting the data and environment variables.

Once I had the info i needed uploaded to the service, I added a conditional statement to the app's gradle file. If we are able to get a travis-ci environment variable, then gather the info for signing the release APK using the environment variables. Otherwise, for local builds try to use the console to get the required data.

#Publishing
After successfully building the signed release apk, it was upload to Google Play store using Google Play developer console. From the console I was able to set up the initial store listing as well as create the service account that is used to update the Android Developer account. Both of these tasks must be done from Google Play Developer Console.

After the initial set up, to publish one needs to access the service account using the service account email and a pk12 file. These items were added to Travis-ci in the same way as were the signing certs.

Then things got interesting. Initially, I set up for publishing using Triple-T/google-play-publisher. Using the plugin was straight forward, but it didn't work for me the way I wanted it too. Google Play requires the upload to have a version higher that the currently released version. I want to exploit this so that I can control when publishing happens by only needing to update the version number. (versionNumber is set in app/build.gradle). At first, I added the call to publish the app to the build server's script, but then I would receive false build fails when I intentionally did not publish. I then added the publish command to the after_success portion of my travis.yml file, but then I received false passes for when the publish command would fail, either intentionally or not.

My second approach to solve the problem (still using triplet/gpp), was to attempt to check for a version update. If there was,  run the publish command. After setting up the buildSrc directory where my custom build sources would live and adding `include ':buildSrc'` to settings.gradle, I found Android Studio or perhaps Intellij-IDEA was unhappy with the directory living under the root directory, giving me errors with `Class 'someclass' already exists in 'somepachage'` as the description. Lucky, I was still able to run gradle commands from the terminal so I pressed on. Using clean removes the error, but after next build it returns.

At this point I started to use AndroidPublisher Api to get the current build version from google play. Once I started becoming familiar with the api, I realized it would be easier to write a custom publish task that also does the check, rather than to do the check then possibly call the triple-t task. Once I found myself writing this code, I realized there was no need to use the triple-t/gpp plugin and removed it.

This solution was still wrong. The check and publish worked so long as their was not a pending update already for the app in the play store. In this case the check would miss the pending update and allow a version of equal or lower value to be pushed up, but would throw an exception since the pending update has a higher version.

To work around this, I decided to no longer check for version before pushing the apk, but instead would catch and handle the exception.

#AndroidPublisher
.
.
.


// Some useful links

https://github.com/Triple-T/gradle-play-publisher

https://github.com/travis-ci/travis.rb

http://slproweb.com/products/Win32OpenSSL.html

https://developer.github.com/webhooks/

http://docs.travis-ci.com/user/languages/android/

http://rkistner.github.io/android/2013/02/05/android-builds-on-travis-ci/

http://stackoverflow.com/questions/29622597/is-there-a-way-to-start-android-emulator-in-travis-ci-build

https://github.com/travis-ci/travis-ci/issues/1395

http://www.kevinrschultz.com/blog/2014/05/31/how-to-use-travisci-for-android-testing/

http://gmariotti.blogspot.com/2014/04/integrating-android-github-repo-with.html

https://github.com/mg6maciej/VielenGamesAndroidClient

http://stackoverflow.com/questions/29919066/what-is-the-best-practice-to-use-keystores-to-sign-release-version-of-an-android

https://docs.gradle.org/current/userguide/organizing_build_logic.html#sec:external_dependencies

https://developers.google.com/resources/api-libraries/documentation/androidpublisher/v2/java/latest/index.html?com/google/api/services/androidpublisher/model/ApksListResponse.html

https://developers.google.com/android-publisher/

https://github.com/googlesamples/android-play-publisher-api/blob/master/v2/java/src/com/google/play/developerapi/samples/ListApks.java

http://commondatastorage.googleapis.com/androiddevelopers/shareables/sdk-tools/android-gradle-plugin-dsl.zip

https://github.com/ysoftdevs/gradle-training/tree/master/22-custom-task-in-buildSrc

https://android.googlesource.com/platform/tools/base/+/d8d045469b91b3a1d4796cfb083cbb106ef67a13/build-system/gradle/src/main/groovy/com/android/build/gradle/api

https://youtrack.jetbrains.com/issue/IDEA-129535



