[![Build Status](https://travis-ci.org/iammck/ECalculon.svg?branch=master)](https://travis-ci.org/iammck/ECalculon)



[![Android app on Google Play](https://developer.android.com/images/brand/en_app_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.mck.ecalculon)

Or get the [alpha] by first joining the [Google Group].

[alpha]:https://play.google.com/apps/testing/com.mck.ecalculon
[Google Group]:https://groups.google.com/forum/#!forum/commckalphatesting

#About
The ECalculon project is for experimenting and learning continuous integration concepts and techniques for Android software development.
Currently this project publishes APKs to Google Play by git commit message after successfully running and passing all tests. The project builds upon a [previous project] found under a separate branch of this repo. In that project, after successful commit and tests, the application is published, only stopping (successfully) if the version code is not updated.

The application is a simple calculator named after Calculon the famous robot actor from *All My Circuts*. The application uses the support library and is a fragment activity. There are several integration tests using junit. Those that test the UI also use espresso 2. What is neat about espresso is that it can run tests using an emulator, which is necessary for running tests on the server.

This project uses [Travis-CI] to host the continuous integration service. The previous project's [README] detailes more accurately the set up process and contains many links to other examples, faqs and tutorials that helped me configure and run ci builds.

[previous project]:https://github.com/iammck/ECalculon/tree/PublishByVersionCode
[Travis-CI]:https://travis-ci.org/
[README]:https://github.com/iammck/ECalculon/blob/PublishByVersionCode/README.markdown

#Gradle Tasks
Two new tasks, `publish` and `getCommitMessagePublishMap`, are defined in app/build.gradle. The second is of type Exec and gets the commit message, strips off the extra bits producing a string representation of a groovy Map. The result is stored in a file. Before the publish task is executed, the file is read and the map string is evaluated into a map object. If any entries are found, the corresponding publishVariant task (defined by the [google-play-publisher] plugin) is found and declared a dependency of the publish task. The dependencies are executed before the publish task.

[google-play-publisher]:https://github.com/Triple-T/gradle-play-publisher

#The Commit Message
A publish item in the commit msg must take the form of a groovy map as a string with the first item specifying the name of the variant and the second item is the track for each variant to be published. The list should be wrapped within `[PUBLISH` to the left and a `]` to the right of the map. In example

    [PUBLISH 'release' : 'alpha', 'flavorRelease' : 'beta']
This project uses

    [PUBLISH 'release' : 'alpha']
The message must be on a single line and can be written anywhere in the commit message.

#CI build Script
The getCommitMessagePublishMap and publish tasks must appear sequentially. The publish task depends upon the execution of getCommitMessagePublishMap task so that it can gain access to the commit message by executing a `git log -1` command. I am currently looking for ways to combine the two tasks.

#Reflections
Commiting successfully tested code then having it published and available for consumers by way of a single command in a git commit message is awesome. I don't think it is a useful solution for publishing to the production track, but it might be useful for the alpha or beta tracks. More importantly, this exercise could be generalized to allow the addition of one time directives for the ci build based on the commit. In example suppose the project is only building and debugging a debug version of an apk by default on the ci server. Having had some successful debug builds, a developer would like to run the tests on the release version and if everything still checks out, the developer would like to publish. A commit message such as `[GRADLE connectedAndroidTest publishRelease]`  could be injected into the commit message and performed during the ci build cycle after a commit. A less serious example could be to describe who should be notified of the build: `[EMAIL SUCCESS:michael@gzup.com, FAIL:john@gzdown.com]`

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






