package com.santeh.petone.crm.Main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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

    EditText edtFarmId, edtClientName, edtContactNumber, edtLname, edtAddress;
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


        Helper.Common.hideKeyboardOnLoad(activity);
        if (getIntent().hasExtra("latitude")){lat= getIntent().getDoubleExtra("lat", 0);}
        if (getIntent().hasExtra("longtitude")){lng= getIntent().getDoubleExtra("long", 0);}

        txttitle = (TextView) findViewById(R.id.title);
        txtnote = (TextView) findViewById(R.id.txt_note);
        edtFarmId = (EditText) findViewById(R.id.edt_farmid);
        edtClientName = (EditText) findViewById(R.id.edt_fname);
        edtLname = (EditText) findViewById(R.id.edt_lname);
        edtContactNumber = (EditText) findViewById(R.id.edt_mname);
        edtAddress = (EditText) findViewById(R.id.edt_BirthPlace);


        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (!edtFarmId.getText().toString().equalsIgnoreCase("") && !edtClientName.getText().toString().equalsIgnoreCase("") &&
                        !edtContactNumber.getText().toString().equalsIgnoreCase("") && !edtLname.getText().toString().equalsIgnoreCase("") &&
                        !edtAddress.getText().toString().equalsIgnoreCase("")){
                     boolean isExisting = db.isCustCodeExisting(edtFarmId.getText().toString());
                     if (isExisting) {
                         Helper.Common.dialogThemedOkOnly(activity, "Oops", "Farm ID already exist. \n\nYou could only have one Farm ID per Customer", "OK", R.color.darkgreen_800);
                     }else{

                         finish();
                     }
                } else {
                    Helper.Common.dialogThemedOkOnly(activity, "Warning", "You must complete fields with '*' to continue.", "OK", R.color.red);
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

