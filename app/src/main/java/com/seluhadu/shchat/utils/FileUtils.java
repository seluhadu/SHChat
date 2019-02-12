package com.seluhadu.shchat.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.util.Hashtable;

public class FileUtils {
    private FileUtils() {
    }

    public static Hashtable<String, Object> getFileInformation(final Context context, final Uri uri) {

        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorage(uri)) {
                final String documentId = DocumentsContract.getDocumentId(uri);
                final String[] split = documentId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    Hashtable<String, Object> value = new Hashtable<>();
                    value.put("path", Environment.getExternalStorageDirectory() + "/" + split[1]);
                    value.put("size", (int) new File((String) value.get("path")).length());
                    value.put("mime", "application/octet-stream");
                    return value;
                }
            } else if (isDownloadDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String documentId = DocumentsContract.getDocumentId(uri);
                final String[] split = documentId.split(":");
                final String type = split[1];

                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotos(uri)) {
                Hashtable<String, Object> value = getDataColumn(context, uri, null, null);
                Bitmap bitmap;

                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    File file = File.createTempFile("seluhadu", ".jpg");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new BufferedOutputStream(new FileOutputStream(file)));
                    value.put("path", file.getAbsolutePath());
                    value.put("size", (int) file.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value;
            } else {
                return getDataColumn(context, uri, null, null);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Hashtable<String, Object> value = new Hashtable<>();
            value.put("path", uri.getPath());
            value.put("size", new File((String) value.get("path")).length());
            return value;
        }
        return null;
    }

    private static Hashtable<String, Object> getDataColumn(Context context, Uri contentUri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column_date = MediaStore.MediaColumns.DATA;
        String column_mime = MediaStore.MediaColumns.MIME_TYPE;
        String column_size = MediaStore.MediaColumns.SIZE;

        String projection[] = {
                column_date,
                column_mime,
                column_size
        };

        try {

            try {

                cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
            } catch (IllegalArgumentException e) {
                column_mime = "mimetype";
                projection[1] = column_mime;
                cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArgs, null);
            }

            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(column_date);
                String path = cursor.getString(column_index);

                column_index = cursor.getColumnIndexOrThrow(column_mime);
                String mime = cursor.getString(column_index);

                column_index = cursor.getColumnIndexOrThrow(column_size);
                int size = cursor.getInt(column_index);
                Hashtable<String, Object> value = new Hashtable<>();
                if (path == null) path = "";
                if (mime == null) mime = "application/octet-stream";

                value.put("path", path);
                value.put("mime", mime);
                value.put("size", size);
                return value;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static boolean isExternalStorage(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotos(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}
