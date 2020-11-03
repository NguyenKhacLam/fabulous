package com.project.fabulous.ui.journal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.fabulous.R;
import com.project.fabulous.adapters.JournalAdapter;
import com.project.fabulous.models.Journal;
import com.project.fabulous.ui.editor.Editor;

import java.util.ArrayList;

public class JournalActivity extends Fragment {
    private RecyclerView rc;
    private JournalAdapter adapter;
    private FloatingActionButton actionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        rc = getActivity().findViewById(R.id.rcJournal);
        adapter = new JournalAdapter(getLayoutInflater());
        actionButton = getActivity().findViewById(R.id.faAddPost);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Editor.class);
                startActivity(intent);
            }
        });

        rc.setAdapter(adapter);
    }

    private void initData() {
        ArrayList<Journal> journals = new ArrayList<>();
        journals.add(new Journal("Nov 20 11 month", "Today",getString(R.string.lorem_ipsum2) ));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        adapter.setData(journals);
    }

}