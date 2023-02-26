package com.attend.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.attend.data.models.users.Admin;
import com.attend.data.models.users.Student;
import com.attend.data.models.users.Teacher;
import com.google.gson.Gson;

public class UserPreference {
    private static volatile UserPreference instance;
    private final SharedPreferences ref;

    private UserPreference(Context context) {
        ref = context.getSharedPreferences("SettingsRef", Context.MODE_PRIVATE);
    }

    public static UserPreference getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreference(context);
        }
        return instance;
    }

    //  Admin data
    public void saveUser(Admin data) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("AdminData", data != null ? gson.toJson(data) : null);
        editor.apply();
    }

    public Admin getAdmin() {
        String data = ref.getString("AdminData", "");
        if (data.equals(""))
            return null;
        return new Gson().fromJson(data, Admin.class);
    }


    //  Teacher data
    public void saveUser(Teacher data) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("TeacherData", data != null ? gson.toJson(data) : null);
        editor.apply();
    }

    public Teacher getTeacher() {
        String data = ref.getString("TeacherData", "");
        if (data.equals(""))
            return null;
        return new Gson().fromJson(data, Teacher.class);
    }


    //  Student data
    public void saveUser(Student data) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("StudentData", data != null ? gson.toJson(data) : null);
        editor.apply();
    }

    public Student getStudent() {
        String data = ref.getString("StudentData", "");
        if (data.equals(""))
            return null;
        return new Gson().fromJson(data, Student.class);
    }


/*    public boolean isLoggedIn() {
        return getUser() != null;
    }*/

    public void deleteUser(Context context) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = ref.edit();
        editor.remove("UserData");
        // context.startActivity(new Intent(context, LoginActivity.class));
    }

//    public boolean isAdmin() {
//        return getUser() != null && getUser().type == UserType.Admin;
//    }
//    public boolean isCustomer() {
//        return getUser() != null && getUser().type == UserType.CUSTOMER;
//    }
//    public boolean isStore() {
//        return getUser() != null && getUser().type == UserType.STORE;
//    }

    //  Firebase Token
    public void saveFirebaseToken(String token) {
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("FirebaseToken", token);
        editor.apply();
    }

    public String getFirebaseToken() {
        return ref.getString("FirebaseToken", "");
    }


    //  get auto notification id
    public int getAutoNotificationId() {
        int id = ref.getInt("AutoNotificationId", 0) + 1;
        SharedPreferences.Editor editor = ref.edit();
        editor.putInt("AutoNotificationId", id);
        editor.apply();
        return id;
    }

    public String checkUserState() {
        if (getAdmin() != null) return UserTypes.ADMIN;
        if (getTeacher() != null) return UserTypes.TEACHER;
        if (getStudent() != null) return UserTypes.STUDENT;
        return "";
    }

    public void logout() {
        ref.edit().remove("AdminData").apply();
        ref.edit().remove("TeacherData").apply();
        ref.edit().remove("StudentData").apply();
    }

    public void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
