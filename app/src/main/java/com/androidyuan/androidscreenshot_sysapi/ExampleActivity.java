package com.androidyuan.androidscreenshot_sysapi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.androidyuan.lib.screenshot.ScreenShotActivity;
import com.androidyuan.lib.screenshot.Shooter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a demo to show you how to use Shooter.
 */
public class ExampleActivity extends AppCompatActivity {

    private static final int REQ_CODE_PER = 0x2304;
    private static final int REQ_CODE_ACT = 0x2305;
    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    Boolean hasPermission = true;
    private void checkPermission() {
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission[i]);
            }
        }
        if (!mPermissionList.isEmpty()) {
            //代表有权限未授予，申请权限
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            //这里表示所有权限都授予了
            //do someing
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean hasAllPermision = true; //判断是否拥有所有权限
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//用户拒绝授权的权限
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (!showRequestPermission) {
//                        Toast.makeText(MainActivity.this, "禁止后不再询问", Toast.LENGTH_SHORT).show();
                        hasAllPermision = false;
                    }
                    hasPermission = false;
                }
            }
            if (!hasAllPermision) {//用户拒绝了一些权限，且是禁止后不再询问
                hasPermission = false;
                Toast.makeText(this, "缺少必要权限 你选择了禁止后不再询问，请主动授予 。", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    /**
     * This is an example for using Shooter.
     * This method will request permission and take screenshot on this Activity.
     */
//    public void onClickReqPermission(View view) {
//        if (Build.VERSION.SDK_INT >= 21) {
//            startActivityForResult(createScreenCaptureIntent(), REQ_CODE_PER);
//        }
//    }

    /**
     * using {@see ScreenShotActivity} to take screenshot on current Activity directly.
     * If you press home it will take screenshot on another app.
     * @param view
     */
    public void onClickShot(View view) {
        startActivityForResult(ScreenShotActivity.createIntent(this, null,5000), REQ_CODE_ACT);
//        toast("Press home key,open another app.");//if you want to take screenshot on another app.
    }

//    public void onClickShot10(View view) {
//        startActivityForResult(ScreenShotActivity.createIntent(this, null,10000), REQ_CODE_ACT);
//        toast("Press home key,open another app.");//if you want to take screenshot on another app.
//    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Intent createScreenCaptureIntent() {
        //Here using media_projection instead of Context.MEDIA_PROJECTION_SERVICE to  make it successfully build on low api.
        return ((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent();
    }

    private String getSavedPath() {
//        return getExternalFilesDir("screenshot").getAbsoluteFile() + "/"
//                + SystemClock.currentThreadTimeMillis() + ".png";
        return Environment.getExternalStorageDirectory()
                +
                "/Pictures/ScreenShots/"
                +
                SystemClock.currentThreadTimeMillis() + ".png";
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQ_CODE_ACT: {
                if (resultCode == RESULT_OK && data != null) {
//                    toast("Screenshot saved at " + data.getData().toString());
                }
                else{
//                    toast("You got wrong.");
                }
            }
            break;
            case REQ_CODE_PER: {
                if (resultCode == RESULT_OK && data != null) {
                    Shooter shooter = new Shooter(ExampleActivity.this, resultCode, data);
                    shooter.startScreenShot(getSavedPath(), new Shooter.OnShotListener() {
                                @Override
                                public void onFinish(String path) {
                                    //here is done status.
//                                    toast("Screenshot saved at " + path);
                                }

                                @Override
                                public void onError() {
//                                    toast("You got wrong.");
                                }
                            }
                    );
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled.
                } else {

                }
            }
        }
    }


    private void toast(String str) {
        Toast.makeText(ExampleActivity.this, str, Toast.LENGTH_LONG).show();
    }

    private void goBackground() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
