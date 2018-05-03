package com.bigkoo.convenientbanner.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bigkoo.convenientbanner.CBPageAdapter;

/**
 * Created by Sai on 15/8/4.
 * 本地图片Holder例子
 */
public class LocalImageHolderView implements CBPageAdapter.Holder<Integer>{
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context, final int position, Integer data) {
        imageView.setImageResource(data);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件
//                Toast.makeText(view.getContext(),"未实现",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(context, WebViewActivity.class);
                in.putExtra("showActionBar","true");
                in.putExtra("url","http://www.wlbank.com.mo");
                in.putExtra("title",context.getResources().getString(R.string.wlb_hompage));
                context.startActivity(in);
            }
        });
    }
}
