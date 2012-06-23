package com.bearstouch.android.core.ui;
import java.util.LinkedList;
import com.bearstouch.android.core.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
/**
 * 
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */
public class BearActionBar extends LinearLayout {

	private RelativeLayout						mActionBarView;
	private ImageView								mLogoViewIcon;																																																											
	private Drawable								mLogoDrawable	= null;					
	private LinearLayout							mLogoViewLayout;
	private TextView								mTitleView;																																		
	private String									mTitleText		= "";
//	private Drawable								mDividerDrawable;																														
//	private float									mDividerWidth;
//	private ImageView							 mDividerHomeLayout;
	private LinearLayout							mActionsLayout;
	private LinkedList<BearActionBarItem>	mItems;
	private Context								mContext;
	private static final String				LOGTAG			= "BearActionBar";
	private LayoutInflater						mInflater;

	
	
	/**
	 * @param context
	 */
	public BearActionBar(Context context) {
		this(context, null);
		mContext = context;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BearActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defstyle
	 */
	public BearActionBar(Context context, AttributeSet attrs, int defstyle) {
		super(context, attrs);
		mContext = context;
		init(attrs);

	}

	/**
	 * @param attrs
	 */
	private void init(AttributeSet attrs) {

		mItems=new LinkedList<BearActionBarItem>();
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		

		// Get Resources
		TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.BearActionBar);
		mLogoDrawable = a.getDrawable(R.styleable.BearActionBar_action_bar_logo_drawable);	
		mTitleText = a.getString(R.styleable.BearActionBar_action_bar_title);
		//mTitleText=a.getNonResourceString(R.styleable.BearActionBar_action_bar_title);
		//mDividerWidth = a.getDimensionPixelSize(R.styleable.BearActionBar_action_bar_divider_width, 0);

		// Inflate Action Bar Layout
		mActionBarView = (RelativeLayout) mInflater.inflate(
				R.layout.action_bar, null);
		//mActionBarView.setBackgroundResource(R.drawable.action_bar);
		mLogoViewIcon = (ImageView) mActionBarView
				.findViewById(R.id.logoDrawable);
		mLogoViewLayout = (LinearLayout) mActionBarView
				.findViewById(R.id.home_drawable_layout);
		mLogoViewIcon.setBackgroundDrawable(mLogoDrawable);

//		mDividerHomeLayout = (ImageView) mActionBarView
//				.findViewById(R.id.titleDividerLayout);

		mTitleView = (TextView) mActionBarView
				.findViewById(R.id.action_bar_title_text);
		
		mTitleView.setText(mTitleText);
		mActionsLayout = (LinearLayout) mActionBarView
				.findViewById(R.id.itemsLayout);
		
		addView(mActionBarView);

	}

	/**
	 * @param resid
	 */
	public void setHomeLogo(int resid) {
		mLogoDrawable = mContext.getResources().getDrawable(resid);
		mLogoViewIcon.setBackgroundDrawable(mLogoDrawable);

	}

	/**
	 * @param image
	 */
	public void setHomeLogo(Drawable image) {
		mLogoDrawable = image;
		mLogoViewIcon.setBackgroundDrawable(mLogoDrawable);
	}

	/**
	 * @param resid
	 */
	public void setTitle(int resid) {
		mTitleText = mContext.getResources().getString(resid);
		mTitleView.setText(mTitleText);
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		mTitleText = title;
		mTitleView.setText(mTitleText);
	}

	/**
	 * @param homelistener
	 */
	public void setOnLogoClickListener(OnClickListener homelistener) {
		mLogoViewLayout.setOnClickListener(homelistener);
	}

	/**
	 * @param position
	 * @param actionlistener
	 */
	public void setOnActionClickListener(int position,
			OnClickListener actionlistener) {
		mItems.get(position).setOnClickListener(actionlistener);
		mActionsLayout.getChildAt(position);
	}

	/**
	 * @param position
	 * @return
	 */
	public BearActionBarItem getItem(int position) {
		return mItems.get(position);
	}

	/**
	 * @param item
	 */
	public void addItem(BearActionBarItem item) {
		mItems.push(item);
		mActionsLayout.addView(item.getItemView());

	}

	/**
	 * @param position
	 * @param item
	 */
	public void addItem(int position, BearActionBarItem item) {
		mItems.add(position, item);
		mActionsLayout.addView(item.getItemView());

	}

	/**
	 * @return
	 */
	public int getItemsCount() {
		return mItems.size();
	}

	/**
	 * @param position
	 */
	public void removeItem(int position) {
		mItems.remove(position);
		mActionsLayout.removeViewAt(position);
	}

	/**
	 * @param item
	 */
	public void removeItem(BearActionBarItem item) {
		mItems.remove(item);
		mActionsLayout.removeView(item.getItemView());
	}

	/**
	 * 
	 */
	public void removeItems() {
		mItems.clear();
		mActionsLayout.removeAllViews();
	}

	/**
	 * @param description
	 * @param resid
	 * @return
	 */
	public BearActionBarItem createItem(String description,int resid){
		
		return new BearActionBarItem(this, mContext, resid, description);
	}
	
	public static class BearActionBarItem {

		protected Context			mContext					= null;
		private BearActionBar	mActionBar;
		protected int				mDrawable				= -1;
		protected String			mContentDescription	= "";
		private LinearLayout		mLayout;
		private ImageView			mView;
		private OnClickListener	mOnClickListener			= null;
		private LayoutInflater	mInflater;


		/**
		 * @param ActionBar
		 * @param context
		 * @param drawableid
		 * @param contentdescription
		 */
		public BearActionBarItem(BearActionBar ActionBar, Context context,
				int drawableid, String contentdescription) {
			mContext = context;
			mActionBar = ActionBar;
			mContentDescription = contentdescription;
			mDrawable = drawableid;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mLayout = (LinearLayout) mInflater.inflate(R.layout.action_bar_item,
					null);
			mView = (ImageView) mLayout
					.findViewById(R.id.action_bar_item_drawable);
			
			mView.setImageResource(drawableid);
		}

		/**
		 * @param clikListener
		 */
		public void setOnClickListener(OnClickListener clikListener) {
			mOnClickListener = clikListener;
			mLayout.setOnClickListener(clikListener);
		}

		/**
		 * @return
		 */
		public View getItemView() {
			return mLayout;
		}

		/**
		 * @param resid
		 */
		public void setDrawable(int resid) {
			mView.setBackgroundResource(resid);
		}

		/**
		 * @param drawable
		 */
		public void setDrawable(Drawable drawable) {
			mView.setBackgroundDrawable(drawable);

		}

		/**
		 * @return
		 */
		public String getDescription() {
			return mContentDescription;
		}

		/**
		 * @param description
		 */
		public void setDescription(String description) {
			mContentDescription = description;
		}
	}

}
