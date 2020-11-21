package com.xp.music.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Enzo Cotter on 2020-11-10.
 */
@Entity
public class LoveMusic {
    @Index(unique = true)
    public String name;
    public String mess;
    public String pic_url;
    public String music_url;
    @Generated(hash = 1686235938)
    public LoveMusic(String name, String mess, String pic_url, String music_url) {
        this.name = name;
        this.mess = mess;
        this.pic_url = pic_url;
        this.music_url = music_url;
    }
    @Generated(hash = 812727792)
    public LoveMusic() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMess() {
        return this.mess;
    }
    public void setMess(String mess) {
        this.mess = mess;
    }
    public String getPic_url() {
        return this.pic_url;
    }
    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
    public String getMusic_url() {
        return this.music_url;
    }
    public void setMusic_url(String music_url) {
        this.music_url = music_url;
    }


}
