package paulbriseno.ittepi.edu.mx.tpdm_u2_practica1_briseno

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    var menu2 : ListView?=null

    var descripcionTa : EditText ?= null
    var fecharealizado : EditText ?= null
    var idlista : EditText ?= null
    var mostrarListaTarea : TextView ?= null
    var mostrarTodasListas : TextView ?= null
    var mostrarListas : TextView ?= null

    var basedatos = BaseDatos(this,"practica1",null,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        menu2=findViewById(R.id.menuLW2)


        descripcionTa = findViewById(R.id.descTarea2)
        fecharealizado = findViewById(R.id.fecharealizado2)
        idlista = findViewById(R.id.idlista2)
        mostrarTodasListas=findViewById(R.id.mostrarTodasTareas2)


        mostrarListaTarea  = findViewById(R.id.listaTarea2)


        mostrarListas = findViewById(R.id.mostrarListasEnTareas2)
        mostrarTodosList()

        menu2?.setOnItemClickListener { parent, view, i, l ->
            when(i){
                0-> insertarTareas(idlista?.text.toString())
                1->mostrarTodas()
                2-> pedirID("e")
                3->finish()
            }
        }
    }//metodos
    fun mensaje(t: String, m: String){
        AlertDialog.Builder(this)
            .setTitle(t)
            .setMessage(m)
            .setPositiveButton("OK")
            { dialogInterface, i ->}.show()
    }

    fun limpiarCampos(){
        descripcionTa?.setText("")
        fecharealizado?.setText("")
        idlista?.setText("")
    }

    fun validaCampos(): Boolean{
        if(descripcionTa?.text!!.toString().isEmpty()||fecharealizado?.text!!.toString().isEmpty()||idlista?.text!!.toString().isEmpty()){
            return false
        }else{
            return true
        }
    }

    fun insertarTareas(idtarea: String) {
        try {
            var trans = basedatos.writableDatabase
            var con = "INSERT INTO TAREAS VALUES(NULL,'DESCRIPCION','REALIZADO',IDLISTA)"

            if (validaCampos() == false) {
                mensaje(
                    "Error!","Existe algun campo vacio  equivocado (\"Descripcion\" y/o \"Fecha realizado\" y/o \"ID lista\")"
                )
                return
            }

            con = con.replace("DESCRIPCION", descripcionTa?.text.toString())
            con = con.replace("REALIZADO", fecharealizado?.text.toString())
            con = con.replace("IDLISTA",idtarea)
            trans.execSQL(con)
            trans.close()
            mensaje("Registro exitoso!", "Se inserto correctamente")
            limpiarCampos()
        } catch (er: SQLiteException) {
            mensaje("Error!", "No se pudo insertar el registro, datos incorrectos ")
        }
    }

    fun mostrarTodosList(){
        var sel = ""
        try {
            var trans = basedatos.readableDatabase
            var con = "SELECT * FROM LISTA"
            var cur = trans.rawQuery(con,null)
            var encEnLiTa = "ID     Descrcipcion"
            mostrarListaTarea?.setText(encEnLiTa)
            if(cur != null){
                if(cur.moveToFirst()==true){
                    do{
                        sel += "  ${cur.getString(0)}     ${cur.getString(1)}\n"
                    }while (cur.moveToNext())
                    mostrarTodasListas?.setText(sel)
                }else{
                    mensaje("Advertencia","No existen listas")
                }
            }
        }catch (er: SQLiteException){
            mensaje("Error!","No se encuentran registros en la BD")
        }
    }

    fun mostrarTodas(){
        var sel = ""
        try {
            var trans = basedatos.readableDatabase
            var con = "SELECT * FROM TAREAS"
            var cur = trans.rawQuery(con,null)
            if(cur != null) {
                if (cur.moveToFirst() == true) {
                    do{
                        sel +="ID: ${cur.getString(0)}\nDescripcion: ${cur.getString(1)}\nFecha de creacion: ${cur.getString(2)}\nID Lista: ${cur.getString(3)}\n"
                    }while (cur.moveToNext())
                    mostrarTodasTareas2?.setText(sel)
                }else{
                    mensaje("Advertencia!","No existen tareas")
                }
            }
            cur.close()
        }
        catch (er: android.database.SQLException){
            mensaje("Error!","No se encuentran registros en la BD")
        }
    }

    fun pedirID(etiqueta:String){
        var elemento = EditText(this)
        elemento.inputType = InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this).setTitle("Atencion!").setMessage("Escriba el ID en ${etiqueta}: ").setView(elemento)
            .setPositiveButton("OK"){dialog,which ->
                if(validarCampo(elemento) == false){
                    Toast.makeText(this@Main2Activity, "Error! campo vacio", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                buscar(elemento.text.toString(),etiqueta)

            }.setNeutralButton("Cancelar"){dialog, which ->  }.show()
    }

    fun validarCampo(elemento: EditText): Boolean{
        if(elemento.text.toString().isEmpty()){
            return false
        }else{
            return true
        }
    }

    fun buscar(id: String, btnEtiqueta: String){
        try {
            var trans = basedatos.readableDatabase
            var con="SELECT * FROM TAREAS WHERE IDTAREA="+id
            var  cur = trans.rawQuery(con,null)

            if (cur.moveToFirst()==true){
                if (btnEtiqueta.startsWith("e")){
                    var sel = "Seguro de eliminar la tarea: \"${cur.getString(1)}\" con el ID \"${cur.getString(0)}\" ?"
                    var alerta = AlertDialog.Builder(this)
                    alerta.setTitle("Atencion").setMessage(sel).setNeutralButton("NO"){dialog,which->
                        return@setNeutralButton
                    }.setPositiveButton("si"){dialog,which->
                        eliminar(id)
                        mostrarTodas()
                    }.show()
                }
            }else{
                mensaje("Error!","No existe el id: ${id}")
            }
        }catch (err: SQLiteException){
            mensaje("Error!","No se encontro el registro")
        }
    }

    fun eliminar(id:String){
        try{
            var trans = basedatos.writableDatabase
            var SQL = "DELETE FROM TAREAS WHERE IDTAREA="+id
            trans.execSQL(SQL)
            trans.close()
            mensaje("Exito!", "Se elimino correctamente el id: ${id}")
        }catch (err: SQLiteException){
            mensaje("Error!", "No se pudo eliminar")

        }
    }



}
