package com.bankscene.bes.welllinkbank.activity.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.FileUtil;
import com.bankscene.bes.welllinkbank.Util.GifSizeFilter;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.adapter.common.ImageShape;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.biz.NameImgPair;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.bankscene.bes.welllinkbank.core.PermissionResult;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.module.User;
import com.bankscene.bes.welllinkbank.view.ActionSheet;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.callback.Callback;
import com.okhttplib.callback.ProgressCallback;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.ui.MatisseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class PersonInfo extends HttpActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.iv_round)
    ImageView iv_round;
    User user;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_en_name)
    TextView tv_en_name;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_name;
    private ActionSheet mActionSheet;
    private final int REQUEST_CODE_CHOOSE=23;
    private final int REQUEST_CODE_CAPTURE=24;
    private Gson gson;
    String fileD=android.os.Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + File.separator
            + "nylon";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        gson=new Gson();
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MessageEvent messageEvent){
        switch (messageEvent.getWhat()){
            case 64:
                Trace.e("设置图片","");
                Bitmap bm= (Bitmap) messageEvent.getO();
                iv_round.setImageBitmap(bm);
                break;
        }
    }
    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.personal_info), R.string.wlb_arrow_l,"", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                PersonInfo.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void initView() {
        super.initView();
        user = BaseApplication.getInstance().getUser();
        tv_name.setText(user.getName()+""+user.getEnglish_name());
        tv_en_name.setText(user.getEnglish_name());
        tv_phone_name.setText(user.getMobileNum());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.userIcon))){
//            FileUtil.base64ToBitmap(user.getImage_base64(),BaseApplication.baseImagePath);
            Trace.e("image_path==",DBHelper.getDataByKey(DataKey.userIcon));
            setImage(iv_round, DBHelper.getDataByKey(DataKey.userIcon), ImageShape.CIRCLE,
                    R.mipmap.icon_photo, R.mipmap.icon_photo);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_person_info;
    }
    public void onHeadImgChange(View view){
        InitActionSheet();

    }

    private void InitActionSheet() {
        mActionSheet=new ActionSheet(this);
        mActionSheet.addMenuItem(getResources().getString(R.string.camera), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askPermission(CAMERA_CODE, new PermissionResult() {
                    @Override
                    public void executeMethod() {
                        GotoCapture();
                    }

                    @Override
                    public void onDenied() {

                    }
                });
            }
        });
        mActionSheet.addMenuItem(getResources().getString(R.string.album), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToMartisseActivity();
            }
        });
        askPermission(READ_EXTERNAL_STORAGE_CODE, new PermissionResult() {
            @Override
            public void executeMethod() {
                mActionSheet.show();
            }

            @Override
            public void onDenied() {

            }
        });

    }

    private void GoToMartisseActivity() {
        Matisse.from(PersonInfo.this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
//                .capture(false)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.wlllbk.fileprovider"))
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forAddResult(REQUEST_CODE_CHOOSE);
    }

    private void GotoCapture() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivityForResult(intent,REQUEST_CODE_CAPTURE);
    }
    @SuppressLint("SimpleDateFormat")
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        user.setImage_path(mediaFile.getAbsolutePath());
        return Uri.fromFile(mediaFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode){
            case REQUEST_CODE_CAPTURE://拍照所得
                if (resultCode==Activity.RESULT_OK){
                    Trace.e("path==",user.getImage_path()+"");
                    String target=fileD+"/"+System.currentTimeMillis()+".png";

                    File file=new File(fileD);
                    if (!file.exists()){
                        file.mkdirs();
                    }
                    File f= scalFile(new File(user.getImage_path()),target);
//                    uploadFile(user.getImage_path());
                    UploadBase64(f);
                }

                break;
            case REQUEST_CODE_CHOOSE://相冊選擇的
                if (resultCode== Activity.RESULT_OK){
                    ArrayList<String> paths=intent.getStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH);
                    if (null!=paths&&paths.size()!=0){
                        String path=paths.get(0);
                        Trace.e("path---",path+"");
                        Trace.e("size---",new File(path).length()+"");
                        user.setImage_path(path);
                        String target=fileD+"/"+System.currentTimeMillis()+".png";
                        File file=new File(fileD);
                        if (!file.exists()){
                            file.mkdirs();
                        }
                        File f= scalFile(new File(path),target);
                        UploadBase64(f);
                    }
                }
                break;
        }
    }

    private void uploadFile(String path){
        if(TextUtils.isEmpty(path)){
            Toast.makeText(this, "请选择上传文件！", Toast.LENGTH_LONG).show();
            return;
        }
        Map params = new HashMap();
        params.put("_ChannelId","PMBS");
        HttpInfo info = HttpInfo.Builder()
                .addParams(params)
                .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHead("cookie", DBHelper.getDataByKey(DataKey.cookie))
                .setUrl(CommDictAction.PictureModify)
                .addUploadFile(new UploadFileInfo()
                        .setInterfaceParamName("Picture")
                        .setFilePathWithName(path)
                        .setProgressCallback(new ProgressCallback(){
                            @Override
                            public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
//                                uploadProgress.setProgress(percent);
//                                LogUtil.d(TAG, "上传进度：" + percent);\
                                Trace.e("上传进度",percent+"%");
                            }

                            @Override
                            public void onResponseMain(String filePath, HttpInfo info) {
                                Trace.e("filePath==",filePath);
                                Trace.e("Detail==",info.getRetDetail());
                                noticeUtils.showNotice(getResources().getString(R.string.UPLOAD_SUCCEED));
                                setImage(iv_round, user.getImage_path(), ImageShape.CIRCLE,
                                        R.mipmap.icon_photo, R.mipmap.icon_photo);
                            }
                        }))
                .build();
        OkHttpUtil.getDefault(this).doUploadFileAsync(info);
    }
    private void UploadBase64(final File file) {
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        params.put("Picture",FileUtil.fileToBase64(file).replace("+", "%2B"));
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHead("cookie",DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.PictureModify)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
//                        Response response=info.getResponse();
                        String result=info.getRetDetail();
                        if (!TextUtils.isEmpty(result)){
                            try {
                                JSONObject json=new JSONObject(result);
                                if (json.has(_REJCODE)){
                                    if (json.opt(_REJCODE).equals("000000")){
                                        DBHelper.insert(new Data(DataKey.userIcon,file.getAbsolutePath()));
                                        noticeUtils.showNotice(getResources().getString(R.string.modify_head_img_succeed));
                                        setImage(iv_round, file.getAbsolutePath(), ImageShape.CIRCLE,
                                                R.mipmap.icon_photo, R.mipmap.icon_photo);
                                        saveName_Image(DBHelper.getDataByKey(DataKey.userName),file.getAbsolutePath());
                                    }else {
                                        noticeUtils.showNotice(json.optString(_REJMSG));
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {

                    }
                });
    }
    private void saveName_Image(String loginId, String imagepath) {
        List<NameImgPair> nips;
        Type type=new TypeToken<List<NameImgPair>>(){}.getType();
        NameImgPair nip=new NameImgPair();
        nip.setLoginId(loginId);
        nip.setImagePath(imagepath);
        String Nips=DBHelper.getDataByKey(DataKey.nips);
        if (TextUtils.isEmpty(Nips)){
            nips=new ArrayList<>();
        }else {
            nips=gson.fromJson(Nips,type);
        }
        for (NameImgPair np:nips){
            if (np.getLoginId().equals(loginId)){
                np.setImagePath(imagepath);
            }
        }
        String FinalNipstr=gson.toJson(nips,type);
        DBHelper.insert(new Data(DataKey.nips,FinalNipstr));
    }

}
