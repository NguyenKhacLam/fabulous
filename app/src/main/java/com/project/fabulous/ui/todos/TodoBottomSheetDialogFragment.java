package com.project.fabulous.ui.todos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.fabulous.R;

public class TodoBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText editText;
    private Button addButton, cancelBtn;

    public static TodoBottomSheetDialogFragment newInstance(){
        return new TodoBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_todo_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.edAddSubtask);
        addButton = view.findViewById(R.id.addSubTaskButton);
        cancelBtn = view.findViewById(R.id.cancelAddButton);

        addButton.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addSubTaskButton:
                String subTaskTitle = editText.getText().toString().trim();
                if (subTaskTitle.isEmpty()){
                    Toast.makeText(getContext(), "Field can not be null!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancelAddButton:
                dismiss();
                break;
        }
    }
}
