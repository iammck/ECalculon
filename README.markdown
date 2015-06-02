[![Build Status](https://travis-ci.org/iammck/ECalculon.svg?branch=master)](https://travis-ci.org/iammck/ECalculon)

[![Android app on Google Play](https://developer.android.com/images/brand/en_app_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.mck.ecalculon)

#About
The ECalculon project is for experimenting and learning continuous integration concepts and techniques for Android software development. My goal with this project is to publish APKs to Google Play by git commit message.

The application is a simple calculator named after Calculon the famous robot actor from *All My Circuts*. The application uses the support library and is a fragment activity. There are several integration tests using junit. Those that test the UI also use espresso 2.

This project builds upon a [previous project] found under a separate branch of this repo.

[previous project]:https://github.com/iammck/ECalculon/tree/PublishByVersionCode


##Some useful links
####Travis-CI
[Travis CI Client (CLI and Ruby library)](https://github.com/travis-ci/travis.rb)
[Travis-CI Android Project Guide](http://docs.travis-ci.com/user/languages/android/)
[Android as first class citizen](https://github.com/travis-ci/travis-ci/issues/1395)
[Integrating an Android Github repo with Travis Ci](http://gmariotti.blogspot.com/2014/04/integrating-android-github-repo-with.html)
[Starting an emulator](http://stackoverflow.com/questions/29622597/is-there-a-way-to-start-android-emulator-in-travis-ci-build)
[How to Use TravisCI for Android Testing](http://www.kevinrschultz.com/blog/2014/05/31/how-to-use-travisci-for-android-testing/)
[Ralf Kistner's Android builds on Travis CI built with Maven](http://rkistner.github.io/android/2013/02/05/android-builds-on-travis-ci/)
####Google Play
[Google Play Developer API v2 (revision 19)](https://developers.google.com/resources/api-libraries/documentation/androidpublisher/v2/java/latest/)
[Google Play Developer publishing API](https://developers.google.com/android-publisher/#publishing)
[googlesamples/android-play-publisher-api](https://github.com/googlesamples/android-play-publisher-api/blob/master/v2/java/src/com/google/play/developerapi/samples/ListApks.java)

####Gradle
[Build sources in the buildSrc project](https://docs.gradle.org/current/userguide/organizing_build_logic.html#sec:build_sources)
[Chapter 57. Writing Custom Task Classes](https://docs.gradle.org/current/userguide/custom_tasks.html)
[Gradle Plugin User Guide](http://tools.android.com/tech-docs/new-build-system/user-guide)
[22-custom-task-in-buildSrc](https://github.com/ysoftdevs/gradle-training/tree/master/22-custom-task-in-buildSrc)
[buildSrc bug](https://youtrack.jetbrains.com/issue/IDEA-129535)

####Android
[Android build System source](https://android.googlesource.com/platform/tools/base/+/d8d045469b91b3a1d4796cfb083cbb106ef67a13/build-system/gradle/src/main/groovy/com/android/build/gradle/api)
[android-gradle-plugin-dsl.zip](http://commondatastorage.googleapis.com/androiddevelopers/shareables/sdk-tools/android-gradle-plugin-dsl.zip)

####Certs
[What is the best practice to use keystores to sign release version of an Android app on Travis CI?](http://stackoverflow.com/questions/29919066/what-is-the-best-practice-to-use-keystores-to-sign-release-version-of-an-android)
[Vielen Games Android Client repo](https://github.com/mg6maciej/VielenGamesAndroidClient))

####Other stuff
[Triple-T/gradle-play-publisher](https://github.com/Triple-T/gradle-play-publisher)
[Win32 OpenSSL](http://slproweb.com/products/Win32OpenSSL.html)
[GitHub Developer, Webhooks](https://developer.github.com/webhooks/)






