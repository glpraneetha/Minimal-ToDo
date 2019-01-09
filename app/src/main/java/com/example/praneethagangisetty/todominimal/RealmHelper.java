package com.example.praneethagangisetty.todominimal;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    Realm realm;

    public RealmHelper(Realm r) {
        realm = r;
    }

    public void save(final DataItems data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DataItems d = realm.copyToRealm(data);
            }
        });
    }

    public ArrayList<DataItems> retrieve() {
        ArrayList<DataItems> dataItems = new ArrayList<DataItems>();
        RealmResults<DataItems> data = realm.where(DataItems.class).findAll();
        for (DataItems d : data) {
            //dataItems.add(new DataItems(d.getTitle(),"28/04/2018","3:15pm"));
            dataItems.add(new DataItems(d.getTitle(), d.getDate(), d.getTime()));
        }
        return dataItems;
    }
}
