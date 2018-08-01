/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bankscene.bes.welllinkbank.activity.pdf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.activity.MenuList;
import com.bankscene.bes.welllinkbank.activity.pdf.adapter.BasePDFPagerAdapter;
import com.bankscene.bes.welllinkbank.activity.pdf.asset.CopyAsset;
import com.bankscene.bes.welllinkbank.activity.pdf.asset.CopyAssetThreadImpl;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.core.BaseActivity;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;


public class AssetOnSDActivity extends BaseActivity {
    final String[] sampleAssets = {"adobe.pdf", "sample.pdf"};
    LinearLayout ll_contaner;
    TranslucentActionBar actionBar;
    PDFViewPager pdfViewPager;
    File pdfFolder;
    String path="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle(R.string.asset_on_sd);
//        setContentView(R.layout.layout_pdf);
        path=getIntent().getStringExtra("pdf");
        pdfFolder = Environment.getExternalStorageDirectory();
        actionBar= (TranslucentActionBar) findViewById(R.id.actionBar);
        ll_contaner= (LinearLayout) findViewById(R.id.ll_container);
        copyAssetsOnSDCard();
    }

    protected void copyAssetsOnSDCard() {
        final Context context = this;
        pdfViewPager = new PDFViewPager(context, getPdfPathOnSDCard());
        ll_contaner.addView(pdfViewPager);
//        rootview.add
//        setContentView(pdfViewPager);
//        CopyAsset copyAsset = new CopyAssetThreadImpl(getApplicationContext(), new Handler(), new CopyAsset.Listener() {
//            @Override
//            public void success(String assetName, String destinationPath) {
//                pdfViewPager = new PDFViewPager(context, getPdfPathOnSDCard());
//                setContentView(pdfViewPager);
//            }
//
//            @Override
//            public void failure(Exception e) {
//                e.printStackTrace();
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

//        for (String asset : sampleAssets) {
//            copyAsset.copy(asset, new File(pdfFolder, asset).getAbsolutePath());
//        }
    }

    protected String getPdfPathOnSDCard() {
//        File f = new File(pdfFolder, "adobe.pdf");
        File f = new File(path);
        return f.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (pdfViewPager != null) {
            ((BasePDFPagerAdapter) pdfViewPager.getAdapter()).close();
        }


    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.monthy_statement), R.string.wlb_arrow_l, "", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
            AssetOnSDActivity.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_pdf;
    }

    public static void open(Context context) {
        Intent i = new Intent(context, AssetOnSDActivity.class);
        context.startActivity(i);
    }
}