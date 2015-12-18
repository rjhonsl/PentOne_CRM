package com.santeh.petone.crm.DBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB_Query_AquaCRM {

	private static final String LOGTAG = "GPS";
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase db;

	/********************************************
	 * 				DEFAULTS					*
	 ********************************************/
	public DB_Query_AquaCRM(Context context){
		//Log.d("DBSource", "db connect");
		dbhelper = new DB_Helper_AquaCRM(context);
		//opens the db connection
	}

	public void open(){
		//Log.d("DBSource", "db open");
		db = dbhelper.getWritableDatabase();
	}
	public void close(){
		//Log.d("DBSource", "db close");
		dbhelper.close();
	}



	/********************************************
	 * 				INSERTS						*
	 * 	returns index/rowNum of inserted values *
	 ********************************************/


	public long insertClientInfo(String lat, String lng, String address, String clientName, String custCode, String contactNumber, String dateAdded, String addedBy){

		ContentValues values = new ContentValues();
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_LAT, lat);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_LNG, lng);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_ADDRESS, address);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_CLIENT_NAME, clientName);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_CUSTCODE, custCode);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_C_NUMBER, contactNumber);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_dateAdded, dateAdded);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_addedby, addedBy);
		values.put(DB_Helper_AquaCRM.CL_CLIENTINFO_IsPosted, 0);

		return  db.insert(DB_Helper_AquaCRM.TBL_CLIENTINFO, null, values);
	}



	/********************************************
	 * 				VALIDATIONS					*
	 ********************************************/


	/********************************************
	 * 				SELECTS						*
	 ********************************************/

	public int getUser_Count() {
		String query = "SELECT * FROM "+DB_Helper_AquaCRM.TBL_USERS+";";
		String[] params = new String[] {};
		Cursor cur = db.rawQuery(query, params);
		return cur.getCount();
	}



	public boolean isCustCodeExisting(String farmid){
		boolean isexisting = false;
		String query = "SELECT * FROM "+DB_Helper_AquaCRM.TBL_CLIENTINFO +" WHERE "
				+ DB_Helper_AquaCRM.CL_CLIENTINFO_CUSTCODE + " = ? "
				;
		String[] params = new String[] {farmid};
		Cursor cur = db.rawQuery(query, params);
		if (cur!=null){
			if (cur.getCount() > 0){
				isexisting = true;
			}
		}
		return  isexisting;
	}

	public Cursor getClientInfoByUserID(String userID){
		String query = "SELECT * FROM "+DB_Helper_AquaCRM.TBL_CLIENTINFO+" WHERE "
				+ DB_Helper_AquaCRM.CL_CLIENTINFO_addedby + " = ? "
				;


		String[] params = new String[] {userID};
		return db.rawQuery(query, params);
	}


	public Cursor getUserIdByLogin(String username, String password, String deviceid){
		String query = "SELECT * FROM "+DB_Helper_AquaCRM.TBL_USERS+" WHERE "
				+ DB_Helper_AquaCRM.CL_USERS_username + " = ? AND "
				+ DB_Helper_AquaCRM.CL_USERS_password + " = ? AND "
				+ DB_Helper_AquaCRM.CL_USERS_deviceid + " = ? "
				;
		String[] params = new String[] {username, password, deviceid };
		return db.rawQuery(query, params);
	}


	/********************************************
	 * 				DELETES						*
	 ********************************************/


	/********************************************
	 * 				UDPATES						*
	 ********************************************/

}
