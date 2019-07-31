package com.crows.src;

import java.util.ArrayList;
import java.util.List;

public class CoordinatesVO {
    private int x;
    private int y;
    private List<Integer> xList;
    private List<Integer> yList;

    public CoordinatesVO() {
        x = 300;
        y = 300;
        xList = new ArrayList<Integer>();
        yList = new ArrayList<Integer>();
    }

    public void addX(int x) {
        xList.add(x);
    }

    public void addY(int y) {
        yList.add(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<Integer> getxList() {
        return xList;
    }

    public void setxList(List<Integer> xList) {
        this.xList = xList;
    }

    public List<Integer> getyList() {
        return yList;
    }

    public void setyList(List<Integer> yList) {
        this.yList = yList;
    }

    @Override
    public String toString() {
        return "CoordinatesVO{x: " + x + ", y: " + y + ", xList: " + xList.toString() + ", yList: " + yList.toString() + "}";
    }
}
