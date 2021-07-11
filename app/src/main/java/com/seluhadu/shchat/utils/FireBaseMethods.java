package com.seluhadu.shchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seluhadu.shchat.models.BaseMessage;
import com.seluhadu.shchat.models.FileMessage;
import com.seluhadu.shchat.models.User;
import com.seluhadu.shchat.models.UserMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FireBaseMethods {
    private static final String TAG = "FireBaseMethods";
    private Context mContext;

    private byte[] mUplodeBytes;
    private Uri mSelectedUri;
    private Bitmap mSelecetedBitmap;

    public FireBaseMethods() {
    }

    public void uploadNewPhoto(String imageType, String caption, final int count, String url, Bitmap bitmap) {

    }

//    public void addNewImageToFireBase(String caption, String url) {
//        Photo photo = new Photo();
//        String newImageKey = firebaseFirestore.collection("UsersModel").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Posts").document().getId();
//        photo.setImageId(newImageKey);
//        photo.setDatePosted(getTimestamp());
//        photo.setCaption(caption);
//        photo.setImageUrl(url);
//        photo.setTags(""); // need to add tag
//        photo.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        firebaseFirestore.collection("UsersModel")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .collection("Posts")
//                .document(newImageKey)
//                .set(photo);
//    }

    public static String replaceWithDot(String username) {
        return username.replace(" ", ".");
    }

    public static String replaceWithSpace(String username) {
        return username.replace(".", " ");
    }

//    public class BackgroudnImageReciser extends AsyncTask<Uri, Integer, byte[]> {
//        Bitmap mBitmap;
//
//        public BackgroudnImageReciser(Bitmap bitmap) {
//            if (mBitmap != null) {
//                this.mBitmap = bitmap;
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected void onPostExecute(byte[] bytes) {
//            super.onPostExecute(bytes);
//            mUplodeBytes = bytes;
//        }
//
//        @Override
//        protected byte[] doInBackground(Uri... uris) {
//            if (mBitmap == null) {
//                try {
//                    mBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uris[0]);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            byte[] bytes = null;
//            bytes = getBytesFromBitmap(mBitmap, 100);
//            return bytes;
//        }
//    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    public static void deleteField(@NonNull String userId, @NonNull String documentId, @NonNull String field) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Messages").document(userId).collection("Chats").document(documentId);
        documentReference.update(field, FieldValue.delete()).addOnCompleteListener(task -> {

        });
    }

    public static void deleteUserMessage(@NonNull String receiverId, @NonNull final UserMessage message, final OnUserMessageDeleteHandler deleteHandler) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Messages").document(sortedUsersId(FirebaseAuth.getInstance().getCurrentUser().getUid(), receiverId))
                .collection("Chats")
                .document(String.valueOf(message.getMessageId()))
                .delete()
                .addOnSuccessListener(aVoid -> deleteHandler.onMessageDelete(message, null)).addOnFailureListener(e -> deleteHandler.onMessageDelete(null, e.getMessage()));
    }

    public static UserMessage sendUserMessage(String userId, String receiverId, String message, long newDay, FireBaseMethods.SendUserMessageHandler handler) {
        return sendUserMessage(userId, receiverId, message, System.currentTimeMillis(), 0, newDay, handler);
    }

    public static User getUser(final String userId) {
        final User user = new User();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference dr = firebaseFirestore.collection("Users").document(userId);
        dr.get().addOnSuccessListener(documentSnapshot -> {
            user.setUserDisplayName((String) documentSnapshot.get("userDisplayName"));
            user.setUserProfile((String) documentSnapshot.get("userProfile"));
            user.setUserName((String) documentSnapshot.get("userName"));
            user.setUserId((String) documentSnapshot.get("userId"));
        }).addOnFailureListener(e -> {
        });
        return user;
    }

    private static UserMessage sendUserMessage(String userId, String receiverId, String message, long createdAt, long updatedAt, long newDay, final FireBaseMethods.SendUserMessageHandler handler) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final UserMessage userMessage = new UserMessage(UserMessage.build(userId, receiverId, message, createdAt, updatedAt, System.currentTimeMillis(), newDay));
        firebaseFirestore.collection("Messages")
                .document(sortedUsersId(userId, receiverId)).collection("Chats").document(String.valueOf(userMessage.getMessageId())).set(userMessage).addOnSuccessListener(aVoid -> {
            if (handler != null) {
                handler.onSent(userMessage, null);
            }
        }).addOnFailureListener(e -> handler.onSent(null, e.getMessage()));
        return userMessage;
    }

    public static FileMessage sendFileMessage(final File file, String name, String type, Integer size, final String senderId, String receiverId, final FireBaseMethods.SendFileMessageHandler handler) {
        if (file == null) {
            if (handler != null) {
                handler.onMessageSent(null, "fill is null");
                return null;
            }
        } else {
            if (name == null || name.length() == 0) {
                name = file.getName();
            }
            if (type == null || type.length() == 0) {
                type = "";
            }
            if (size == null) {
                size = (int) file.length();
            }
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("Chats/Pictures/");
        final FileMessage fileMessage = new FileMessage(FileMessage.build(FirebaseAuth.getInstance().getCurrentUser().getUid(), receiverId, "  ", name, size, type, "", null, System.currentTimeMillis(), 0L, System.currentTimeMillis(), System.currentTimeMillis()));
        UploadTask uploadTask = storageReference.putFile(Uri.fromFile(file));
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return storageReference.getDownloadUrl();
        }).addOnSuccessListener(uri -> {
            if (handler!=null){
                handler.onFileSent();
            }
            String url = String.valueOf(uri);
            firebaseFirestore.collection("Messages").document(sortedUsersId(senderId, receiverId)).collection("Chats").document(String.valueOf(fileMessage.getMessageId())).update("url", url);
        });
        uploadTask.addOnProgressListener(taskSnapshot -> {
            if (handler != null) {
                handler.onProgress(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
            }
        }).addOnPausedListener(taskSnapshot -> {

        }).addOnCanceledListener(() -> {

        });
//        storageReference.child(sortedUsersId(senderId, receiverId)).child(file.getName()).putFile(Uri.fromFile(file)).addOnSuccessListener(taskSnapshot -> {
//            String msgId = String.valueOf(fileMessage.getMessageId());
//            Log.d(TAG, "msgId" + msgId);
//        }).addOnProgressListener(taskSnapshot -> {
//            if (handler != null)
//                handler.onProgress(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
//        }).addOnFailureListener(e -> {
//            if (handler != null)
//                handler.onMessageSent(null, "Put File Error: " + e.getMessage());
//        });
//        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
//            String url = uri.toString();
//            String isNull = url.isEmpty() ? "null" : "url" + url;
//            Log.d(TAG, "msgUrl " + isNull);
//            firebaseFirestore.collection("Messages").document(sortedUsersId(senderId, receiverId)).collection("Chats").document(String.valueOf(fileMessage.getMessageId())).update("url", url);
//        });
        firebaseFirestore.collection("Messages").document(sortedUsersId(senderId, receiverId)).collection("Chats").document(String.valueOf(fileMessage.getMessageId())).set(fileMessage).addOnSuccessListener(aVoid -> {
            if (handler != null)
                handler.onMessageSent(fileMessage, null);
        }).addOnFailureListener(e -> {
            if (handler != null)
                handler.onMessageSent(null, "Send file info Error: " + e.getMessage());
        });
        return fileMessage;
    }

    public static void getMessagesByTimestamp(String usersId, int limit, final FireBaseMethods.GetMessagesHandler handler) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Messages").document(usersId).collection("Chats");
        Query query = collectionReference.limit(limit).orderBy("createdAt", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(task -> {
            List<BaseMessage> messages = new ArrayList<>();
            if (task.isSuccessful()) {
                for (DocumentChange documentChange : Objects.requireNonNull(task.getResult()).getDocumentChanges()) {
                    BaseMessage baseMessage = BaseMessage.getMessages(documentChange);
                    if (baseMessage != null) {
                        messages.add(baseMessage);
                    }
                }
                if (handler != null) {
                    handler.onResult(messages, null);
                }
            }
        });
    }


    public static String sortedUsersId(String userOne, String userTwo) {
        int compare = userOne.compareToIgnoreCase(userTwo);
        if (compare > 0) {
            return userOne + userTwo;
        } else {
            return userTwo + userOne;
        }
    }

    public interface SendUserMessageHandler {
        void onSent(UserMessage message, String e);
    }

    public interface SendFileMessageHandler {
        void onMessageSent(FileMessage message, String e);

        void onProgress(long totalBytesSent, long totalBytesToSend);

        boolean  onFileSent();
    }

    public interface GetMessagesHandler {
        void onResult(List<BaseMessage> messageList, Exception e);
    }

    public interface OnUserMessageDeleteHandler {
        void onMessageDelete(UserMessage message, String e);
    }
}
