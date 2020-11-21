package com.xp.music.network;

import com.xp.music.bean.MsgInfo;
import com.xp.music.bean.MusicInfo;
import com.xp.music.bean.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Enzo Cotter on 2020-11-07.
 */
public interface CommonService {
    @GET("rand.music?sort=%E7%83%AD%E6%AD%8C%E6%A6%9C&format=json")
    Call<MusicInfo> getRandMusic();
    @GET("rand.music?sort=飙升榜&format=json")
    Call<MusicInfo> getUpRandMusic();
    @POST("register")
    Call<ResponseBody> register(@Query("id") String id,@Query("password") String password,@Query("name") String name );
    @POST("haskey")
    Call<ResponseBody> isRegister(@Query("key") String key);
    @POST("signin")
    Call<ResponseBody> signin(@Query("id") String id,@Query("password") String password);
    @GET("api.php")
    Call<MsgInfo> getMessage(@Query("msg") String msg,@Query("key") String key,@Query("appid") String app);
}
