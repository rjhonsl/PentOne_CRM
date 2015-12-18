package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.santeh.petone.crm.DBase.DB_Query_AquaCRM;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.Helper;

/**
 * Created by rjhonsl on 10/7/2015.
 */
public class Activity_Add_ClientInfo extends FragmentActivity{

    ImageButton btnOK;
    double lat=0, lng=0;

    public static final String DATEPICKER_TAG = "datepicker";

    DatePickerDialog datePickerDialog;
    int y, m, d;

    Activity activity;
    Context context;

    DB_Query_AquaCRM db;

    EditText edtCustomerCode, edtClientName, edtContactNumber, edtAddress;
    TextView txtnote, txttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clientinfo);

        activity = this;
        context = Activity_Add_ClientInfo.this;

        btnOK = (ImageButton) findViewById(R.id.btn_ok);

        db = new DB_Query_AquaCRM(this);
        db.open();


        Helper.common.hideKeyboardOnLoad(activity);
        if (getIntent().hasExtra("lat")){lat= getIntent().getDoubleExtra("lat", 0);}
        if (getIntent().hasExtra("long")){lng= getIntent().getDoubleExtra("long", 0);}

        txttitle = (TextView) findViewById(R.id.title);
        txtnote = (TextView) findViewById(R.id.txt_note);
        edtCustomerCode = (EditText) findViewById(R.id.edt_custCode);
        edtClientName = (EditText) findViewById(R.id.edt_clientName);
        edtContactNumber = (EditText) findViewById(R.id.edt_contactNumber);
        edtAddress = (EditText) findViewById(R.id.edtAddress);


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (!edtCustomerCode.getText().toString().equalsIgnoreCase("") &&
                         !edtClientName.getText().toString().equalsIgnoreCase("") &&
                         !edtContactNumber.getText().toString().equalsIgnoreCase("") &&
                         !edtAddress.getText().toString().equalsIgnoreCase("")){
                     boolean isExisting = db.isCustCodeExisting(edtCustomerCode.getText().toString());
                     if (isExisting) {
                         Helper.common.dialogThemedOkOnly(activity, "Oops", "Customer Code already exist. \n\nYou could only have one input Customer Code once.", "OK", R.color.darkgreen_800);
                     }else{
                         final Dialog d = Helper.common.dialogThemedYesNO(activity, "Save Client Information to device storage?", "Save", "NO", "YES", R.color.blue);
                         Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                         Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                         no.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                d.hide();
                             }
                         });

                         yes.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {

                                 String[] latlng = {lat+"", lng+""};

                                 Intent intent = new Intent(activity, MapsActivity.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 intent.putExtra("fromAddClient", "1");
                                 intent.putExtra("latlng", latlng);
                                 String currentDateTime = Helper.timeConvert.longtoDateTime_DB_Format(System.currentTimeMillis());
                                 db.insertClientInfo(String.valueOf(lat), String.valueOf(lng), edtAddress.getText().toString(), edtClientName.getText().toString(), edtCustomerCode.getText().toString(),
                                         edtContactNumber.getText().toString(), currentDateTime, Helper.variables.getGlobalVar_currentUserID(activity)+"");

                                 setResult(MapsActivity.requestCODE_addMarker, intent);
                                 finish();

                             }
                         });

                     }
                } else {
                    Helper.common.dialogThemedOkOnly(activity, "Warning", "You must complete fields with '*' to continue.", "OK", R.color.red);
                }
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
}

