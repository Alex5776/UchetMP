package com.example.isproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.LinkedList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String AUTH = "auth";
    private static final String AUTH_ID = "auth_id";
    private static final String AUTH_LOGIN = "auth_login";
    private static final String AUTH_PASSWORD = "auth_password";
    private static final String AUTH_GRANT = "auth_grant";
    private static final String GUESTS = "guests";
    private static final String GUEST_ID = "guest_id";
    private static final String GUEST_NAME = "guest_name";
    private static final String GUEST_PHONE = "guest_phone";
    private static final String GUEST_DATE = "guest_date";
    private static final String EVENTS = "events";
    private static final String EVENT_ID = "event_id";
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_PLACE = "event_place";
    private static final String EVENT_DATE = "event_date";
    private static final String RESERVE = "reserve";
    private static final String RESERVE_ID = "reserve_id";
    private static final String DB_NAME = "isdb.db";
    private static final int SCHEMA = 1;




    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + AUTH + " (" + AUTH_ID + " INTEGER NOT NULL UNIQUE ," + AUTH_LOGIN + " TEXT NOT NULL, " + AUTH_PASSWORD + " TEXT NOT NULL, " + AUTH_GRANT + " TEXT NOT NULL, PRIMARY KEY(" + AUTH_ID +" AUTOINCREMENT));");
    db.execSQL("CREATE TABLE " + GUESTS + " (" + GUEST_ID + " INTEGER NOT NULL UNIQUE, " + GUEST_NAME + " TEXT NOT NULL, " + GUEST_PHONE + " TEXT NOT NULL, " + GUEST_DATE + " INTEGER NOT NULL, PRIMARY KEY(" + GUEST_ID +" AUTOINCREMENT));");
    db.execSQL("CREATE TABLE " + EVENTS + " (" + EVENT_ID + " INTEGER NOT NULL UNIQUE, " + EVENT_NAME + " TEXT NOT NULL, " + EVENT_PLACE + " INTEGER NOT NULL, " + EVENT_DATE + " INTEGER NOT NULL, PRIMARY KEY(" + EVENT_ID +" AUTOINCREMENT));");
    db.execSQL("CREATE TABLE " + RESERVE + " (" + RESERVE_ID + " INTEGER NOT NULL UNIQUE, " + GUEST_ID + "  INTEGER NOT NULL, " + EVENT_ID + "  INTEGER NOT NULL, PRIMARY KEY(" + RESERVE_ID + " AUTOINCREMENT), FOREIGN KEY(" +
            GUEST_ID + ") REFERENCES " + GUESTS + "(" + GUEST_ID + "), FOREIGN KEY(" + EVENT_ID + ") REFERENCES " + EVENTS + "(" + EVENT_ID + "));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    public Boolean AuthUpdateRow(AuthData data) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(data.authPassword.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            cv.put(AUTH_LOGIN, data.authLogin.toUpperCase());
            cv.put(AUTH_PASSWORD, hashtext);
            cv.put(AUTH_GRANT, data.authGrant);
            db.update(AUTH, cv, AUTH_ID + "=" + data.auth_id, null);
        }
        catch (NullPointerException e) {
            result = false;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        db.close();
        return result;
    }

    public Boolean ReserveUpdateRow(ReserveData data) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(EVENT_ID, data.event_id);
            cv.put(GUEST_ID, data.guest_id);
            db.update(RESERVE, cv, RESERVE_ID + "=" + data.reserve_id, null);
        }
        catch (NullPointerException e) {
            result = false;
        }
        db.close();
        return result;
    }

    public Boolean GuestUpdateRow(GuestData data) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(GUEST_NAME, data.guest_name.toUpperCase());
            cv.put(GUEST_PHONE, data.guest_phone);
            cv.put(GUEST_DATE, data.guest_date);
            db.update(GUESTS, cv, GUEST_ID + "=" + data.guest_id, null);
        }
        catch (NullPointerException e) {
            result = false;
        }
        db.close();
        return result;
    }

    public Boolean EventUpdateRow(EventData data) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(EVENT_NAME, data.event_name.toUpperCase());
            cv.put(EVENT_PLACE, data.event_place);
            cv.put(EVENT_DATE, data.event_date);
            db.update(EVENTS, cv, EVENT_ID + "=" + data.event_id, null);
        }
        catch (NullPointerException e) {
            result = false;
        }
        db.close();
        return result;
    }

    public void DeleteRow(String table, String KEY_NAME, Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, KEY_NAME + "=" + id, null);
        db.close();
    }

    public Boolean InsertAuthRow(AuthData data) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(data.authPassword.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
                cv.put(AUTH_LOGIN, data.authLogin.toUpperCase());
                cv.put(AUTH_PASSWORD, hashtext);
                cv.put(AUTH_GRANT, data.authGrant);
                db.insert(AUTH, null, cv);
        }
        catch (NullPointerException e) {
            result = false;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        db.close();
        return result;
    }

    public Boolean InsertGuestRow(GuestData data
    ) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(GUEST_NAME, data.guest_name.toUpperCase());
            cv.put(GUEST_PHONE, data.guest_phone);
            cv.put(GUEST_DATE, data.guest_date);
            db.insert(GUESTS, null, cv);
        } catch (NullPointerException e) {
            result = false;
        }

        db.close();
        return result;
    }

    public Boolean InsertEventRow(EventData data) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put(EVENT_NAME, data.event_name.toUpperCase());
            cv.put(EVENT_PLACE, data.event_place);
            cv.put(EVENT_DATE, data.event_date);
            db.insert(EVENTS, null, cv);
        }

        catch (NullPointerException e) {
                result = false;
                }

                db.close();
                return result;
    }

