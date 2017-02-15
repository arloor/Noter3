package com.arloor.noter3.model;

import org.litepal.crud.DataSupport;

/**
 * Created by arloor on 2017/2/15.
 */

public class Note extends DataSupport{
    private String time;
    private String theme;
    private String content;

    public Note() {
    }

    public Note(String time, String theme) {
        this.time = time;
        this.theme = theme;
    }

    public Note(String time, String theme, String content) {
        this.time = time;
        this.theme = theme;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
