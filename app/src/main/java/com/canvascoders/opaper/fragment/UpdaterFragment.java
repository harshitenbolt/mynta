package com.canvascoders.opaper.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.canvascoders.opaper.R;
import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.RealPathUtil;
import com.canvascoders.opaper.activity.DashboardActivity;

import java.io.File;

public class UpdaterFragment extends Fragment {
    private AppCompatButton btn_update;
    private ProgressBar progressBar;
    private AppCompatTextView tv_note;
    private Context mcontext;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_updater, container, false);

        mcontext = this.getActivity();

        btn_update = (AppCompatButton) view.findViewById(R.id.btn_update);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tv_note = (AppCompatTextView) view.findViewById(R.id.txt_note);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                tv_note.setVisibility(View.VISIBLE);
                appUpdate();
            }
        });
        return view;
    }

    /*@Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Please wait till app update", Toast.LENGTH_SHORT).show();
    }*/

    private void appUpdate() {


        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "opaper.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);
        File file = new File(RealPathUtil.getRealPath(mcontext, uri));
        if (file.exists())
            file.delete();

        String url = Constants.APKROOT + "apk/opaper.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("opaper Version" + Constants.APP_VERSION);
        request.setTitle("Opaper Update");
        request.setDestinationUri(uri);
        final DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri, manager.getMimeTypeForDownloadedFile(downloadId));
                startActivity(install);
                getActivity().unregisterReceiver(this);
                getActivity().finish();
            }
        };
        //register receiver for when .apk download is compete
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
