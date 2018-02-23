package com.wonders.bean;

import java.util.ArrayList;

/**
 * Created by bjy on 2016/12/12.
 */

public class SavePlanBean {
    private ArrayList<SopListViewBean> items;
    private ArrayList<PicBean> planPic;

    public ArrayList<SopListViewBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<SopListViewBean> items) {
        this.items = items;
    }

    public ArrayList<PicBean> getPlanPic() {
        return planPic;
    }

    public void setPlanPic(ArrayList<PicBean> planPic) {
        this.planPic = planPic;
    }
}
