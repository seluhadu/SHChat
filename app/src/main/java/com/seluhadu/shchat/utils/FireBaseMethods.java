package com.seluhadu.shchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.seluhadu.shchat.models.Photo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FireBaseMethods {
    private Context mContext;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageReference;
    private byte[] mUplodeBytes;
    private Uri mSelectedUri;
    private Bitmap mSelecetedBitmap;

    public FireBaseMethods(Context context) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;
    }

    public void uploadNewPhoto(String imageType, String caption, final int count, String url, Bitmap bitmap) {

    }

    public void addNewImageToFireBase(String caption, String url) {
        Photo photo = new Photo();
        String newImageKey = firebaseFirestore.collection("UsersModel").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Posts").document().getId();
        photo.setImageId(newImageKey);
        photo.setDateCreated(getTimestamp());
        photo.setCaption(caption);
        photo.setImagePath(url);
        photo.setTags(""); // need to add tag
        photo.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        firebaseFirestore.collection("UsersModel")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Posts")
                .document(newImageKey)
                .set(photo);
    }

    public static String replaceWithDot(String username) {
        return username.replace(" ", ".");
    }

    public static String replaceWithSpace(String username) {
        return username.replace(".", " ");
    }

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone(""));
        return sdf.format(new Date());
    }

    public class BackgroudnImageReciser extends AsyncTask<Uri, Integer, byte[]> {
        Bitmap mBitmap;

        public BackgroudnImageReciser(Bitmap bitmap) {
            if (mBitmap != null) {
                this.mBitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUplodeBytes = bytes;
        }

        @Override
        protected byte[] doInBackground(Uri... uris) {
            if (mBitmap == null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uris[0]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] bytes = null;
            bytes = getBytesFromBitmap(mBitmap, 100);
            return bytes;
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}
