package org.sdrc.dgacg.collect.android.utilities;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdrc.dgacg.collect.android.BuildConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

import java.util.List;

/**
 * Test for checking permissions in {@link AndroidManifest}
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PermissionsTest {

    @Test
    public void permissionCheck() {
        AndroidManifest androidManifest = new AndroidManifest(Fs.fileFromPath("build/intermediates/manifests/full/debug/AndroidManifest.xml"), null, null);
        List<String> permissions = androidManifest.getUsedPermissions();

        //List of expected permissions to be present in AndroidManifest.xml
        String[] expectedPermissions = {"android.permission.READ_PHONE_STATE",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.INTERNET",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.GET_ACCOUNTS",
                "android.permission.USE_CREDENTIALS",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.MANAGE_ACCOUNTS",
                "android.permission.WAKE_LOCK",
                "org.sdrc.dgacg.collect.android.permission.MAPS_RECEIVE",
                "com.google.android.providers.gsf.permission.READ_GSERVICES"
        };

        //Checking expected permissions one by one
        for (String permission : expectedPermissions) {
            if (!permissions.contains(permission)) {
                showError(permission);
            }
        }
    }

    /**
     * Method to display missing permission error.
     */
    private void showError(String permission) {
        Description description = new StringDescription();
        description.appendText("Expected permission ")
                .appendText(permission)
                .appendText(" is missing from AndroidManifest.xml");

        throw new AssertionError(description.toString());
    }
}

