package com.seluhadu.shchat.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.models.Photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DialogNewPost extends DialogFragment {
    public static final int PICK_IMAGE_REQUEST = 101;
    private FirebaseFirestore mFireBaseFireStore;
    private FirebaseAuth mFireBaseAuth;
    private StorageReference mStorageReference;
    private ImageView mImage;
    private AppCompatButton mPost;
    private EditText mCaption;
    private ImageButton mPickImage;
    private Uri mImageUri;
    private List<String> listFileName;

    public DialogNewPost() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        listFileName = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_post, container, false);
        mPickImage = rootView.findViewById(R.id.pick_image);
        mPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImage = new Intent();
                pickImage.setAction(Intent.ACTION_GET_CONTENT);
                pickImage.setType("image/*");
                startActivityForResult(Intent.createChooser(pickImage, "Select Image(S)"), PICK_IMAGE_REQUEST);
            }
        });
        mPost = rootView.findViewById(R.id.post);
        mCaption = rootView.findViewById(R.id.caption);
        mImage = rootView.findViewById(R.id.image);
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPost();
            }
        });

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void newPost() {
        final String caption = mCaption.getText().toString();
        if (!TextUtils.isEmpty(caption) && mImageUri != null) {
            final String postId = mFireBaseFireStore.collection(getString(R.string.posts)).document().getId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd,yyyy, HH:mm:ssa", Locale.getDefault());
            final String date = simpleDateFormat.format(Calendar.getInstance().getTime());
            final StorageReference imageFile = mStorageReference.child(getString(R.string.photos)).child(getString(R.string.posts)).child(postId);
            imageFile.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Photo photo = new Photo();
                            photo.setCaption(caption);
                            photo.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            photo.setDatePosted(date);
                            photo.setImageId(postId);
                            photo.setImagePath(uri.toString());
                            mFireBaseFireStore.collection(getString(R.string.userPosts))//UserPosts for each person
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .document(postId).set(photo);
                            mFireBaseFireStore.collection(getString(R.string.posts))//Posts all
                                    .document(postId).set(photo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Uploaded!", Toast.LENGTH_SHORT).show();
                                            getDialog().dismiss();
                                        }
                                    });

                        }
                    });
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                mImageUri = data.getData();
                Glide.with(Objects.requireNonNull(getActivity())).load(mImageUri).into(mImage);

            } else if (data != null && data.getClipData() != null) {
                int totalCount = data.getClipData().getItemCount();
                for (int i = 0; i < totalCount; i++) {
                    mImageUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getName(mImageUri);
                    listFileName.add(fileName);
                }
            }
        }
    }

    private String getName(Uri uri) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }

        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf("/");
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
