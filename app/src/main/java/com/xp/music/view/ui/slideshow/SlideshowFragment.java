package com.xp.music.view.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.TxtViewHolder;
import com.xp.music.R;
import com.xp.music.bean.Msg;
import com.xp.music.bean.MsgAdapter;
import com.xp.music.bean.MsgInfo;
import com.xp.music.network.AIHttpManager;
import com.xp.music.network.CommonService;
import com.xp.music.network.UserHttpManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SlideshowFragment extends Fragment {
    private List<Msg> msgList = new ArrayList<>();
    private ListView msgListView;

    private EditText inputText;

    private Button send;
    private AIHttpManager manager=new AIHttpManager();
    private CommonService commonService;
    private MsgAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        commonService =manager.getService(CommonService.class);
        return inflater.inflate(R.layout.fragment_rebot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMsg();
        adapter = new MsgAdapter(getActivity(),R.layout.message_item, msgList);
        inputText = view.findViewById(R.id.input_text);
        send = view.findViewById(R.id.send);
        msgListView = view.findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(v -> {
            String content = inputText.getText().toString();
            if(!"".equals(content)){
                Msg msg = new Msg(content, Msg.SENT);
                msgList.add(msg);
                adapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                inputText.setText("");//清空输入框的内容
                commonService.getMessage(content,"free","0").enqueue(new Callback<MsgInfo>() {
                    @Override
                    public void onResponse(Call<MsgInfo> call, Response<MsgInfo> response) {
                        assert response.body() != null;
                        Msg msg1 = new Msg(response.body().getContent(), Msg.RECEIVED);
                        msgList.add(msg1);
                        adapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                        msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                    }

                    @Override
                    public void onFailure(Call<MsgInfo> call, Throwable t) {
                        Toast.makeText(getContext(),"服务返回异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void initMsg() {
        Msg msg1 = new Msg("你好~有什么事情跟我说说吧~!", Msg.RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("我有三秒的延迟哦~!", Msg.RECEIVED);
        msgList.add(msg2);
    }

}
