package com.bederr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bederr.beans.Empresa_DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gantz on 28/08/14.
 */
public class Database_Maven extends SQLiteOpenHelper {

    private static final int MAVEN_DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Maven";
    private final String TABLE_EMPRESA = "Empresa_User";
    private final String KEY_ID = "id";
    private final String KEY_EMPRESA_ID = "empresaId";

    public Database_Maven(Context context) {
        super(context,DATABASE_NAME,null,MAVEN_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EMPRESA + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EMPRESA_ID + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPRESA);
        onCreate(db);
    }

    public void addEmpresaId(Empresa_DTO empresa_dto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMPRESA_ID,empresa_dto.getEmpresaId());
        db.insert(TABLE_EMPRESA, null, values);
        db.close();
    }

    public List<Empresa_DTO> getAllEmpresaIds() {
        List<Empresa_DTO> empresa_dtos = new ArrayList<Empresa_DTO>();
        String selectQuery = "SELECT  * FROM " + TABLE_EMPRESA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Empresa_DTO empresa_dto = new Empresa_DTO();
                empresa_dto.set_id(Integer.parseInt(cursor.getString(0)));
                empresa_dto.setEmpresaId(cursor.getString(1));
                empresa_dtos.add(empresa_dto);
            } while (cursor.moveToNext());
        }
        return empresa_dtos;
    }

    public void deleteAllEmpresaId() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPRESA,null,null);
        db.close();
    }

    public void deleteEmpresaId(String empresaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPRESA, KEY_EMPRESA_ID + " = ?", new String[]{empresaId});
        db.close();
    }

    public boolean isExists(String empresaId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EMPRESA_USER WHERE empresaId = "+empresaId,null);
        if (cursor.getCount() != 0){
            Log.e("EMPRESA", empresaId + "Existe");
            return true;
        }else{
            return false;
        }
    }
}
