package com.mvw.medicalvisualteaching.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadSerialQueue;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.FileUtils;
import com.mvw.medicalvisualteaching.utils.Utils;
import com.orhanobut.logger.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class BookDownloadService extends Service {
	private BookDownloadListener progressListener;
	private Map<String,BaseDownloadTask> taskList = new ConcurrentHashMap<>();
//	private FileDownloadQueueSet fileDownloadQueueSet;//并行
	private FileDownloadSerialQueue fileDownloadSerialQueue;//串行
	@Override
	public IBinder onBind(Intent intent) {
		return new DownloadBinder();
	}

	public class DownloadBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 */
		public BookDownloadService getService() {
			return BookDownloadService.this;
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

//	public boolean isMainThread() {
//		return Looper.getMainLooper().getThread() == Thread.currentThread();
//	}

	/**
	 *
     */
//	public void addDownloadTask(Book localBook){
//		if(fileDownloadQueueSet == null){
//			fileDownloadQueueSet = new FileDownloadQueueSet(downloadListener);
//			fileDownloadQueueSet.setCallbackProgressMinInterval(100);
//		}
////		FileDownloader.getImpl().start(downloadListener,true);
//		BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(localBook.getFileUrl())
//				.setTag(localBook).setPath(localBook.getDownloadPath(),true);
////		taskList.put(localBook.getId(),baseDownloadTask);
////		List<BaseDownloadTask> tasks = new ArrayList<>(taskList.values());
//		fileDownloadQueueSet.downloadSequentially(baseDownloadTask).start();
//	}

	/**
	 * 添加下载任务
	 * @param localBook 书籍
   */
	public void addDownloadTask(Book localBook){
		if(fileDownloadSerialQueue == null){
			fileDownloadSerialQueue = new FileDownloadSerialQueue();
		}
		BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(localBook.getPath())
				.setTag(localBook)
				.setPath(localBook.getDownloadPath(),true)
				.setCallbackProgressMinInterval(2000)
				.setListener(downloadListener);
		fileDownloadSerialQueue.enqueue(baseDownloadTask);
		if(!taskList.containsKey(localBook.getIsbn())){
			taskList.put(localBook.getIsbn(),baseDownloadTask);
		}
		Logger.i("id = "+baseDownloadTask.getId() + " path = "+baseDownloadTask.getPath());
	}

//	public void addDownloadTask(Book localBook){
//		if(fileDownloadQueueSet == null){
//			fileDownloadQueueSet = new FileDownloadQueueSet(downloadListener);
//		}
//		BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(localBook.getPath())
//				.setTag(localBook)
//				.setPath(localBook.getDownloadPath(),true)
//				.setCallbackProgressMinInterval(2000)
//				.setListener(downloadListener);
//		fileDownloadQueueSet.downloadSequentially(baseDownloadTask);
//		fileDownloadQueueSet.start();
//		if(!taskList.containsKey(localBook.getIsbn())){
//			taskList.put(localBook.getIsbn(),baseDownloadTask);
//		}
//		Logger.i("id = "+baseDownloadTask.getId() + " path = "+baseDownloadTask.getPath());
//	}

//	public void addDownloadTask(Book localBook){
//
//		BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(localBook.getPath())
//            .setCallbackProgressMinInterval(2000) // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.
//            .setListener(downloadListener)
//						.setTag(localBook)
//						.setPath(localBook.getDownloadPath(),true);
//		baseDownloadTask.asInQueueTask()
//            .enqueue();
//		if(!taskList.containsKey(localBook.getIsbn())){
//			taskList.put(localBook.getIsbn(),baseDownloadTask);
//		}
//		FileDownloader.getImpl().start(downloadListener, true);
//
//	}

//
//	public void addDownloadTask(List<Book> bookTasks){
//		if(fileDownloadQueueSet == null){
//			fileDownloadQueueSet = new FileDownloadQueueSet(downloadListener);
//			fileDownloadQueueSet.setCallbackProgressMinInterval(100);
//		}
//		List<BaseDownloadTask>  tasks = new ArrayList<>();
//		for (int i=0; i<bookTasks.size(); i++){
//			Book localBook = bookTasks.get(i);
//			tasks.add( FileDownloader.getImpl().create(localBook.getFileUrl())
//					.setTag(localBook).setPath(localBook.getDownloadPath(),true));
//		}
//		fileDownloadQueueSet.downloadSequentially(tasks)
//				.start();
//	}

	/**
	 * 暂停下载任务
     */
	public void stopDownloadTask(Book localBook){
		if(taskList.containsKey(localBook.getIsbn())){
			BaseDownloadTask baseDownloadTask = taskList.get(localBook.getIsbn());
			baseDownloadTask.setTag(Constant.DOWNLOAD_BOOK_FLAG,Constant.PAUSE_DOWNLOAD_BOOK_FLAG);
			baseDownloadTask.pause();
			taskList.remove(localBook.getIsbn());
			if(fileDownloadSerialQueue != null) {
				fileDownloadSerialQueue.removeTask(baseDownloadTask);
			}
		}
	}
//	public void stopDownloadTask(Book localBook){
//		int downloadId = FileDownloadUtils.generateId(localBook.getPath(),localBook.getDownloadPath(),true);
//		Logger.i(" download Id = "+downloadId);
//		FileDownloader.getImpl().pause(downloadId);
//	}

	/**
	 * 取消下载任务
	 * @param localBook 书籍
   */
	public void cancelDownloadTask(Book localBook){
		if(taskList.containsKey(localBook.getIsbn())){
			BaseDownloadTask baseDownloadTask = taskList.get(localBook.getIsbn());
			baseDownloadTask.setTag(Constant.DOWNLOAD_BOOK_FLAG,Constant.CANCEL_DOWNLOAD_BOOK_FLAG);
			baseDownloadTask.pause();
			taskList.remove(localBook.getIsbn());
			if(fileDownloadSerialQueue != null) {
				fileDownloadSerialQueue.removeTask(baseDownloadTask);
			}
		}
	}

//	public void cancelDownloadTask(Book localBook){
//		int downloadId = FileDownloadUtils.generateId(localBook.getPath(),localBook.getDownloadPath(),true);
////		FileDownloader.getImpl().clear(downloadId,localBook.getDownloadPath()+File.separator+Utils.md5Digest(localBook.getPath()));
//		FileDownloader.getImpl().pause(downloadId);
//	}

	/**
	 * 停止所有任务
	 */
	public void stopAllDownloadTask(){
		FileDownloader.getImpl().pause(downloadListener);
		if(fileDownloadSerialQueue != null) {
			fileDownloadSerialQueue.shutdown();
			fileDownloadSerialQueue = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopAllDownloadTask();
	}
	/**
	 * 获取当前要下载的路径
     */
//	private String getTargetPath() {
//		// 查询这本书的存储位置
//		String SDCardPath = Utils.getBookSDCardPath(INSTANCE);
//		// 目标路径文件夹
//		String targetPath = SDCardPath + "/localBook/bundles/";
//		File targetFile = new File(targetPath);
//		if (!targetFile.exists()) {
//			targetFile.mkdirs();
//		}
//		return targetPath;
//
//	}
	public void addOnDownloadListener(BookDownloadListener progressListener) {
		this.progressListener = progressListener;
	}

	FileDownloadListener downloadListener = new FileDownloadListener() {

		@Override
		protected void started(BaseDownloadTask task) {
			Book localBook = (Book) task.getTag();
			if(!taskList.containsKey(localBook.getIsbn())){
				taskList.put(localBook.getIsbn(),task);
			}
			if (progressListener != null){
				progressListener.downloadStart(task);
			}
		}

		@Override
		protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
			Book localBook = (Book) task.getTag();
			Logger.e(" BookDownloadService pending ...");
			if(!taskList.containsKey(localBook.getIsbn())){
				taskList.put(localBook.getIsbn(),task);
			}
			if (progressListener != null){
				progressListener.downloadPending(task,soFarBytes,totalBytes);
			}
		}

		@Override
		protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
			if (progressListener != null){
				progressListener.downloadProgress(task,soFarBytes,totalBytes);
			}
		}

		@Override
		protected void completed(BaseDownloadTask task) {
			Book localBook = (Book) task.getTag();
			if(taskList.containsKey(localBook.getIsbn())){
				taskList.remove(localBook.getIsbn());
			}
			if (progressListener != null){
				progressListener.downloadCompleted(task);
				Logger.e(task.getFilename());
				Logger.e(task.getPath());
				Logger.e(task.getTargetFilePath());
				Book taskTag = (Book) task.getTag();
				if(TextUtils.equals(Constant.TEXT_BOOK, taskTag.getTextbook())){
					//只有教材才可解压
					ZipTask zipTask = new ZipTask(taskTag);
					zipTask.setListener(progressListener);
					zipTask.execute();
				}
			}
		}

		@Override
		protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
			//暂停，移除任务
//			task.reuse();
			Book localBook = (Book) task.getTag();
			if(taskList.containsKey(localBook.getIsbn())){
				taskList.remove(localBook.getIsbn());
			}
			if (progressListener != null){
				progressListener.downloadPaused(task,soFarBytes,totalBytes);

			}
		}

		@Override
		protected void error(BaseDownloadTask task, Throwable e) {
			Book localBook = (Book) task.getTag();
			if(taskList.containsKey(localBook.getIsbn())){
				taskList.remove(localBook.getIsbn());
			}
			if (progressListener != null){
				progressListener.downloadError(task,e);
			}
		}
		@Override
		protected void warn(BaseDownloadTask task) {
			if (progressListener != null){
				progressListener.downloadWarn(task);
			}
		}
	};
	public interface BookDownloadListener {
		void downloadStart(BaseDownloadTask task);
		void downloadCompleted(BaseDownloadTask task);//下载完成
		void downloadError(BaseDownloadTask task, Throwable e);//下载失败
		void downloadWarn(BaseDownloadTask task);//警告
		void downloadPending(BaseDownloadTask task, int soFarBytes, int totalBytes);//下载等待
		void downloadProgress(BaseDownloadTask task, int soFarBytes, int totalBytes);//下载进度
		void downloadPaused(BaseDownloadTask task, int soFarBytes, int totalBytes);//下载暂停
		void upZipStart(Book localBook);//开始解压
		void upZipProgress(Book localBook,int soFar, int total);//解压进度
		void upZipCompleted(Book localBook);//解压完成
		void upZipError(Book localBook);//解压失败
	}

	public int getTaskCount(){
		return taskList.size();
	}
	public Map<String,BaseDownloadTask> getTaskList(){
		return taskList;
	}

	/**
	 * 教材解压
	 */
	public class ZipTask extends AsyncTask<Void, Integer, Long> {
		private final String TAG = "ZipTask";
		private BookDownloadListener progressListener;
		private Book localBook;
		private final File mInput;
		private final File mOutput;
		private int mProgress = 0;
		ZipTask(Book localBook){
			super();
			this.localBook = localBook;
			mInput = new File(localBook.getDownloadPath()+File.separator+ Utils.md5Digest(localBook.getPath()));
			mOutput = new File(localBook.getDownloadPath()+File.separator+ localBook.getIsbn()+File.separator+Utils.getBookVideoPath(localBook.getIsbn()));
			if(!mOutput.exists()){
				if(!mOutput.mkdirs()){
					Logger.e(TAG, "Failed to make directories:"+mOutput.getAbsolutePath());
				}
			}
		}
		@Override
		protected Long doInBackground(Void... params) {
			// TODO Auto-generated method stub
			long size = 0;
			try {
				size = unzip();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return size;
		}

		@Override
		protected void onPostExecute(Long result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			if(result == 0){
				//解压失败
				if(progressListener!=null){
						progressListener.upZipError(localBook);
				}
			}else {
				if(progressListener!=null){
					progressListener.upZipCompleted(localBook);
				}
			}
//			if(isCancelled()){
//			}

		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
			if(progressListener!=null){
				progressListener.upZipStart(localBook);
			}
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			//super.onProgressUpdate(values);
			if(progressListener!=null){
				if(values.length>0){
					progressListener.upZipProgress(localBook,values[0],values[0]);
				}
			}
		}
		private long unzip() throws IOException {
			long extractedSize = 0L;
			Enumeration<ZipEntry> entries;
			ZipFile zip = new ZipFile(mInput);
			long uncompressedSize = getOriginalSize(zip);
			publishProgress(0, (int) uncompressedSize);
			entries = (Enumeration<ZipEntry>) zip.entries();
			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				if(entry.isDirectory()){
					continue;
				}
				File destination = new File(mOutput, entry.getName());

				if(!destination.getParentFile().exists()){
					FileUtils.mkdirs(destination.getParentFile());
					if(!destination.getParentFile().getParentFile().exists()){
						destination.getParentFile().getParentFile().mkdir();
					}
					String path = destination.getParentFile().getPath();
					Logger.e(path);
					destination.getParentFile().mkdir();
					File pathFile = new File(path);
					boolean ex = pathFile.exists();
					Logger.e(ex+"");
				}
//					if(destination.exists()&&mContext!=null&&!mReplaceAll){
//
//					}
				ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(destination);
				extractedSize+=copy(zip.getInputStream(entry),outStream);
				outStream.close();
			}
			zip.close();

			return extractedSize;
		}

		private long getOriginalSize(ZipFile file){
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
			long originalSize = 0;
			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				if(entry.getSize()>=0){
					originalSize+=entry.getSize();
				}
			}
			return originalSize;
		}

		private int copy(InputStream input, OutputStream output) throws IOException {
			byte[] buffer = new byte[1024*8];
			BufferedInputStream in = new BufferedInputStream(input, 1024*8);
			BufferedOutputStream out  = new BufferedOutputStream(output, 1024*8);
			int count =0,n=0;
				while((n=in.read(buffer, 0, 1024*8))!=-1){
					out.write(buffer, 0, n);
					count+=n;
				}
				out.flush();
			out.close();

			in.close();
			return count;
		}

		private final class ProgressReportingOutputStream extends FileOutputStream {

			ProgressReportingOutputStream(File file)
					throws FileNotFoundException {
				super(file);
				// TODO Auto-generated constructor stub
			}

			@Override
			public void write(@NonNull byte[] buffer, int byteOffset, int byteCount)
					throws IOException {
				// TODO Auto-generated method stub
				super.write(buffer, byteOffset, byteCount);
				mProgress += byteCount;
				publishProgress(mProgress);
			}

		}
		void setListener(BookDownloadListener progressListener){
			this.progressListener = progressListener;
		}
	}

}
