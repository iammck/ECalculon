[![Build Status](https://travis-ci.org/iammck/ECalculon.svg?branch=master)](https://travis-ci.org/iammck/ECalculon)
[![Android app on Google Play](https://developer.android.com/images/brand/en_app_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.mck.ecalculon)
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
To publish from the ci server, the project produces an additional task, `publishAlpha`. This class is of type PublishAlphaTask which is written in groovy and found under the buildSrc directory. This class uses AndroidPublisherHelper (also under buildSrc) to performs all necessary setup steps for running requests against the API. That is, it authorizes the account obtaining credentials then uses them to set up and returns the AndroidPublisher service used by the PublishAlphaTask. I was able to leverage samples to aid development of the helper.

To publish using the PublishAlphaTask, the task needed information about the apk as well as the actual apk file. The information was gathered using Android Gradle plugin DSL. Documentation for this seems to be split into a few places. In the Gradle Plugin User Guide the [manipulating tasks](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Manipulating-tasks) section had the info needed to acquire what is called an ApplicationVariant. Apparently, this class houses all info necessary for variant (build type/product flavor) task execution. it can be obtained in code via

    ApplicationVariant releaseVariant =
        project.android.applicationVariants.collect().find { variant ->
            variant.buildType.name.toUpperCase().equals('RELEASE')
        }
The collection of applicationVariants is of type DomainObjectCollection, a Gradle DSL item. In the same section can be found a list of properties. The DSL for the properties can mostly be found in the actual android gradle plugin DSL guide. Above, I get the varaints build type and look for the word release. I then used the result to get the application name.

    applicationName = releaseVariant.applicationId

I also used the ApplicationVaraint to get the apk File

    def apkOutput = releaseVariant.outputs.find { variantOutput ->
        variantOutput instanceof ApkVariantOutput }
    FileContent apkFile = new FileContent( MIME_TYPE_APK, apkOutput.outputFile)
Notice, i was unable to use the outputFile property listed in the documentation and the outputs property that i did use to find the ApkVariantOutput is not listed at all. I had to discover this one with the help of the triplet/gpp and google samples sources.

Having obtained the needed info, I used the AndroidPublisher service to obtain an AndroidPublisher.Edits instance. This object houses the methods used to instantiate objects which implement AbstractGoogleClientRequest. These objects are then used to make and send (execute) the metadata request to the server and returns the parsed metadata response. for example

    AppEdit appEdit = edits.insert(applicationName, null ).execute()
                String editId = appEdit.getId()
edits.inserts() creates the AndroidPublisher.Edits.Insert instance which is then executed. The response is an AppEdit instance. The AppEdit.getId() method returns an id that is used to interact with the appropriate Edits instance.  An important point about the edits.insert() method is that it creates a new edit for an app, populated with the app's current state. One can then use the edits instance to request further info from the service. Below is an example from the google [samples](https://github.com/googlesamples/android-play-publisher-api/blob/master/v2/java/src/com/google/play/developerapi/samples/ListApks.java) collection. It retrieves a list of apks after the edits.instert().execute() is executed.

    ApksListResponse apksResponse = edits.apks()
            .list(ApplicationConfig.PACKAGE_NAME, appEdit.getId()).execute();

After the Edits.insert().execute() method is invoked from PublishAlphaTask.publishAlpha(), the apk (having been acquired via the releaseVariant) is uploaded and assigned to the alphaTrack before instantiating and executing a commit request.

In order to catch the case where the version is not updated, meaning the release apk shouldn't be uploaded, the PublishAlphaTask.publishAlpha() method details are enclosed in a try/catch block. When the version is not updated, a GoogleJsonResponseException is raised. The exception is checked to see if it contains an 'apkUpgradeVersionConflict' reason and if so the exception is noted before the method returns.



#Some useful links

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

[Google Play Developer API v2 (revision 19)](https://developers.google.com/resources/api-libraries/documentation/androidpublisher/v2/java/latest/)

[Google Play Developer publishing API](https://developers.google.com/android-publisher/#publishing)

[googlesamples/android-play-publisher-api](https://github.com/googlesamples/android-play-publisher-api/blob/master/v2/java/src/com/google/play/developerapi/samples/ListApks.java)

[android-gradle-plugin-dsl.zip](http://commondatastorage.googleapis.com/androiddevelopers/shareables/sdk-tools/android-gradle-plugin-dsl.zip)

[Gradle Plugin User Guide](http://tools.android.com/tech-docs/new-build-system/user-guide)

https://github.com/ysoftdevs/gradle-training/tree/master/22-custom-task-in-buildSrc

[Android build System source](https://android.googlesource.com/platform/tools/base/+/d8d045469b91b3a1d4796cfb083cbb106ef67a13/build-system/gradle/src/main/groovy/com/android/build/gradle/api)

https://youtrack.jetbrains.com/issue/IDEA-129535



