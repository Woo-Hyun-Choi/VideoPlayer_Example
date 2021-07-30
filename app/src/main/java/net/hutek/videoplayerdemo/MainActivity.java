package net.hutek.videoplayerdemo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "whchoi";
    ListView m_list;
    VideoView m_videoView, videoView, videoView2, videoView3;
    ArrayList<String> m_fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        m_videoView = findViewById(R.id.m_videoView);
        videoView = findViewById(R.id.videoView);
        videoView2 = findViewById(R.id.videoView2);
        videoView3 = findViewById(R.id.videoView3);

        m_fileList = getVideoList();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, m_fileList);
        m_list = findViewById(R.id.listview_item);
        m_list.setAdapter(adapter);
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strImg = m_fileList.get(position);
                Toast.makeText(getApplicationContext(), strImg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "1");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strImg));
                Log.d(TAG, "URI = " + Uri.parse(strImg));
                Log.d(TAG, "intent = " + intent);
                startActivity(intent);
                Log.d(TAG, "3");
            }
        });


    }

    public void bt1(View view) {    // 동영상 선택 누르면 실행됨 동영상 고를 갤러리 오픈
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                MediaController mc = new MediaController(this); // 비디오 컨트롤 가능하게(일시정지, 재시작 등)
                videoView.setMediaController(mc);

                Uri fileUri = data.getData();
                videoView.setVideoPath(String.valueOf(fileUri));    // 선택한 비디오 경로 비디오뷰에 셋
                videoView.start();  // 비디오뷰 시작
            }
        }
    }

//    public void bt2(View view) {
//        MediaController mc = new MediaController(this); // 비디오 컨트롤 가능하게(일시정지, 재시작 등)
//        videoView2.setMediaController(mc);
//        videoView2.setVideoURI(Uri.parse(editText.getText().toString()));    // 선택한 비디오 경로 비디오뷰에 셋
//        videoView2.requestFocus();
//        videoView2.start();  // 비디오뷰 시작
//    }

    public void bt3(View view) {
        MediaController mc = new MediaController(this); // 비디오 컨트롤 가능하게(일시정지, 재시작 등)
        videoView3.setMediaController(mc);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videoviewdemo);
        videoView3.setVideoURI(videoUri);
        videoView3.start();
    }


    private ArrayList<String> getVideoList() {
        ArrayList<String> fileList = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        Cursor cursor = getContentResolver().query(
                uri,
                projection,
                null,
                null,
                MediaStore.MediaColumns.DATE_ADDED + " desc");

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int lastIndex;

        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);
            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

            if (!TextUtils.isEmpty(absolutePathOfImage)) {
                fileList.add(absolutePathOfImage);
            }
        }

        //        ArrayAdapter adapter = (ArrayAdapter) m_list.getAdapter();
//        // app 수명이 남아있는 동안 어댑터가 읽는 기본 데이터를 변경하는 경우 해당 라인으로 데이터가 변경됨을 알려줘야함
//        adapter.notifyDataSetChanged();
        return fileList;
    }

//    public void getVideoList() {
//        m_fileList.clear();
//        m_fileList = new ArrayList();
//        Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {
//                MediaStore.MediaColumns.DATA,
//                MediaStore.MediaColumns.DISPLAY_NAME
//        };
//
//        Cursor cursor = getContentResolver().query(
//                uri,
//                projection,
//                null,
//                null,
//                MediaStore.MediaColumns.DATE_ADDED + " desc");
//
//        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
//        int lastIndex;
//        while (cursor.moveToNext()) {
//            String absolutePathOfVideo = cursor.getString(columnIndex);
//            String nameOfFile = cursor.getString(columnDisplayname);
//            lastIndex = absolutePathOfVideo.lastIndexOf(nameOfFile);
//            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;
//
//            if (!TextUtils.isEmpty(absolutePathOfVideo)) { // 일종의 필터
//                m_fileList.add(absolutePathOfVideo);
//            }
//        }
//
//        ArrayAdapter adapter = (ArrayAdapter) m_list.getAdapter();
//        // app 수명이 남아있는 동안 어댑터가 읽는 기본 데이터를 변경하는 경우 해당 라인으로 데이터가 변경됨을 알려줘야함
//        adapter.notifyDataSetChanged();
//    }


    // onPause
    @Override
    protected void onPause() {
        super.onPause();
        if (m_videoView != null && m_videoView.isPlaying()) {
            m_videoView.pause();
        }
    }

    // onResume
    @Override
    protected void onResume() {
        super.onResume();
//        getVideoList();
    }


    // onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_videoView != null) {
            m_videoView.stopPlayback();
        }
    }


}