public Boolean InsertReserveRow(ReserveData data) {
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
        cv.put(GUEST_ID, data.guest_id);
        cv.put(EVENT_ID, data.event_id);
        db.insert(RESERVE, null, cv);
        }
        catch (NullPointerException e) {
        result = false;
        }
        db.close();
        return result;
        }

    public LinkedList<AuthData> GetAllAuth(){
        LinkedList<AuthData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(AUTH, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                int auth_id = cursor.getColumnIndex(AUTH_ID);
                int id_log = cursor.getColumnIndex(AUTH_LOGIN);
                int id_pass = cursor.getColumnIndex(AUTH_PASSWORD);
                int id_grant = cursor.getColumnIndex(AUTH_GRANT);
                AuthData data = new AuthData(cursor.getInt(auth_id), cursor.getString(id_log).substring(0, 1) + cursor.getString(id_log).substring(1).toLowerCase(), cursor.getString(id_pass), cursor.getString(id_grant));
                list.add(data);
            }while(cursor.moveToNext());
        db.close();
        return list;
    }

    /*public boolean ReserveEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(RESERVE, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            return true;}
        else {
            return false;}
    }*/

    public LinkedList<GuestData> GetAllGuests(){
        LinkedList<GuestData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(GUESTS, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                int guest_id = cursor.getColumnIndex(GUEST_ID);
                int id_name = cursor.getColumnIndex(GUEST_NAME);
                int id_phone = cursor.getColumnIndex(GUEST_PHONE);
                int id_date = cursor.getColumnIndex(GUEST_DATE);
                String s1 = cursor.getString(id_name).toLowerCase(), s2 = "";
                s2 += s1.substring(0, 1).toUpperCase();
                for (int i = 1; i < s1.length(); i++) {
                    if (" ".equals(s1.substring(i-1, i)))
                        s2 = s2 + s1.substring(i, i+1).toUpperCase();
                    else
                        s2 = s2 + s1.substring(i, i+1);
                }
                GuestData data = new GuestData(cursor.getInt(guest_id), s2, cursor.getString(id_phone), cursor.getLong(id_date));
                list.add(data);
            }while(cursor.moveToNext());
        db.close();
        return list;
    }

    public LinkedList<GuestData> GetAllGuestsNotDup(){
        LinkedList<GuestData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select " + GUEST_NAME + ", " + GUEST_ID + " from " + GUESTS + " group by " + GUEST_NAME, null);
        if (cursor.moveToFirst())
            do {
                int guest_id = cursor.getColumnIndex(GUEST_ID);
                int id_name = cursor.getColumnIndex(GUEST_NAME);
                String s1 = cursor.getString(id_name).toLowerCase(), s2 = "";
                s2 += s1.substring(0, 1).toUpperCase();
                for (int i = 1; i < s1.length(); i++) {
                    if (" ".equals(s1.substring(i-1, i)))
                        s2 = s2 + s1.substring(i, i+1).toUpperCase();
                    else
                        s2 = s2 + s1.substring(i, i+1);
                }
                GuestData data = new GuestData(cursor.getInt(guest_id), s2, null, null);
                list.add(data);
            }while(cursor.moveToNext());
        db.close();
        return list;
    }

    public Long GetGuestsDate(int id){
        LinkedList<GuestData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select " + GUEST_DATE + " from " + GUESTS + " where " + GUEST_ID + " = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        int guest_date = cursor.getColumnIndex(GUEST_DATE);
        db.close();
        return cursor.getLong(guest_date);
    }

    public LinkedList<ReserveData> GetAllReserve(){
        LinkedList<ReserveData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select " + RESERVE_ID + ", " + RESERVE + "." + GUEST_ID + ", " + GUEST_NAME + ", " + RESERVE + "." + EVENT_ID + ", " +
                EVENT_NAME + " from " + RESERVE + " inner join " +
                GUESTS + " on " + RESERVE + "." + GUEST_ID + " = " + GUESTS + "." + GUEST_ID + " inner join " + EVENTS + " on " +
                RESERVE + "." + EVENT_ID + " = " + EVENTS + "." + EVENT_ID, null);
        if (cursor.moveToFirst())
            do {
                int reserve_id = cursor.getColumnIndex(RESERVE_ID);
                int guest_id = cursor.getColumnIndex(GUEST_ID);
                int guest_name = cursor.getColumnIndex(GUEST_NAME);
                int guest_date = cursor.getColumnIndex(GUEST_DATE);
                int event_id = cursor.getColumnIndex(EVENT_ID);
                int event_name = cursor.getColumnIndex(EVENT_NAME);
                String s1 = cursor.getString(guest_name).toLowerCase(), s2 = "";
                s2 += s1.substring(0, 1).toUpperCase();
                for (int i = 1; i < s1.length(); i++) {
                    if (" ".equals(s1.substring(i-1, i)))
                        s2 = s2 + s1.substring(i, i+1).toUpperCase();
                    else
                        s2 = s2 + s1.substring(i, i+1);
                }
                ReserveData data = new ReserveData(cursor.getInt(reserve_id), cursor.getInt(guest_id), s2,
                        cursor.getInt(event_id), cursor.getString(event_name).substring(0, 1) + cursor.getString(event_name).substring(1).toLowerCase());
                list.add(data);
            }while(cursor.moveToNext());
        db.close();
        return list;
    }

    public LinkedList<EventData> GetAllEvent(){
        LinkedList<EventData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(EVENTS, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                int event_id = cursor.getColumnIndex(EVENT_ID);
                int id_name = cursor.getColumnIndex(EVENT_NAME);
                int id_place = cursor.getColumnIndex(EVENT_PLACE);
                int id_date = cursor.getColumnIndex(EVENT_DATE);
                EventData data = new EventData(cursor.getInt(event_id), cursor.getString(id_name).substring(0, 1) + cursor.getString(id_name).substring(1).toLowerCase(), cursor.getInt(id_place), cursor.getLong(id_date));
                list.add(data);
            }while(cursor.moveToNext());
        db.close();
        return list;
    }

    public int GetGuestId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select " + GUEST_ID + " from " + GUESTS + " where " + GUEST_NAME + " = ?",  new String[]{name.toUpperCase()});
        if (cursor.moveToFirst()){
        int guest_id = cursor.getColumnIndex(GUEST_ID);
        int id = cursor.getInt(guest_id);
        db.close();
        return id;}
        else {
        return 0;}
    }

    public int GetEventId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select " + EVENT_ID + " from " + EVENTS + " where " + EVENT_NAME + " = ?",  new String[]{name.toUpperCase()});
        if (cursor.moveToFirst()){
        int event_id = cursor.getColumnIndex(EVENT_ID);
        int id = cursor.getInt(event_id);
        db.close();
        return id;}
        else {
        return 0;}
    }

    public LinkedList<AuthData> SearchAuth(String SearchString){
        LinkedList<AuthData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AUTH +" where " + AUTH_LOGIN + " like ? or " + AUTH_GRANT + " like ?",
                new String[]{"%" + SearchString.toUpperCase() + "%", "%" + Character.toUpperCase(SearchString.charAt(0))
                        + SearchString.substring(1).toLowerCase() + "%"});
        if (cursor.moveToFirst())
            do {
                int auth_id = cursor.getColumnIndex(AUTH_ID);
                int id_log = cursor.getColumnIndex(AUTH_LOGIN);
                int id_pass = cursor.getColumnIndex(AUTH_PASSWORD);
                int id_grant = cursor.getColumnIndex(AUTH_GRANT);
                AuthData data = new AuthData(cursor.getInt(auth_id), cursor.getString(id_log).substring(0, 1) + cursor.getString(id_log).substring(1).toLowerCase(), cursor.getString(id_pass), cursor.getString(id_grant));
                list.add(data);
            }while(cursor.moveToNext());
        db.close();
        return list;
    }

    public LinkedList<ReserveData> SearchReserve(String SearchString){
        LinkedList<ReserveData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select " + RESERVE_ID + ", " + RESERVE + "." + GUEST_ID + ", " + GUEST_NAME + ", " + RESERVE + "." + EVENT_ID + ", " +
                        EVENT_NAME + " from " + RESERVE + " join " +
                        GUESTS + " on " + RESERVE + "." + GUEST_ID + " = " + GUESTS + "." + GUEST_ID + " join " + EVENTS + " on " +
                        RESERVE + "." + EVENT_ID + " = " + EVENTS + "." + EVENT_ID + " where " + GUEST_NAME + " like ? or " + EVENT_NAME +
                        " like ?",
                new String[]{"%" + SearchString.toUpperCase() + "%", "%" + SearchString.toUpperCase() + "%"});
        if (cursor.moveToFirst())
            do {
                int reserve_id = cursor.getColumnIndex(RESERVE_ID);
                int guest_id = cursor.getColumnIndex(GUEST_ID);
                int guest_name = cursor.getColumnIndex(GUEST_NAME);
                int event_id = cursor.getColumnIndex(EVENT_ID);
                int event_name = cursor.getColumnIndex(EVENT_NAME);
                String s1 = cursor.getString(guest_name).toLowerCase(), s2 = "";
                s2 += s1.substring(0, 1).toUpperCase();
                for (int i = 1; i < s1.length(); i++) {
                    if (" ".equals(s1.substring(i-1, i)))
                        s2 = s2 + s1.substring(i, i+1).toUpperCase();
                    else
                        s2 = s2 + s1.substring(i, i+1);
                }
                ReserveData data = new ReserveData(cursor.getInt(reserve_id), cursor.getInt(guest_id), s2,
                        cursor.getInt(event_id), cursor.getString(event_name).substring(0, 1) + cursor.getString(event_name).substring(1).toLowerCase());
                list.add(data);
            }while(cursor.moveToNext());
        Cursor tcursor = db.rawQuery("Select " + RESERVE_ID + ", " + RESERVE + "." + GUEST_ID + ", " + GUEST_DATE + ", " + GUEST_NAME + ", " + RESERVE + "." + EVENT_ID + ", " +
                        EVENT_NAME + " from " + RESERVE + " join " +
                        GUESTS + " on " + RESERVE + "." + GUEST_ID + " = " + GUESTS + "." + GUEST_ID + " join " + EVENTS + " on " +
                        RESERVE + "." + EVENT_ID + " = " + EVENTS + "." + EVENT_ID + " where " + GUEST_NAME + " not like ? and " + EVENT_NAME +
                        " not like ?",
                new String[]{"%" + SearchString.toUpperCase() + "%", "%" + SearchString.toUpperCase() + "%"});
        if (tcursor.moveToFirst())
            do {
                int reserve_id = tcursor.getColumnIndex(RESERVE_ID);
                int guest_id = tcursor.getColumnIndex(GUEST_ID);
                int guest_name = tcursor.getColumnIndex(GUEST_NAME);
                int guest_date = tcursor.getColumnIndex(GUEST_DATE);
                int event_id = tcursor.getColumnIndex(EVENT_ID);
                int event_name = tcursor.getColumnIndex(EVENT_NAME);
                String s1 = tcursor.getString(guest_name).toLowerCase(), s2 = "";
                s2 += s1.substring(0, 1).toUpperCase();
                for (int i = 1; i < s1.length(); i++) {
                    if (" ".equals(s1.substring(i-1, i)))
                        s2 = s2 + s1.substring(i, i+1).toUpperCase();
                    else
                        s2 = s2 + s1.substring(i, i+1);
                }
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(tcursor.getLong(guest_date));
                int iDay = c.get(Calendar.DAY_OF_MONTH);
                int iMonth = c.get(Calendar.MONTH)+1;
                String sMonth;
                String sDay;
                if (iDay < 10) {
                    sDay = "0" + Integer.toString(iDay);
                }
                else {
                    sDay = Integer.toString(iDay);
                }
                if (iMonth < 10){
                    sMonth = "0" + Integer.toString(iMonth);
                }
                else
                    sMonth = Integer.toString(iMonth);
                String str = sDay + "-" + sMonth + "-" + Integer.toString(c.get(Calendar.YEAR));
                if (str.contains(SearchString)){
                ReserveData data = new ReserveData(tcursor.getInt(reserve_id), tcursor.getInt(guest_id), s2,
                        tcursor.getInt(event_id), tcursor.getString(event_name).substring(0, 1) + tcursor.getString(event_name).substring(1).toLowerCase());
                list.add(data);}
            }while(tcursor.moveToNext());
        db.close();
        return list;
    }

    public LinkedList<EventData> SearchEvent(String SearchString){
        LinkedList<EventData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + EVENTS +" where " + EVENT_NAME + " like ? or " +
                EVENT_PLACE + " like ? ", new String[]{"%" + SearchString.toUpperCase() + "%", "%" + SearchString + "%"});
        if (cursor.moveToFirst())
            do {
                int event_id = cursor.getColumnIndex(EVENT_ID);
                int id_name = cursor.getColumnIndex(EVENT_NAME);
                int id_place = cursor.getColumnIndex(EVENT_PLACE);
                int id_date = cursor.getColumnIndex(EVENT_DATE);
                EventData data = new EventData(cursor.getInt(event_id), cursor.getString(id_name).substring(0, 1) + cursor.getString(id_name).substring(1).toLowerCase(), cursor.getInt(id_place), cursor.getLong(id_date));
                list.add(data);
            }while(cursor.moveToNext());
        Cursor tcursor = db.rawQuery("Select * from " + EVENTS +" where " + EVENT_NAME + " not like ? and " +
                EVENT_PLACE + " not like ? ", new String[]{"%" + SearchString.toUpperCase() + "%", "%" + SearchString + "%"});
        if (tcursor.moveToFirst())
            do {
                int event_id = tcursor.getColumnIndex(EVENT_ID);
                int id_name = tcursor.getColumnIndex(EVENT_NAME);
                int id_place = tcursor.getColumnIndex(EVENT_PLACE);
                int id_date = tcursor.getColumnIndex(EVENT_DATE);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(tcursor.getLong(id_date));
                int iDay = c.get(Calendar.DAY_OF_MONTH);
                int iMonth = c.get(Calendar.MONTH)+1;
                String sMonth;
                String sDay;
                if (iDay < 10) {
                    sDay = "0" + Integer.toString(iDay);
                }
                else {
                    sDay = Integer.toString(iDay);
                }
                if (iMonth < 10){
                    sMonth = "0" + Integer.toString(iMonth);
                }
                else
                    sMonth = Integer.toString(iMonth);
                String str = sDay + "-" + sMonth + "-" + Integer.toString(c.get(Calendar.YEAR));
                if (str.contains(SearchString)){
                    EventData data = new EventData(tcursor.getInt(event_id), tcursor.getString(id_name).substring(0, 1) + tcursor.getString(id_name).substring(1).toLowerCase(), tcursor.getInt(id_place), tcursor.getLong(id_date));
                    list.add(data);}
            }while(tcursor.moveToNext());
        db.close();
        return list;
    }

    public LinkedList<GuestData> SearchGuest(String SearchString){
        LinkedList<GuestData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + GUESTS +" where " + GUEST_NAME + " like ? or " +
                GUEST_PHONE + " like ? " , new String[]{"%" + SearchString.toUpperCase() + "%", "%" + SearchString + "%"});
        if (cursor.moveToFirst())
            do {
                int guest_id = cursor.getColumnIndex(GUEST_ID);
                int id_name = cursor.getColumnIndex(GUEST_NAME);
                int id_phone = cursor.getColumnIndex(GUEST_PHONE);
                int id_date = cursor.getColumnIndex(GUEST_DATE);
                String s1 = cursor.getString(id_name).toLowerCase(), s2 = "";
                s2 += s1.substring(0, 1).toUpperCase();
                for (int i = 1; i < s1.length(); i++) {
                    if (" ".equals(s1.substring(i-1, i)))
                        s2 += s1.substring(i, i+1).toUpperCase();
                    else
                        s2 += s1.substring(i, i+1);
                }
                GuestData data = new GuestData(cursor.getInt(guest_id), s2, cursor.getString(id_phone), cursor.getLong(id_date));
                list.add(data);
            }while(cursor.moveToNext());
        Cursor tcursor = db.rawQuery("Select * from " + GUESTS +" where " + GUEST_NAME + " not like ? and " +
                GUEST_PHONE + " not like ? ", new String[]{"%" + SearchString.toUpperCase() + "%", "%" + SearchString + "%"});
        if (tcursor.moveToFirst())
            do {
                int guest_id = tcursor.getColumnIndex(GUEST_ID);
                int id_name = tcursor.getColumnIndex(GUEST_NAME);
                String s1 = tcursor.getString(id_name).toLowerCase(), s2 = "";
                s2 += s1.substring(0, 1).toUpperCase();
                for (int i = 1; i < s1.length(); i++) {
                    if (" ".equals(s1.substring(i-1, i)))
                        s2 = s2 + s1.substring(i, i+1).toUpperCase();
                    else
                        s2 = s2 + s1.substring(i, i+1);
                }
                int id_phone = tcursor.getColumnIndex(GUEST_PHONE);
                String pstr = tcursor.getString(id_phone);
                pstr = pstr.replaceAll("[()-]","");
                int id_date = tcursor.getColumnIndex(GUEST_DATE);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(tcursor.getLong(id_date));
                int iDay = c.get(Calendar.DAY_OF_MONTH);
                int iMonth = c.get(Calendar.MONTH) + 1;
                String sMonth;
                String sDay;
                if (iDay < 10) {
                    sDay = "0" + Integer.toString(iDay);
                }
                else {
                    sDay = Integer.toString(iDay);
                }
                if (iMonth < 10){
                    sMonth = "0" + Integer.toString(iMonth);
                }
                else
                    sMonth = Integer.toString(iMonth);
                String str = sDay + "-" + sMonth + "-" + Integer.toString(c.get(Calendar.YEAR));
                if (str.contains(SearchString) || pstr.contains(SearchString)){
                    GuestData data = new GuestData(tcursor.getInt(guest_id), s2, tcursor.getString(id_phone), tcursor.getLong(id_date));
                    list.add(data);}
            }while(tcursor.moveToNext());
        db.close();
        return list;
    }

    public Boolean checkLoginPassword(String login, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            password = hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Cursor cursor = db.rawQuery("Select * from " + AUTH +" where " + AUTH_LOGIN +
                " = ? and " + AUTH_PASSWORD + " = ?", new String[]{login.toUpperCase(), password});
        if (cursor.getCount() > 0) {
            db.close();
            return true;
        }else {
            db.close();
            return false;
        }
    }

    public LinkedList<GuestData> GetDupGuest(String GN){
        LinkedList<GuestData> list = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + GUESTS +" where " + GUEST_NAME + " like ? or " +
                GUEST_PHONE + " like ? " , new String[]{"%" + GN.toUpperCase() + "%"});
        if (cursor.moveToFirst())
            do {
                int guest_id = cursor.getColumnIndex(GUEST_ID);
                int id_name = cursor.getColumnIndex(GUEST_NAME);
                int id_phone = cursor.getColumnIndex(GUEST_PHONE);
                int id_date = cursor.getColumnIndex(GUEST_DATE);
                GuestData data = new GuestData(cursor.getInt(guest_id), cursor.getString(id_name), cursor.getString(id_phone), cursor.getLong(id_date));
                list.add(data);
            }while(cursor.moveToNext());
        db.close();
        return list;
    }

    public boolean cheackAdmin(String login){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AUTH +" where " + AUTH_LOGIN + " = ?" , new String[]{login.toUpperCase(),});
        cursor.moveToFirst();
        int id_grant = cursor.getColumnIndex(AUTH_GRANT);
        if (cursor.getString(id_grant).equals("Администратор"))
            return true;
        else
            return false;
    }

    public boolean globalCheackAdmin(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AUTH +" where " + AUTH_GRANT + " = ?" , new String[]{"Администратор",});
        if (cursor.moveToFirst())
            return true;
        else
            return false;
    }

    public Integer getPlace(String mp) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor ecursor = db.rawQuery("Select " + EVENT_PLACE + " from " + EVENTS +" where " + EVENT_NAME + " == ?", new String[]{mp.toUpperCase()});
        ecursor.moveToFirst();
        int id_place = ecursor.getColumnIndex(EVENT_PLACE);
        int place = ecursor.getInt(id_place);
        Cursor cursor = db.rawQuery("Select " + RESERVE_ID + " from " + RESERVE + " join " +
                        EVENTS + " on " + RESERVE + "." + EVENT_ID + " = " + EVENTS + "." +
                        EVENT_ID + " where " + EVENT_NAME + " like ?",
                new String[]{mp.toUpperCase()});
        return place - cursor.getCount();
    }

    public Boolean checkPlace(String mp, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor ecursor = db.rawQuery("Select " + EVENT_PLACE + " from " + EVENTS +" where " + EVENT_NAME + " == ?", new String[]{mp.toUpperCase()});
        ecursor.moveToFirst();
        int id_place = ecursor.getColumnIndex(EVENT_PLACE);
        int place = ecursor.getInt(id_place);
        Cursor cursor = db.rawQuery("Select " + RESERVE_ID + " from " + RESERVE + " join " +
                        EVENTS + " on " + RESERVE + "." + EVENT_ID + " = " + EVENTS + "." +
                        EVENT_ID + " where " + EVENT_NAME + " like ? and " + RESERVE_ID + " <> ?",
                new String[]{mp.toUpperCase(), Integer.toString(id)});
        if (cursor.getCount() < place) {
            db.close();
            return true;
        }else {
            db.close();
            return false;
        }
    }

    public Boolean checkAuth(String login, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + AUTH +" where " + AUTH_LOGIN + " = ? and " + AUTH_ID + " <> ?" , new String[]{login.toUpperCase(), Integer.toString(id)});
        if (cursor.getCount() == 0) {
            db.close();
            return true;
        }else {
            db.close();
            return false;
        }
    }

    public void destroyDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AUTH, null, null);
        db.delete(GUESTS, null, null);
        db.delete(EVENTS, null, null);
        db.delete(RESERVE, null, null);
        db.close();
    }
}
