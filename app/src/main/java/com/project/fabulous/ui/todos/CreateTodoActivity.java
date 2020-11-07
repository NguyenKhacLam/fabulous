package com.project.fabulous.ui.todos;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.fabulous.R;
import com.project.fabulous.adapters.SubTaskAdapter;
import com.project.fabulous.models.SubTask;
import com.project.fabulous.models.Todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateTodoActivity extends AppCompatActivity implements View.OnClickListener, SubTaskAdapter.OnClickSubTaskListener, TodoBottomSheetDialogFragment.BottomClick {
    private Toolbar toolbar;
    private EditText edTaskName, edDescription;
    private ImageView setReminderBtn;
    private TextView reminder, addSubTask;
    private RecyclerView recyclerView;
    private SubTaskAdapter adapter;
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    private AlertDialog dialog;
    private Todo readyTodo;

    private TodoBottomSheetDialogFragment todoBottomSheetDialogFragment;

    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setUpToolbar();
        initViews();

        if (getIntent().getSerializableExtra("todo") != null) {
            readyTodo = (Todo) getIntent().getSerializableExtra("todo");
            updateOrViewTodo();
        }

        loadData();

    }

    private void updateOrViewTodo() {
        edTaskName.setText(readyTodo.getTitle());
        edDescription.setText(readyTodo.getDescription());
        reminder.setText(readyTodo.getReminder());
        subTasks = readyTodo.getSubTasks();
    }

    private void loadData() {
        adapter.setData(subTasks);
    }

    private void initViews() {
        edTaskName = findViewById(R.id.edTaskName);
        edDescription = findViewById(R.id.edTaskDescription);
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
                saveTodo();
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
                todoBottomSheetDialogFragment = TodoBottomSheetDialogFragment.newInstance();
                todoBottomSheetDialogFragment.setBottomClick(this);
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

    private void saveTodo(){
        if (edTaskName.getText().toString().equals("")){
            Toast.makeText(this, "Field can not be null!", Toast.LENGTH_SHORT).show();
            return;
        }

        Todo newTodo = new Todo();
        newTodo.setTitle(edTaskName.getText().toString().trim());
        newTodo.setReminder(reminder.getText().toString().trim());
        newTodo.setDescription(edDescription.getText().toString().trim());
        newTodo.setStatus(false);
        newTodo.setCreatedAt(new Date().toString());
        newTodo.setUserId(currentUser.getUid());
        newTodo.setSubTasks(subTasks);

        if (readyTodo == null){
            db.collection("todos").add(newTodo)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(CreateTodoActivity.this, "Todo created!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "onFailure: " + e.getMessage() );
                        }
                    });
        }else {
            db.collection("todos").document(readyTodo.getId())
                    .update(
                            "title", edTaskName.getText().toString().trim(),
                            "description", edDescription.getText().toString().trim(),
                            "reminder", reminder.getText().toString().trim(),
                            "subTasks", subTasks
                    )
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(CreateTodoActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }

    // Click to subtask
    @Override
    public void onClickSubTask(SubTask subTask) {
        subTask.setStatus(!subTask.getStatus());
    }

    @Override
    public void onLongClickSubTask(SubTask subTask, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to do this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        subTasks.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void OnBottomClick(String title) {
        SubTask subTask = new SubTask();
        subTask.setId((int) new Date().getTime());
        subTask.setStatus(false);
        subTask.setTitle(title);
        subTasks.add(subTask);

        todoBottomSheetDialogFragment.dismiss();
    }
}