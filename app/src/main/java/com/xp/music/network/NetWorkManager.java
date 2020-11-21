package com.xp.music.network;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xp.music.bean.MusicData;
import com.xp.music.bean.MusicInfo;
import com.xp.music.bean.User;
import com.xp.music.callback.MusicGetCallback;
import com.xp.music.callback.UserCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enzo Cotter on 2020-11-07.
 */
public class NetWorkManager {
    private HttpManager httpManager =new HttpManager();
    private UserHttpManager userHttpManager =new UserHttpManager();
    public NetWorkManager() {
    }
    public void getRandMusic(MusicGetCallback callback)  {
        CommonService commonService =httpManager.getService(CommonService.class);
        List<MusicData> datas =new ArrayList<>();
        new Thread(()->{
            for(int i=0;i<7;i++){
                try {
                   Response<MusicInfo> response = commonService.getRandMusic().execute();
                  if(response.body()!=null){
                      datas.add(response.body().getData());
                  }
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.getError(e);
                }
            }
            callback.getSuccess(datas);

        }).start();
    }
    public void getOneMusic(MusicGetCallback callback)  {
        CommonService commonService =httpManager.getService(CommonService.class);
        List<MusicData> datas =new ArrayList<>();
        new Thread(()->{
                try {
                    Response<MusicInfo> response = commonService.getRandMusic().execute();
                    if(response.body()!=null){
                        datas.add(response.body().getData());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.getError(e);
                }
            callback.getSuccess(datas);

        }).start();
    }
    public void register(User user, UserCallback callback){
        CommonService commonService =userHttpManager.getService(CommonService.class);

        commonService.register(user.getUid(),user.getUpassword(),user.getUname()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if(response.code()==200){
                   try {
                       String s =response.body().string();
                       Log.e("TAG", "onResponse: "+s );
                       callback.onState(Integer.parseInt(s));
                   } catch (IOException e) {
                       onFailure(call,e);
                   }
               }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("zza", "onFailure: ",t );
            }
        });
    }
    public void signIn(String id,String password,UserCallback callback){
        CommonService commonService =userHttpManager.getService(CommonService.class);
        commonService.signin(id, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.code()==200){
                        String  s =response.body().string();
                        if(s.length()<2){
                            callback.onState(Integer.parseInt(s));
                        }else {
                            Gson gson = new Gson();
                            User user = gson.fromJson(s, User.class);
                            callback.signInSuccess(user);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void hasKey(String key,UserCallback callback){
        CommonService commonService =userHttpManager.getService(CommonService.class);
        commonService.isRegister(key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    try {
                        callback.onState(Integer.parseInt(response.body().string()));
                    } catch (IOException e) {
                        onFailure(call,e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("zza", "onFailure: ",t );
            }
        });
    }
}
