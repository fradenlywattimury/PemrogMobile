package mobile.amikom.id.ac.workout.database;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperPrograms extends SQLiteOpenHelper{
	 
	// application package to store database
    private static String DB_PATH = "/data/data/mobile.amikom.id.ac.workout/databases/";
 
    final static String DB_NAME = "db_programs";
	public final static int DB_VERSION = 1;
    public static SQLiteDatabase db; 
 
    private final Context context;
    
    // tables and fields name of database
    private final String TABLE_DAYS = "tbl_days";
	private final String DAY_ID = "day_id";
    private final String DAY_NAME = "day_name";
    
	private final String TABLE_PROGRAMS = "tbl_programs";
	private final String PROGRAM_ID = "program_id";
	private final String WORKOUT_ID = "workout_id";
	private final String NAME = "name";
	private final String IMAGE = "image";
	private final String TIME = "time";
	private final String STEPS = "steps";
	
	
	
    public DBHelperPrograms(Context context) {
 
    	super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }	
 
    // method to create database
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
    	SQLiteDatabase db_Read = null;

 
    	if(dbExist){
    		//do nothing - database already exist
    		
    	}else{
    		db_Read = this.getReadableDatabase();
    		db_Read.close();
 
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
 
    }
    
 
   
    // method to check database
    private boolean checkDataBase(){
 
    	File dbFile = new File(DB_PATH + DB_NAME);

    	return dbFile.exists();
    	
    }
 
    // method to copy database from assets to application package
    private void copyDataBase() throws IOException{
    	
    	InputStream myInput = context.getAssets().open(DB_NAME);
 
    	String outFileName = DB_PATH + DB_NAME;
 
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    // method to open database
    public void openDataBase() throws SQLException{
    	String myPath = DB_PATH + DB_NAME;
    	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
 
    @Override
	public void close() {
    	db.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	
	// method to get day from database
	public ArrayList<ArrayList<Object>> getAllDays(){
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
	 
		Cursor cursor = null;
	 	try{
	 		cursor = db.query(
					TABLE_DAYS,
					new String[]{DAY_ID, DAY_NAME},
					null, null, null, null, null);
							
			cursor.moveToFirst();
			if (!cursor.isAfterLast()){
				do{
					ArrayList<Object> dataList = new ArrayList<Object>();
					long id = countWorkouts(cursor.getLong(0));
					
					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));
					dataList.add(id);
		
					dataArrays.add(dataList);
				}
				
				while (cursor.moveToNext());
			}
			cursor.close();
		}catch (SQLException e){
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
			
		return dataArrays;
	}

	// method to get number of workout programs on each day
	public int countWorkouts(long id) {
		Cursor dataCount = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PROGRAMS +" WHERE "+DAY_ID +" = "+id , null);
	    dataCount.moveToFirst();
	    int count = dataCount.getInt(0);
	    dataCount.close();
	    return count;
	}
	
 	
	// method to get all workout programs by day from database
 	public ArrayList<ArrayList<Object>> getWorkoutListByDay(int selectedID){
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
 
		Cursor cursor = null;
		try{
			cursor = db.query(
					TABLE_PROGRAMS,
					new String[]{PROGRAM_ID, WORKOUT_ID, NAME, IMAGE, TIME},
					DAY_ID +" = "+selectedID, null, null, null, null);
					
			cursor.moveToFirst();
			if (!cursor.isAfterLast()){
				do{
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getLong(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
	 
					dataArrays.add(dataList);
				}
				
				while (cursor.moveToNext());
			}
			cursor.close();
		}catch (SQLException e){
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		
		
		return dataArrays;
	}
	
 	// method to get detail workout from database
 	public ArrayList<Object> getDetail(int selectedID){
		
		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;
 
		try{
			cursor = db.query(
					TABLE_PROGRAMS,
					new String[] {PROGRAM_ID, WORKOUT_ID, NAME, DAY_ID, IMAGE, TIME, STEPS},
					PROGRAM_ID + " = " + selectedID,
					null, null, null, null, null);
 
			cursor.moveToFirst();
 
			if (!cursor.isAfterLast()){
				do{
					rowArray.add(cursor.getLong(0));
					rowArray.add(cursor.getLong(1));
					rowArray.add(cursor.getString(2));
					rowArray.add(cursor.getLong(3));
					rowArray.add(cursor.getString(4));
					rowArray.add(cursor.getString(5));
					rowArray.add(cursor.getString(6));
				}
				while (cursor.moveToNext());
			}
 
			cursor.close();
		}
		catch (SQLException e) 
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
 
		return rowArray;
	}
 	
 	// method to check whether workout program is available or not on day
 	public boolean isDataAvailable(int dayID, int workoutID){
 		boolean isAvailable = false;
		
		Cursor cursor = null;
 
			try{
				cursor = db.query(
						TABLE_PROGRAMS,
						new String[]{WORKOUT_ID},
						DAY_ID +" = "+ dayID +" AND "+ WORKOUT_ID +" = "+ workoutID,
						null, null, null, null, null);
				
				
				if(cursor.getCount() > 0){
					isAvailable = true;
				}

				cursor.close();
			}catch (SQLException e){
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
		
		return isAvailable;
	}
 	
 	// method to add workout program to selected day
 	public void addData(int workoutID, String name, int dayID, String image, String time, String steps){
		ContentValues values = new ContentValues();
		values.put(WORKOUT_ID, workoutID);
		values.put(NAME, name);
		values.put(DAY_ID, dayID);
		values.put(IMAGE, image);
		values.put(TIME, time);
		values.put(STEPS, steps);
 
		try{db.insert(TABLE_PROGRAMS, null, values);}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
 	
 	// method to update day of workout program
 	public void updateData(int selectedDayID, int programID){
		ContentValues values = new ContentValues();
		values.put(DAY_ID, selectedDayID);
 
		try {db.update(TABLE_PROGRAMS, values, PROGRAM_ID + "=" + programID, null);}
		catch (Exception e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}
 	
 	// method to delete workout program from day
 	public void deleteData(int programID){
 		// ask the database manager to delete the row of given id
 		try {db.delete(TABLE_PROGRAMS, PROGRAM_ID + "=" + programID, null);}
 		catch (Exception e)
 		{
 			Log.e("DB ERROR", e.toString());
 			e.printStackTrace();
 		}
	}
}