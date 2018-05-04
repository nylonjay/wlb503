package com.bankscene.bes.welllinkbank.activity.NoticeInfo;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.bankscene.bes.welllinkbank.pulltorefresh.entity.Status;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class NoticeDetail extends HttpActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    String key="";
    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.iv_main)
//    ImageView iv_main;
    @BindView(R.id.tv_mcontent)
    TextView tv_mcontent;
    List<Status> stas;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key=getIntent().getStringExtra("key");
        gson=new Gson();
        getDetail(key);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_notice_detail;
    }

    public void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.notice_detail), R.string.wlb_arrow_l, "", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
            NoticeDetail.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
//        actionBar.setStatusBarHeight();
    }
    private void getDetail(String key) {
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        params.put("Mkey",key);
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.MadvmsgDetailedQry)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
//                        Response response=info.getResponse();
                        try {
                            JSONObject jsonObject=new JSONObject(info.getRetDetail());
                            String list=jsonObject.getString("List");
                            Type type=new TypeToken<List<Status>>(){}.getType();
                            stas=gson.fromJson(list,type);
                            Status status=stas.get(0);
                            Message message=new Message();
                            message.what=1;
                            message.obj=status;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {

                    }
                });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Status status= (Status) msg.obj;
                    tv_title.setText(status.getMtitle());
//                        iv_main.setImageBitmap(FileUtil.base64ReturnBitmap(status.getMimg()));
                    tv_mcontent.setText(status.getMcontent());
                    break;
            }
        }
    };

}
