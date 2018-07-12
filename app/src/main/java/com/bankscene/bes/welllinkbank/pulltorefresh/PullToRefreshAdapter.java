package com.bankscene.bes.welllinkbank.pulltorefresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.FileUtil;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.adapter.GlideImageLoader;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.pulltorefresh.entity.Status;
import com.bankscene.bes.welllinkbank.pulltorefresh.util.SpannableStringUtils;
import com.bankscene.bes.welllinkbank.pulltorefresh.util.Utils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 文 件 名: PullToRefreshAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 19:55
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class PullToRefreshAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {
    List<Status> status;
    public PullToRefreshAdapter() {
        super( R.layout.layout_animation, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
//        switch (helper.getLayoutPosition()%
//                3){
//            case 0:
//                helper.setImageResource(R.id.image,R.mipmap.pub_pic_model1);
//                break;
//            case 1:
//                helper.setImageResource(R.id.image,R.mipmap.pub_pic_model2);
//                break;
//            case 2:
//                helper.setImageResource(R.id.image,R.mipmap.pub_pic_model3);
//                break;
//        }
        ImageView img=(ImageView) helper.getView(R.id.image);
        if (!TextUtils.isEmpty(item.getMimg())){
            Trace.e("imgpath==",item.getMimg());
            try {
                helper.setImageBitmap(R.id.image,FileUtil.base64ReturnBitmap(item.getMimg()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            new GlideImageLoader().displayImage(BaseApplication.getInstance(),item.getMimgurl(),img);
        }
        if ("zh".equals(DBHelper.getDataByKey(DataKey.language))){
            helper.setText(R.id.title,item.getMtitle());
            String msg=item.getMcontent();
            ((TextView) helper.getView(R.id.info)).setText(msg);
        }else {
            helper.setText(R.id.title,item.getMengtitle());
            String msg=item.getMengcontent();
            ((TextView) helper.getView(R.id.info)).setText(msg);
        }

        //        ( (TextView)helper.getView(R.id.info)).setText(SpannableStringUtils.getBuilder(msg).append("landscapes and nedes").setClickSpan(clickableSpan).create());
//        ( (TextView)helper.getView(R.id.tweetDate)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            ToastUtils.showShortToast("事件触发了 landscapes and nedes");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Utils.getContext().getResources().getColor(R.color.clickspan_color));
            ds.setUnderlineText(true);
        }
    };

    public Bitmap getBitmapFromPath(String path) {

        if (!new File(path).exists()) {
            System.err.println("getBitmapFromPath: file not exists");
            return null;
        }
        // Bitmap bitmap = Bitmap.createBitmap(1366, 768, Config.ARGB_8888);
        // Canvas canvas = new Canvas(bitmap);
        // Movie movie = Movie.decodeFile(path);
        // movie.draw(canvas, 0, 0);
        //
        // return bitmap;

        byte[] buf = new byte[1024 * 1024];// 1M
        Bitmap bitmap = null;

        try {

            FileInputStream fis = new FileInputStream(path);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
            if (bitmap == null) {
                System.out.println("len= " + len);
                System.err
                        .println("path: " + path + "  could not be decode!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return bitmap;
    }


}
