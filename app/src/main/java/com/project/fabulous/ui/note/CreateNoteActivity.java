package com.project.fabulous.ui.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.fabulous.R;
import com.project.fabulous.models.Note;
import com.project.fabulous.utils.LoadingDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class CreateNoteActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_IMAGE = 10;
    private final static int REQUEST_CODE_STORAGE = 11;
    private static final String TAG = "CreateNoteActivity";

    private ImageView back, save;
    private EditText inputNoteTittle, inputNoteSubTittle, inputNoteContent;
    private TextView textDatetime, textUrl;
    private LinearLayout layoutUrl;
    private AlertDialog dialog;
    private AlertDialog deleteDialog;
    private View viewSubtitleIndicator;
    private ImageView imageNote;

    private String selectedColor;
    private Note readyNote;

    private LoadingDialog loadingDialog;

    private Uri imageUri;
    String imgUrl = "";

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        selectedColor = "#ebebeb";

        loadingDialog = new LoadingDialog(this);

        findViewById(R.id.imgDeleteUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textUrl.setText(null);
                layoutUrl.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.imgDeleteImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNote.setImageURI(null);
                imageNote.setVisibility(View.GONE);
                imgUrl = "";
                findViewById(R.id.imgDeleteImage).setVisibility(View.GONE);
            }
        });

        initViews();
        initMiscellaneous();
    }

    private void initViews() {
        textUrl = findViewById(R.id.textUrl);
        layoutUrl = findViewById(R.id.layoutUrl);
        imageNote = findViewById(R.id.imageNote);
        inputNoteTittle = findViewById(R.id.edNoteTitle);
        inputNoteSubTittle = findViewById(R.id.edNoteSubTitle);
        inputNoteContent = findViewById(R.id.edNoteContent);
        textDatetime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        textDatetime.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

        back = findViewById(R.id.imageBack);
        save = findViewById(R.id.imageSave);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            readyNote = (Note) getIntent().getSerializableExtra("note");
            Log.d("TAG", "initViews: " + readyNote.getContent());
            setViewOrUpdateNote();
        }
    }

    private void saveNote() {
        if (inputNoteTittle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note title can't not be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNoteSubTittle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note subtitle can't not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (readyNote != null){
            updateNote();
        }else {
            loadingDialog.startLoadingDialog();
            uploadImage();
        }
    }

    private void uploadImage() {
        final Note note = new Note();
        note.setTitle(inputNoteTittle.getText().toString().trim());
        note.setSubtitle(inputNoteSubTittle.getText().toString().trim());
        note.setContent(inputNoteContent.getText().toString().trim());
        note.setDatetime(textDatetime.getText().toString());
        note.setUserId(currentUser.getUid());
        note.setColor(selectedColor);

        if (layoutUrl.getVisibility() == View.VISIBLE) {
            note.setWeblink(textUrl.getText().toString().trim());
        }

        if (imageNote.getVisibility() == View.VISIBLE){
            final StorageReference filePath = storageReference.child("note_images")
                    .child("my_image_" + Timestamp.now().getSeconds());

            filePath.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        note.setImageUrl(task.getResult().toString());
                                        db.collection("notes").add(note)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            loadingDialog.dismissDialog();
                                                            Toast.makeText(CreateNoteActivity.this, "Note created!", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("TAG", "onFailure: " + e.getMessage());
                                                        Toast.makeText(CreateNoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Fail", "onFailure: " + e.getMessage());
                            Toast.makeText(CreateNoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            db.collection("notes").add(note)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(CreateNoteActivity.this, "Note created!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: " + e.getMessage());
                            Toast.makeText(CreateNoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateNote(){
        db.collection("notes").document(readyNote.getId()).update(
                "title", inputNoteTittle.getText().toString().trim(),
                "subtitle", inputNoteSubTittle.getText().toString().trim(),
                "content", inputNoteContent.getText().toString().trim(),
                "color", selectedColor,
                "imageUrl", imgUrl,
                "weblink", textUrl.getText().toString().trim().equals("") ? null : textUrl.getText().toString().trim(),
                "datetime", textDatetime.getText().toString().trim()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreateNoteActivity.this, "Note updated!", Toast.LENGTH_SHORT).show();
                finish();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getMessage() );
            }
        });
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);


        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#ebebeb";
                imageColor5.setImageResource(R.drawable.ic_baseline_check_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor1.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#FFCD30";
                imageColor2.setImageResource(R.drawable.ic_baseline_check_24);
                imageColor1.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#009faf";
                imageColor3.setImageResource(R.drawable.ic_baseline_check_24);
                imageColor2.setImageResource(0);
                imageColor1.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#000000";
                imageColor4.setImageResource(R.drawable.ic_baseline_check_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor1.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor = "#c2211b";
                imageColor1.setImageResource(R.drawable.ic_baseline_check_24);
                imageColor2.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor3.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDialog();
            }
        });

        if (readyNote != null && readyNote.getColor() != null && !readyNote.getColor().trim().isEmpty()) {
            Log.d("TAG", "initMiscellaneous: " + readyNote.getColor());
            switch (readyNote.getColor()) {
                case "#ebebeb":
                    layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                    break;
                case "#FFCD30":
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#009faf":
                    layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#000000":
                    layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#c2211b":
                    layoutMiscellaneous.findViewById(R.id.viewColor1).performClick();
                    Log.d("TAG", "initMiscellaneous: 1 click");
                    break;
            }
        }

        if (readyNote != null){
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog();
                }
            });
        }

        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                selectImage();
            }
        });
    }

    private void setSubtitleIndicator() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedColor));
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK){
            if (data != null){
                imageUri = data.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    imageNote.setImageBitmap(bitmap);
                    imageNote.setVisibility(View.VISIBLE);

                    findViewById(R.id.imgDeleteImage).setVisibility(View.VISIBLE);

                    imgUrl = imageUri.getPath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void showDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.layout_add_url, null
                            , false);
            builder.setView(view);
            dialog = builder.create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText input = view.findViewById(R.id.inputUrl);
            input.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (input.getText().toString().trim().isEmpty()) {
                        Toast.makeText(CreateNoteActivity.this, "Field cannot be null!", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(input.getText().toString()).matches()) {
                        Toast.makeText(CreateNoteActivity.this, "Invalid Url!", Toast.LENGTH_SHORT).show();
                    } else {
                        textUrl.setText(input.getText().toString().trim());
                        layoutUrl.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    public void showDeleteDialog() {
        if (deleteDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_delete_note, null
                            , false);
            builder.setView(view);
            deleteDialog = builder.create();
            if (deleteDialog.getWindow() != null) {
                deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.tvDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("notes").document(readyNote.getId()).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CreateNoteActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });

            deleteDialog.show();
        }
    }

    public void setViewOrUpdateNote() {
        inputNoteTittle.setText(readyNote.getTitle());
        inputNoteContent.setText(readyNote.getContent());
        inputNoteSubTittle.setText(readyNote.getSubtitle());
        textUrl.setText(readyNote.getWeblink());
        textDatetime.setText(readyNote.getDatetime());
        if (readyNote.getWeblink() != null) {
            findViewById(R.id.imgDeleteUrl).setVisibility(View.VISIBLE);
            textUrl.setText(readyNote.getWeblink());
            layoutUrl.setVisibility(View.VISIBLE);
        }

        if (readyNote.getImageUrl() != null && !readyNote.getImageUrl().equals("")){
            findViewById(R.id.imgDeleteImage).setVisibility(View.VISIBLE);
            imgUrl = readyNote.getImageUrl();
            Glide.with(this).load(readyNote.getImageUrl()).into(imageNote);
            imageNote.setVisibility(View.VISIBLE);
        }
    }
}