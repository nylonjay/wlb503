package com.bankscene.bes.welllinkbank.biz;

import java.io.Serializable;

/**
 * Created by Nylon on 2018/3/18.22:56
 */

public class MenuBiz implements Serializable {
    private static final long serialVersionUID = 12L;
    int icon_Rsid;
    int menu_Name;
    boolean is_Checked=true;

    public int getIcon_Rsid() {
        return icon_Rsid;
    }

    public void setIcon_Rsid(int icon_Rsid) {
        this.icon_Rsid = icon_Rsid;
    }

    public int getMenu_Name() {
        return menu_Name;
    }

    public void setMenu_Name(int menu_Name) {
        this.menu_Name = menu_Name;
    }

    public boolean is_Checked() {
        return is_Checked;
    }

    public void setIs_Checked(boolean is_Checked) {
        this.is_Checked = is_Checked;
    }
}
