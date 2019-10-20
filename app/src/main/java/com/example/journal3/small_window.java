//package com.example.journal3;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//public class small_window extends AppCompatActivity {
//    Dialog demo_dialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.demo_window);
//        demo_dialog= new Dialog(this);
//    }
//    public void ShowPopup_order(View v)
//    {
//        TextView txtclose;
//        demo_dialog.setContentView(R.layout.pop_up);
//        txtclose=(TextView)demo_dialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                demo_dialog.dismiss();
//            }
//        });
//        demo_dialog.show();
//    }
//
//    public void ShowPopup_music(View v)
//    {
//        TextView txtclose;
//        demo_dialog.setContentView(R.layout.pop_up_music);
//        txtclose=(TextView)demo_dialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                demo_dialog.dismiss();
//            }
//        });
//        demo_dialog.show();
//    }
//
//    public void ShowPopup_calendar(View v)
//    {
//        TextView txtclose;
//        demo_dialog.setContentView(R.layout.pop_up_calendar);
//        txtclose=(TextView)demo_dialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                demo_dialog.dismiss();
//            }
//        });
//        demo_dialog.show();
//    }
//
//    public void save_share(View v)
//    {
//        TextView txtclose;
//        demo_dialog.setContentView(R.layout.pop_up_calendar);
//        txtclose=(TextView)demo_dialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                demo_dialog.dismiss();
//            }
//        });
//        demo_dialog.show();
//    }
//
//    public void upload_video(View v) {
//        TextView txtclose;
//        demo_dialog.setContentView(R.layout.pop_upload_video);
//        txtclose = (TextView) demo_dialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                demo_dialog.dismiss();
//            }
//        });
//        demo_dialog.show();
//    }
//
//    public void video1(View v) {
//        TextView txtclose;
//        demo_dialog.setContentView(R.layout.pop_upload_video);
//        txtclose = (TextView) demo_dialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                demo_dialog.dismiss();
//            }
//        });
//        demo_dialog.show();
//    }
//
//    public void video2(View v) {
//        TextView txtclose;
//        demo_dialog.setContentView(R.layout.pop_upload_video);
//        txtclose = (TextView) demo_dialog.findViewById(R.id.txtclose);
//        txtclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                demo_dialog.dismiss();
//            }
//        });
//        demo_dialog.show();
//    }
//}
