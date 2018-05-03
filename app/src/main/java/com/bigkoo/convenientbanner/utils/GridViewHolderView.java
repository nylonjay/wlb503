package com.bigkoo.convenientbanner.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bigkoo.convenientbanner.CBPageAdapter;

/**
 * Created by Sai on 15/8/4.
 * 本地图片Holder例子
 */
public class GridViewHolderView implements CBPageAdapter.Holder<Object>{
    private GridView view;
	
    @Override
    public View createView(Context context) {
    	view = new GridView(context);
    	LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lParams);
		view.setNumColumns(CommDictAction.gridColumn);
		view.setGravity(Gravity.CENTER);
		view.setSelector(new ColorDrawable(Color.TRANSPARENT));
		view.setBackgroundResource(R.drawable.corners_layout_bg);
//		view.setVerticalSpacing(2);
//		view.setHorizontalSpacing(2);
		view.scrollTo(0, 0);
//		view.setBackgroundResource(R.color.red);
//    	view = new ImageView(context);
//    	view.setScaleType(ImageView.ScaleType.FIT_XY);
//    	view = new TextView(context);
        return view;
    }

    @Override
    public void UpdateUI(Context context, final int position, Object data) {
//    	view.setText(data.toString());
    	ArrayList<HashMap<String, Object>> dataList = (ArrayList<HashMap<String, Object>>)data;

    	String[] from = { "icon", "text" };
		int[] to = { R.id.iv, R.id.tv };
		SimpleAdapter adapter = new SimpleAdapter(context, dataList,
				R.layout.mainitem, from, to);
		view.setAdapter(adapter);
		
		final Context fContext = context;
		final int fPosition = position;
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if(fContext instanceof NormalFinanceActivity)
//				{
//					((NormalFinanceActivity)fContext).addGridViewListener(fPosition*CommDictAction.gridItemCount+position);
//				}
//				else if(fContext instanceof CommonFunctionsActivity)
//				{
//					((CommonFunctionsActivity)fContext).addGridViewListener(fPosition*CommDictAction.gridItemCountComm+position);
//				}
			}
		});
    }
}
