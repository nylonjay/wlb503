package com.bankscene.bes.welllinkbank.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.exception.WLBException;
import com.bankscene.bes.welllinkbank.view.WlbEditText;
import com.kh.keyboard.CSIICypher;
import com.kh.keyboard.SecurityCypherException;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;
import com.okhttplib.callback.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * Activity基类：支持网络请求、加载提示框
 * @author zhousf
 */
public class HttpActivity extends ShareActivity implements BaseHandler.CallBack {

    private BaseHandler mainHandler;
    protected String encoding="UTF-8";
    public static final String _REJCODE="_RejCode";
    public static final String _REJMSG="_RejMsg";
//    private LoadingDialog loadingDialog;//加载提示框

    private final int SHOW_DIALOG = 0x001;
    private final int DISMISS_DIALOG = 0x002;
    private final int LOAD_SUCCEED = 0x003;
    private final int LOAD_FAILED = 0x004;
    protected String timestamp;
    protected String hms;
    protected String dbp;
    private final String requestTag = "download-tag-1001";//请求标识
    protected WlbEditText CurrentEditext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    protected void GetTimeStampAndKeyWithoutEditor() {
        QueryPublicKey();
        RefreshTimeStamp();
//        editText.setDbp(dbp);
//        editText.setHms(hms);
//        editText.setTimestamp(timestamp);


//        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"JIAMIJI/";
//        String target=path+"rsa.txt";
//        FileUtil.saveFile(encyped,target);
//        FileUtil.saveFile(encyped.replace("+", "%2B"),target);
    }

    protected void GetTimeStampAndKey(WlbEditText editText) {
        CurrentEditext=editText;
        QueryPublicKey();
        RefreshTimeStamp();
//        editText.setDbp(dbp);
//        editText.setHms(hms);
//        editText.setTimestamp(timestamp);


//        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"JIAMIJI/";
//        String target=path+"rsa.txt";
//        FileUtil.saveFile(encyped,target);
//        FileUtil.saveFile(encyped.replace("+", "%2B"),target);
    }
    public void RefreshTimeStamp(){
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(false);
        doHttpAsyncWhioutDialog(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.RefreshTimeStamp)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String redetail=info.getRetDetail();
                        try {
                            JSONObject result=new JSONObject(redetail);
                            timestamp=result.getString("Timestamp");
                            if (null!=CurrentEditext)
                                CurrentEditext.setTimestamp(timestamp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                    }
                });
    }

    public void QueryPublicKey(){
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(false);
        doHttpAsyncWhioutDialog(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.QryPublicKey)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String redetail=info.getRetDetail();
                        try {
                            JSONObject result=new JSONObject(redetail);
                            dbp=result.optString("DbpPublicKey");
                            hms=result.optString("HsmPublicKey");
                            if (null!=CurrentEditext){
                                CurrentEditext.setDbp(dbp);
                                CurrentEditext.setHms(hms);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                    }
                });
    }

    /**
     * 同步请求
     * @param info 请求信息体
     * @return HttpInfo
     */
    protected HttpInfo doHttpSync(HttpInfo info) {
        //显示加载框
        getMainHandler().sendEmptyMessage(SHOW_DIALOG);
        info = OkHttpUtil.getDefault(this).doSync(info);
        if(info.isSuccessful()){
            //显示加载成功
            getMainHandler().sendEmptyMessage(LOAD_SUCCEED);
        }else {
            //显示加载失败
            getMainHandler().sendEmptyMessage(LOAD_FAILED);
        }
        //隐藏加载框
        getMainHandler().sendEmptyMessageDelayed(DISMISS_DIALOG,1000);
        return info;
    }
    /**
     * 异步请求
     * @param info 请求信息体
     * @param callback 结果回调接口
     */
    protected void doHttpAsync(HttpInfo info, final Callback callback){
        getMainHandler().sendEmptyMessage(SHOW_DIALOG);
        OkHttpUtil.getDefault(this).doAsync(info, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
//                getLoadingDialog().dismiss();
                setProgressDisplay(false);
                callback.onSuccess(info);
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                setProgressDisplay(false);
                callback.onFailure(info);
            }
        });
    }
    protected void doHttpAsyncWhioutDialog(HttpInfo info, final Callback callback){
//        getMainHandler().sendEmptyMessage(SHOW_DIALOG);
        OkHttpUtil.getDefault(this).doAsync(info, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
//                getLoadingDialog().dismiss();
                setProgressDisplay(false);
                callback.onSuccess(info);
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                setProgressDisplay(false);
                callback.onFailure(info);
            }
        });
    }

