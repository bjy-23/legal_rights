package com.wonders.bean;

import java.util.List;

/**
 * Created by bjy on 2017/3/10.
 */

public class Node<T> {
    /*
    * 当前节点的等级
    * */
    private int level;

    /*
    * 节点是否展开
    * */
    private boolean isExpandable;


    /*
    * 下一级的子节点
    * */
    private List<Node> children;

    private T t;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
