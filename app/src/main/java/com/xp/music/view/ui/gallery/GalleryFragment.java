package com.xp.music.view.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xp.music.R;
import com.xp.music.adapter.MyMusicLoveAdapter;
import com.xp.music.db.LoveMusicManager;
import com.xp.music.view.MainActivity;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private LoveMusicManager musicManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        musicManager =new LoveMusicManager(getContext());
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.love_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MyMusicLoveAdapter adapter = new MyMusicLoveAdapter(getContext(), data -> MainActivity.getIn().playMusic(data),musicManager.searchAll());
        recyclerView.setAdapter(adapter);
    }
}
