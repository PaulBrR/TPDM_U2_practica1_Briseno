package paulbriseno.ittepi.edu.mx.tpdm_u2_practica1_briseno

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var menu1 :ListView?=null

    var descripcionLista: EditText ?= null
    var fechacreacionLista : EditText ?= null
    var insertarLista : Button ?= null
    var mostrarTodasListas : Button ?= null
    var abrirTareas : Button ?= null
    var mostrarLista : TextView ?= null
    var bdLista = BaseDatos(this,"practica1",null,1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menu1=findViewById(R.id.menuLW1)

        descripcionLista = findViewById(R.id.descLista1)
        fechacreacionLista = findViewById(R.id.fechaLista)
        /*insertarLista = findViewById(R.id.insertarLista)
        mostrarTodasListas = findViewById(R.id.mostrarTodasListas)
        abrirTareas = findViewById(R.id.abrirTareas)*/
        mostrarLista = findViewById(R.id.mostrarLista1)
        mostrar()

        menu1?.setOnItemClickListener { parent, view, i, l ->
            when(i){
                0-> insertarListas()
                1->mostrar()
                2-> irActivity()
                3->finish()
            }
        }

    }//metodos
    fun irActivity(){
        val ventanaTarea = Intent(this,Main2Activity::class.java)
        startActivity(ventanaTarea)
    }

    fun mensaje(a: String, b: String){
        AlertDialog.Builder(this)
            .setTitle(a)
            .setMessage(b)
            .setPositiveButton("OK")
            { dialogInterface, i ->}.show()
    }

    fun limpiarCampos(){
        descripcionLista?.setText("")
        fechacreacionLista?.setText("")
    }

    fun validaCampos(): Boolean{
        if(descripcionLista?.text!!.toString().isEmpty()||fechacreacionLista?.text!!.isEmpty()){
            return false
        }else{
            return true
        }
    }

    fun insertarListas(){
        try {
            var trans = bdLista.writableDatabase
            var SQL = "INSERT INTO LISTA VALUES(NULL,'DESC','FECHACREA')"
            if (validaCampos() == false) {
                mensaje("Error!", "Existe algun campo vacio (\"Descripcion\" y/o \"Fecha de creacion\")")
                return
            }

            SQL = SQL.replace("DESC", descripcionLista?.text.toString())
            SQL = SQL.replace("FECHACREA", fechacreacionLista?.text.toString())
            trans.execSQL(SQL)
            trans.close()
            mensaje("Registro exitoso!", "Se inserto correctamente")
            limpiarCampos()
        }
        catch (er: SQLiteException) {
            mensaje("Error!","No se pudo insertar el registro, verifique sus datos!")
        }
    }

    fun mostrar(){
        var sel = ""
        try {
            var transicion = bdLista.readableDatabase
            var con = "SELECT * FROM LISTA"
            var cur = transicion.rawQuery(con,null)
            if(cur != null) {
                if (cur.moveToFirst() == true) {
                    do{
                        sel +="ID: ${cur.getString(0)}\nDescripcion: ${cur.getString(1)}\nFecha de creacion: ${cur.getString(2)}\n"+
                                "______________________________________________\n"
                    }while (cur.moveToNext())
                    mostrarLista?.setText(sel)
                }else{
                    mensaje("Advertencia!","No existen listas")
                }
            }
            cur.close()
        }
        catch (er: SQLiteException){
            mensaje("Error!","No se encuentran registros en la BD")
        }
    }
}

