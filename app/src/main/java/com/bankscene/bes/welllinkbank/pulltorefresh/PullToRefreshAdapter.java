package com.bankscene.bes.welllinkbank.pulltorefresh;

import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.FileUtil;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.pulltorefresh.entity.Status;
import com.bankscene.bes.welllinkbank.pulltorefresh.util.SpannableStringUtils;
import com.bankscene.bes.welllinkbank.pulltorefresh.util.Utils;

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
        try {
            if (!TextUtils.isEmpty(item.getMimg())){
                helper.setImageBitmap(R.id.image,FileUtil.base64ReturnBitmap(item.getMimg()));
            }else {
                helper.setImageBitmap(R.id.image,null);
            }
            helper.setText(R.id.title,item.getMtitle());
            String msg=item.getMcontent();
            ((TextView) helper.getView(R.id.info)).setText(msg);
        } catch (IOException e) {
            e.printStackTrace();
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


}