//    protected LoadingDialog getLoadingDialog(){
//        if(null == loadingDialog){
//            loadingDialog = new LoadingDialog(this);
//            //点击空白处Dialog不消失
//            loadingDialog.setCanceledOnTouchOutside(false);
//        }
//        return loadingDialog;
//    }

    /**
     * 获取通用句柄，自动释放
     */
    protected BaseHandler getMainHandler(){
        if(null == mainHandler){
            mainHandler = new BaseHandler(this, Looper.getMainLooper());
        }
        return mainHandler;
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_DIALOG:
//                getLoadingDialog().showDialog();
                setProgressDisplay(true);
                break;
            case LOAD_SUCCEED:
//                getLoadingDialog().succeed();
                break;
            case LOAD_FAILED:
//                getLoadingDialog().failed();
                break;
            case DISMISS_DIALOG:
//                getLoadingDialog().dismiss();
                setProgressDisplay(false);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if(null != mainHandler)
            mainHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void setActionBar() {

    }

    @Override
    protected int setLayoutId() {
        return 0;
    }

    public static File scalFile(File file, String targetPath){
        long fileSize = file.length();
        Trace.e("filesize===================",fileSize+"");
        final long fileMaxSize = 10* 1024;//超过50K的图片需要进行压缩
        if(fileSize > fileMaxSize){
            try {
                byte[] bytes = getBytesFromFile(file);//将文件转换为字节数组
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅仅解码边缘区域
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                //得到宽高
                int width = options.outWidth;
                int height = options.outHeight;
                float scaleWidth=0f;
                float scaleHeight=0f;
                Matrix matrix=new Matrix();
                if(width>height)
                {
                    scaleWidth=(float)1280/width;
                    scaleHeight=(float)960/height;

                }else{
                    scaleWidth=(float)960/width;
                    scaleHeight=(float)1280/height;
                }
                Bitmap bitmap=BitmapFactory.decodeFile(file.getPath());
                matrix.postScale(scaleWidth, scaleHeight);//执行缩放
                Bitmap resizeBitmap=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                if(resizeBitmap != null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    int quality = 100;
                    int quality = 100;
                    resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    //限制压缩后图片最大为200K，否则继续压缩  直接压缩成1%是因为公司的破服务器只让上传10k以下的图片
                    while (baos.toByteArray().length > fileMaxSize&&quality>1) {
                        baos.reset();
                        quality -=33;
                        if (quality<0){
                            quality=1;

                        }
                        Trace.e("quality===================",quality+"");
                        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    }
                    baos.close();
                    File targetFile = new File(targetPath);
                    if(targetFile.exists()){
                        boolean flag = targetFile.delete();
                        Log.i("ImageUtils.scalFile()", "flag: " + flag);
                    }
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                    return targetFile;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ImageUtils.scalFile()",e.getMessage());
            }
            return null;
        }else{
            return file;
        }
    }
    public static byte[] getBytesFromFile(File f){
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    protected void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void LogOut(){
        Map params = new HashMap();
        params.put("_ChannelId","PMBS");
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.logout)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addHead("cookie", DBHelper.getDataByKey(DataKey.cookie))//添加头参数
//                        .addParam("param", "test")//添加接口参数
                        .addParams(params)
//                        .setDelayExec(2, TimeUnit.SECONDS)//延迟2秒执行
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        Trace.e("logout_",info.getRetDetail().toString());
                        try {
                            JSONObject result= new JSONObject(info.getRetDetail());
                            if (result.has(_REJCODE)&&result.get(_REJCODE).equals("000000")){
                                ToastUtils.showShortToast(getResources().getString(R.string.logout_succed));
                                DBHelper.insert(new Data(DataKey.cookie,""));
                                State.isLogin=false;
                                EventBus.getDefault().post(new MessageEvent(Constant.LOGOUT_SUCCEED));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    protected void downloadFile(final ProgressDialog downloadProgress, final Handler handler, String fileURL) {
        final HttpInfo info = HttpInfo.Builder()
                .addDownloadFile(fileURL,CommDictAction.appFileName, new ProgressCallback() {
                    @Override
                    public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                        downloadProgress.setProgress(percent);
                    }

                    @Override
                    public void onResponseMain(String filePath, HttpInfo info) {
                        downloadProgress.cancel();
                        Trace.e("filename==",filePath+"");
                        Trace.e("info===",info.getRetDetail()+"");
//                        ToastUtils.showShortToast(info.getRetDetail());
                        Message msg=new Message();
                        msg.what=2;
                        msg.obj=info.getRetDetail();
                        handler.sendMessage(msg);
                    }
                })
                .build();
        OkHttpUtil.Builder()
                .setReadTimeout(120)
                .build(requestTag)//绑定请求标识
                .doDownloadFileAsync(info);

    }

}
