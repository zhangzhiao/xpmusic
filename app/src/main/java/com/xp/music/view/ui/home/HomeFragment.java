package com.xp.music.view.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.xp.music.R;
import com.xp.music.adapter.MyMusicListAdapter;
import com.xp.music.bean.MusicData;
import com.xp.music.bean.MusicListInfo;
import com.xp.music.callback.MusicGetCallback;
import com.xp.music.callback.MusicOnclickCallback;
import com.xp.music.network.NetWorkManager;
import com.xp.music.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MusicGetCallback, MusicOnclickCallback {
    private SwipeRefreshLayout swipeRefreshLayout;
    private NetWorkManager manager;
    private MyMusicListAdapter adapter;
    private RecyclerView recyclerView;
    private Handler handler = new Handler();
    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 15;
    private GridLayoutManager mLayoutManager;
    private List<MusicListInfo> list;
    private boolean isFirst=true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        list =new ArrayList<>();
    }

    private void initData() {
        manager = new NetWorkManager();
        manager.getRandMusic(this);
    }

    private void initView(View view) {
        swipeRefreshLayout = view.findViewById(R.id.base_refresh);
        recyclerView =view.findViewById(R.id.music_recycle);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);

    }
    private void initRecyclerView() {
        isFirst=false;
        adapter = new MyMusicListAdapter(getDatas(0, PAGE_COUNT), getContext(), getDatas(0, PAGE_COUNT).size() > 0,this);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        handler.postDelayed(() -> {
                            initData();
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        handler.postDelayed(() -> {
                            initData();
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
    private List<MusicListInfo> getDatas(final int firstIndex, final int lastIndex) {
        List<MusicListInfo> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < list.size()) {
                resList.add(list.get(i));
            }
        }
        return resList;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<MusicListInfo> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

    //下拉刷新数据
    @Override
    public void onRefresh() {
        list.clear();
        initData();
        isFirst=true;
    }

    @Override
    public void getSuccess(List<MusicData> datas) {
        requireActivity().runOnUiThread(()->swipeRefreshLayout.setRefreshing(false));
        for (MusicData data:datas) {
            list.add(new MusicListInfo(data.getName(),data.getPicurl(),data.getArtistsname(),data.getUrl()));
        }

        if(isFirst){
            requireActivity().runOnUiThread(this::initRecyclerView);
        }else {
            requireActivity().runOnUiThread(()-> updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT));
        }
    }

    @Override
    public void getError(Throwable throwable) {

    }

    @Override
    public void onclick(MusicListInfo data) {
        MainActivity.getIn().playMusic(data);
    }
}
