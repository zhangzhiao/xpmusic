package com.xp.music.callback;

import com.xp.music.bean.User;

/**
 * Created by Enzo Cotter on 2020-11-09.
 */
public interface UserCallback {
    void onState(int state);
    void signInSuccess(User user);
}
