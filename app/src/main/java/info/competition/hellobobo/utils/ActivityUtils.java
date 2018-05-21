package info.competition.hellobobo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import info.competition.hellobobo.R;

import static android.support.v4.util.Preconditions.checkNotNull;

public class ActivityUtils {
    public static final int LOCATION_PERMISSION_CODE = 2000;
    public static final int STORAGE_PERMISSION_CODE = 2001;

    @SuppressLint("RestrictedApi")
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static boolean checkNull(String s) {
        if (s == null && s.isEmpty())
            return true;

        return false;
    }

    public static void setBarTransparent(AppCompatActivity context) {
        context.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = context.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(context.getResources().getColor(R.color.colorTransparent, null));
        }
    }

    public static int getCode(String permission) {

        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return LOCATION_PERMISSION_CODE;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return STORAGE_PERMISSION_CODE;
            default:
                break;
        }
        return 0;
    }

    public static String getPermission(int requestCode) {
        String permission = "";
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                break;
            case STORAGE_PERMISSION_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }
        return permission;
    }

    public static boolean checkPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
