package com.bigkoo.convenientbanner.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements CBPageAdapter.Holder<String>{
    private ImageView imageView;

    private List<Map<String, String>> imageInfoMapList = new ArrayList<Map<String,String>>();

    public NetworkImageHolderView(List<Map<String, String>> imageInfoMapList)
    {
        this.imageInfoMapList=imageInfoMapList;
    }

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        RelativeLayout relativeLayout=new RelativeLayout(context);
        imageView = new ImageView(context);
        relativeLayout.addView(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return relativeLayout;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
        imageView.setImageResource(R.mipmap.bannertest);

        final Context fContext = context;
        Trace.e("data==",data);
        Glide.with(context).load(data).into(imageView);
//        ImageLoader.getInstance().displayImage("http://192.168.2.124:8780/pweb/zh_CN/ynrcb/images/loginheadR.jpg",imageView);
//        ImageLoader.getInstance().displayImage(CommDictAction.FHttpUrl+data,imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击事件
                if(imageInfoMapList != null &&
                        imageInfoMapList.size() > position)
                {
                    Map<String, String> temp = imageInfoMapList.get(position);
                    String url=temp.get("url");
                    Trace.e("跳转到",url);
                    Intent in=new Intent(fContext, WebViewActivity.class);
                    in.putExtra("url",url);
                    fContext.startActivity(in);

                }
            }
        });
    }
}
