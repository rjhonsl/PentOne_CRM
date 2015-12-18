package com.santeh.petone.crm.Main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.santeh.petone.crm.DBase.DB_Helper_AquaCRM;
import com.santeh.petone.crm.DBase.DB_Query_AquaCRM;
import com.santeh.petone.crm.R;
import com.santeh.petone.crm.Utils.FusedLocation;
import com.santeh.petone.crm.Utils.Helper;

/**
 * Created by rjhonsl on 9/4/2015.
 */
public class Activity_LoginScreen extends Activity{

    EditText txtusername, txtpassword;
    TextView txtappname1, txtappname2, txtshowpassword, txtforgot, txtrequestaccount, txttester, txtprogressdialog_message, lblusername, lblpassword;

    CheckBox chkshowpasword;
    ImageButton btnLogin;

    LatLng curlatlng;
    String longitude = " ", latitude=" ";

    Activity activity; Context context;
    Dialog PD;
    ProgressDialog mProgressDialog;

    String versionName, fileDir = "/sdcard/Download/", fulladdress, latestVersionName, versionFile;
    int versionCode;
//    List<CustInfoObject> listaccounts = new ArrayList<>();
    FusedLocation fusedLocation;
    PackageInfo pInfo = null;

    DB_Helper_AquaCRM dbHelper;
    DB_Query_AquaCRM db;

    float filesize;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        activity = this;
        context = Activity_LoginScreen.this;

        dbHelper = new DB_Helper_AquaCRM(this);
        db = new DB_Query_AquaCRM(this);
        db.open();

        fusedLocation = new FusedLocation(context, activity);
        fusedLocation.buildGoogleApiClient(context);



        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (pInfo !=null){
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        }


        PD =  Helper.random.initProgressDialog(activity);
        txtprogressdialog_message = (TextView) PD.findViewById(R.id.progress_message);

        initViews();

        Typeface font_roboto = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");


        Helper.common.hideKeyboardOnLoad(activity);

        btnLogin.requestFocus();
        txtappname1.setTypeface(font_roboto);
        txtappname2.setTypeface(font_roboto);
        txtpassword.setTypeface(font_roboto);
        txtusername.setTypeface(font_roboto);


        txttester.setText(
                Helper.common.getMacAddress(context)
                        + "\n" +
                        "update: " + versionCode + "    V." + versionName
        );


        txtshowpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chkshowpasword.isChecked()) {
                    chkshowpasword.setChecked(false);
                } else {
                    chkshowpasword.setChecked(true);
                }
                toggle_showpassword();
            }
        });


        if (txtpassword.isFocused() || !txtpassword.getText().toString().equalsIgnoreCase("")){
            txtpassword.setVisibility(View.VISIBLE);
        }

        txtusername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    if (!txtusername.getText().toString().equalsIgnoreCase("") || hasFocus){
                        txtusername.setVisibility(View.VISIBLE);

                        lblusername.setAlpha(0);
                        lblusername.setVisibility(View.VISIBLE);
                        lblusername.animate()
                                .translationY(0)
                                .alpha(255)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        txtusername.setHint("");
                                    }
                                });

                    }
                    else{
                        lblusername.animate()
                                .alpha(0)
                                .translationY(lblpassword.getHeight())
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        lblusername.setVisibility(View.GONE);
                                        txtusername.setHint("Username...");
                                    }
                                });
                    }
            }
        });

        txtpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!txtpassword.getText().toString().equalsIgnoreCase("") || hasFocus){

                    lblpassword.setAlpha(0);
                    lblpassword.setVisibility(View.VISIBLE);
                    lblpassword.animate()
                            .translationY(0)
                            .alpha(255)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    txtpassword.setHint("");
                                }
                            });

                }
                else{
                    lblpassword.animate()
                            .alpha(0)
                            .translationY(lblpassword.getHeight())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    lblpassword.setVisibility(View.GONE);
                                    txtpassword.setHint("Password...");
                                }
                            });
                }
            }
        });


        txtrequestaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ((txtpassword.getText().toString().equalsIgnoreCase("") || txtpassword.getText().toString().trim().equalsIgnoreCase(""))) &&
                        (txtusername.getText().toString().equalsIgnoreCase("") || txtusername.getText().toString().trim().equalsIgnoreCase(""))){
                    Helper.common.toastShort(activity, "Username and Password is needed to continue");
                }else if(txtpassword.getText().toString().equalsIgnoreCase("") || txtpassword.getText().toString().trim().equalsIgnoreCase("")){
                    Helper.common.toastShort(activity, "Password is needed to continue");
                }else if(txtusername.getText().toString().equalsIgnoreCase("") || txtusername.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Helper.common.toastShort(activity,"Username is needed to continue");
                }else
                {
                    login();
                }
            }
        });


        chkshowpasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_showpassword();
            }
        });
    }

    private void initViews() {

        txtusername = (EditText) findViewById(R.id.txt_loginscreen_username);
        txtpassword = (EditText) findViewById(R.id.txt_loginscreen_password);
        txtappname1 = (TextView) findViewById(R.id.txt_loginscreen_apptitle1);
        txtappname2 = (TextView) findViewById(R.id.txt_loginscreen_apptitle2);
        txtshowpassword = (TextView) findViewById(R.id.txt_loginscreen_showpassword);
        txtforgot = (TextView) findViewById(R.id.txtforgot_password);
        txtrequestaccount = (TextView) findViewById(R.id.txt_requestaccount);
        lblpassword = (TextView) findViewById(R.id.lbl_login_password);
        lblusername = (TextView) findViewById(R.id.lbl_login_username);
        txttester = (TextView) findViewById(R.id.txttester);

        btnLogin = (ImageButton) findViewById(R.id.btn_login);

        chkshowpasword = (CheckBox) findViewById(R.id.chk_loginscreen_showpassword);

        txtusername.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
        txtpassword.getBackground().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);

        txtusername.setText("jhonar10");
        txtpassword.setText("10");

    }

    private void toggle_showpassword() {
        if (chkshowpasword.isChecked()){
            txtpassword.setTransformationMethod(null);
        }else{
            txtpassword.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    public void login() {

        fusedLocation.connectToApiClient();
        Helper.map.isLocationAvailablePrompt(context, activity);
//        if(Helper.common.isNetworkAvailable(activity)) { // if network was available
////            updatingUserDB();
//            txtprogressdialog_message.setText("Logging in...");
//
//        }else{//if there was no network
//            if ( db.getUser_Count()  <=  0 ) { //if user db was null
//                //require user to connect to internet
//                if(!Helper.isNetworkAvailable(activity)) { Helper.toastShort(activity, "Internet connection is needed to start using the app."); }
//                else{
//                    txtprogressdialog_message.setText("Preparing app please wait...");
//                    PD.show();
//                    updatingUserDB(); }
//            }else { //if there is an existing account in local db
                txtprogressdialog_message.setText("Logging in...");
                PD.show();
                Cursor cur =  db.getUserIdByLogin(txtusername.getText().toString(), txtpassword.getText().toString(), Helper.common.getMacAddress(context));
                if (cur.getCount() > 0 ){
                    for (int i = 0; i < cur.getCount() ; i++) {
                        while (cur.moveToNext()) {
                            Helper.variables.setGlobalVar_currentlevel(cur.getInt(cur.getColumnIndex(DB_Helper_AquaCRM.CL_USERS_userlvl)), activity);
                            Helper.variables.setGlobalVar_currentUserID(cur.getInt(cur.getColumnIndex(DB_Helper_AquaCRM.CL_USERS_ID)), activity);
                            Helper.variables.setGlobalVar_currentFirstname(cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_USERS_firstName)), activity);
                            Helper.variables.setGlobalVar_currentLastname(cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_USERS_lastName)), activity);
                            Helper.variables.setGlobalVar_currentUsername(txtusername.getText().toString(),activity);
                            Helper.variables.setGlobalVar_currentUserpassword(txtpassword.getText().toString(),activity);
                            Helper.variables.setGlobalVar_currentAssignedArea("n/a", activity);
                            Helper.variables.setGlobalVar_DateAddedToDb(cur.getString(cur.getColumnIndex(DB_Helper_AquaCRM.CL_USERS_dateAdded)), activity);
                            Helper.variables.setGlobalVar_currentIsActive(cur.getInt(cur.getColumnIndex(DB_Helper_AquaCRM.CL_USERS_isactive)), activity);
                        }

                        if (Helper.variables.getGlobalVar_currentisActive(activity) == 1){

                            final Intent intent = new Intent(Activity_LoginScreen.this, MapsActivity.class);
                            intent.putExtra("userid", Helper.variables.getGlobalVar_currentUserID(activity));
                            intent.putExtra("userlevel", Helper.variables.getGlobalVar_currentLevel(activity));
                            intent.putExtra("username", Helper.variables.getGlobalVar_currentUserName(activity));
                            intent.putExtra("firstname",Helper.variables.getGlobalVar_currentUserFirstname(activity));
                            intent.putExtra("lastname", Helper.variables.getGlobalVar_currentUserLastname(activity));
                            intent.putExtra("userdescription", Helper.variables.getGlobalVar_currentAssignedArea(activity));
                            intent.putExtra("fromActivity", "login");
                            intent.putExtra("lat", fusedLocation.getLastKnowLocation().latitude + "");
                            intent.putExtra("long", fusedLocation.getLastKnowLocation().longitude + "");

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PD.hide();
                                    startActivity(intent);
                                }
                            }, 800);
                        }else{
                            Helper.common.toastShort(activity, "Account is not available. Pleas contact admin for further support.");
                            PD.hide();
                        }
                    }
                }else{
                    Helper.common.toastShort(activity, "Wrong account credentials. Please try again");
                    PD.hide();
                }
//            }
//        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        Helper.map.isLocationAvailablePrompt(context, activity);
        fusedLocation.connectToApiClient();
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

    private void exitApp() {
        final Dialog d = Helper.common.dialogThemedYesNO(activity, "Do you wish to wish to exit the app?", "EXIT", "YES", "NO", R.color.red);
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


}//end of class
