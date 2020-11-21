package com.xp.music.view;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.hrb.library.MiniMusicView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xp.music.R;
import com.xp.music.bean.MusicData;
import com.xp.music.bean.MusicListInfo;
import com.xp.music.callback.MusicGetCallback;
import com.xp.music.callback.MusicViewCallback;
import com.xp.music.fragment.MusicViewFragment;
import com.xp.music.network.NetWorkManager;
import com.xp.music.utils.SUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MiniMusicView miniMusicView;
    private SUtils sUtils;
    private Banner banner;
    private String TAG = "xp";
    private static MainActivity activity;
    private boolean isfirst = true;
    private TextView tv_time, name;
    private MusicViewFragment viewFragment;
    private List<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        applyKitKatTranslucency();
        Toolbar toolbar = findViewById(R.id.toolbar);
        strings.add("http://p1.music.126.net/U4aC7WQ91X0cSFsgpb5oFg==/109951165481068028.jpg?imageView&quality=89");
        strings.add("http://p1.music.126.net/Cm2DIJDqmMNXRQvComUOpg==/109951165481065884.jpg?imageView&quality=89");
        strings.add("http://p1.music.126.net/HYIG27vbTeo1Emtl6C7qgA==/109951165481169430.jpg?imageView&quality=89");
        strings.add("http://p1.music.126.net/gIg-cC3SISeb1al3WiXzOA==/109951165481850859.jpg?imageView&quality=89");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        tv_time = navigationView.getHeaderView(0).findViewById(R.id.times);
        name = navigationView.getHeaderView(0).findViewById(R.id.user_nav_name);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        sUtils = new SUtils(getApplicationContext());
        initView();
        activity = this;
    }

    public static MainActivity getIn() {
        return activity;
    }

    private void applyKitKatTranslucency() {
        // KitKat translucent navigation/status bar.
        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色

    }

    public int getTime() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
        return Integer.parseInt(sdf1.format(new Date()));
    }

    private void initView() {
        miniMusicView = findViewById(R.id.music);

        miniMusicView.setOnNextBtnClickListener(this::randomMusic);
        randomMusic();
        if (getTime() >= 12 && getTime() <= 19) {
            tv_time.setText("下午好,");
        } else if (getTime() > 6 && getTime() < 12) {
            tv_time.setText("早上好,");
        } else {
            tv_time.setText("早点睡，");
        }
        name.setText(sUtils.getString("uname"));
        banner = findViewById(R.id.banner);
        useBanner(strings);
        miniMusicView.setOnMusicStateListener(new MiniMusicView.OnMusicStateListener() {
            @Override
            public void onPrepared(int duration) {
                Log.e(TAG, "onPrepared: " + duration);
            }

            @Override
            public void onError(int what, int extra) {
            }

            @Override
            public void onInfo(int what, int extra) {
                Log.e(TAG, "what: " + what + " extra :" + extra);
            }

            @Override
            public void onMusicPlayComplete() {
                randomMusic();
            }

            @Override
            public void onSeekComplete() {

            }

            int i = 1;

            @Override
            public void onProgressUpdate(int duration, int currentPos) {
                if (i == 1) {
                    i = currentPos;
                }
                int is = duration / i;
                sUtils.setString("pro", is + "");
                sUtils.setString("now", (currentPos / i) + "");
                if (isfirst) {
                    miniMusicView.pausePlayMusic();
                    isfirst = false;
                }
                Log.e(TAG, "onPrepared: " + duration + "current" + currentPos);
            }

            @Override
            public void onHeadsetPullOut() {

            }
        });
        miniMusicView.setOnClickListener((v) ->
                showMusicView());
    }

    public void showMusicView() {
        viewFragment = new MusicViewFragment(sUtils.getString("title"), sUtils.getString("icon_url"), callback);
        viewFragment.show(getSupportFragmentManager(), "01");
    }

    public void randomMusic() {
        NetWorkManager manager = new NetWorkManager();
        manager.getOneMusic(new MusicGetCallback() {
            @Override
            public void getSuccess(List<MusicData> datas) {
                MusicData data = datas.get(0);
                playMusic(new MusicListInfo(data.getName(), data.getPicurl(), data.getArtistsname(), data.getUrl()));
            }

            @Override
            public void getError(Throwable throwable) {

            }
        });
    }

    public void playMusic(MusicListInfo musicInfo) {
        runOnUiThread(() -> {
            miniMusicView.setTitleText(musicInfo.getTitle());
            miniMusicView.setAuthor(musicInfo.getMess());
            miniMusicView.startPlayMusic(musicInfo.getMusic_url());
            Glide.with(this).load(musicInfo.getIcon_url()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    miniMusicView.setIconDrawable(resource);
                }
            });
            miniMusicView.setProgressMax(100);
            sUtils.setString("title", musicInfo.getTitle());
            sUtils.setString("mess", musicInfo.getMess());
            sUtils.setString("music_url", musicInfo.getMusic_url());
            sUtils.setString("icon_url", musicInfo.getIcon_url());
            if (viewFragment != null) {
                viewFragment.refresh();
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private MusicViewCallback callback = new MusicViewCallback() {
        @Override
        public void stopMusic() {
            miniMusicView.pausePlayMusic();
        }

        @Override
        public void startMusic() {
            miniMusicView.resumePlayMusic();
        }

        @Override
        public void nextMusic() {

        }

        @Override
        public void lastMusic() {

        }

        @Override
        public void loveMusic() {

        }
    };

    public void useBanner(List<String> s) {
        banner.setAdapter(new BannerImageAdapter<String>(s) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(holder.itemView)
                        .load(data)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(this));
        //更多使用方法仔细阅读文档，或者查看demo
    }
}
