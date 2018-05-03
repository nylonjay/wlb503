package com.bankscene.bes.welllinkbank.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.bankscene.bes.welllinkbank.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ActionSheet extends Dialog {

	private Context mContext;

	private List<String> mItemKeys = new ArrayList<String>();
	private HashMap<String, View.OnClickListener> mItems = new HashMap<String, View.OnClickListener>();

	private String mCancelText;
	private View.OnClickListener mCancelClickListener;

	private int mWindowPadding;
	private int mMenuItemLayoutPadding;
	
	private int mRowHeight;

	public ActionSheet(Context context) {
		super(context, R.style.ActionSheetDialog);
		// TODO Auto-generated constructor stub
		mContext = context;
		mWindowPadding = context.getResources().getDimensionPixelSize(
				R.dimen.activity_horizontal_margin);
		mMenuItemLayoutPadding = context.getResources().getDimensionPixelSize(R.dimen.menu_padding);
		mRowHeight = context.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_stacked_max_height);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setGravity(Gravity.BOTTOM);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;

		LinearLayout contentView = new LinearLayout(mContext);
		contentView.setPadding(mWindowPadding, 0, mWindowPadding, 0);
		contentView.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(screenWidth,
				LayoutParams.WRAP_CONTENT);
		setContentView(contentView, lp);
		if (mItemKeys.size() > 0) {
			LinearLayout itemLayout = new LinearLayout(mContext);
			itemLayout.setOrientation(LinearLayout.VERTICAL);
			itemLayout.setPadding(0, mMenuItemLayoutPadding, 0,
					mMenuItemLayoutPadding);
			int count = mItemKeys.size();
			for (int i = 0; i < count; i++) {
				final String item = mItemKeys.get(i);
				Button itemButton = new Button(mContext);
				itemButton.setHeight(mRowHeight);
				itemButton.setText(item);

				if (count == 1) {
					itemButton
							.setBackgroundResource(R.drawable.rounded_rectangle_selector);
				} else if (i == 0) {
					itemButton
							.setBackgroundResource(R.drawable.rounded_rectangle_head_selector);
				} else if (i == count - 1) {
					itemButton
							.setBackgroundResource(R.drawable.rounded_rectangle_bottom_selector);
				} else {
					itemButton
							.setBackgroundResource(R.drawable.rectangle_selector);
				}

				itemButton.setTextColor(mContext.getResources().getColor(
						R.color.blackText));
				itemButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mItems.get(item).onClick(v);
						dismiss();
					}
				});
				LayoutParams llp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				itemLayout.addView(itemButton, llp);
				if (i != count - 1) {
					ImageView divider = new ImageView(mContext);
					divider.setBackgroundColor(Color.GRAY);
					LayoutParams lpDivider = new LayoutParams(
							LayoutParams.MATCH_PARENT, 1);
					itemLayout.addView(divider, lpDivider);
				}
			}
			contentView.addView(itemLayout);
		}

		if (mCancelText == null) {
			mCancelText = mContext.getResources().getString(
					R.string.cancle_1);
		}
		LinearLayout cancleLayout = new LinearLayout(mContext);
		cancleLayout.setOrientation(LinearLayout.VERTICAL);
		int bottom = mContext.getResources().getDimensionPixelSize(
				R.dimen.linear_menu_bottom_padding);
		cancleLayout.setPadding(0, bottom, 0, bottom);

		Button cancelbutton = new Button(mContext);
		cancelbutton.setHeight(mRowHeight);
		cancelbutton.setText(mCancelText);
		cancelbutton.setTextColor(mContext.getResources().getColor(
				R.color.blackText));
		cancelbutton
				.setBackgroundResource(R.drawable.rounded_rectangle_selector);
		cancelbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mCancelClickListener != null) {
					mCancelClickListener.onClick(v);
				}
				dismiss();
			}
		});
		cancelbutton.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		cancleLayout.addView(cancelbutton);
		contentView.addView(cancleLayout);

	}

	public ActionSheet addMenuItem(String title, View.OnClickListener listener) {
		mItemKeys.add(title);
		mItems.put(title, listener);
		return this;
	}

	public ActionSheet addMenuItem(int resId, View.OnClickListener listener) {
		String title = mContext.getResources().getString(resId);
		mItemKeys.add(title);
		mItems.put(title, listener);
		return this;
	}

	public ActionSheet setCancelButton(String title, View.OnClickListener listener) {
		mCancelText = title;
		mCancelClickListener = listener;
		return this;
	}

	public ActionSheet setCancelButton(int resId, View.OnClickListener listener) {
		mCancelText = mContext.getResources().getString(resId);
		mCancelClickListener = listener;
		return this;
	}

}
