package com.project.fabulous.ui.note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.fabulous.R;
import com.project.fabulous.adapters.NoteAdapter;
import com.project.fabulous.models.Note;

import java.util.ArrayList;

public class NoteActivity extends Fragment implements NoteAdapter.OnClickNoteListener, TextWatcher {
    public static final int REQUEST_CODE_ADD_NOTE = 5;
    public static final int REQUEST_CODE_UPDATE_NOTE = 6;

    private Toolbar toolbar;
    private EditText edSearchNote;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_note, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        initViews();
        loadData();

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_note);
//
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        setUpToolbar();
//        initViews();
//        loadData();
//    }

    private void loadData() {
        db.collection("notes")
                .whereEqualTo("userId", currentUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Note> notes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            Note note = new Note();
                            note.setId(document.getId());
                            note.setTitle(document.get("title").toString());
                            note.setSubtitle(document.get("subtitle").toString());
                            note.setDatetime(document.get("datetime").toString());
                            note.setUserId(document.get("userId").toString());
                            note.setContent(document.get("content").toString());
                            note.setImageUrl(document.get("imageUrl") != null ? document.get("imageUrl").toString() : "");
                            note.setColor(document.get("color") != null ? document.get("color").toString() : "#ebebeb");
                            note.setWeblink(document.get("weblink") != null ? document.get("weblink").toString() : null);

                            notes.add(note);
                        }
                        noteAdapter.setData(notes);
                    }
                });
    }

//    private void setUpToolbar() {
//        toolbar = findViewById(R.id.noteToolbar);
//        toolbar.setTitle("My note");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//    }

    private void initViews() {
        edSearchNote = getActivity().findViewById(R.id.edSearchNote);
        recyclerView = getActivity().findViewById(R.id.rcListNote);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteAdapter = new NoteAdapter(getLayoutInflater());
        noteAdapter.setListener(this);
        recyclerView.setAdapter(noteAdapter);

        edSearchNote.addTextChangedListener(this);

        fab = getActivity().findViewById(R.id.fabCreateNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateNoteActivity.class));
            }
        });

    }

    @Override
    public void onClickNote(Note note, int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivity(intent);
    }

    @Override
    public void onClickLongNote(Note note) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        noteAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}