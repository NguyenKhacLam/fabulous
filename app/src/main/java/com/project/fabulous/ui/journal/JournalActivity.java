package com.project.fabulous.ui.journal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.fabulous.R;
import com.project.fabulous.adapters.JournalAdapter;
import com.project.fabulous.models.Journal;

import java.util.ArrayList;

public class JournalActivity extends Fragment {
    private RecyclerView rc;
    private JournalAdapter adapter;

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
        rc.setAdapter(adapter);
    }

    private void initData() {
        ArrayList<Journal> journals = new ArrayList<>();
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        journals.add(new Journal("Nov 20 11 month", "Today", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English"));
        adapter.setData(journals);
    }

}