package com.xp.music.bean;

/**
 * Auto-generated: 2020-11-06 15:15:53
 *
 * @author www.jsons.cn
 * @website http://www.jsons.cn/json2java/
 */
public class MusicData {

    private String name;
    private String url;
    private String picurl;
    private String artistsname;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
    public String getPicurl() {
        return picurl;
    }

    public void setArtistsname(String artistsname) {
        this.artistsname = artistsname;
    }
    public String getArtistsname() {
        return artistsname;
    }

    @Override
    public String toString() {
        return "MusicData{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", picurl='" + picurl + '\'' +
                ", artistsname='" + artistsname + '\'' +
                '}';
    }
}