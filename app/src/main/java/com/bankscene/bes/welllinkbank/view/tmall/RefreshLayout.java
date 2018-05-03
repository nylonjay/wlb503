package com.bankscene.bes.welllinkbank.view.tmall;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 仿天猫下拉刷新view
 * Created by shenminjie on 2016/12/6.
 */

public class RefreshLayout extends PtrFrameLayout {

    /**
     * headerView
     */
     RefreshHeader mHeaderView;

    public RefreshLayout(Context context) {
        super(context);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    /**
     * 初始化view
     */
    private void initView() {
        mHeaderView = new RefreshHeader(getContext());
        setHeaderView(mHeaderView);
        addPtrUIHandler(mHeaderView);
    }


}
