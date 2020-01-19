package com.canvascoders.opaper.utils;

/**
 * Created by narendra on 10/3/2016.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Author: Mario Velasco Casquero
 * Date: 08/09/2015
 * Email: m3ario@gmail.com
 */
public class ImagePicker {

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 50;        // min pixels
    private static final String TAG = "ImagePicker";
    private static final String TEMP_IMAGE_NAME = "tempImage";

    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;
    public static String filePath = "";

    public static Intent getGalleryIntenr(Context context) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return pickIntent;
    }

    // Old Code
//    public static Intent getCameraIntent(Context context) {
//        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePhotoIntent.putExtra("return-data", true);
//        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
//        return takePhotoIntent;
//    }
    public static Intent getCameraIntent(Context context) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);

        if (Build.VERSION.SDK_INT > 21 && Build.VERSION.SDK_INT <= 28) {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", getTempFile()));
            Log.e("datadtaa", String.valueOf(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", getTempFile())));
        } else if (Build.VERSION.SDK_INT >= 29) {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", getTempFile()));
            Log.e("datadtaa", String.valueOf(FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", getTempFile())));

        } else {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
            Log.e("datadtaa", String.valueOf(Uri.fromFile(getTempFile())));

        }
        return takePhotoIntent;

    }

    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Intent getPickImageIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Uri bmpUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", getTempFile());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, bmpUri);
        } else {

            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
        }

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }
        return chooserIntent;
    }

//    public static Intent getPickImageIntent(Context context) {
//        Intent chooserIntent = null;
//
//        List<Intent> intentList = new ArrayList<>();
//
//        Intent pickIntent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePhotoIntent.putExtra("return-data", true);
//        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
//            Uri bmpUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", getTempFile());
//            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, bmpUri);
//        } else {
//
//            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));
//        }
//
//        intentList = addIntentsToList(context, intentList, pickIntent);
//        intentList = addIntentsToList(context, intentList, takePhotoIntent);
//
//        if (intentList.size() > 0) {
//            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
//                    "Select Image");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
//        }
//        return chooserIntent;
//    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            Log.d(TAG, "Intent: " + intent.getAction() + " package: " + packageName);
        }
        return list;
    }


    public static Bitmap getImageFromResult(Context context, int resultCode,
                                            Intent imageReturnedIntent) {
        Log.d(TAG, "getImageFromResult, resultCode: " + resultCode);
        Bitmap bm = null;
        File imageFile = getTempFile();
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null ||
                    imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {
                Log.e("build_versionn", String.valueOf(Build.VERSION.SDK_INT));/** CAMERA **/
                if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
                    selectedImage = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", imageFile);
                    Constants.KEY_PHOTO = selectedImage;
                    Log.e("datadata", String.valueOf(Constants.KEY_PHOTO));
                } else {
                    selectedImage = Uri.fromFile(imageFile);

                }
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            filePath = selectedImage.getPath();
            Log.d("selectedImage:", selectedImage.toString());

            bm = getImageResized(context, selectedImage);
            // int rotation = getRotation(context, selectedImage, isCamera);
            //bm = rotate(bm, rotation);
        }
        return bm;
    }


    private static final String IMAGE_DIRECTORY_NAME = ".oppr";

    private static File getTempFile() {
        Log.e("TEMP ", "File Called");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            Log.e("Directory", "Not Exists");
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Directory", "Not Made");
                Mylogger.getInstance().Logit(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            } else {
                Log.e("Directory", "Made");
            }
        }

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "O_" + TEMP_IMAGE_NAME + ".jpeg");

        Log.e("Media File", "Launched:" + mediaFile.getPath());
        return mediaFile;
//        File imageFile = new File(Environment.getExternalStorageDirectory(), TEMP_IMAGE_NAME);
//        imageFile.getParentFile().mkdirs();
//        return imageFile;
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = sampleSize;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);
        Log.d(TAG, options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());
        return actuallyUsableBitmap;
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            Log.d(TAG, "resizer: new bitmap width = " + bm.getWidth());
            i++;
        } while (bm.getWidth() < minWidthQuality && i < sampleSizes.length);
        return bm;
    }


    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        Log.d(TAG, "Image rotation: " + rotation);
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            ContentResolver content = context.getContentResolver();

            content.notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(Objects.requireNonNull(imageFile.getPath()));
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }


    public static int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        String[] columns = {MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } catch (Exception e) {
            //Do nothing
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }//End of try-catch block
        return result;
    }


    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bmOut = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            return bmOut;
        }
        return bm;
    }

    public static String getImagePath() {
        return filePath;
    }

    public static String getPathFromUri(Context context, Uri uri) {

        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    //// start from here...
    public static String getBitmapPath(Bitmap bmp, Context mContext) {
        try {

            Cursor cursor;
            int idx;
            //
            if (Build.VERSION.SDK_INT <= 28) {
               /* ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Title");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");*/

                //Uri path = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                try {


                    //String path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title",null);
                    String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bmp, "Title", null);

                    Uri tempUri = Uri.parse(path);
                    cursor = mContext.getContentResolver().query(tempUri, null, null, null, null);
                    cursor.moveToFirst();
                    idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

                    return cursor.getString(idx);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }

            } else {
                OutputStream stream = null;
                final ContentResolver resolver = mContext.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Title");
                //values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures" + File.separator + "opaper");
                Uri uri = null;
                // Uri path = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                // String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bmp, "Title", null);

                // Uri tempUri = Uri.parse(path);


                uri = resolver.insert(contentUri, values);

                if (uri == null) {
                    throw new IOException("Failed to create new MediaStore record.");
                }

                stream = resolver.openOutputStream(uri);

                if (stream == null) {
                    throw new IOException("Failed to get output stream.");
                }

                if (bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream) == false) {
                    throw new IOException("Failed to save bitmap.");
                }


                String[] projection = {
                        MediaStore.Files.FileColumns._ID,
                        MediaStore.Images.Media.DATE_TAKEN,
                        MediaStore.Images.Media.WIDTH,
                        MediaStore.Images.Media.HEIGHT,
                        MediaStore.MediaColumns.TITLE,
                        MediaStore.Images.Media.MIME_TYPE,

                };

                cursor = mContext.getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                //  ImageDecoder.Source source = ImageDecoder.createSource(mContext.getContentResolver(), uri);
                idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private void saveImage(Bitmap bitmap, @NonNull String name, Context mContext) throws IOException {
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = mContext.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, name + ".jpg");
            fos = new FileOutputStream(image);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).close();
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}