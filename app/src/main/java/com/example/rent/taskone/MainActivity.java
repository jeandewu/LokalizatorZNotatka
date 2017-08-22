package com.example.rent.taskone;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.localizationText)
    TextView localizationText;

    @BindView(R.id.showSavedLocalization)
    RecyclerView recyclerView;

    @BindView(R.id.add_item_note)
    EditText note;



    private FusedLocationProviderClient mFusedLocationClient;
    private int LOCATION_REQUEST_CODE = 100;
    private Realm realm;
    public List<LocationItem> allNotes;
    int counter = 0;
    public Context context;
    public Location actualLocalization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Realm.init(this);
        context = getApplicationContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        realm = Realm.getDefaultInstance();
        counter = generateInitialCounterValue(realm);
    }

    private int generateInitialCounterValue(Realm realm) {
        Number maxNumber = realm.where(LocationItem.class).max("id");
        return maxNumber != null ? maxNumber.intValue() : 0;
    }

    @OnClick(R.id.getLocalizationButton)
    public void getLocalization() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "NIE PRZYZNANO UPOWAZNIENIA", Toast.LENGTH_LONG).show();
            return;
        } else {
            Log.d("PERMISSION", "===>  GRANTED");
        }
      //  note.setText("");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("WARTOSC LOKALIZACJI", location.toString());
                            actualLocalization = location;
                            localizationText.setText(location.getLatitude() + " " + location.getLongitude() + "\n");
                        } else {
                            Toast.makeText(context, "LOKALIZACJA JEST NULLEM !!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @OnClick(R.id.saveButton)
    public void saveLocalizationToRealm() {
        counter++;
        LocationItem locationItem = createLocationItem();
        createRealmItem(locationItem);
        Toast.makeText(context, "Dodano do Realm !!!", Toast.LENGTH_LONG).show();
        note.setText("");
      localizationText.setText("Localization - not downloaded");
    }

    private void createRealmItem(LocationItem locationItem) {
        realm.beginTransaction();
        realm.copyToRealm(locationItem);
        realm.commitTransaction();
    }

    @NonNull
    private LocationItem createLocationItem() {
        LocationItem locationItem = new LocationItem();
        locationItem.setId(counter);
        locationItem.setNote(actualLocalization.getLatitude() + " ");
        locationItem.setLatitude(actualLocalization.getLatitude());
        locationItem.setLonglitude(actualLocalization.getLongitude());
        locationItem.setNote(note.getText().toString());
        return locationItem;
    }

    @OnClick(R.id.showButton)
    public void showLocalizationFromRealm() {
        realm.beginTransaction();
        RealmResults<LocationItem> items = realm.where(LocationItem.class).findAll();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LocationItemAdapter(items));
        realm.commitTransaction();
        }

    }
