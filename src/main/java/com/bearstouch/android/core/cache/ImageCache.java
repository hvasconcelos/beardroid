package com.bearstouch.android.core.cache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.bearstouch.android.core.FileUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
/**
 * <p> Image Cache for Android Images</p>
 * <p> Cache can be persisted on Internal Storage or External Storage SDCard
 * 
 * @author HŽlder Vasconcelos
 */
//TODO Expire Listener to remove file from disk
public class ImageCache {

	public enum CacheType {

		MEMORYCACHE, // Memory Cache only
		MEMORY_PLUS_INTERNAL, // File persisted to Internal Storage only
		MEMORY_PLUS_EXTERNAL; // File persisted to External Storage only
	}

	String						mName;
	long							mExpireTimeInMinutes;
	int							mInitialCapacity;
	int							mMaxCapacity;
	Cache<String, byte[]>	mCache;
	int							mMaxConcurrentThreads;
	CacheType					mCacheType;
	String						mCacheDir;
	Context						mCtx;

	public ImageCache(Context ctx, String name, CacheType type, int initial_capacity,
			int max_capacity, long expireTimeInMinutes, int maxConcurrentThreads) throws IOException {

		mCtx = ctx;
		mName = name;
		mInitialCapacity = initial_capacity;
		mMaxCapacity = max_capacity;
		mCacheType = type;
		mExpireTimeInMinutes = expireTimeInMinutes;
		initCache();
	}

	

	public synchronized void putImageForKey(String key, byte[] imageData) {
		if (mCacheType != CacheType.MEMORYCACHE) {
			cacheToDisk(key, imageData);
		}
		mCache.put(key, imageData);
	}

	private void removefileforKey(String key) {
		File cachedFile = new File(mCacheDir + "/" + key);
		cachedFile.delete();
	}

	// Clear all data not expired also
	public synchronized void clear() {
		if (mCacheType != CacheType.MEMORYCACHE) {
			cleanAllDiskCacheFiles();
		}
		mCache.invalidateAll();
	}

	@SuppressWarnings("unchecked")
	public boolean containsKey(String key) throws ExecutionException {
		if (mCache.get(key) == null) {
			return existsFileForKey(key);
		}
		else {
			return true;
		}
	}

	public byte[] getImageForKey(String key) throws ExecutionException, IOException {
		if (!containsKey(key)) return null;
		byte[] cachedata = mCache.getIfPresent(key);
		if (cachedata != null) return cachedata;
		return getFileForKey(key);

	}

	public Bitmap getBitmapForKey(String key) throws ExecutionException, IOException {
		if (!containsKey(key)) return null;
		byte[] result = getImageForKey(key);
		return BitmapFactory.decodeByteArray(result, 0, result.length);
	}

	public int getSize() {
		return (int) mCache.size();
	}

	public void removeImageForKey(String key) throws ExecutionException {

		if (!containsKey(key)) return;

		if (!(mCacheType == CacheType.MEMORYCACHE)) {
			removefileforKey(key);
		}
		mCache.invalidate(key);
	}

	public boolean isEmpty() {
		return mCache.size() > 0;
	}

	/**
	 * @return the mName
	 */
	public  String getName() {
		return mName;
	}

	/**
	 * @return the mExpireTimeInMinutes
	 */
	public long getExpireTimeInMinutes() {
		return mExpireTimeInMinutes;
	}

	/**
	 * @return the mInitialCapacity
	 */
	public int getInitialCapacity() {
		return mInitialCapacity;
	}

	/**
	 * @return the mMaxCapacity
	 */
	public int getMaxCapacity() {
		return mMaxCapacity;
	}

	/**
	 * @return the mMaxConcurrentThreads
	 */
	public int getMaxConcurrentThreads() {
		return mMaxConcurrentThreads;
	}

	/**
	 * @return the mCacheType
	 */
	public CacheType getCacheType() {
		return mCacheType;
	}

	/**
	 * @return the mCacheDir
	 */
	public String getCacheDir() {
		return mCacheDir;
	}

	protected void onImageExpired(String key, byte data[]) {
	}
	
	
	/**
	 *  Inicializaao da cache 
	 *  Cria uma cache em memoria ou em disco tambem
	 *  No arranque se existirem ficheiros de anteriores execu›es os ficheiros
	 *  expirados s‹o removidos da diretoria
	 */
	private void initCache() throws IOException {

		mCache = CacheBuilder.newBuilder().initialCapacity(mInitialCapacity)
				.maximumSize(mMaxCapacity).expireAfterWrite(mExpireTimeInMinutes, TimeUnit.MINUTES)
				.concurrencyLevel(mMaxConcurrentThreads).build();

		switch (mCacheType) {
		case MEMORYCACHE:
			break;
		case MEMORY_PLUS_INTERNAL:
			mCacheDir = FileUtil.getInternalCachePath(mCtx) + "/" + mName;
			File outDir = new File(mCacheDir);
			if (!outDir.exists()) {
				if (outDir.mkdirs() == true) {

				}
				else {
					throw new IOException("Failed creating a Internal chae directoy for cache " + mName);
				}
			}
			else {
				cleanDiskExpiredCacheFiles();
			}
			break;
		case MEMORY_PLUS_EXTERNAL:
			mCacheDir = FileUtil.getSDCardCachePath(mCtx) + "/" + mName;
			File sdoutDir = new File(mCacheDir);
			if (!sdoutDir.exists()) {
				if (sdoutDir.mkdirs() == true) {

				}
				else {
					throw new IOException("Failed creating a Internal chae directoy for cache " + mName);
				}
			}
			else {
				cleanDiskExpiredCacheFiles();
			}
			break;
		default:
			break;
		}
	}

	private void cleanDiskExpiredCacheFiles() {
		File cacheDir = new File(mCacheDir);
		File files[] = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {

				long lastModified = f.lastModified();
				Date now = new Date();
				long ageInMinutes = ((now.getTime() - lastModified) / (1000 * 60));

				if (ageInMinutes >= mExpireTimeInMinutes) {
					Log.d(mName, "DISK cache expiration for file " + f.toString());
					f.delete();
				}
			}

		}

	}

	private void cleanAllDiskCacheFiles() {
		File cacheDir = new File(mCacheDir);
		File files[] = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}

		}

	}

	// Os ficheiros sao escritos no disco e no final da execu‹o sao apagados
	// pela VM
	private void cacheToDisk(String key, byte[] imageData) {

		File cachedFile = new File(mCacheDir + "/" + key);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(cachedFile);
			fos.write(imageData);
			fos.close();
			//A Imagem n‹o ser‡ apagada quando a aplica‹o terminar
			//cachedFile.deleteOnExit();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean existsFileForKey(String Key) {
		File cachedFile = new File(mCacheDir + "/" + Key);
		return cachedFile.exists();

	}

	private byte[] getFileForKey(String Key) throws IOException {

		File cachedFile = new File(mCacheDir + "/" + Key);
		if (!cachedFile.exists()) return null;

		BufferedInputStream istream = new BufferedInputStream(new FileInputStream(cachedFile));
		long fileSize = cachedFile.length();
		if (fileSize > Integer.MAX_VALUE) { throw new IOException("Cannot read files larger than "
				+ Integer.MAX_VALUE + " bytes"); }
		int imageDataLength = (int) fileSize;

		byte[] imageData = new byte[imageDataLength];
		istream.read(imageData, 0, imageDataLength);
		istream.close();

		return imageData;
	}
}
