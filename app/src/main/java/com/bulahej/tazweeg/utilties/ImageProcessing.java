package com.bulahej.tazweeg.utilties;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageProcessing {

    public static final int PICTURE_FROM_CAMERA = 1435;
    public static final int PICTURE_FROM_GALLERY = 1862;
    public static final String PICTURE_EXTENSION = ".jpeg";
    public static final String CACHE_IMAGE_NAME = "7590" + PICTURE_EXTENSION;

    private Activity activity;

    public ImageProcessing(Activity activity) {
        this.activity = activity;
    }

    public void dialogPickImage(String message, String bCameraName, String bStorageName) {

        final Activity activity1 = activity;

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(message);
        dialog.setPositiveButton(bCameraName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadFromCamera();
            }
        });
        dialog.setNegativeButton(bStorageName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadFromGallery();
            }
        });
        dialog.setCancelable(true);

        dialog.create().show();

    }

    public void loadFromCamera() {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(intent, PICTURE_FROM_CAMERA);
        }

    }

    public void loadFromGallery() {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0
            );

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent, PICTURE_FROM_GALLERY);
        }

    }

    public byte[] onActivityResult(int requestCode, int resultCode, Intent data) {

        byte[] pictureBytes = null;

        if (data != null) {

            if (requestCode == PICTURE_FROM_CAMERA && resultCode == activity.RESULT_OK) {
                Utilities.myLogDebug("PICTURE_FROM_CAMERA");

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                pictureBytes = getPictureBytes(bitmap);

            } else if (requestCode == PICTURE_FROM_GALLERY && resultCode == activity.RESULT_OK) {
                Utilities.myLogDebug("PICTURE_FROM_GALLERY");

                Uri pictureUri = data.getData();
                pictureBytes = getPictureBytes(pictureUri);

            }

        }

        return pictureBytes;
    }

    public String getImageInformation(Bitmap bitmap) {

        //Hint: Each information is separated by coma(,).
        Utilities.myLogWarning("Picture from camera: Width=%d, Height=%d, ByteCount=%d, RowBytes x Height=%d, Height/Width=%f",
                bitmap.getWidth(),
                bitmap.getHeight(),
                bitmap.getByteCount(),
                bitmap.getRowBytes() * bitmap.getHeight(),
                (float) bitmap.getHeight() / bitmap.getWidth()
        );

        return "";
    }

    public String getImageInformation(byte[] bitmap) {

        //Hint: Each information is separated by coma(,).
//        Utilities.myLogWarning("Picture from camera: Width=%d, Height=%d, ByteCount=%d, RowBytes x Height=%d, Height/Width=%f",
//                bitmap.getWidth(),
//                bitmap.getHeight(),
//                bitmap.getByteCount(),
//                bitmap.getRowBytes() * bitmap.getHeight(),
//                (float) bitmap.getHeight() / bitmap.getWidth()
//        );

        return "";
    }

    /*Rotate bitmap image*/
    public Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /*Get path of the picture from external storage.*/
    public String getPicturePath(Context context, Uri ImageUri) {
        final String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String picturePath = null;
        Cursor cursor = context.getContentResolver().query(
                ImageUri, filePathColumn, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);

            Utilities.myLogWarning(picturePath);
        }
        cursor.close();

        return picturePath;
    }

    /*Convert picture to byte array*/
    public byte[] getPictureBytes(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

//        if (result == true) {
//            Utilities.myLogDebug("MyBitmap converted to bytes array successful.");
//        } else {
//            Utilities.myLogError("MyBitmap can not converted to bytes array.");
//        }

        return outputStream.toByteArray();
    }

    public boolean savePictureInCacheMemory(Uri imageUri) {

        //Get actual path of picture.
        String readingPicPath = getPicturePath(activity, imageUri);

        String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
        Utilities.myLogError("Reading pic path: %s", readingPicPath);
        Utilities.myLogError("Writing pic path: %s", writingPicPath);

        //Reading and writing all picture bytes.
        FileInputStream readingPic = null;
        FileOutputStream writingPic = null;
        byte[] readingBytes = null;

        try {
            readingPic = new FileInputStream(readingPicPath);
            writingPic = new FileOutputStream(writingPicPath);

            readingBytes = new byte[readingPic.available()];
//            readingBytes = new byte[256];
            Utilities.myLogError("size: %d", readingPic.available());

            int read;
            while ((read = readingPic.read(readingBytes)) != -1) {
                writingPic.write(readingBytes, 0, read);
                Utilities.myLogError("Bytes = %d, read = %d, size = %d", readingBytes.length, read, readingPic.available());
            }

            Utilities.myLogDebug("MyBitmap converted to bytes array successful.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.myLogError("Exception: %s", e.getMessage());
            return false;
        } finally {

            if (readingPic != null) {
                try {
                    readingPic.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Utilities.myLogError("Exception: %s", e.getMessage());
                }
            }

            if (writingPic != null) {
                try {
                    writingPic.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Utilities.myLogError("Exception: %s", e.getMessage());
                }
            }
        }

    }

    public boolean savePictureInCacheMemory(Bitmap bitmap) {

        byte[] picBytes = getPictureBytes(bitmap);

        FileOutputStream outputFile = null;
        try {
            String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
            outputFile = new FileOutputStream(writingPicPath);
            outputFile.write(picBytes);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputFile != null) {
                try {
                    outputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean savePictureInCacheMemory(byte[] picBytes) {

        FileOutputStream outputFile = null;

        try {
            String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
            outputFile = new FileOutputStream(writingPicPath);
            outputFile.write(picBytes);

            Utilities.myLogDebug("Cache Image Path: %s", writingPicPath);

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputFile != null) {
                try {
                    outputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public File loadCacheFilePath() {

        String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
        Utilities.myLogDebug("File Path: %s", writingPicPath);
        return new File(writingPicPath);

    }

    public Bitmap loadPictureFormCacheMemory() {

        String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
        FileInputStream readingCachedImg = null;
        byte[] bytes = null;

        try {
            readingCachedImg = new FileInputStream(writingPicPath);
            bytes = new byte[readingCachedImg.available()];
            readingCachedImg.read(bytes);
            readingCachedImg.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = getPictureFromBytes(bytes);
        return bitmap;

    }

    public byte[] loadPictureBytesFormCacheMemory() {

        String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
        FileInputStream readingCachedImg = null;
        byte[] bytes = null;

        try {
            readingCachedImg = new FileInputStream(writingPicPath);
            bytes = new byte[readingCachedImg.available()];
            readingCachedImg.read(bytes);
            readingCachedImg.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;

    }

    public void clearPictureCacheMemory() {
        Utilities.myLogError("Clearing picture cache memory");

        String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
        File file = new File(writingPicPath);
        file.delete();

    }

    public boolean isCacheMemoryEmpty() {

        String writingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;
        File file = new File(writingPicPath);

        if (file.exists()) {
            return false;
        } else {
            return true;
        }

    }

    public boolean saveCachePictureInExternalStorage(String path, String fileName) {

        //Get actual path of picture.
        String readingPicPath = activity.getCacheDir().getAbsolutePath() + "/" + CACHE_IMAGE_NAME;

        String writingPicPath = path + "/" + fileName + PICTURE_EXTENSION;
        Utilities.myLogError("Reading pic path: %s", readingPicPath);
        Utilities.myLogError("Writing pic path: %s", writingPicPath);

        //Reading and writing all picture bytes.
        FileInputStream readingPic = null;
        FileOutputStream writingPic = null;
        byte[] readingBytes = null;

        try {
            readingPic = new FileInputStream(readingPicPath);
            writingPic = new FileOutputStream(writingPicPath);

            readingBytes = new byte[readingPic.available()];

            int read;
            while ((read = readingPic.read(readingBytes)) != -1) {
                writingPic.write(readingBytes, 0, read);
            }

            Utilities.myLogDebug("MyBitmap converted to bytes array successful.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.myLogError("Exception: %s", e.getMessage());
            return false;
        } finally {

            if (readingPic != null) {
                try {
                    readingPic.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Utilities.myLogError("Exception: %s", e.getMessage());
                }
            }

            if (writingPic != null) {
                try {
                    writingPic.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Utilities.myLogError("Exception: %s", e.getMessage());
                }
            }
        }

    }

    public Bitmap loadBitmapFromExternalStorage(String fileNameAndPath) {

        FileInputStream inputStream = null;
        byte[] bytes = null;

        try {
            inputStream = new FileInputStream(fileNameAndPath);
            bytes = new byte[inputStream.available()];

            inputStream.read(bytes);
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bytes != null) {
                Bitmap bitmap = getPictureFromBytes(bytes);
                return bitmap;
            } else {
                return null;
            }
        }

    }

    public byte[] loadPictureBytesFromExternalStorage(String fileNameAndPath) {

        FileInputStream inputStream = null;
        byte[] bytes = null;

        try {
            inputStream = new FileInputStream(fileNameAndPath);
            bytes = new byte[inputStream.available()];

            inputStream.read(bytes);
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            return bytes;
        }

    }

    /*Convert Uri Image to Bytes array*/
    public byte[] getPictureBytes(Uri ImageUri) {

        String picturePath = getPicturePath(activity, ImageUri);
        FileInputStream pictureFile;
        byte[] pictureBytes = null;

        try {
            pictureFile = new FileInputStream(picturePath);
            pictureBytes = new byte[pictureFile.available()];
            pictureFile.read(pictureBytes);
            Utilities.myLogDebug("MyBitmap converted to bytes array successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pictureBytes;
    }

    /*Get path of the picture from external storage.*/
    public String GetPicturePath(Uri ImageUri) {
        final String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String picturePath = null;
        Cursor cursor = activity.getContentResolver().query(ImageUri, filePathColumn, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Utilities.myLogDebug("%s", picturePath);
        }
        cursor.close();

        return picturePath;
    }

    public Bitmap getPictureFromBytes(byte[] pictureBytes) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int requiredWidth) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap resizedBitmap;

        if (width > requiredWidth) {
            int requiredHeight = (int) (height / (float) (width / requiredWidth));
            resizedBitmap = Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, false);

            return resizedBitmap;
        } else {
            return bitmap;
        }

    }

    public byte[] resizeBitmap(byte[] picBytes, int requiredWidth) {

        Bitmap bitmap = getPictureFromBytes(picBytes);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap resizedBitmap;

        if (width > requiredWidth) {
            int requiredHeight = (int) (height / (float) (width / requiredWidth));
            resizedBitmap = Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, false);

            return getPictureBytes(resizedBitmap);
        } else {
            return getPictureBytes(bitmap);
        }

    }

    public Bitmap cropBitmap() {

        return null;
    }

    public byte[] resizeBitmapIfRequired(byte[] pictureBytes) {

        return null;
    }

}
