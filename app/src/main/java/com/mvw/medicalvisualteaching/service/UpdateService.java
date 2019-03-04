package com.mvw.medicalvisualteaching.service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.mvw.medicalvisualteaching.R;
import java.io.File;
/**
 * 应用更新
 */
public class UpdateService extends Service {

	private UpdateService INSTANCE;
	private NotificationManager mNotificationManager;
	private FileDownloadQueueSet fileDownloadQueueSet;
	private BaseDownloadTask task;

	@Override
	public IBinder onBind(Intent intent) {
		return new UpdateBinder();
	}

	public class UpdateBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 */
		public UpdateService getService() {
			return INSTANCE;
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		INSTANCE = UpdateService.this;
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		if(intent != null){
			String url = intent.getStringExtra("apkURL");
			String path = intent.getStringExtra("apkPath");
			String version = intent.getStringExtra("version");
			startDownload(url,path,version);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.pause();
		}
	}
	private void startDownload(String url,String path,String version){
		if(fileDownloadQueueSet == null){
			fileDownloadQueueSet = new FileDownloadQueueSet(downloadListener);
			fileDownloadQueueSet.setCallbackProgressMinInterval(100);
		}
		task = FileDownloader.getImpl().create(url).setPath(path,true).setTag(version);
		fileDownloadQueueSet.downloadTogether(task)
				.start();
	}

	FileDownloadLargeFileListener downloadListener = new FileDownloadLargeFileListener() {
		@Override
		protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {

		}
		@Override
		protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
			Notification.Builder mBuilder = new Notification.Builder(INSTANCE);
			// 系统收到通知时，通知栏上面显示的文字。
			mBuilder.setTicker("开始下载");
			// 通知标题
			mBuilder.setContentTitle("国家医学电子书包.apk");
			// 通知内容
			mBuilder.setContentText((soFarBytes*100/totalBytes)+"%");
			mBuilder.setSmallIcon(R.mipmap.small_icon);
			// 设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
			mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
					R.mipmap.large_icon));
			mBuilder.setProgress((int) totalBytes, (int)soFarBytes, false);
			//设置点击一次后消失（如果没有点击事件，则该方法无效。）
			mBuilder.setAutoCancel(true);
			// 设置为不可清除模式
			mBuilder.setOngoing(false);
			// 显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
			mNotificationManager.notify(1, mBuilder.build());
		}
		@Override
		protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {

		}
		@Override
		protected void completed(BaseDownloadTask task) {
			//目标文件路径
			String targetFilePath = task.getTargetFilePath();
			//apk存放文件夹路径
			String path = task.getPath();
			String version = (String) task.getTag();
			File targetFile = new File(targetFilePath);
			File newFile = new File(path+File.separator+version+".apk");
			if(targetFile.exists()){
				boolean flag = targetFile.renameTo(newFile);
				if(!flag){
					return;
				}
				Notification.Builder mBuilder = new Notification.Builder(INSTANCE);
				// 通知标题
				mBuilder.setContentTitle("国家医学电子书包.apk");
				// 通知内容
				mBuilder.setContentText("下载完成，点击安装");
				mBuilder.setSmallIcon(R.mipmap.small_icon);
				// 设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
				mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
						R.mipmap.large_icon));

				//设置点击一次后消失（如果没有点击事件，则该方法无效。）
				mBuilder.setAutoCancel(true);

				Uri uri = Uri.fromFile(newFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
				PendingIntent pIntent = PendingIntent.getActivity(INSTANCE, 0,
						installIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(pIntent);

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				startActivity(intent);

				// 设置为不可清除模式
				mBuilder.setOngoing(false);

				// 显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
				mNotificationManager.notify(1, mBuilder.build());
			}
			Intent service = new Intent(INSTANCE, UpdateService.class);
			stopService(service);
		}
		@Override
		protected void error(BaseDownloadTask task, Throwable e) {
			Notification.Builder mBuilder = new Notification.Builder(INSTANCE);
			// 通知标题
			mBuilder.setContentTitle("国家医学电子书包.apk");
			// 通知内容
			mBuilder.setContentText("下载失败");
			mBuilder.setSmallIcon(R.mipmap.small_icon);
			// 设置大图标，即通知条上左侧的图片（如果只设置了小图标，则此处会显示小图标）
			mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
					R.mipmap.large_icon));
			//设置点击一次后消失（如果没有点击事件，则该方法无效。）
			mBuilder.setAutoCancel(true);
			// 设置为不可清除模式
			mBuilder.setOngoing(false);
			// 显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
			mNotificationManager.notify(1, mBuilder.build());
			Intent service = new Intent(INSTANCE, UpdateService.class);
			stopService(service);
		}
		@Override
		protected void warn(BaseDownloadTask task) {

		}
	};

}
