package com.xp.music.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.xp.music.R;
import com.xp.music.bean.User;
import com.xp.music.callback.UserCallback;
import com.xp.music.network.NetWorkManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    EditText uid, pass,repass,name;
    Button regist, sigin;
    int count = 0;
    public int getTime(){
        SimpleDateFormat sdf1=new SimpleDateFormat("HH");
        return Integer.parseInt(sdf1.format(new Date()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_regist);
        imageView = findViewById(R.id.imageView);
        uid = findViewById(R.id.uid);
        pass = findViewById(R.id.pass);
        repass=findViewById(R.id.repass);
        name=findViewById(R.id.name);
        textView = findViewById(R.id.textView);
        regist = findViewById(R.id.register);
        sigin = findViewById(R.id.signin);
        title();
        sigin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        regist.setOnClickListener(v->{
          if(pass.getText() != null && uid.getText() != null&&repass.getText() != null && name.getText() != null){
             if(pass.getText().toString().equals(repass.getText().toString())){
                 NetWorkManager manager = new NetWorkManager();
                 manager.register(new User(name.getText().toString(),
                         pass.getText().toString(),
                         uid.getText().toString()), new UserCallback() {
                     @Override
                     public void onState(int state) {
                         if(state==1){
                             Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                             finish();
                         }else {
                             Toast.makeText(getApplicationContext(),"注册失败，数据库中已经存在",Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void signInSuccess(User user) {

                     }
                 });
             }else {
                 Toast.makeText(getApplicationContext(),"两次密码输入不一致",Toast.LENGTH_LONG).show();
             }
          }else {
              Toast.makeText(getApplicationContext(),"请全部输入",Toast.LENGTH_LONG).show();
          }

        });
    }
    public void title() {
        if (getTime() >= 12 && getTime() <= 19) {
            imageView.setImageResource(R.drawable.good_morning_img);
            textView.setText("Afternoon");
        } else if (getTime() > 6 && getTime() < 12) {
            imageView.setImageResource(R.drawable.good_morning_img);
            textView.setText("Morning");
        } else {
            imageView.setImageResource(R.drawable.good_night_img);
            textView.setText("Night");
        }
    }
}
