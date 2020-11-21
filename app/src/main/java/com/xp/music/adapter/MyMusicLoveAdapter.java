package com.xp.music.adapter;

import android.content.Context;
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
import com.xp.music.callback.MusicViewCallback;
import com.xp.music.db.LoveMusic;
import com.xp.music.db.LoveMusicManager;

import java.util.List;

/**
 * Created by Enzo Cotter on 2020-11-11.
 */
public class MyMusicLoveAdapter extends RecyclerView.Adapter<MyMusicLoveAdapter.MusicHolder> {
    private Context context;
    private List<LoveMusic> loveMusics;
    private MusicOnclickCallback callback;

    public MyMusicLoveAdapter(Context context,MusicOnclickCallback callback,List<LoveMusic> loveMusics) {
        this.context = context;
        this.loveMusics=loveMusics;
        this.callback =callback;
    }

    public LoveMusicManager loveMusicManager;


    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        loveMusicManager = new LoveMusicManager(context);
        return new MyMusicLoveAdapter.MusicHolder(LayoutInflater.from(context).inflate(R.layout.item_love_music_list, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        holder.title.setText(loveMusics.get(position).getName());
        holder.message.setText(loveMusics.get(position).getMess());
        Glide.with(context).load(loveMusics.get(position).getPic_url()).into(holder.icon);
        holder.start.setOnClickListener((v)->callback.onclick(new MusicListInfo(loveMusics.get(position).getName(),loveMusics.get(position).getPic_url()
                ,loveMusics.get(position).getMess(),loveMusics.get(position).getMusic_url())));
        holder.remove.setOnClickListener(v->{
            loveMusicManager.delete(loveMusics.get(position).getName());
            loveMusics.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return loveMusics.size();
    }

    static class MusicHolder extends RecyclerView.ViewHolder {
        TextView title, message;
        ImageView icon, start, remove;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.music_title);
            message = itemView.findViewById(R.id.music_people);
            icon = itemView.findViewById(R.id.music_icon);
            start = itemView.findViewById(R.id.music_start);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
