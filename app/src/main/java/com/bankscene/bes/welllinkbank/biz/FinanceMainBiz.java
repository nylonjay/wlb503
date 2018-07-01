package com.bankscene.bes.welllinkbank.biz;

import android.content.Context;

import com.bankscene.bes.welllinkbank.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static android.R.attr.rotation;

/**
 * Created by Nylon on 2018/2/1.11:36
 */

public class FinanceMainBiz {
    Context mContext;

    public FinanceMainBiz(Context context){
        mContext=context;
    }

    public ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> date = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < function_icons.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("icon", function_icons[i]);
            map.put("text",mContext.getResources().getString( (int)functions[i]));
            date.add(map);
        }
        return date;
    }

    static Object[] functions={R.string.account_query,R.string.transaction_history,R.string.loan_inquiry,
            R.string.synonym_transfer,R.string.my_regular,R.string.regular_billing,R.string.regular_withdrawal,
            R.string.remittance,
            R.string.my_loans,
//            ,R.string.repayment_history,R.string.repayment_plan,
//            R.string.repayment_query,
            R.string.bank_rate,R.string.exchange_rate_quotation
//            ,R.string.shares_query
//            ,R.string.states_msci,
//            R.string.noble_metal_query,
//            R.string.custom
    };

    static Object[] function_icons={
            R.mipmap.wlb_icon_personinfo, R.mipmap.wlb_icon_history, R.mipmap.wlb_icon_money,
            R.mipmap.wlb_icon_exchange,R.mipmap.wlb_icon_myregular, R.mipmap.wlb_icon_regular,
            R.mipmap.wlb_icon_menu,
            R.mipmap.wlb_icon_remmitance,
            R.mipmap.wlb_icon_myloan,
//            R.mipmap.wlb_icon_repay_his, R.mipmap.wlb_icon_plan,
//            R.mipmap.wlb_icon_repay_query,
            R.mipmap.wlb_icon_rate, R.mipmap.wlb_icon_curey
//            , R.mipmap.wlb_shares_q
//            R.mipmap.wlb_icon_states,
//            R.mipmap.wlb_noble_metal,
//            R.mipmap.wlb_icon_custom,
    };


    //    static Object[] financeBiz={R.string.my_loans,R.string.repayment_history,R.string.repayment_plan,
//            R.string.repayment_query,R.string.bank_rate,R.string.exchange_rate_quotation,R.string.states_msci,
//            R.string.noble_metal_query,R.string.shares_query};
//
//    static Object[] finance_icons={
//            R.mipmap.wlb_icon_myloan, R.mipmap.wlb_icon_repay_his, R.mipmap.wlb_icon_plan,
//            R.mipmap.wlb_icon_repay_query,R.mipmap.wlb_icon_rate, R.mipmap.wlb_icon_curey,
//            R.mipmap.wlb_icon_states,
//            R.mipmap.wlb_noble_metal, R.mipmap.wlb_shares_q
//    };
    static Object[] financeBiz={
//        R.string.deposits_product,
            R.string.my_regular,R.string.regular_billing,
            R.string.regular_withdrawal,R.string.live_rotation,R.string.regular_history,R.string.loan_inquiry,
            R.string.bank_rate,R.string.exchange_rate_quotation,
            R.string.reimbursement_enquiries
//        ,R.string.subscribe_exchange_foreign_currency
    };

    static Object[] finance_icons={
//            R.mipmap.wlb_icon_deposits,
            R.mipmap.wlb_icon_myregular, R.mipmap.wlb_icon_regular,
            R.mipmap.wlb_icon_withdrawal,R.mipmap.wlb_icon_money, R.mipmap.wlb_icon_menu,
            R.mipmap.wlb_icon_find,
            R.mipmap.wlb_icon_rate, R.mipmap.wlb_icon_quoteprice, R.mipmap.wlb_icon_query
//            ,
//            R.mipmap.wlb_icon_subscribe
    };


    public ArrayList<HashMap<String, Object>> getFinanceData() {
        ArrayList<HashMap<String, Object>> date = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < finance_icons.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("icon", finance_icons[i]);
            map.put("text",financeBiz[i]);
            date.add(map);
        }
        return date;
    }

//    public static Object[] Merge(Object[] oo1, Object[] oo2){
//        Object[] o1=oo1.clone();
//        Object[] o2=oo2.clone();
//        int str1Length = o1.length;
//        int str2length = o2.length;
//
//        o1 = Arrays.copyOf(o1, str1Length+str2length);//数组扩容
//        System.arraycopy(o2,0,o1,str1Length,str2length);
//
//        return o1;
//    }

    public static Object[] GetAllMenuNames(){
        return  functions;
    }

    public static Object[] getAllIcons(){
        return function_icons;
    }

    public int getFirstArraySize() {
        return functions.length;
    }
}

