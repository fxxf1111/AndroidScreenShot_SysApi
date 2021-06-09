package com.androidyuan.androidscreenshot_sysapi;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.service.quicksettings.Tile;
import android.util.Log;


@TargetApi(24)
public class TileService extends android.service.quicksettings.TileService {
//    IntentFilter filters = new IntentFilter();
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            updateTile();
//        }
//    };

    @Override
    public void onCreate() {
        super.onCreate();
//        filters.addAction(RecordingActivity.START_RECORDING);
//        filters.addAction(RecordingActivity.STOP_RECORDING);
//        registerReceiver(receiver, filters);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    void updateTile() {
        Tile tile = getQsTile();
//        if (tile == null)
//            return; // some broken devices has tile == null within onStartListening()
//        if (AudioApplication.from(this).recording != null) {
//            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_stop_black_24dp));
//            tile.setLabel(getString(R.string.tile_stop_recording));
//        } else {
//            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_mic_24dp));
//            tile.setLabel(getString(R.string.tile_start_recording));
//        }
        tile.setIcon(Icon.createWithResource(this, R.drawable.ic_stop_black_24dp));
        tile.setLabel("截屏");
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

    @Override
    public void onClick() {
        super.onClick();
//        if (getQsTile().getLabel().equals(getString(R.string.tile_start_recording)))
//            RecordingActivity.startActivity(this, false);
//        else
//            RecordingActivity.stopRecording(this);
        //这里启动新的透明的activity后 上个页面的dialog 键盘可能会因为页面pause而隐藏.
        Log.d("TileService","onClick");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TileService","onClick run()");

                //这里启动新的透明的activity后 上个页面的dialog 键盘可能会因为页面pause而隐藏.
                Intent i = new Intent("androidyuan.shotter");
                // 这个不是必需的
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }, 5000);//这里留足够的时间切换到别的app
    }
}
