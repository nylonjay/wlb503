package com.bankscene.bes.welllinkbank.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.activity.NoticeInfo.NoticeDetail;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.biz.NoiceBiz;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.pulltorefresh.BaseQuickAdapter;
import com.bankscene.bes.welllinkbank.pulltorefresh.PullToRefreshAdapter;
import com.bankscene.bes.welllinkbank.pulltorefresh.entity.Status;
import com.bankscene.bes.welllinkbank.pulltorefresh.listener.OnItemClickListener;
import com.bankscene.bes.welllinkbank.pulltorefresh.loadmore.CustomLoadMoreView;
import com.bankscene.bes.welllinkbank.pulltorefresh.util.Utils;
import com.bankscene.bes.welllinkbank.view.droplistview.DropItemObject;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Nylon on 2018/1/26.10:23
 */
interface RequestCallBack {
    void success(List<Status> data);

    void fail(Exception e);
}


class Request extends Thread {
    private static final int PAGE_SIZE = 6;
    private int mPage;
    private RequestCallBack mCallBack;
    private Handler mHandler;

    private static boolean mFirstPageNoMore;
    private static boolean mFirstError = true;
    private List<Status> nbs;
    public Request(int page, RequestCallBack callBack) {
        mPage = page;
        mCallBack = callBack;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        try {Thread.sleep(500);} catch (InterruptedException e) {}

        if (mPage == 2 && mFirstError) {
            mFirstError = false;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.fail(new RuntimeException("fail"));
                }
            });
        } else {
            int size = PAGE_SIZE;
            if (mPage == 1) {
                if (mFirstPageNoMore) {
                    size = 1;
                }
                mFirstPageNoMore = !mFirstPageNoMore;
                if (!mFirstError) {
                    mFirstError = true;
                }
            } else if (mPage == 4) {
                size = 1;
            }

            final int dataSize = 10;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallBack.success(GetNoticeData());
                }
            });
        }
    }

    private List<Status> GetNoticeData() {
        Gson gson=new Gson();
        String noticejson= DBHelper.getDataByKey(DataKey.notice);
        Type type=new TypeToken<List<Status>>(){}.getType();
        nbs=gson.fromJson(noticejson,type);
        return nbs;

    }
}
public class NoticeFragment extends BaseFragment{
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.notice_list)
    RecyclerView notice_list;
    private List<DropItemObject> type = new ArrayList<DropItemObject>();
    private List<Map<String, Object>> financeList = new ArrayList<>();
    private PullToRefreshAdapter mAdapter;
    private int mNextRequestPage;
    private int PAGE_SIZE=10;
    private List<Status> status;
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void onResume() {
        super.onResume();
//        GetNoticeData();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    protected void initView() {
        Utils.init(activity);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(165, 201, 0));
        notice_list.setLayoutManager(new LinearLayoutManager(activity));
        initAdapterHF();
        initRefreshLayout();
        swipeRefreshLayout.setRefreshing(true);
        refresh();
    }
    private void initAdapterHF() {
        mAdapter = new PullToRefreshAdapter();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        mAdapter.setPreLoadNumber(3);
        notice_list.setAdapter(mAdapter);

        notice_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
//               String key= ((Status)adapter.getItem(position)).getMkey();
                Status status= (Status) adapter.getItem(position);
                Intent in=new Intent(activity, NoticeDetail.class);
//                in.putExtra("key",key);
                if ("zh".equals(DBHelper.getDataByKey(DataKey.language))){
                    in.putExtra("title",status.getMtitle()+"");
                    in.putExtra("content",status.getMcontent()+"");
                }else {
                    in.putExtra("title",status.getMengtitle()+"");
                    in.putExtra("content",status.getMengcontent()+"");
                }
                    in.putExtra("url",status.getMimgurl());
                startActivity(in);
            }
        });
    }
    private void initRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void addHeadView() {
        View headView =activity.getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) notice_list.getParent(), false);
        headView.findViewById(R.id.iv).setVisibility(View.GONE);
        ((TextView) headView.findViewById(R.id.tv)).setText("change load view");
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setNewData(null);
                mAdapter.setLoadMoreView(new CustomLoadMoreView());
                notice_list.setAdapter(mAdapter);
                Toast.makeText(activity, "change complete", Toast.LENGTH_LONG).show();

                swipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });
        mAdapter.addHeaderView(headView);
    }

        private void refresh() {
            mNextRequestPage = 1;
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
            new Request(mNextRequestPage, new RequestCallBack() {
                @Override
                public void success(List<Status> data) {
                    setData(true, data);
                    mAdapter.setEnableLoadMore(true);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void fail(Exception e) {
                    Toast.makeText(activity, R.string.network_err, Toast.LENGTH_LONG).show();
                    mAdapter.setEnableLoadMore(true);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }).start();
        }

    private void setData(boolean isRefresh, List<Status> data) {
        status=data;
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
//            Toast.makeText(activity, "no more data", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }

    }

    private void initAdapter() {
        mAdapter = new PullToRefreshAdapter();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        mAdapter.setPreLoadNumber(3);
        notice_list.setAdapter(mAdapter);

        notice_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(activity, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadMore() {
        new Request(mNextRequestPage, new RequestCallBack() {
            @Override
            public void success(List<Status> data) {
                setData(false, data);
            }

            @Override
            public void fail(Exception e) {
                mAdapter.loadMoreFail();
                Toast.makeText(activity, R.string.network_err, Toast.LENGTH_LONG).show();
            }
        }).start();

    }

    @Override
    public void setActionBar() {
        super.setActionBar();
        actionBar.setBackground(R.color.white);
        actionBar.setTitleColor(getResources().getColor(R.color.actionBarText));
        actionBar.setActionBar(getResources().getString(R.string.Notice), TranslucentActionBar.ICON_NULL, "", TranslucentActionBar.ICON_NULL, "",
                new ActionBarClickListener() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {

                    }
                });

        actionBar.setStatusBarHeight();
    }


    @Subscribe(threadMode = ThreadMode.PostThread ,sticky = true)
    public void onMessageEventPostThread(MessageEvent messageEvent){

    }

    @Subscribe(threadMode = ThreadMode.MainThread,sticky = true)
    public void onMessageEventMainThread(MessageEvent messageEvent){

    }
    @Subscribe(threadMode = ThreadMode.Async,sticky = true)
    public void onMessageEventAsyncThread(MessageEvent messageEvent){

    }
    @Subscribe(threadMode = ThreadMode.BackgroundThread,sticky = true)
    public void onMessageEventBackgroundThread(MessageEvent messageEvent){

    }

}