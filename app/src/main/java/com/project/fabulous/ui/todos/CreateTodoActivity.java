package com.project.fabulous.ui.todos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.project.fabulous.R;
import com.project.fabulous.adapters.SubTaskAdapter;
import com.project.fabulous.models.SubTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateTodoActivity extends AppCompatActivity implements View.OnClickListener, SubTaskAdapter.OnClickSubTaskListener {
    private Toolbar toolbar;
    private EditText edTaskName;
    private ImageView setReminderBtn;
    private TextView reminder, addSubTask;
    private RecyclerView recyclerView;
    private SubTaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);


        setUpToolbar();
        initViews();
        loadData();
    }

    private void loadData() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        subTasks.add(new SubTask(1,"subtask 1", false));
        subTasks.add(new SubTask(2,"subtask 2", false));
        subTasks.add(new SubTask(3,"subtask 3", true));
        adapter.setData(subTasks);
    }

    private void initViews() {
        edTaskName = findViewById(R.id.edTaskName);
        setReminderBtn = findViewById(R.id.setReminder);
        reminder = findViewById(R.id.reminder);
        addSubTask = findViewById(R.id.addSubTask);
        recyclerView = findViewById(R.id.rcListSubTask);

        adapter = new SubTaskAdapter(getLayoutInflater());
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        setReminderBtn.setOnClickListener(this);
        addSubTask.setOnClickListener(this);
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.todoToolbar);
        toolbar.setTitle("To do");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_todo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.navCreateTodo:
                Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setReminder:
                showDateTimePickerDialog();
                break;
            case R.id.addSubTask:
                TodoBottomSheetDialogFragment todoBottomSheetDialogFragment = TodoBottomSheetDialogFragment.newInstance();
                todoBottomSheetDialogFragment.show(getSupportFragmentManager(), "todoBottomSheetDialogFragment");
                break;
        }
    }

    private void showDateTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String dateToRemind = "";

                        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String remindate = simpleDateFormat.format(calendar.getTime());
                        String localDate = simpleDateFormat.format(new Date());

                        if (remindate.split(" ")[0].equals(localDate.split(" ")[0])){
                            dateToRemind = "Today at " + remindate.split(" ")[1];
                        }else {
                            dateToRemind = remindate;
                        }

                        reminder.setText(dateToRemind);
                    }
                };

                new TimePickerDialog(CreateTodoActivity.this, timeSetListener, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(CreateTodoActivity.this, dateSetListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    // Click to subtask
    @Override
    public void onClickSubTask(SubTask subTask) {
        Log.d("TAG", "onClickSubTask: b " + subTask.getStatus());
        subTask.setStatus(!subTask.getStatus());
        Log.d("TAG", "onClickSubTask: a " + subTask.getStatus());
    }
}