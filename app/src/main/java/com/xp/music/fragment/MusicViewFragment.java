package com.xp.music.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hrb.library.MiniMusicView;
import com.xp.music.R;

import co.mobiwise.library.MusicPlayerView;
import com.xp.music.callback.MusicViewCallback;
import com.xp.music.db.LoveMusic;
import com.xp.music.db.LoveMusicManager;
import com.xp.music.utils.SUtils;
import com.xp.music.view.MainActivity;

public class MusicViewFragment extends DialogFragment {
    private MusicPlayerView musicPlayerView;
    private String name, url;
    private ImageView imageView,dia_love,dia_next,dia_last;
    private TextView title;
    private MusicViewCallback callback;
    private SUtils sUtils;
    LoveMusicManager loveMusicManager;
    public MusicViewFragment(String name, String url,MusicViewCallback callback) {
        this.name = name;
        this.url = url;
        this.callback =callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen);
        sUtils =new SUtils(getContext());
        loveMusicManager =new LoveMusicManager(getContext());
        return inflater.inflate(R.layout.fragment_dialog_musicview, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onViewCreated(view, savedInstanceState);
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0xcccccc));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView(view);
    }

    private void initView(View view) {
        musicPlayerView = view.findViewById(R.id.musicview);
        dia_last=view.findViewById(R.id.dialog_last);
        dia_love=view.findViewById(R.id.dialog_love);
        dia_next=view.findViewById(R.id.dialog_next);
        musicPlayerView.setCoverURL(url);
        musicPlayerView.setProgressLoadedColor(0xffffff);
        musicPlayerView.setProgressEmptyColor(0xcccccc);
        imageView = view.findViewById(R.id.back);
        imageView.setOnClickListener(v -> dismiss());
        musicPlayerView.start();
        title = view.findViewById(R.id.musicview_title);
        title.setText(name);
        dia_next.setOnClickListener(v-> MainActivity.getIn().randomMusic());
        dia_last.setOnClickListener(v-> MainActivity.getIn().randomMusic());
        musicPlayerView.setProgress(Integer.parseInt(sUtils.getString("now")));
        musicPlayerView.setMax(Integer.parseInt(sUtils.getString("pro")));
        isHas();
        dia_love.setOnClickListener(v->{
            Log.e("TAG", "LOVE: " );
             if(loveMusicManager.selectByName(title.getText().toString()).size()!=0){
                loveMusicManager.delete(title.getText().toString());
            }else {
                 loveMusicManager.insertOrReplace(new LoveMusic(sUtils.getString("title"),sUtils.getString("mess"),
                         sUtils.getString("icon_url"),sUtils.getString("music_url")));

             }
            isHas();
        });
        musicPlayerView.setOnClickListener(v->{
            if(musicPlayerView.isRotating()){
                callback.stopMusic();
                musicPlayerView.stop();
            }else {
                musicPlayerView.start();
                callback.startMusic();
            }

        });
        start();
    }
    public void isHas(){
        if(loveMusicManager.selectByName(title.getText().toString()).size()!=0){
            dia_love.setImageDrawable(getResources().getDrawable(R.drawable.loved));
        }else {
            dia_love.setImageDrawable(getResources().getDrawable(R.drawable.xin));
        }
    }
    public void refresh(){
        musicPlayerView.setProgress(0);
        musicPlayerView.setCoverURL(sUtils.getString("icon_url"));
        title.setText(sUtils.getString("title"));
        musicPlayerView.setMax(Integer.parseInt(sUtils.getString("pro")));

    }
    public void start() {
        musicPlayerView.start();
    }

    public void stop() {
        musicPlayerView.stop();
    }

    public void setPro(int progress, int max) {
        musicPlayerView.setProgress(progress);
        musicPlayerView.setMax(max);
    }

}
