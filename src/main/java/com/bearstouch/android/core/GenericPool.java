package com.bearstouch.android.core;

import java.util.Stack;

public abstract class GenericPool<T> {

	public static final int	DEFAULT_SIZE	= 16;
	private final Stack<T>	mFreeItems		= new Stack<T>();
	private int					mUnrecycledCount;
	private int					mMaxSize;

	public GenericPool() {
		this(DEFAULT_SIZE);
	}

	public GenericPool(int startSize) {
		this(DEFAULT_SIZE, Integer.MAX_VALUE);
	}

	public GenericPool(int startSize, int maxSize) {
		for (int i = 0; i < startSize; i++) {
			mFreeItems.push(onAllocatePoolItem());
		}
		mUnrecycledCount = 0;
		mMaxSize = maxSize;
	}

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

	public synchronized void recycle(T item) {
		if (item == null) { throw new IllegalArgumentException(
				"Cannot free null item!"); }
		mUnrecycledCount--;
		mFreeItems.push(item);
		onHandleRecycleItem(item);
	}

	public int getSize() {
		return mUnrecycledCount + mFreeItems.size();
	}

	protected abstract T onAllocatePoolItem();

	protected abstract void onHandleRecycleItem(T itemRecycled);

	public synchronized void clearPool() {
		mFreeItems.clear();
	}
}
