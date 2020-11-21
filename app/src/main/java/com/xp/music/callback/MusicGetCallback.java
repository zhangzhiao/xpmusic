package com.xp.music.callback;

import com.xp.music.bean.MusicData;

import java.util.List;

/**
 * Created by Enzo Cotter on 2020-11-07.
 */
public interface MusicGetCallback {
    void getSuccess(List<MusicData> datas);
    void getError(Throwable throwable);
}
