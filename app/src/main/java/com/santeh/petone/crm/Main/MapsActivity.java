package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.santeh.petone.crm.DBase.DB_Helper_AquaCRM;
import com.santeh.petone.crm.DBase.DB_Query_AquaCRM;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.FusedLocation;
import com.santeh.petone.crm.Utils.Helper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocation fusedLocation;
    Activity activity;
    Context context;

    ImageButton btn_AddMarker, btn_closeAddMarker;

    CircleOptions circleOptions_addLocation;
    Circle mapcircle;

    DB_Helper_AquaCRM dbHelper;
    DB_Query_AquaCRM db;
    public static int requestCODE_addMarker = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        activity = this;
        context = MapsActivity.this;
        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.buildGoogleApiClient(context);
        fusedLocation.connectToApiClient();

        dbHelper = new DB_Helper_AquaCRM(this);
        db = new DB_Query_AquaCRM(this);
        db.open();

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        mMap = googleMap;

        btn_AddMarker = (ImageButton) findViewById(R.id.btnAddMarker);
        btn_closeAddMarker = (ImageButton) findViewById(R.id.btnCloseAddMarker);
        btn_closeAddMarker.setVisibility(View.GONE);

        final CameraUpdate zoom = CameraUpdateFactory.zoomTo(6);
        fusedLocation.connectToApiClient();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    LatLng latLng = fusedLocation.getLastKnowLocation();

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(zoom);
//                    Helper.common.toastShort(activity, Helper.variables.getGlobalVar_currentUserName(activity));

                } catch (Exception e) {
                    LatLng center = new LatLng(12.832288, 122.524313);
                    Helper.map.moveCameraAnimate(googleMap, center, 6);
                }
            }
        }, 200);


        btn_closeAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closerAddingMarker();
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String[] splitted = marker.getSnippet().split("#*#");
                String id = splitted[0];

//                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
//                intent.putExtra("id", id);
//                startActivity(intent);

            }
        });


        btn_AddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.map.isLocationEnabled(context)){
                    fusedLocation.disconnectFromApiClient();
                    fusedLocation.connectToApiClient();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final LatLng center = fusedLocation.getLastKnowLocation();
                            Helper.map.moveCameraAnimate(googleMap, center, 19);

                            final Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (mapcircle == null || !mapcircle.isVisible()){
                                        circleOptions_addLocation = Helper.map.addCircle(activity, center, 1, R.color.skyblue_20,
                                                R.color.skyblue_20, 1000);
                                        mapcircle = googleMap.addCircle(circleOptions_addLocation);
                                    }
                                    btn_closeAddMarker.setVisibility(View.VISIBLE);
                                    Helper.common.dialogThemedOkOnly(activity, "Add Marker", "Long press any location inside the blue circle to add a marker.", "OK", R.color.skyblue_500);

                                    if (btn_AddMarker.isEnabled()) {
                                        btn_AddMarker.setEnabled(false);
                                    }

                                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                        @Override
                                        public void onMapLongClick(LatLng latLng) {

                                            Intent intent = new Intent(activity, Activity_Add_ClientInfo.class);
                                            intent.putExtra("userid", Helper.variables.getGlobalVar_currentUserID(activity)+"");
                                            intent.putExtra("lat", latLng.latitude);
                                            intent.putExtra("long", latLng.longitude);
                                            closerAddingMarker();
                                            startActivityForResult(intent, requestCODE_addMarker);


                                        }
                                    });
                                }
                            }, 1400);
                        }
                    }, 200);
                }else{
                    Helper.common.toastShort(activity, "Location Service not Available!");}

            }
        });

        showAllMarker(googleMap);
    }

    private void showAllMarker(GoogleMap googleMap) {
        db.open();
        Cursor cur = db.getClientInfoByUserID(Helper.variables.getGlobalVar_currentUserID(activity)+"");
        if (cur.getCount() > 0 ){
            while (cur.moveToNext()){
                LatLng latLng = new LatLng(
                        Double.parseDouble(cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_LAT))),
                        Double.parseDouble(cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_LNG))));

                String address = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_ADDRESS));
                String clientName = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_CLIENT_NAME));
                String id = cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_CLIENTINFO_ID));

                googleMap.setInfoWindowAdapter(new CustomerInfoWindow());
                Helper.map.addMarker(googleMap, latLng, R.drawable.ic_pet24, clientName, address, id);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requestCODE_addMarker){
            if (data.hasExtra("fromAddClient")){
                mMap.clear();

                showAllMarker(mMap);

                String[] latlng = data.getStringArrayExtra("latlng");
                LatLng ltlg = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
                Helper.map.moveCameraAnimate(mMap, ltlg, 18);
            }
        }
    }

    private void closerAddingMarker() {
        if(mapcircle!=null){
            mapcircle.remove();
            mapcircle = null;
        }

        btn_closeAddMarker.setVisibility(View.GONE);
        btn_AddMarker.setEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }




    @Override
    public void onBackPressed() {
        exitApp();
    }


    class CustomerInfoWindow implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;
        CustomerInfoWindow() {
            myContentsView = getLayoutInflater().inflate(R.layout.infowindow_customer_address, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            TextView tvSnippet = ((TextView) myContentsView.findViewById(R.id.address));
            TextView tvTitle = ((TextView) myContentsView.findViewById(R.id.title));
            String[] splitted = marker.getSnippet().split("#*#");
            String id = splitted[0] +" "+ splitted[1];

            tvSnippet.setText(id);
            tvTitle.setText( marker.getTitle());
            return myContentsView;
        }


        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private void exitApp() {
        final Dialog d = Helper.common.dialogThemedYesNO(activity, "Do you wish to wish to exit the app? You will have to login next time.", "EXIT", "YES", "NO", R.color.red);
        d.show();
        Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
        Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.hide();
                finishAffinity();
                Intent setIntent = new Intent(Intent.ACTION_MAIN);
                setIntent.addCategory(Intent.CATEGORY_HOME);
                setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(setIntent);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.hide();
            }
        });
    }
}
