package com.example.praneethagangisetty.todominimal;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public ArrayList<DataItems> data;
    Context c;
    ImageView i;
    ColorGenerator clrg;
    View view;
    Drawable d;
    String group = "group_todo";
    private final String CHANNEL_ID = "notifications";
    private int notificationID = 001;

    public MyAdapter(Context con, ArrayList<DataItems> d) {
        c = con;
        data = d;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time, nodateandtime;

        public MyViewHolder(View item) {
            super(item);
            title = (TextView) item.findViewById(R.id.title);
            nodateandtime = (TextView) item.findViewById(R.id.nodateandtime);
            date = (TextView) item.findViewById(R.id.date);
            time = (TextView) item.findViewById(R.id.time);
        }
    }

    public ArrayList<DataItems> getData() {
        return data;
    }

    public void setData(ArrayList<DataItems> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String s;
        s = data.get(position).getDate();
        if (s.equalsIgnoreCase("")) {
            holder.title.setText("");
            holder.date.setText("");
            holder.nodateandtime.setText(data.get(position).getTitle());
            holder.time.setText("");
        } else {
            holder.title.setText(data.get(position).getTitle());
            holder.date.setText(data.get(position).getDate());
            holder.nodateandtime.setText("");
            holder.time.setText(data.get(position).getTime());
        }

        String title = data.get(position).getTitle();
        String firstchar = Character.toString(title.charAt(0)).toUpperCase();
        i = (ImageView) view.findViewById(R.id.image);
        clrg = ColorGenerator.MATERIAL;
        d = TextDrawable.builder().buildRound(firstchar, clrg.getRandomColor());
        i.setImageDrawable(d);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(c, AddToDo.class);
                it.putExtra("title", data.get(position).getTitle().toString());
                it.putExtra("date", data.get(position).getDate().toString());
                it.putExtra("time", data.get(position).getTime().toString());
                c.startActivity(it);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listlayout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
}