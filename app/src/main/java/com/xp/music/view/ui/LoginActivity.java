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
import com.xp.music.utils.SUtils;
import com.xp.music.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    EditText uid, pass;
    Button regist, sigin;
    SUtils sUtils;
    int count = 0;

    public int getTime() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
        return Integer.parseInt(sdf1.format(new Date()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sUtils = new SUtils(getApplicationContext());
        if(!sUtils.getString("uname").equals("0")){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        imageView = findViewById(R.id.imageView);
        uid = findViewById(R.id.uid);
        pass = findViewById(R.id.pass);
        textView = findViewById(R.id.textView);
        regist = findViewById(R.id.res);
        sigin = findViewById(R.id.signin);
        title();
        regist.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
        sigin.setOnClickListener(v -> {
            if (pass.getText() != null && uid.getText() != null) {
                NetWorkManager manager = new NetWorkManager();
                manager.signIn(uid.getText().toString(), pass.getText().toString(), new UserCallback() {
                    @Override
                    public void onState(int state) {
                        runOnUiThread(() -> {
                            if (state == 0) {
                                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_LONG).show();
                            } else if (state == 1) {
                                Toast.makeText(getApplicationContext(), "账号密码验证成功", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "账号未注册", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void signInSuccess(User user) {
                        runOnUiThread(() -> {
                            sUtils.setString("uname", user.getUname());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        });
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "请输入密码或账号", Toast.LENGTH_SHORT).show();
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
