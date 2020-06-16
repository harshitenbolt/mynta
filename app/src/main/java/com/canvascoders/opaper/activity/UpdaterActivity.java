package com.canvascoders.opaper.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.canvascoders.opaper.BuildConfig;
import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.RealPathUtil;


import java.io.File;

public class UpdaterActivity extends AppCompatActivity {
    private AppCompatButton btn_update;
    private ProgressBar progressBar;
    private AppCompatTextView txt_note;

    //    private PermissionUtil.PermissionRequestObject mALLPermissionRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_updater);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("");

        btn_update = (AppCompatButton) findViewById(R.id.btn_update);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_note = (AppCompatTextView) findViewById(R.id.txt_note);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                txt_note.setVisibility(View.VISIBLE);
                appUpdate();
            }
        });
//        RequestAllPermission();
    }
//    public void RequestAllPermission() {
//        String[] allPermission = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};
//        mALLPermissionRequest = PermissionUtil.with(this).request(allPermission).onResult(
//                new Func2() {
//                    @Override
//                    protected void call(int requestCode, String[] permissions, int[] grantResults) {
//
//                    }
//                }).onAllGranted(new Func() {
//            @Override
//            protected void call() {
//
//            }
//        }).ask(101);
//    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Please wait till app update", Toast.LENGTH_SHORT).show();
    }

//version 23


    private void appUpdate() {

        //  String destination = Environment.getExternalStoragePublicDirectory()(Environment.DIRECTORY_PICTURES) + "/";

        String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_PICTURES + "/";
        ;
        String fileName = "opaper.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);
        File file = new File(RealPathUtil.getRealPath(UpdaterActivity.this, uri));
        /*if (file.exists())
            file.delete();*/
        String url = Constants.APKROOT + "apk/opaper.apk";
        /* String url = "http://139.59.94.135/apk/opaper.apk"; */ //Constants.BaseURL + "apk/opaper.apk";//
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("opaper Version" + Constants.APP_VERSION);
        request.setTitle("Opaper Update");
        request.setDestinationUri(uri);
        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    Uri apkUri = FileProvider.getUriForFile(UpdaterActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);

                    install.setDataAndType(apkUri,
                            manager.getMimeTypeForDownloadedFile(downloadId));
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(install);

                    unregisterReceiver(this);
                    finish();

                    /*Uri apkUri = FileProvider.getUriForFile(UpdaterActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);*/
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Uri apkUri = FileProvider.getUriForFile(UpdaterActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    install.setDataAndType(uri, manager.getMimeTypeForDownloadedFile(downloadId));
                    startActivity(install);
                    unregisterReceiver(this);
                    finish();
                }
            }
        };
        //register receiver for when .apk download is compete
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
