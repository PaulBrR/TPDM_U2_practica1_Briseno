package paulbriseno.ittepi.edu.mx.tpdm_u2_practica1_briseno

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos (
    context: Context?,name:String?,factory: SQLiteDatabase.CursorFactory?,version: Int )
    :SQLiteOpenHelper(context,name,factory,version){
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE LISTA (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, DESCRIPCION VARCHAR(500), FECHACREACION DATE)")
        p0?.execSQL("CREATE TABLE TAREAS (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, DESCRIPCION VARCHAR (400),REALIZADO DATE,IDLISTA INTEGER NOT NULL,CONSTRAINT fk_LISTA FOREIGN KEY (IDLISTA) REFERENCES LISTA(ID))")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // si la base de datos difiere
    }
}
