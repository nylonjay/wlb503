package com.bankscene.bes.welllinkbank.view.droplistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.adapter.common.CommonAdapter;
import com.bankscene.bes.welllinkbank.adapter.common.ViewHolder;
import com.bankscene.bes.welllinkbank.view.CustomListView;

import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
public class DropListView extends ScrollView {

    private Context context;
    private CustomListView listView;
    public int current;

    private Animation dropdown_in;
    private Animation dropdown_out;
    private Animation dropdown_mask_out;

    private Control control;
    private View mask;
    private View tab;

    public DropListView(Context context) {
        this(context, null);
        this.context = context;
    }

    public DropListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public DropListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout._view_drop_list, this, true);
        listView = (CustomListView) view.findViewById(R.id.listView);
//        dropdown_in = AnimationUtils.loadAnimation(context, R.anim.dropdown_in);
//        dropdown_out = AnimationUtils.loadAnimation(context, R.anim.dropdown_out);
        dropdown_in = AnimationUtils.loadAnimation(context, R.anim.in_from_top);
        dropdown_out = AnimationUtils.loadAnimation(context, R.anim.out_to_top);
        dropdown_mask_out = AnimationUtils.loadAnimation(context, R.anim.dropdown_mask_out);
    }

    public void bind(List<? extends DropItemObject> list, View button, View mask, Control control, int selectedId, View tab){
        bind(list,button,mask,control,selectedId);
        this.tab = tab;
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropListView.this.control.onHide();
                hide();
            }
        });
    }

    public void bind(List<? extends DropItemObject> list, View button, View mask, Control control, int selectedId) {
        current = selectedId;
        this.control = control;
        this.mask = mask;

        reset();

        CommonAdapter adapter = new CommonAdapter(context, list, R.layout._view_drop_list_item) {
            @Override
            public void convert(ViewHolder helper, int position, Object item) {
                DropItemObject map = (DropItemObject) item;
                helper.setText(R.id.text, map.getText());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == current) {
                    hide();
                } else {
                    hide();
                    current = position;
                    DropListView.this.control.onSelection(position);
                }
            }
        });
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVisibility() == VISIBLE) {
                    DropListView.this.control.onHide();
                    hide();
                } else {
                    DropListView.this.control.onShow();
                    show();
                }
            }
        });

        dropdown_mask_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                reset();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DropListView.this.control.onHide();
                hide();
            }
        });
    }

    private void hide() {
        clearAnimation();
        startAnimation(dropdown_out);

        mask.clearAnimation();
        mask.startAnimation(dropdown_mask_out);

        if(tab!=null){
            tab.clearAnimation();
            tab.startAnimation(dropdown_mask_out);
        }
    }

    private void show() {
        mask.clearAnimation();
        mask.setVisibility(View.VISIBLE);

        if(tab!=null){
            tab.clearAnimation();
            tab.setVisibility(View.VISIBLE);
        }
        clearAnimation();
        startAnimation(dropdown_in);
        setVisibility(View.VISIBLE);
    }

    private void reset() {
        setVisibility(View.GONE);
        mask.setVisibility(View.GONE);
        clearAnimation();
        mask.clearAnimation();
        if(tab!=null){
            tab.setVisibility(View.GONE);
            tab.clearAnimation();
        }
    }
}
