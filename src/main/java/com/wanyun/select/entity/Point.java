package com.wanyun.select.entity;

import lombok.Data;

/**
 * @Auther: cxy
 * @Description: Point
 * @Version 1.0.0
 */
@Data
public class Point {
    String id;
    float data;
    float x, y;
    int xx;
    int yy;

    public Point() {

    }

    public Point(String id, float data, float x, float y, int xx, int yy) {
        this.id = id;
        this.data = data;
        this.x = x;
        this.y = y;
        this.xx = xx;
        this.yy = yy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getXx() {
        return xx;
    }

    public void setXx(int xx) {
        this.xx = xx;
    }

    public int getYy() {
        return yy;
    }

    public void setYy(int yy) {
        this.yy = yy;
    }
}