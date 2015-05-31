package com.mck.ecalculon

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.FileContent
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.model.Apk
import com.google.api.services.androidpublisher.model.AppEdit
import com.google.api.services.androidpublisher.model.Track
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public class PublishAlphaTask extends DefaultTask{
    String applicationName
    String email
    File pk12
    static final String MIME_TYPE_APK = "application/vnd.android.package-archive"

    @TaskAction
    public void publishAlpha() {
        try {
            System.out.print("Publishing to alpha track. ")

            ApplicationVariant releaseVariant =
                    project.android.applicationVariants.collect().find { variant ->
                        variant.buildType.name.toUpperCase().equals('RELEASE')
                    }
            if (releaseVariant != null){
                applicationName = releaseVariant.applicationId
            } else {
                System.out.println();
                System.out.println("Unable to find release build type, returning. ")
                return
            }

            AndroidPublisher service = AndroidPublisherHelper.init(applicationName, email, pk12)
            AndroidPublisher.Edits edits = service.edits()

            // Create a new app edit will return an edit for the current listing.
            AppEdit appEdit = edits.insert(applicationName, null ).execute()
            String editId = appEdit.getId()

            // get the the apk file for upload.
            def apkOutput = releaseVariant.outputs.find { variantOutput ->
                variantOutput instanceof ApkVariantOutput }
            FileContent apkFile = new FileContent( MIME_TYPE_APK, apkOutput.outputFile)

            // make and execute apk upload request
            AndroidPublisher.Edits.Apks.Upload uploadRequest =
                    edits.apks().upload(applicationName, editId, apkFile)
            Apk apk = uploadRequest.execute()

            // Assign apk to alpha track.
            List<Integer> apkVersionCodes = new ArrayList<>()
            apkVersionCodes.add(apk.getVersionCode())
            AndroidPublisher.Edits.Tracks.Update updateTrackRequest = edits.tracks().update(
                    applicationName, editId, "alpha", new Track().setVersionCodes(apkVersionCodes))
            Track updatedTrack = updateTrackRequest.execute()


            // Commit changes for edit.
            AndroidPublisher.Edits.Commit commitRequest = edits.commit(applicationName, editId)
            commitRequest.execute()
            System.out.println("Published to Alpha track.")
        } catch (GoogleJsonResponseException e){
            def expectedReason = e.getDetails().getErrors().find {it.getReason().equals("apkUpgradeVersionConflict")}
            if (expectedReason != null){
                System.out.println();
                System.out.println("An expected apkUpgradeVersionConflict exception has been " +
                        "raised, returning without publishing.")
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}