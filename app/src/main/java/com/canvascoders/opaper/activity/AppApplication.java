package com.canvascoders.opaper.activity;

import android.content.Context;
import android.os.StrictMode;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.canvascoders.opaper.utils.Constants;
import com.canvascoders.opaper.utils.NetworkConnectivity;
import com.canvascoders.opaper.utils.SessionManager;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AppApplication extends MultiDexApplication {

    String TAG = AppApplication.class.getSimpleName();
    private static AppApplication mInstance;
    public static float dip;
//    private RequestQueue mRequestQueue;

    public static final String BASE_URL = Constants.BaseURL;

    public static Retrofit retrofit = null;
    public  static  Retrofit retrofit2 = null;
    public static NetworkConnectivity networkConnectivity;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        mInstance = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        dip = getResources().getDisplayMetrics().widthPixels;
        dip = dip / 320;

        networkConnectivity = new NetworkConnectivity(getApplicationContext());

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(6, TimeUnit.MINUTES);
        httpClient.writeTimeout(6, TimeUnit.MINUTES);
        httpClient.readTimeout(6, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging2 = new HttpLoggingInterceptor();
        logging2.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder();
        httpClient2.connectTimeout(60, TimeUnit.SECONDS);
        httpClient2.writeTimeout(60, TimeUnit.SECONDS);
        httpClient2.readTimeout(60, TimeUnit.SECONDS);
        httpClient2.addInterceptor(logging2);
        httpClient2.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                okhttp3.Request original = chain.request();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                okhttp3.Request request = original.newBuilder()
                        .header("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQ4MDNmZWIzNzU4NWZkNGI1NGE2NWFlYTgzZGI2NDJmZDBkZTIyYTg5YTVhZmVjN2M3M2E0MzZjZTk4ZmM2NjQ2YmZhNTgzMDZiNGRlY2E5In0.eyJhdWQiOiIzIiwianRpIjoiNDgwM2ZlYjM3NTg1ZmQ0YjU0YTY1YWVhODNkYjY0MmZkMGRlMjJhODlhNWFmZWM3YzczYTQzNmNlOThmYzY2NDZiZmE1ODMwNmI0ZGVjYTkiLCJpYXQiOjE1NTI1Njc3MjEsIm5iZiI6MTU1MjU2NzcyMSwiZXhwIjoxNTg0MTkwMTIxLCJzdWIiOiIzIiwic2NvcGVzIjpbIioiXX0.VL_2DtY6w1rBcNZA01g2w-5CS9c5QXDhjL7beYbK3o0LqffJY9YHP5RELzNUbL1gnDpepdFUIgAQxkbxZP4fZVaVs1k-DT7wVArOoLqVqaBK5dgGWQQKBAq_wK1v7ys-FwrUNRMgQm5W67q7Ks4hIcCRIeTrXPp5dPyHaos_OWtALykJqdGHSBpOwurvTQ_RkQKfAQrCYpvRZjgllQl22YMzE2tzrfxPWPoWkPz4ZCzTX21KTY7xzCpj2qs60XF5baWDnoB_S3vrJdD79PEjaaJHo22-mjhbjcjODPzwksXSebmy2XZDHBSVypEkwB_lsJ5nFVR_taFNmkdn-xQ56CF5liEeumNinFOMhURRsY8tyIOtdaPnIECodIakajMWrWdBYaLI__X4pLMbhXdqpP9NxkO9EMyYfh-P--8EnjNcsHR_HCVcJKbWPm06TXyfw7r8fX-CjP8xWHHFOeU85crEfIGnUTkJkvWGNKGWjxa2Qptq5rh7ruFt1SDO2ce5fgNq6MjIOWhXr31PqzxGC8uBmNy5okgsfrVgElXjCRwD5z2oj2GNtcINga4NHb43Tya_DOTKIw4YGLNDcqUdrwaBpqzV1PNm3IUJ_rrrAYwwZ0SMGJNo03xwFsR2PYT8O5PXu6mGbPbnGBiaVL7AyLQGQDS28X-Pe4dbmYUA65U")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        retrofit2 = new Retrofit.Builder()
                .baseUrl(Constants.BaseURLOCR)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient2.build())
                .build();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


    }

    public static AppApplication getInstance() {
        return mInstance;

    }

    @Override
    public void onTerminate() {
        try {
            deleteCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onTerminate();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception ignored) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}