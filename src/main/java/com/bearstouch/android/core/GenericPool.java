package com.bearstouch.android.core;

/**
 * 
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */

import java.util.Stack;

public abstract class GenericPool<T> {

	public static final int	DEFAULT_SIZE	= 16;
	private final Stack<T>	mFreeItems		= new Stack<T>();
	private int					mUnrecycledCount;
	private int					mMaxSize;

	/**
	 * 
	 */
	public GenericPool() {
		this(DEFAULT_SIZE);
	}

	/**
	 * @param startSize
	 */
	public GenericPool(int startSize) {
		this(DEFAULT_SIZE, Integer.MAX_VALUE);
	}

	/**
	 * @param startSize
	 * @param maxSize
	 */
	public GenericPool(int startSize, int maxSize) {
		for (int i = 0; i < startSize; i++) {
			mFreeItems.push(onAllocatePoolItem());
		}
		mUnrecycledCount = 0;
		mMaxSize = maxSize;
	}

	/**
	 * @return
	 */
	public synchronized T get() {

		if (mFreeItems.size() > 0) {
			mUnrecycledCount++;
			return mFreeItems.pop();
		}
		else if (getSize() < mMaxSize) {
			mUnrecycledCount++;
			return onAllocatePoolItem();
		}
		else {
			System.out.println("Pool is depleted with MaxSize=" + getSize());
			return null;
		}

	}

	/**
	 * @param item
	 */
	public synchronized void recycle(T item) {
		if (item == null) { throw new IllegalArgumentException(
				"Cannot free null item!"); }
		mUnrecycledCount--;
		mFreeItems.push(item);
		onHandleRecycleItem(item);
	}

	/**
	 * @return
	 */
	public int getSize() {
		return mUnrecycledCount + mFreeItems.size();
	}

	/**
	 * @return
	 */
	protected abstract T onAllocatePoolItem();

	/**
	 * @param itemRecycled
	 */
	protected abstract void onHandleRecycleItem(T itemRecycled);

	/**
	 * 
	 */
	public synchronized void clearPool() {
		mFreeItems.clear();
	}
}
