package com.bearstouch.android.core.ui;
import java.util.ArrayList;
import java.util.List;
import com.bearstouch.android.core.R;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;


/**
 * 
 * @author H�lder Vasconcelos heldervasc@bearstouch.com
 *
 */

public class HomeStackLayout extends ViewGroup {

	public static final int			HORIZONTAL		= 0;
	public static final int			VERTICAL			= 1;

	private List<HomeStackItem>	mItems;
	private int							mOrientation	= VERTICAL;
	private static final float		ICON_SIZE		= 140;

	private int							mWidthSize		= -1;
	private int							mHeightSize		= -1;
	private Context mContext;

	
	/**
	 * @param context
	 */
	public HomeStackLayout(Context context) {
		super(context);
		initLayout();
		
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public HomeStackLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initLayout();
	}

	/**
	 * 
	 */
	private void initLayout() {
		mItems = new ArrayList<HomeStackItem>();
		setWillNotDraw(false);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		setMeasuredDimension(widthSize, heightSize);

	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		removeAllItems();
		mWidthSize = r - l;
		mHeightSize = b - t;

		if (mOrientation == VERTICAL) {
			layoutVertical(l, t, r, b);
		}
		else {
			layoutHorizontal();
		}

	}

	/**
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	private void layoutVertical(int l, int t, int r, int b) {

		stackItems(mItems, l, t);
	}

	/**
	 * 
	 */
	private void layoutHorizontal() {

	}

	/**
	 * @param mItems
	 * @param childLeft
	 * @param childTop
	 */
	private void stackItems(List<HomeStackItem> mItems, int childLeft, int childTop) {

		int widthSpec;
		int heightSpec;

		float density = mContext.getResources().getDisplayMetrics().density;
		
		int columns = (int) (mWidthSize / (density*ICON_SIZE));
		int cellsize = mWidthSize / columns;
		int columnIndex = 0;
		int lineIndex = 0;
		final int count = mItems.size();
		for (int i = 0; i< count; i++) {

			HomeStackItem item = mItems.get(i);
			View childView = item.getView();
			
			if(childView==null){
				throw new IllegalStateException(
						"HomeStackLayout childview is null");
			}			
			
			childView.setLayoutParams(new LayoutParams(cellsize,cellsize));
			widthSpec = MeasureSpec.makeMeasureSpec(cellsize, MeasureSpec.EXACTLY);
			heightSpec = MeasureSpec.makeMeasureSpec(cellsize, MeasureSpec.EXACTLY);
			childView.measure(widthSpec, heightSpec);

			addViewInLayout(childView, -1, childView.getLayoutParams());
			
			int leftpos = childLeft + columnIndex * cellsize;
			int toppos = childTop + lineIndex * cellsize;
			int rightpos = childLeft + cellsize + columnIndex * cellsize;
			int botpos = childTop + cellsize + lineIndex * cellsize;
			childView.layout(leftpos, toppos, rightpos, botpos);
			columnIndex++;

			if (columnIndex >= columns) {
				lineIndex++;
				columnIndex = 0;
			}

		}

	}

	/**
	 * 
	 */
	private void removeAllItems() {
		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			removeViewAt(i);
		}
	}

	/**
	 * @param item
	 */
	public void addItem(HomeStackItem item) {
		mItems.add(item);

	}

	/**
	 * @param index
	 * @param item
	 */
	public void addItem(int index, HomeStackItem item) {
		mItems.add(index, item);

	}

	/**
	 * @param item
	 */
	public void removeItem(HomeStackItem item) {

		mItems.remove(item);
	}

	/**
	 * @param index
	 */
	public void removeItem(int index) {

		mItems.remove(index);
	}

	/**
	 * 
	 */
	public void removeItems() {
		mItems.clear();

	}

	/**
	 * @param drawableid
	 * @param stringid
	 * @param intent
	 */
	public void createHomeStackItem(int drawableid, int stringid, Intent intent) {

		HomeStackItem item = new HomeStackItem(getContext(), drawableid, stringid, intent);
		mItems.add(item);
	}


	public static class HomeStackItem {

		private Context			mContext;
		private LayoutInflater	mInflater;
		private LinearLayout		mLayout=null;
		private ImageView			mView;
		private TextView			mTextView;

		/**
		 * @param context
		 * @param drawableid
		 * @param stringid
		 * @param intent
		 */
		public HomeStackItem(Context context, int drawableid,
				int stringid, Intent intent) {

			mContext = context;
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mLayout = (LinearLayout) mInflater.inflate(R.layout.home_stack_item, null);
			mView = (ImageView) mLayout.findViewById(R.id.item_drawable);
			mView.setImageResource(drawableid);
			mTextView = (TextView) mLayout.findViewById(R.id.item_text);
			mTextView.setText(stringid);
			setIntent(intent);
		}

		/**
		 * @param intent
		 */
		private void setIntent(Intent intent) {

			final Intent inte = intent;
			mLayout.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					mContext.startActivity(inte);
				}
			});
		}

		/**
		 * @return
		 */
		protected View getView() {
			return mLayout;
		}
	}

}
