package com.github.lwxntm.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lei
 */

public class TodoList implements Serializable {
    static SimpleDateFormat sdf = new SimpleDateFormat();

    static {
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
    }

    @JsonProperty("Todo信息")
    private String info;
    private String createTime;
    private boolean finished;
    public TodoList() {
    }


    public TodoList(String info) {
        this.info = info;
        this.createTime = sdf.format(new Date());
        this.finished = false;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }


}
