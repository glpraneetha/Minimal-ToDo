package com.example.praneethagangisetty.todominimal;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DataItems extends RealmObject {
    String title, date, time;
    @PrimaryKey
    String id = UUID.randomUUID().toString();

    public DataItems() {

    }

    public DataItems(String a, String b, String c) {
        title = a;
        date = b;
        time = c;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
