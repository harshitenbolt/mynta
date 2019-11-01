package com.canvascoders.opaper.database;

import android.app.Activity;
import android.app.Application;
import androidx.fragment.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }


    public void clearAll() {
        realm.beginTransaction();
        realm.delete(TrackerModel.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<TrackerModel> getTracker() {
        return realm.where(TrackerModel.class).findAll();
    }


    public void save(final TrackerModel trackerModel) {
        realm.beginTransaction();
        realm.copyToRealm(trackerModel);
        realm.commitTransaction();
    }

    public void delete(final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TrackerModel> result = realm.where(TrackerModel.class).equalTo("isSync", true).findAll();
                result.deleteAllFromRealm();

            }
        });
    }


    public void update(int id) {
        RealmResults<TrackerModel> results = realm.where(TrackerModel.class).equalTo("id", id).findAll();

        realm.beginTransaction();
        results.get(0).setAgentID("test demo");


        realm.commitTransaction();
    }
}