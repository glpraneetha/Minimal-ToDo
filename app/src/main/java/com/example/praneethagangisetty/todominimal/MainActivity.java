package com.example.praneethagangisetty.todominimal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static RecyclerView.Adapter adapter;
    public static ArrayList<DataItems> list;
    private RecyclerView.LayoutManager layoutManager;
    private Realm realm;
    private RealmHelper helper;
    private TextView noTodosTV;
    RecyclerView rv;
    DataItems d;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFake();
        setDataIn();
        swipeDelete();
    }

    private void swipeDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    d = list.get(position);
                    String title = d.getTitle();
                    String date = d.getDate();
                    String time = d.getTime();
                    String id = d.getId();
                    final RealmResults<DataItems> data = realm.where(DataItems.class).findAll();
                    d = data.where().equalTo("title", title).equalTo("date", date).equalTo("time", time).findFirst();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            d.deleteFromRealm();
                        }
                    });
                    list.remove(position);
                    noTodosCheck(list);
                    adapter.notifyItemRemoved(position);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = helper.retrieve();
        noTodosCheck(list);
        adapter = new MyAdapter(MainActivity.this, list);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
    }

    private void noTodosCheck(ArrayList<DataItems> list) {
        if(list.isEmpty()){
            rv.setVisibility(View.GONE);
            noTodosTV.setVisibility(View.VISIBLE);
        }
        else {
            rv.setVisibility(View.VISIBLE);
            noTodosTV.setVisibility(View.GONE);
        }
    }

    void initViews() {
        layoutManager = new LinearLayoutManager(this);
        rv = findViewById(R.id.recycler);
        noTodosTV = (TextView) findViewById(R.id.notodos);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        noTodosCheck(list);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    void initFake() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        helper = new RealmHelper(realm);
        list=new ArrayList<>();
        initViews();
    }

    void setDataIn() {
        list = helper.retrieve();
        adapter = new MyAdapter(MainActivity.this, list);
        rv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent it = new Intent(MainActivity.this, AddToDo.class);
                startActivity(it);
                break;
        }
    }
}
