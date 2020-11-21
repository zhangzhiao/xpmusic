package com.xp.music.bean;

/**
 * Created by Enzo Cotter on 2020-11-07.
 */
public class MusicListInfo {
    private String title;
    private String icon_url;
    private String mess;
    private String music_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getMusic_url() {
        return music_url;
    }

    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }

    public MusicListInfo(String title, String icon_url, String mess, String music_url) {
        this.title = title;
        this.icon_url = icon_url;
        this.mess = mess;
        this.music_url = music_url;
    }
}
