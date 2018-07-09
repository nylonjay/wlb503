package com.bankscene.bes.welllinkbank.activity.pdf;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.activity.mine.PersonInfo;
import com.bankscene.bes.welllinkbank.core.BaseActivity;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import butterknife.BindView;

/**
 * Created by Nylon on 2018/7/5.18:13
 */

public class PDFActivity extends BaseActivity{
//    @BindView(R.id.pdfView)
//    PDFView pdfView;
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.monthy_statement), R.string.wlb_arrow_l,"", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                PDFActivity.this.finish();
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

//    @Override
//    protected void initView() {
//        super.initView();
//        String localPdfPath=getIntent().getStringExtra("pdf");
//        pdfView.fromFile(new File(localPdfPath))  //pdf地址
//                .defaultPage(1)//默认页面
//                .enableDoubletap(true)
//                .swipeHorizontal(false)//是不是横向查看
//                .onPageChange(this)
//                .enableSwipe(true)
//                .load();
//
//    }
//
//    @Override
//    public void onPageChanged(int page, int pageCount) {
//
//    }
}
