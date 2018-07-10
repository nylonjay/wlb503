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
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.activity.pdf.adapter.BasePDFPagerAdapter;
import com.bankscene.bes.welllinkbank.activity.pdf.asset.CopyAsset;
import com.bankscene.bes.welllinkbank.activity.pdf.asset.CopyAssetThreadImpl;

import java.io.File;


public class AssetOnSDActivity extends BaseSampleActivity {
    final String[] sampleAssets = {"adobe.pdf", "sample.pdf"};

    PDFViewPager pdfViewPager;
    File pdfFolder;
    String path="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle(R.string.asset_on_sd);
        setContentView(R.layout.layout_pdf);
        path=getIntent().getStringExtra("pdf");
        pdfFolder = Environment.getExternalStorageDirectory();
        copyAssetsOnSDCard();
    }

    protected void copyAssetsOnSDCard() {
        final Context context = this;
        pdfViewPager = new PDFViewPager(context, getPdfPathOnSDCard());
        setContentView(pdfViewPager);
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

    public static void open(Context context) {
        Intent i = new Intent(context, AssetOnSDActivity.class);
        context.startActivity(i);
    }
}