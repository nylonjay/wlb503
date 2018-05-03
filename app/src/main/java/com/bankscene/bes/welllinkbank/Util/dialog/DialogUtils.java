package com.bankscene.bes.welllinkbank.Util.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.adapter.finance.AccountlistAdapter;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseActivity;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.AccountSpannerItem;

import java.util.ArrayList;
import java.util.Locale;

import de.greenrobot.event.EventBus;


public class DialogUtils {

    private Context context;


    public DialogUtils(Context context) {
        this.context = context;
    }

    public void ShowDialogOne(String title, String msg, String submit,
                              final DialogCallBack callback) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();

        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setContentView(R.layout._view_dialog_one);

        TextView Title = (TextView) window.findViewById(R.id.title);
        TextView Msg = (TextView) window.findViewById(R.id.msg);
        TextView Submit = (TextView) window.findViewById(R.id.submit);

        Title.setText(title);
        Msg.setText(msg);
        Submit.setText(submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onPositive();
            }
        });
    }

    public void ShowDialogTwo(String title, String msg, String submit, String cancel,
                              final DialogCallBack callback) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setContentView(R.layout._view_dialog_two);

        TextView Title = (TextView) window.findViewById(R.id.title);
        TextView Msg = (TextView) window.findViewById(R.id.msg);
        TextView Submit = (TextView) window.findViewById(R.id.submit);
        TextView Cancel = (TextView) window.findViewById(R.id.cancel);

        Title.setText(title);
        Msg.setText(msg);
        Submit.setText(submit);
        Cancel.setText(cancel);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onPositive();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onNegative();
            }
        });
    }

    public void ShowDialogList(String title, final ArrayList<String> datas, final AccountSpannerItem Asi){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();

        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setContentView(R.layout.view_dialog_accounts);

        TextView Title = (TextView) window.findViewById(R.id.title);
        ListView mlist=(ListView) window.findViewById(R.id.data_listview);
        int inSelect = 0;
        for (int i=0;i<datas.size();i++){
            if (datas.get(i).equals(Asi.getResult())){
                inSelect=i;
            }
        }
        final AccountlistAdapter acnoAdapter=new AccountlistAdapter(datas,context);
        acnoAdapter.restoreRadiobtns(inSelect);
        Title.setText(title);
        mlist.setAdapter(acnoAdapter);
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Asi.setResult(datas.get(position));
                acnoAdapter.restoreRadiobtns(position);
                dialog.dismiss();
            }
        });
    }
    public void ShowDialogList2(String title, final ArrayList<String> datas, final Activity activity){
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();

        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setContentView(R.layout.view_dialog_accounts);

        TextView Title = (TextView) window.findViewById(R.id.title);
        ListView mlist=(ListView) window.findViewById(R.id.data_listview);
         int inSelect = 0;

        if (DBHelper.getDataByKey(DataKey.language).equals("zh")){
            inSelect=0;
        }else {
            inSelect=1;
        }
        final int pos=inSelect;
        final AccountlistAdapter acnoAdapter=new AccountlistAdapter(datas,context);
        Title.setText(title);
        mlist.setAdapter(acnoAdapter);
        acnoAdapter.restoreRadiobtns(inSelect);
        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        if (pos!=1)
                        SwitchLanguage("zh",activity);
                        break;
                    case 0:
                        if (pos!=0)
                        SwitchLanguage("en",activity);
                        break;
                    default:
                        break;
                }
//                acnoAdapter.restoreRadiobtns(position);
                dialog.dismiss();
            }
        });
    }
    private void SwitchLanguage(String sta,Activity activity) {
//        Trace.e("minefragment","中英切換");
//        String sta=getResources().getConfiguration().locale.getLanguage();
//        if(sta.equals("zh")){
//            Locale.setDefault(Locale.ENGLISH);
//            Configuration config = activity.getBaseContext().getResources().getConfiguration();
//            config.locale = Locale.ENGLISH;
//            activity.getBaseContext().getResources().updateConfiguration(config
//                    , activity.getBaseContext().getResources().getDisplayMetrics());
//        }else{
//            Locale.setDefault(Locale.CHINESE);
//            Configuration config =activity. getBaseContext().getResources().getConfiguration();
//            config.locale = Locale.CHINESE;
//            activity.getBaseContext().getResources().updateConfiguration(config
//                    , activity.getBaseContext().getResources().getDisplayMetrics());
//        }
        EventBus.getDefault().post(new MessageEvent(Constant.EVENT_REFRESH_LANGUAGE));
//        ((ShareActivity)activity).refreshSelf();
    }

    private boolean isZh() {
        Locale locale =context. getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        Trace.e("language",language);
        if (language.equals("zh"))
            return true;
        else
            return false;
    }

}
