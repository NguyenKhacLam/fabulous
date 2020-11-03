package com.project.fabulous.api;

import com.project.fabulous.models.DailyHabit;
import com.project.fabulous.models.FocusSession;
import com.project.fabulous.models.Todo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {
    @GET("todos")
    Call<List<Todo>> getTodayTodos();

    @GET("habits/user/{userId}")
    Call<List<DailyHabit>> getHabitByUser(@Path("userId") String userId);

    // Focus mode
    @POST("focus")
    Call<ResponseBody> createFocusSession(@Body FocusSession focusSession);
}
