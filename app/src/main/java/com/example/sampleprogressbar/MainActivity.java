package com.example.sampleprogressbar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ProgressBar pBar,pWheel;

    Button button1, button2, button3, button4;
    int pValue;

    public static class MyDialog extends DialogFragment {
        public static MyDialog newInstance(){return new MyDialog();}
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LayoutInflater inf =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View convertView = inf.inflate(R.layout.progress_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("");
            builder.setView(convertView);
            return builder.create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pBar = findViewById(R.id.pBar);
        pWheel = findViewById(R.id.pWheel);

        pBar.setIndeterminate(false); //첫시작을 멈춰진 상태

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        button1.setOnClickListener(new View.OnClickListener() {
            // 앱내에 지연될 작업이 있는경우 쓰레드를 이용해 유지(ANR 방지)
            // 안드로이드는 기본적으로 제대로 동작하지 않으면 자동으로 중단시키기 때문에
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (pValue < 100) {

                            pValue++;
                            pBar.setProgress(pValue);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        pValue = 0;
                    }
                });
                t.start();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyDialog myDialog = MyDialog.newInstance();
                myDialog.show(getSupportFragmentManager(), "TAG");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 초기 메인 앱 화면을 보여준후 다른 곳으로 이동시킬때 사용
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                myDialog.dismiss();
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 3000);
                    }
                });
                t.start();
            }
        });
    }
}
