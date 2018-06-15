package com.seluhadu.shchat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seluhadu.shchat.models.User;

import junit.runner.Version;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    public static final int PICK_IMAGE_REQUEST = 101;
    public static final int TAKE_PICTURE_REQUEST = 102;
    private static final int PERMISSION_REQUEST = 103;

    private static final String DOMAIN_NAME = "gmail.com";
    private FirebaseAuth mFireBaseAuth;
    private ProgressDialog mProgressDialog;
    private FirebaseFirestore mFireBaseFireStore;
    private StorageReference mStorageReference;
    private EditText mEmail, mPassword, mConfirm, mUserName;
    private AppCompatButton mSignUp;
    private CircleImageView mCircleImageView;
    private Uri mProfileUri;
    private String userId;

    private String[] permissionsRequired = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mSignUp = findViewById(R.id.sign_up);
        mEmail = findViewById(R.id.email);
        mCircleImageView = findViewById(R.id.profile);
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImage = new Intent();
                pickImage.setAction(Intent.ACTION_GET_CONTENT);
                pickImage.setType("image/*");
                startActivityForResult(Intent.createChooser(pickImage, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        TextView mSignIn = findViewById(R.id.sign_in);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSignIn();
            }
        });
        mPassword = findViewById(R.id.password);
        mConfirm = findViewById(R.id.confirm);
        mProgressDialog = new ProgressDialog(this);
        mUserName = findViewById(R.id.name);
        mEmail.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);
        mConfirm.addTextChangedListener(textWatcher);
        mUserName.addTextChangedListener(textWatcher);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinInUser(mEmail, mPassword, mConfirm, mUserName);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SignUpActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(SignUpActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(SignUpActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(SignUpActivity.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, permissionsRequired[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, permissionsRequired[1]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, permissionsRequired[2]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, permissionsRequired[3])) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(true);
                    builder.setMessage("Read External Storage is necessary to upload picture! \n " +
                            "Internet is necessary to load and upload content! \n " +
                            "Write External Storage is necessary to download to your phone!");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SignUpActivity.this, permissionsRequired, PERMISSION_REQUEST);
                        }
                    });

                } else {
                    ActivityCompat.requestPermissions(SignUpActivity.this, permissionsRequired, PERMISSION_REQUEST);
                }
            } else {
                // Do some stuff

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFireBaseAuth.getCurrentUser() != null) {
            sendToMain();
        }
    }

    private void sinInUser(EditText mEmail, EditText mPassword, EditText mConfirm, final EditText mUserName) {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();
        String confirm = mConfirm.getText().toString();
        final String name = mUserName.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) /*&& mProfileUri != null*/) {
            if (isValidDomain(email)) {
                if (confirm.contains(password)) {
                    showProgress();
                    mFireBaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
//                                sendVerification();
                                userId = mFireBaseAuth.getCurrentUser().getUid();
                                String userEmail = mFireBaseAuth.getCurrentUser().getEmail();

                                final Map<String, String> userMap = new HashMap<>();
                                userMap.put("userName", name);
                                userMap.put("userEmail", email);
                                userMap.put("userId", userId);
                                userMap.put("userDecs", "");
                                userMap.put("userProfile", "");

                                User user = new User();
                                user.setUserName(name);
                                user.setUserId(userId);
                                user.setUserDesc("");
                                user.setUserEmail(userEmail);
                                user.setUserProfile("");
                                mFireBaseFireStore.collection("UsersModel").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mFireBaseFireStore.collection("UsersMap").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        uploadProfile();
                                                        mFireBaseAuth.signOut();
                                                        hideProgress();
                                                        sendToSignIn();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            } else {
                                showErrorMessage(task);
                                hideProgress();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Password Doesn't Much!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email is not valid!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mProfileUri == null) {
                Toast.makeText(this, "Please select profile image!", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Please fill all!", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendToSignIn() {
        Intent sendToSingIn = new Intent(SignUpActivity.this, SignInActivity.class);
        sendToSingIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sendToSingIn);
        finish();
    }

    private void showProgress() {
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showErrorMessage(@Nullable Task<AuthResult> task) {
        assert task != null;
        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
        Toast.makeText(SignUpActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
        Log.d(TAG, "showErrorMessage: " + errorMessage);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().trim().length() > 0) {
                mSignUp.setEnabled(true);
            } else {
                mSignUp.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void sendToMain() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendVerification() {
        final FirebaseUser currentUser = mFireBaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Verification email sent to " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isValidDomain(String email) {
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return domain.equals(DOMAIN_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            mProfileUri = data.getData();
            Glide.with(this).load(mProfileUri).into(mCircleImageView);
        }
    }

    private void uploadProfile() {
        if (mProfileUri != null) {
            final StorageReference profileReference = mStorageReference.child("ProfileImages").child(userId + "." + getExtension(mProfileUri));
            profileReference.putFile(mProfileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();
                            mFireBaseFireStore.collection("UsersMap").document(userId).update("userProfile", imageUri);
                            mFireBaseFireStore.collection("UsersModel").document(userId).update("userProfile", imageUri);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, "URL" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    if (mProgressDialog.isShowing()){
//                        mProgressDialog.setMessage("Uploading" + (int) progress + "%");
//                    }
                }
            });
        }
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getMimeTypeFromExtension(cr.getType(uri));
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {

                    }
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }
                    if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }
                    if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }
                    if (grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }
                }
        }
    }
}
