package com.santeh.petone.crm.DBase;

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
