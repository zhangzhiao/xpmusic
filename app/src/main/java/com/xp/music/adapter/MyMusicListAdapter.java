package com.xp.music.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.xp.music.R;
import com.xp.music.bean.MusicListInfo;
import com.xp.music.callback.MusicOnclickCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enzo Cotter on 2020-11-07.
 */
public class MyMusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MusicListInfo> datas; // 数据源
    private Context context;    // 上下文Context

    private int normalType = 0;     // 第一种ViewType，正常的item
    private int footType = 1;       // 第二种ViewType，底部的提示View

    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示
     MusicOnclickCallback callback;
    private Handler mHandler = new Handler(Looper.getMainLooper()); //获取主线程的Handler

    public MyMusicListAdapter(List<MusicListInfo> datas, Context context, boolean hasMore, MusicOnclickCallback callback) {
        this.datas = datas;
        this.context = context;
        this.callback =callback;
        this.hasMore = hasMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 根据返回的ViewType，绑定不同的布局文件，这里只有两种
        if (viewType == normalType) {
            return new MusicHolder(LayoutInflater.from(context).inflate(R.layout.item_music_list, null));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.view_refresh_foot, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 如果是正常的imte，直接设置TextView的值
        if (holder instanceof MusicHolder) {
           MusicHolder holder1 = ((MusicHolder) holder);
           holder1.title.setText(datas.get(position).getTitle());
            holder1.message.setText(datas.get(position).getMess());
            Glide.with(context).load(datas.get(position).getIcon_url()).into(holder1.icon);
            holder1.start.setOnClickListener((v)->callback.onclick(datas.get(position)));
        } else {
            // 之所以要设置可见，是因为我在没有更多数据时会隐藏了这个footView
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            // 只有获取数据为空时，hasMore为false，所以当我们拉到底部时基本都会首先显示“正在加载更多...”
            if (hasMore == true) {
                // 不隐藏footView提示
                fadeTips = false;
                if (datas.size() > 0) {
                    // 如果查询数据发现增加之后，就显示正在加载更多
                    ((FootHolder) holder).tips.setText("--正在加载--");
                }
            } else {
                if (datas.size() > 0) {
                    // 如果查询数据发现并没有增加时，就显示没有更多数据了
                    ((FootHolder) holder).tips.setText("--到底啦，下拉刷新一下吧--");

                    // 然后通过延时加载模拟网络请求的时间，在500ms后执行
                    mHandler.postDelayed(() -> {
                        // 隐藏提示条
                        ((FootHolder) holder).tips.setVisibility(View.GONE);
                        // 将fadeTips设置true
                        fadeTips = true;
                        // hasMore设为true是为了让再次拉到底时，会先显示正在加载更多
                        hasMore = true;
                    }, 500);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }
    // 自定义方法，获取列表中数据源的最后一个位置，比getItemCount少1，因为不计上footView
    public int getRealLastPosition() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    static class MusicHolder extends RecyclerView.ViewHolder{
         TextView title,message;
         ImageView icon,start;
        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.music_title);
            message =itemView.findViewById(R.id.music_people);
            icon =itemView.findViewById(R.id.music_icon);
            start=itemView.findViewById(R.id.music_start);
        }
    }
    // // 底部footView的ViewHolder，用以缓存findView操作
    static class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = itemView.findViewById(R.id.foot_text);
        }
    }
    // 暴露接口，改变fadeTips的方法
    public boolean isFadeTips() {
        return fadeTips;
    }

    // 暴露接口，下拉刷新时，通过暴露方法将数据源置为空
    public void resetDatas() {
        datas = new ArrayList<>();
    }

    // 暴露接口，更新数据源，并修改hasMore的值，如果有增加数据，hasMore为true，否则为false
    public void updateList(List<MusicListInfo> newDatas, boolean hasMore) {
        // 在原有的数据之上增加新数据
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }
}
