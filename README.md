Get the latest release at

https://play.google.com/store/apps/details?id=com.mck.ecalculon

See the latest builds at

https://travis-ci.org/iammck/ECalculon


The ECalculon project is for experimenting and learning continuous integration
concepts and techniques for Android software development. My goal with this
project is to use a combination of github, Travis-CI and
Triple-T/google-play-publisher (Triple-T) to deliver application updates to
the end user via google play.

The application is a simple calculator named after Calculon, a robot. It uses
the support library and is a fragment activity. There are several integration
tests using junit. Those that test the UI also use espresso 2.

This project uses Travis-Ci as the continuous integration service. Travis-CI has
integrated support for github. When pushes are made to the repo, the service is
triggered into building the project with a Webhook. The service uses the
.travis.yml file found under the root directory. This file is used to customize
the build including when to trigger builds.

The language key is an important part of the yml file. There is now support for
android via the line `language: android`
This simplified development, taking care of installing the Android
SDK and all the necessary components to assemble and build the android project
using the gradle wrapper. There were some complications but were not hard to
find solutions for. First, Travis needed to be able to execute the gradlew file.
Second, Travis had not been updated with the latest support libraries and needed
to be updated. Because the application uses espresso during tests, an emulator
needed to be started. Solutions can be found in the .travis.yml file.

To provide the certs for signing the release apk the necessary information
is stored using encrypted environment variables and the necessary file is
stored using an encrypted file. Perhaps because I am using a windows machine,
I was unable to use the `travis encrypt-file` command. I was able to locally
encrypt the file and encrypt the password as an environment variable on travis,
then pushed the .enc file. I also needed to declare the environment variable and
add lines to decrypt the file before install in `.travis.yml`. To do all this
I needed to install travis cli and openssl. There are links below for help with
installing openssl and travis client on windows. See the .travis.yml file and
Travis-ci online documentation for more info about actually encrypting the data
and environment variables.

Once I had the info i needed uploaded to the service, I added a conditional
statement to the app's gradle file. If we are able to get a travis-ci environment
variable, then gather the info for signing the release APK using the environement
variables. Otherwise, for local builds try to use the console.

After successfully building the signed release apk, it was upload to Google Play
using Google Play developer console. From the console I was able to set up the
initial store listing as well as create the service account Triple-T will use
to update the Android Developer account.

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




