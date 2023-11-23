package com.example.bai3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermission()) {
            String path = Environment.getExternalStorageDirectory().getPath();

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            TextView noFilesText = findViewById(R.id.nofiles_textview);

            File root = new File(path);
            File[] filesAndFolders = root.listFiles();

            if(filesAndFolders==null || filesAndFolders.length ==0){
                noFilesText.setVisibility(View.VISIBLE);
                return;
            }

            noFilesText.setVisibility(View.INVISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MyAdapter(getApplicationContext(),filesAndFolders));

        }else{
            requestPermission();
        }
    }

    private Bitmap getImage(MediaMetadataRetriever retriever){
        byte[] data = retriever.getEmbeddedPicture();
        if (data != null)
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }

    private void LayThongTin(String filePath){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(filePath);

            // Lấy thông tin từ file nhạc
            String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            // In thông tin ra Logcat hoặc làm gì đó khác với nó
            Log.d("MusicInfo", "Title: " + title);
            Log.d("MusicInfo", "Artist: " + artist);
            Log.d("MusicInfo", "Album: " + album);
            Log.d("MusicInfo", "Duration: " + duration + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else
            return false;
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"Storage permission is requires,please allow from settings",Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
    }
}