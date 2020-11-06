package com.project.fabulous.ui.focusMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.fabulous.R;
import com.project.fabulous.api.ApiBuilder;
import com.project.fabulous.models.FocusSession;
import com.project.fabulous.utils.DateTimeFormater;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FocusModeActivity extends Fragment implements View.OnClickListener {

    private static final String CHANNEL_ID = "focusing mode";
    private static final String TAG = "FocusModeActivity";
    private long timeCountInMillis = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private MediaPlayer player;

    private Toolbar toolbar;
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBar;
    private EditText editTextMinute;
    private TextView textViewTime;
    private Button btnStart, btnStop;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_focus_mode, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        createNotificationChannel();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_focus_mode);
//
//        initViews();
//        createNotificationChannel();
//    }

    private void initViews() {
//        setUpActionBar();
        progressBar = getActivity().findViewById(R.id.progressBarCircle);
        btnStart = getActivity().findViewById(R.id.btnStartFocus);
        btnStop = getActivity().findViewById(R.id.btnStopFocus);
        textViewTime = getActivity().findViewById(R.id.tvTime);
        editTextMinute = getActivity().findViewById(R.id.editTextMinute);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

//    private void setUpActionBar() {
//        toolbar = getActivity().findViewById(R.id.focusToolbar);
//        toolbar.setTitle("Focus mode");
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//    }

    private void setUpNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle("Finished...!")
                .setContentText("Your focusing time have finished!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartFocus:
                start();
                break;
            case R.id.btnStopFocus:
                stop();
                break;
        }
    }

    private void start() {
        if (timerStatus == TimerStatus.STOPPED) {
            // call to initialize the timer values
            setTimerValue();
            // call to initialize the progress bar values
            setProgressBarValue();
            btnStop.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.GONE);
            editTextMinute.setEnabled(false);

            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            startCountdownTimer();
        } else {
            btnStop.setVisibility(View.GONE);
            btnStart.setVisibility(View.VISIBLE);
            editTextMinute.setEnabled(true);

            // changing the timer status to started
            timerStatus = TimerStatus.STOPPED;
            stopCountdownTimer();
        }
    }

    private void stop() {
        stopCountdownTimer();
    }

    /**
     * method to initialize the values for count down timer
     */

    private void setTimerValue() {
        int time = 0;
        if (!editTextMinute.getText().toString().trim().isEmpty()) {
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
        }

        timeCountInMillis = time * 60 * 1000;
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValue() {
        progressBar.setMax((int) timeCountInMillis / 1000);
        progressBar.setProgress((int) timeCountInMillis / 1000);
    }

    /**
     * method to start count down timer
     */
    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(DateTimeFormater.hmsTimeFormatter(millisUntilFinished));
                progressBar.setProgress((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                textViewTime.setText(DateTimeFormater.hmsTimeFormatter(timeCountInMillis));
                setProgressBarValue();
                btnStop.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                editTextMinute.setEnabled(true);

                timerStatus = TimerStatus.STOPPED;
                setUpNotification();
                playSound();
                createFocusSessionInDb();
            }
        }.start();
        countDownTimer.start();
    }

    /**
     * method to add data to db when it's done
     */
    private void createFocusSessionInDb() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        int duration = Integer.parseInt(editTextMinute.getText().toString());
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
        String strDate = dateFormat.format(date);

        FocusSession focusSession = new FocusSession(userId, duration);

        ApiBuilder.getInstance().createFocusSession(focusSession).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * method to stop count down timer
     */
    private void stopCountdownTimer() {
        countDownTimer.cancel();
        progressBar.setProgress(progressBar.getMax());
        timerStatus = TimerStatus.STOPPED;
        textViewTime.setText(DateTimeFormater.hmsTimeFormatter(25 * 60 * 1000));
        btnStop.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
        editTextMinute.setEnabled(true);
    }

    /**
     * method to play sound when finish
     */
    private void playSound() {
        if (player == null) {
            player = MediaPlayer.create(getActivity(), R.raw.alarm);
        }
        player.start();
    }
}