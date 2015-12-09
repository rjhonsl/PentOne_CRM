package com.santeh.petone.crm.DBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_Helper_AquaCRM extends SQLiteOpenHelper {

	private static final String LOGTAG = "DB_GPS";
	private static final String DATABASE_NAME = "petone.db";
	//each time you change data structure, you must increment this by 1
	private static final int DATABASE_VERSION = 1;

	//reference for tblarea
	public static final String TBL_CLIENTINFO = "[SALES.CLIENTINFO]";
	public static final String CL_CLIENTINFO_ID 		= "ci_customerId";
	public static final String CL_CLIENTINFO_LAT 		= "latitude";
	public static final String CL_CLIENTINFO_LNG 		= "longtitude";
	public static final String CL_CLIENTINFO_ADDRESS 	= "address";
	public static final String CL_CLIENTINFO_CLIENT_NAME= "client_name";
	public static final String CL_CLIENTINFO_CUSTCODE 	= "custcode";
	public static final String CL_CLIENTINFO_C_NUMBER 	= "contact_number";
	public static final String CL_CLIENTINFO_dateAdded 	= "dateAdded";
	public static final String CL_CLIENTINFO_addedby 	= "addedby";
	public static final String CL_CLIENTINFO_IsPosted 	= "isposted";
	public static final String[] ALL_KEY_fARM			= new String[]{CL_CLIENTINFO_ID, CL_CLIENTINFO_LAT, CL_CLIENTINFO_LNG, CL_CLIENTINFO_ADDRESS, CL_CLIENTINFO_CLIENT_NAME,
	CL_CLIENTINFO_CUSTCODE, CL_CLIENTINFO_C_NUMBER, CL_CLIENTINFO_dateAdded, CL_CLIENTINFO_addedby, CL_CLIENTINFO_IsPosted};



	public static final String TBL_UPDATES 					= "[SALES.UPDATES]";
	public static final String CL_UPDATES_ID 				= "updates_id";
	public static final String CL_UPDATES_REMARKS = "updates_remakrs";
	public static final String CL_UPDATES_CLIENTID 			= "updates_clientid";
	public static final String CL_UPDATES_DATEADDED 		= "updates_dateAdded";
	public static final String CL_UPDATES_isposted 			= "updates_isposted";
	public static final String[] ALL_KEY_WEEKLY_UPDATES		= new String[]{CL_UPDATES_ID, CL_UPDATES_REMARKS, CL_UPDATES_CLIENTID,
			CL_UPDATES_DATEADDED, CL_UPDATES_isposted};


	public static final String TBL_USERS = "[SALES.USERS]";
	public static final String CL_USERS_ID				= "users_id";
	public static final String CL_USERS_userlvl			= "userlvl";
	public static final String CL_USERS_firstName		= "users_firstname";
	public static final String CL_USERS_lastName		= "users_lastname";
	public static final String CL_USERS_username		= "users_username";
	public static final String CL_USERS_password		= "users_password";
	public static final String CL_USERS_deviceid		= "users_device_id";
	public static final String CL_USERS_dateAdded		= "dateAdded";
	public static final String CL_USERS_isactive		= "isactive";
	public static final String[] ALL_KEY_USERS	= new String[]{CL_USERS_ID, CL_USERS_userlvl, CL_USERS_firstName, CL_USERS_lastName, CL_USERS_username,
			CL_USERS_password, CL_USERS_deviceid, CL_USERS_dateAdded , CL_USERS_isactive};


	//reference for tblarea
	public static final String TBL_USER_ACTIVITY = "[SALES.USERS.ACTIVITY]";

	public static final String CL_USER_ACTIVITY_ID			= "user_act_id";
	public static final String CL_USER_ACTIVITY_USERID		= "user_act_userid";
	public static final String CL_USER_ACTIVITY_ACTIONDONE	= "user_act_actiondone";
	public static final String CL_USER_ACTIVITY_LAT			= "user_act_latitude";
	public static final String CL_USER_ACTIVITY_LNG			= "user_act_longitude";
	public static final String CL_USER_ACTIVITY_DATETIME	= "user_act_datetime";
	public static final String CL_USER_ACTIVITY_ACTIONTYPE	= "user_act_actiontype";
	public static final String CL_USER_ACTIVITY_isPosted	= "user_act_isposted";
	public static final String[] ALL_KEY_USERACTIVITY		= new String[]{CL_USER_ACTIVITY_ID, CL_USER_ACTIVITY_USERID, CL_USER_ACTIVITY_ACTIONDONE,
			CL_USER_ACTIVITY_LAT, CL_USER_ACTIVITY_LNG, CL_USER_ACTIVITY_DATETIME, CL_USER_ACTIVITY_ACTIONTYPE, CL_USER_ACTIVITY_isPosted};


	//////////////////////////////////////////////////////////////////
	///////////// STRINGS FOR CREATING AND UPDATING TABLE ////////////
	//////////////////////////////////////////////////////////////////
	//Query to create tables

	private static final String TBL_CREATE_CLIENTINFO =
			"CREATE TABLE " + TBL_CLIENTINFO + " " +
					"(" +
					CL_CLIENTINFO_ID 		+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					CL_CLIENTINFO_LAT 		+ " TEXT, " +
					CL_CLIENTINFO_LNG 		+ " TEXT, " +
					CL_CLIENTINFO_ADDRESS 	+ " TEXT, " +
					CL_CLIENTINFO_CLIENT_NAME + " TEXT, " +
					CL_CLIENTINFO_CUSTCODE 	+ " TEXT, " +
					CL_CLIENTINFO_C_NUMBER 	+ " TEXT, " +
					CL_CLIENTINFO_dateAdded + " DATETIME, " +
					CL_CLIENTINFO_addedby 	+ " INTEGER, " +
					CL_CLIENTINFO_IsPosted 	+ " INTEGER " +
					")";


	private static final String TBL_CREATE_UPDATES =
			"CREATE TABLE " + TBL_UPDATES + " " +
					"(" +
					CL_UPDATES_ID			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					CL_UPDATES_REMARKS		+ " TEXT, " +
					CL_UPDATES_CLIENTID 	+ " TEXT, " +
					CL_UPDATES_DATEADDED 	+ " INTEGER, " +
					CL_UPDATES_isposted 	+ " INTEGER " +
					")";


	private static final String TBL_CREATE_USERS =
			"CREATE TABLE " + TBL_USERS + " " +
					"(" +
					CL_USERS_ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					CL_USERS_userlvl 		+ " INTEGER, " +
					CL_USERS_firstName 		+ " TEXT, " +
					CL_USERS_lastName 		+ " TEXT, " +
					CL_USERS_username 		+ " TEXT, " +
					CL_USERS_password 		+ " TEXT, " +
					CL_USERS_deviceid 		+ " TEXT, " +
					CL_USERS_dateAdded 		+ " DATE, " +
					CL_USERS_isactive 		+ " INTEGER " +
					")";

	private static final String TBL_CREATE_USERS_ACTIVITY =
			"CREATE TABLE " + TBL_USER_ACTIVITY + " " +
					"(" +
					CL_USER_ACTIVITY_ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					CL_USER_ACTIVITY_USERID 		+ " INTEGER, " +
					CL_USER_ACTIVITY_ACTIONDONE 	+ " TEXT, " +
					CL_USER_ACTIVITY_LAT 			+ " TEXT, " +
					CL_USER_ACTIVITY_LNG 			+ " TEXT, " +
					CL_USER_ACTIVITY_DATETIME 		+ " DATETIME, " +
					CL_USER_ACTIVITY_ACTIONTYPE 	+ " TEXT," +
					CL_USER_ACTIVITY_isPosted 		+ " INTEGER" +
					")";






	//connects db
	public DB_Helper_AquaCRM(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d(LOGTAG, "table " + DATABASE_NAME + " has been opened: " + String.valueOf(context));
	}

	@Override
	//create tb
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TBL_CREATE_CLIENTINFO);
		db.execSQL(TBL_CREATE_UPDATES);
		db.execSQL(TBL_CREATE_USERS);
		db.execSQL(TBL_CREATE_USERS_ACTIVITY);
		Log.d(LOGTAG, "tables has been created: " + String.valueOf(db));
	}

	@Override
	//on update version renew tb
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
		_db.execSQL("DROP TABLE IF EXISTS " + TBL_CLIENTINFO);
		_db.execSQL("DROP TABLE IF EXISTS " + TBL_UPDATES);
		_db.execSQL("DROP TABLE IF EXISTS " + TBL_USERS);
		_db.execSQL("DROP TABLE IF EXISTS " + TBL_USER_ACTIVITY);

		Log.d(LOGTAG, "table has been deleted: " + String.valueOf(_db));
		onCreate(_db);
	}

}
