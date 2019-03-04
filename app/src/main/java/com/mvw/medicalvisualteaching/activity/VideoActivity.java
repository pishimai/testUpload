package com.mvw.medicalvisualteaching.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.mvw.medicalvisualteaching.R;
import com.mvw.medicalvisualteaching.view.MyVideoView;
import com.mvw.medicalvisualteaching.view.MyVideoView.SizeChangeLinstener;

public class VideoActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout llWVs;
	private MyVideoView mVideoView;
	private RelativeLayout rlVideoView;
	private RelativeLayout mControllView;
	private ImageView ivPlay, ivBigPlay;
	private View vHint;
	private TextView tvDuration, tvProgress;
	private SeekBar seekBar;

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_PORTRAIT = 1;
	private int screenWidth;
	private int screenHeight;
	private int progress;
	private boolean isPaused;
	private String videoPath;

	private final static int HIDE_CONTROLER = 2;
	private final static int HIDE_HINT = 3;
	private final static int PROGRESS_CHANGED = 4;
	private final static int HIDE_CONTROLER_TIME = 5000;
	private final static int HIDE_HINT_TIME = 100;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_video);
		WindowManager windowManager = getWindow().getWindowManager();
		//WindowManager.LayoutParams.TYPE_TOAST;
		Display display = windowManager.getDefaultDisplay();
		screenHeight = display.getWidth();
		screenWidth = display.getHeight();
		videoPath = getIntent().getStringExtra("url");
		initView();
	}

	private void initView() {
		llWVs = (RelativeLayout) findViewById(R.id.llWVs);
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		View viewVideo = inflater.inflate(R.layout.view_video, null);
		viewVideo.setLayoutParams(lp);
		llWVs.addView(viewVideo, lp);
		rlVideoView = (RelativeLayout) viewVideo.findViewById(R.id.rlVideoView);

		View viewController = inflater.inflate(R.layout.view_controller, null);
		viewController.setLayoutParams(lp);
		llWVs.addView(viewController, lp);

		mVideoView = (MyVideoView) viewVideo.findViewById(R.id.mVideoView);
		rlVideoView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (vHint.getVisibility() == View.GONE) {
					handler.removeMessages(HIDE_CONTROLER);
					if (mControllView.getVisibility() == View.GONE) {
						// 设置控制面板3秒后隐藏
						mControllView.setVisibility(View.VISIBLE);
						handler.sendEmptyMessageDelayed(HIDE_CONTROLER, HIDE_CONTROLER_TIME);
					} else {
						handler.sendEmptyMessage(HIDE_CONTROLER);
					}
				}
				return false;
			}
		});

		mControllView = (RelativeLayout) viewController.findViewById(R.id.mControllView);
		mControllView.setVisibility(View.GONE);
		LinearLayout llControll = (LinearLayout) viewController.findViewById(R.id.llControll);
		ivPlay = (ImageView) viewController.findViewById(R.id.ivPlay);
		ivBigPlay = (ImageView) viewController.findViewById(R.id.ivBigPlay);
		ivBigPlay.setImageResource(R.mipmap.start_big);
		ivPlay.setImageResource(R.mipmap.stop_small);
		tvDuration = (TextView) viewController.findViewById(R.id.tvDuration);
		tvProgress = (TextView) viewController.findViewById(R.id.tvProgress);
		seekBar = (SeekBar) viewController.findViewById(R.id.seekBar);
		// 拖动监听
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			// 当拖动发生改变的时候执行
			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
				if (fromUser) {
					mVideoView.seekTo(progress);
				}
			}

			// 当拖动刚触动的时候执行
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				mVideoView.pause();
				handler.removeMessages(HIDE_CONTROLER);
			}

			// 当拖动停止的时候执行
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mVideoView.start();
				handler.sendEmptyMessageDelayed(HIDE_CONTROLER, HIDE_CONTROLER_TIME);
			}
		});

		llControll.setOnClickListener(this);
		ivPlay.setOnClickListener(this);
		ivBigPlay.setOnClickListener(this);

		// 设置视频未显示出来前的遮挡
		vHint = (View) viewController.findViewById(R.id.vHint);
		vHint.setVisibility(View.VISIBLE);

		setVideoScale(SCREEN_FULL);

		initVideoView();
		startPlay(videoPath);
	}

	/** 初始化VideoView */
	private void initVideoView() {
		mVideoView.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			@Override
			public void onSeekComplete(MediaPlayer mp) {
			}
		});

		mVideoView.setOnInfoListener(new OnInfoListener() {
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				return true;
			}
		});

		mVideoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				if (mVideoView != null) {
					mVideoView.stopPlayback();
				}
				return true;
			}
		});

		mVideoView.setSizeChangeLinstener(new SizeChangeLinstener() {
			@Override
			public void doMyThings() {
			}
		});

		mVideoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer arg0) {
				setVideoScale(SCREEN_FULL);

				int i = mVideoView.getDuration();

				seekBar.setMax(i);
				seekBar.setProgress(progress);

				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				if (hour > 0) {
					tvDuration.setText(String.format("%02d:%02d:%02d", hour, minute, second));
				} else {
					tvDuration.setText(String.format("%02d:%02d", minute, second));
				}

				if (progress > 0) {
					mVideoView.seekTo(progress);
				}

				handler.sendEmptyMessage(PROGRESS_CHANGED);
				mControllView.setVisibility(View.VISIBLE);
				handler.removeMessages(HIDE_CONTROLER);

				if (isPaused) {
					ivBigPlay.setImageResource(R.mipmap.start_big);
					ivPlay.setImageResource(R.mipmap.start_small);
				} else {
					mVideoView.start();
					ivBigPlay.setImageResource(R.mipmap.stop_big);
					ivPlay.setImageResource(R.mipmap.stop_small);
					handler.sendEmptyMessageDelayed(HIDE_HINT, HIDE_HINT_TIME);
					// 设置控制面板3秒后隐藏
					handler.sendEmptyMessageDelayed(HIDE_CONTROLER, HIDE_CONTROLER_TIME);
				}
			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				mVideoView.stopPlayback();
				setResult(300);
				finish();
				overridePendingTransition(0, 0);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case HIDE_CONTROLER:
				mControllView.setVisibility(View.GONE);
				break;
			case HIDE_HINT:
				vHint.setVisibility(View.GONE);
				break;
			case PROGRESS_CHANGED:
				// 更新播放进度
				// 得到当前播放位置
				int i = mVideoView.getCurrentPosition();

				// 更新播放进度
				seekBar.setProgress(i);

				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				if (hour > 0) {
					tvProgress.setText(String.format("%02d:%02d:%02d", hour, minute, second));
				} else {
					tvProgress.setText(String.format("%02d:%02d", minute, second));
				}
				sendEmptyMessageDelayed(PROGRESS_CHANGED, 100);
				break;
			}
		};
	};

	/** 设置视频窗口大小
	 *
	 */
	private void setVideoScale(int flag) {
		switch (flag) {
		case SCREEN_FULL:
			int videoWidth = mVideoView.getVideoWidth();
			int videoHeight = mVideoView.getVideoHeight();
			int width = screenHeight;
			int height = screenWidth;

			if (videoWidth > 0 && videoHeight > 0) {
				if (videoWidth * height > width * videoHeight) {
					height = width * videoHeight / videoWidth;
				} else if (videoWidth * height < width * videoHeight) {
					width = height * videoWidth / videoHeight;
				}
			}

			vHint.getLayoutParams().width = screenHeight;
			vHint.getLayoutParams().height = screenWidth;
			mControllView.getLayoutParams().width = screenHeight;
			mControllView.getLayoutParams().height = screenWidth;
			mVideoView.setVideoScale(width, height);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		case SCREEN_PORTRAIT:
			if (mVideoView != null) {
				int progress = mVideoView.getCurrentPosition();
				if (progress > 1000) {
					progress -= 1000;
				} else {
					progress = 0;
				}
				mVideoView.stopPlayback();
				setResult(300);
			}
			finish();
			overridePendingTransition(0, 0);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		setVideoScale(SCREEN_PORTRAIT);
		super.onBackPressed();
	}

	/** 开始播放 */
	private void startPlay(String path) {
		if (path != null && mVideoView != null) {
			mVideoView.setVideoPath(path);
		}
	}

	private void setVideoPause() {
		if (mVideoView != null) {
			handler.removeMessages(HIDE_CONTROLER);
			if (isPaused) {
				if (mVideoView != null) {
					mVideoView.start();
					handler.sendEmptyMessageDelayed(HIDE_HINT, HIDE_HINT_TIME);
				}
				if (progress == 0) {
					handler.sendEmptyMessage(PROGRESS_CHANGED);
				}
				ivBigPlay.setImageResource(R.mipmap.stop_big);
				ivPlay.setImageResource(R.mipmap.stop_small);
				handler.sendEmptyMessageDelayed(HIDE_CONTROLER, HIDE_CONTROLER_TIME);
			} else {
				mVideoView.pause();
				ivBigPlay.setImageResource(R.mipmap.start_big);
				ivPlay.setImageResource(R.mipmap.start_small);
			}
			isPaused = !isPaused;
		}
	}

	@Override
	protected void onDestroy() {
		if (mVideoView != null && mVideoView.isPlaying()) {
			mVideoView.stopPlayback();
		}
		if(handler != null){
			handler.removeMessages(PROGRESS_CHANGED);
			handler = null;
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		if (mVideoView != null) {
			mVideoView.seekTo(progress);
			mVideoView.pause();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (mVideoView != null) {
			progress = mVideoView.getCurrentPosition();
			mVideoView.pause();
		}
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ivPlay:
				setVideoPause();
				break;
			case R.id.ivBigPlay:
				setVideoPause();
				break;
		}
	}
}
