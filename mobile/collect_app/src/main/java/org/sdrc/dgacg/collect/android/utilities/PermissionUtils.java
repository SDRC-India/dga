package org.sdrc.dgacg.collect.android.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.activities.CollectAbstractActivity;
import org.sdrc.dgacg.collect.android.activities.FormChooserList;
import org.sdrc.dgacg.collect.android.activities.FormEntryActivity;
import org.sdrc.dgacg.collect.android.activities.InstanceChooserList;
import org.sdrc.dgacg.collect.android.activities.InstanceUploaderList;
import org.sdrc.dgacg.collect.android.activities.SplashScreenActivity;
import org.sdrc.dgacg.collect.android.listeners.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * PermissionUtils allows all permission related messages and checks to be encapsulated in one
 * area so that classes don't have to deal with this responsibility; they just receive a callback
 * that tells them if they have been granted the permission they requested.
 */

public class PermissionUtils {

    private PermissionUtils() {

    }

    /**
     * Checks to see if the user granted Collect the permissions necessary for reading
     * and writing to storage and if not utilizes the permissions API to request them.
     *
     * @param activity required for context and spawning of Dexter's activity that handles
     *                 permission checking.
     * @param action   is a listener that provides the calling component with the permission result.
     */
    public static void requestStoragePermissions(@NonNull Activity activity, @NonNull PermissionListener action) {

        MultiplePermissionsListener multiplePermissionsListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    action.granted();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Light_Dialog);

                    builder.setTitle(R.string.storage_runtime_permission_denied_title)
                            .setMessage(R.string.storage_runtime_permission_denied_desc)
                            .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                                action.denied();
                            })
                            .setCancelable(false)
                            .setIcon(R.drawable.sd)
                            .show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };

        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(multiplePermissionsListener)
                .withErrorListener(error -> {
                    Timber.i(error.name());
                })
                .check();
    }

    public static boolean checkIfStoragePermissionsGranted(Context context) {
        int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks to see if an activity is one of the entry points to the app i.e
     * an activity that has a view action that can launch the app.
     *
     * @param activity that has permission requesting code.
     * @return true if the activity is an entry point to the app.
     */
    public static boolean isEntryPointActivity(CollectAbstractActivity activity) {

        List<Class<?>> activities = new ArrayList<>();
        activities.add(FormEntryActivity.class);
        activities.add(InstanceChooserList.class);
        activities.add(FormChooserList.class);
        activities.add(InstanceUploaderList.class);
        activities.add(SplashScreenActivity.class);

        for (Class<?> act : activities) {
            if (activity.getClass().equals(act)) {
                return true;
            }
        }

        return false;
    }

    public static void finishAllActivities(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.finishAndRemoveTask();
        } else {
            activity.finishAffinity();
        }
    }
}
