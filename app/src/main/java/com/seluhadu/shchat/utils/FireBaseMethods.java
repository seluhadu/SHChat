package com.seluhadu.shchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seluhadu.shchat.models.BaseMessage;
import com.seluhadu.shchat.models.FileMessage;
import com.seluhadu.shchat.models.UserMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.inflate;

public class FireBaseMethods {
    private static String url;
    private static FireBaseMethods fireBaseMethods = null;
    private Context mContext;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageReference;
    private byte[] mUplodeBytes;
    private Uri mSelectedUri;
    private Bitmap mSelecetedBitmap;

    public FireBaseMethods() {
    }

    public FireBaseMethods(Context context) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;
    }

    public FireBaseMethods getInstance() {
        if (fireBaseMethods == null) {
            fireBaseMethods = new FireBaseMethods(mContext);
        }
        return fireBaseMethods;
    }

    public void uploadNewPhoto(String imageType, String caption, final int count, String url, Bitmap bitmap) {

    }

//    public void addNewImageToFireBase(String caption, String url) {
//        Photo photo = new Photo();
//        String newImageKey = firebaseFirestore.collection("UsersModel").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Posts").document().getId();
//        photo.setImageId(newImageKey);
//        photo.setDatePosted(getTimestamp());
//        photo.setCaption(caption);
//        photo.setImagePath(url);
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
    public static void deleteField(@NonNull String userId, @NonNull String documentId, @NonNull String field){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Messages").document(userId).collection("Chats").document(documentId);
        documentReference.update(field, FieldValue.delete()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
    public static void deleteUserMessage(@NonNull String receiverId, @NonNull final UserMessage message, final OnUserMessageDeleteHandler deleteHandler) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Messages").document(sortedUsersId(FirebaseAuth.getInstance().getCurrentUser().getUid(), receiverId))
                .collection("Chats")
                .document(String.valueOf(message.getMessageId()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteHandler.onMessageDelete(message, null);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                deleteHandler.onMessageDelete(null, e.getMessage());
            }
        });
    }

    public static UserMessage sendUserMessage(String userId, String receiverId, String message, long newDay, FireBaseMethods.SendUserMessageHandler handler) {
        return sendUserMessage(userId, receiverId, message, System.currentTimeMillis(), 0, newDay, handler);
    }

    private static UserMessage sendUserMessage(String userId, String receiverId, String message, long createdAt, long updatedAt, long newDay, final FireBaseMethods.SendUserMessageHandler handler) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final UserMessage userMessage = new UserMessage(UserMessage.build(userId, receiverId, message, createdAt, updatedAt, System.currentTimeMillis(), newDay));
        firebaseFirestore.collection("Messages")
                .document(sortedUsersId(userId, receiverId)).collection("Chats").document(String.valueOf(userMessage.getMessageId())).set(userMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (handler != null) {
                    handler.onSent(userMessage, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onSent(null, e.getMessage());
            }
        });
        return userMessage;
    }

    public static FileMessage sendFileMessage(final File file, String name, String type, Integer size, final String usersId, final FireBaseMethods.SendFileMessageHandler handler) {
        if (file == null) {
            if (handler != null) {
                handler.onSent(null, "fill is null");
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
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final String msgFileId = firebaseFirestore.collection("Messages").document().getId();
        storageReference.putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                url = storageReference.getDownloadUrl().toString();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                if (handler != null)
                    handler.onProgress(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (handler != null)
                    handler.onSent(null, "Put File Error: " + e.getMessage());
                url = "";
            }
        });
        final FileMessage fileMessage = new FileMessage(FileMessage.build(FirebaseAuth.getInstance().getCurrentUser().getUid(), "", url, name, size, type, "", null, System.currentTimeMillis(), 0l, System.currentTimeMillis()));
        firebaseFirestore.collection("Messages").document(usersId).collection("Chat").document().set(msgFileId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (handler != null)
                    handler.onSent(fileMessage, null);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (handler != null)
                    handler.onSent(null, "Send file info Error: " + e.getMessage());
            }
        });
        return fileMessage;
    }

    public static void getMessagesByTimestamp(String usersId, int limit, final FireBaseMethods.GetMessagesHandler handler) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Messages").document(usersId).collection("Chats");
        Query query = collectionReference.limit(limit).orderBy("createdAt", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
        void onSent(FileMessage message, String e);

        void onProgress(long totalBytesSent, long totalBytesToSend);
    }

    public interface GetMessagesHandler {
        void onResult(List<BaseMessage> messageList, Exception e);
    }
    public interface OnUserMessageDeleteHandler {
        void onMessageDelete(UserMessage message, String e);
    }
}
