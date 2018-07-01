package com.bigkoo.convenientbanner.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.adapter.GlideImageLoader;
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
        View v=LayoutInflater.from(context).inflate(R.layout.layout_banner,null);
        imageView=v.findViewById(R.id.iv_img);
        return v;
    }

    @Override
    public void UpdateUI(final Context context, final int position, String data) {
        imageView.setImageResource(R.mipmap.banner);

        final Context fContext = context;
        Trace.e("data==",data+"");
        new GlideImageLoader().displayImage(fContext,data,imageView);
//        Glide.with(context).load(data).into(imageView);
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
                    if (TextUtils.isEmpty(url)){
                        return;
                    }
                    Intent in=new Intent(fContext, WebViewActivity.class);
//                    if (url.endsWith("/")){
//                        url=url.substring(0,url.length()-1);
//                    }
                    in.putExtra("url",url);
                    in.putExtra("showActionBar","true");
                    in.putExtra("title",fContext.getResources().getString(R.string.details));
                    fContext.startActivity(in);

                }
            }
        });
    }
}
