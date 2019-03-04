package com.mvw.medicalvisualteaching.activity;


import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.mvw.medicalvisualteaching.R;
import com.orhanobut.logger.Logger;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by zhaomengen on 2017/8/11.
 */

public class VideoPlayActivity extends Activity {
  private VideoView surface_view;
  private final static int SCREEN_FULL = 0;
  private RelativeLayout llWVs;
  private MediaController mediaController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!LibsChecker.checkVitamioLibs(this)){
      Logger.i("no video lib");
      return;
    }
    setContentView(R.layout.activity_video);
    surface_view = (VideoView) findViewById(R.id.surface_view);
    llWVs = (RelativeLayout) findViewById(R.id.llWVs);
    setVideoScale(SCREEN_FULL);
    mediaController = new MediaController(this);
    surface_view.setMediaController(mediaController);
    surface_view.requestFocus();
    surface_view.setVideoPath(getIntent().getStringExtra("url"));
    Logger.i("path-------"+getIntent().getStringExtra("url"));
    surface_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        // optional need Vitamio 4.0
        mediaPlayer.setPlaybackSpeed(1.0f);
      }
    });
    surface_view.setOnCompletionListener(new OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        finish();
      }
    });

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (surface_view!=null){
      surface_view.stopPlayback();
    }
  }
  /** 设置视频窗口大小
   *
   */
  private void setVideoScale(int flag) {
    switch (flag) {
      case SCREEN_FULL:

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.FILL_PARENT,
            RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        surface_view.setLayoutParams(layoutParams);
        break;
    }
  }
}
