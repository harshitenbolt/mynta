package com.canvascoders.opaper.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.canvascoders.opaper.R;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Logger;
import com.isseiaoki.simplecropview.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CropImage2Activity extends AppCompatActivity {


    public static final String KEY_SOURCE_URI = "SourceUri";
    private static final String TAG = CropImage2Activity.class.getSimpleName();
    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_SAF_PICK_IMAGE = 10012;
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private static final String KEY_FRAME_RECT = "FrameRect";
    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
            Log.e("", "");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("", "");
        }
    };
    boolean isFromGroup = false;
    // Views ///////////////////////////////////////////////////////////////////////////////////////
    private CropImageView mCropView;
    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            dismissProgress();
            Uri bitmap = mCropView.getSaveUri();
            Intent intent = new Intent();
            intent.putExtra("uri", bitmap.toString());
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onError(Throwable e) {
            dismissProgress();
        }
    };




    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private RectF mFrameRect = null;
    private Uri mSourceUri = null;
    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            int compressQuality = 0;
            File file;
            try {
                if (mSourceUri.toString().startsWith("file://")) {
                    file = new File(getRealPathFromURI(mSourceUri));
                } else {
                    file = new File(getRealPathFromUri(CropImage2Activity.this, mSourceUri));
                }

                //File file = new File(getRealPathFromURI( mSourceUri));
                // Get length of file in bytes
                long fileSizeInBytes = file.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                if (fileSizeInMB > 5) {
                    compressQuality = 30;
                } else if (fileSizeInMB > 4) {
                    compressQuality = 40;
                } else if (fileSizeInMB > 3) {
                    compressQuality = 50;
                } else if (fileSizeInMB > 2) {
                    compressQuality = 60;
                } else if (fileSizeInMB > 1) {
                    compressQuality = 90;
                } else {
                    compressQuality = 95;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mCropView.save(cropped)
                   .compressFormat(mCompressFormat)
                    .compressQuality(compressQuality)
                    .execute(createSaveUri(), mSaveCallback);


        }

        @Override
        public void onError(Throwable e) {
        }
    };
    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonDone:
                    if (isFromGroup) {
                        mCropView.crop(mSourceUri).outputMaxHeight(512).outputMaxWidth(512).execute(mCropCallback);
                    } else {
                        mCropView.crop(mSourceUri).outputMaxHeight(800).outputMaxWidth(800).execute(mCropCallback);
                    }
                    //BasicFragmentPermissionsDispatcher.cropImageWithCheck(BasicFragment.this);
                    break;
                case R.id.buttonFitImage:
                    mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                    break;
                case R.id.button1_1:
                    mCropView.setCropMode(CropImageView.CropMode.SQUARE);
                    break;
                case R.id.button3_4:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                    break;
                case R.id.button4_3:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    break;
                case R.id.button9_16:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                    break;
                case R.id.button16_9:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    break;
                case R.id.buttonCustom:
                    mCropView.setCustomRatio(7, 5);
                    break;
                case R.id.buttonFree:
                    mCropView.setCropMode(CropImageView.CropMode.FREE);
                    break;
                case R.id.buttonCircle:
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                    break;
                case R.id.buttonShowCircleButCropAsSquare:
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
                    break;
                case R.id.buttonRotateLeft:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    break;
                case R.id.buttonRotateRight:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    break;
                case R.id.buttonPickImage:
                    //BasicFragmentPermissionsDispatcher.pickImageWithCheck(BasicFragment.this);
                    break;
            }
        }
    };


    public static String getDirPath() {
        String dirPath = "";
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath() + "/.simplecropview");
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.getPath();
            }
        }
        return dirPath;
    }

    public static Uri getUriFromDrawableResId(Context context, int drawableResId) {
        StringBuilder builder = new StringBuilder().append(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .append("://")
                .append(context.getResources().getResourcePackageName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceTypeName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceEntryName(drawableResId));
        return Uri.parse(builder.toString());
    }

    // Bind views //////////////////////////////////////////////////////////////////////////////////

    public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "scv" + title + "." + getMimeType(format);
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
        values.put(MediaStore.Images.Media.DATA, path);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Logger.i("SaveUri = " + uri);
        return uri;
    }

    public static String getMimeType(Bitmap.CompressFormat format) {
        Logger.i("getMimeType CompressFormat = " + format);
        switch (format) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";
    }

    public static Uri createTempUri(Context context) {
        return Uri.fromFile(new File(context.getCacheDir(), "cropped"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image2);


        bindViews();

        mCropView.setDebug(false);

        if (getIntent().getExtras() != null) {
            // restore data
            mFrameRect = getIntent().getExtras().getParcelable(KEY_FRAME_RECT);
            mSourceUri = Uri.parse(getIntent().getExtras().getString(KEY_SOURCE_URI));
            isFromGroup = getIntent().getExtras().getBoolean("is_from_group", false);
        }

     /*   if (mSourceUri == null) {
            // default data
            mSourceUri = getUriFromDrawableResId(CropImageActivity.this, R.drawable.ic_account_selected);
            Log.e("aoki", "mSourceUri = " + mSourceUri);
        }*/
        // load image
        mCropView.load(null);
        mCropView.load(mSourceUri)
                .initialFrameRect(mFrameRect)
                .useThumbnail(false)
                .execute(mLoadCallback);

        mCropView.setCropMode(CropImageView.CropMode.FREE);
        // Utility.CrashLogActivity("CropImageACtivity-onCreate");

    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromUri(Context context, Uri uri){

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion>=19) {


            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }else if (currentAPIVersion>=11 && currentAPIVersion<=18) {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }else if (currentAPIVersion<=10){
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data
        outState.putParcelable(KEY_FRAME_RECT, mCropView.getActualCropRect());
        outState.putParcelable(KEY_SOURCE_URI, mCropView.getSourceUri());
    }
/*

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(CropImageActivity.this).setPositiveButton(R.string.button_allow,
                new DialogInterface.OnClickListener() {
                    @Override public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
            @Override public void onClick(@NonNull DialogInterface dialog, int which) {
                request.cancel();
            }
        }).setCancelable(false).setMessage(messageResId).show();
    }

    // Handle button event /////////////////////////////////////////////////////////////////////////
*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == Activity.RESULT_OK) {
            // reset frame rect
            mFrameRect = null;
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    mSourceUri = result.getData();
                    mCropView.load(mSourceUri)
                            .initialFrameRect(mFrameRect)
                            .useThumbnail(true)
                            .execute(mLoadCallback);
                    break;
                case REQUEST_SAF_PICK_IMAGE:
                    mSourceUri = Utils.ensureUriPermission(CropImage2Activity.this, result);
                    mCropView.load(mSourceUri)
                            .initialFrameRect(mFrameRect)
                            .useThumbnail(true)
                            .execute(mLoadCallback);
                    break;
            }
        }
    }

    // Callbacks ///////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //BasicFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void bindViews() {
        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        findViewById(R.id.button1_1).setOnClickListener(btnListener);
        findViewById(R.id.button3_4).setOnClickListener(btnListener);
        findViewById(R.id.button4_3).setOnClickListener(btnListener);
        findViewById(R.id.button9_16).setOnClickListener(btnListener);
        findViewById(R.id.button16_9).setOnClickListener(btnListener);
        findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        findViewById(R.id.buttonPickImage).setOnClickListener(btnListener);
        findViewById(R.id.buttonRotateLeft).setOnClickListener(btnListener);
        findViewById(R.id.buttonRotateRight).setOnClickListener(btnListener);
        findViewById(R.id.buttonCustom).setOnClickListener(btnListener);
        findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
        findViewById(R.id.buttonShowCircleButCropAsSquare).setOnClickListener(btnListener);
    }

    /* @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE) public void pickImage() {
         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
             startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                     REQUEST_PICK_IMAGE);
         } else {
             Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
             intent.addCategory(Intent.CATEGORY_OPENABLE);
             intent.setType("image/*");
             startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE);
         }
     }

     @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) public void cropImage() {
         showProgress();
         mCropView.crop(mSourceUri).execute(mCropCallback);
     }

     @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
     public void showRationaleForPick(PermissionRequest request) {
         showRationaleDialog(R.string.permission_pick_rationale, request);
     }

     @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
     public void showRationaleForCrop(PermissionRequest request) {
         showRationaleDialog(R.string.permission_crop_rationale, request);
     }

     public void showProgress() {
         ProgressDialogFragment f = ProgressDialogFragment.getInstance();
         getFragmentManager().beginTransaction().add(f, PROGRESS_DIALOG).commitAllowingStateLoss();
     }

     public void dismissProgress() {
         if (!isResumed()) return;
         android.support.v4.app.FragmentManager manager = getFragmentManager();
         if (manager == null) return;
         ProgressDialogFragment f = (ProgressDialogFragment) manager.findFragmentByTag(PROGRESS_DIALOG);
         if (f != null) {
             getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
         }
     }
 */
    public Uri createSaveUri() {
        return createNewUri(CropImage2Activity.this, mCompressFormat);
    }

    public void dismissProgress() {
       /* if (!isResumed()) return;
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        if (manager == null) return;
        ProgressDialogFragment f = (ProgressDialogFragment) manager.findFragmentByTag(PROGRESS_DIALOG);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }*/
    }
}
