package com.example.arlet.dadm_u4_practica01_arlethkarinaacostatorres;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText idReceta, nombreReceta, ingredientesReceta, observacionesReceta, preparacionReceta;
    Button eliminarBoton, insertarButon, modificarBoton, consultarBoton;
    BaseDatos base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idReceta = findViewById(R.id.idEdit);
        nombreReceta = findViewById(R.id.nombreEdit);
        ingredientesReceta = findViewById(R.id.ingredientesEdit);
        preparacionReceta = findViewById(R.id.preparacionEdit);
        observacionesReceta = findViewById(R.id.observacionesEdit);
        eliminarBoton = findViewById(R.id.eliminar);
        insertarButon = findViewById(R.id.insertar);
        modificarBoton = findViewById(R.id.modificar);
        consultarBoton = findViewById(R.id.consultar);

        base = new BaseDatos(this, "receta", null, 1);

        insertarButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigoInsertar();
            }
        });

        consultarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirID(1);
            }
        });

        eliminarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirID(2);
            }
        });

        modificarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modificarBoton.getText().toString().startsWith("CONFIRMAR"))
                {
                    confirmacionActualizacion();
                }
                else {
                    pedirID(3);
                }
            }
        });



    }

    private void pedirID(final int i) {
        final EditText numeroID = new EditText(this);
        String mensaje ="";
        String mensajeButton ="";
        numeroID.setInputType(InputType.TYPE_CLASS_NUMBER);
        switch (i)
        {
            case 1:
                mensaje ="¿QUÉ ID DESEAS BUSCAR?";
                mensajeButton = "BUSCAR";
                break;
            case 2:
                mensaje = "¿QUÉ ID QUIERES ELIMINAR?";
                mensajeButton ="ELIMINAR";
                break;
            case 3:
                mensaje="¿QUÉ ID DESEAS MODIFICAR?";
                mensajeButton = "MODIFICAR";
                break;
        }
        numeroID.setHint(mensaje);
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        alerta.setTitle("ATENCIÓN").setMessage(mensaje)
                .setView(numeroID)
                .setPositiveButton(mensajeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(numeroID.getText().toString().isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "DEBES INGRESAR UN DATO OBLIGATORIAMENTE", Toast.LENGTH_LONG).show();
                            return;
                        }
                        buscarDato(numeroID.getText().toString(), i);
                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCELAR", null).show();
    }

    private void buscarDato(String idBuscar, int i) {
        try
        {
            SQLiteDatabase tabla = base.getReadableDatabase();
            String SQL = "SELECT * FROM RECETA WHERE ID="+idBuscar;
            Cursor resultado = tabla.rawQuery(SQL, null);

            if(resultado.moveToFirst())
            {
                idReceta.setText(resultado.getString(0));
                nombreReceta.setText(resultado.getString(1));
                ingredientesReceta.setText(resultado.getString(2));
                preparacionReceta.setText(resultado.getString(3));
                observacionesReceta.setText(resultado.getString(4));

                if(i==2)
                {
                    String dato = idBuscar+"&"+resultado.getString(1)+"&"+resultado.getString(2)+"&"+resultado.getString(3)+"&"+resultado.getString(4);
                    confirmarEliminacion(dato);
                    return;
                }
                if(i==3)
                {
                    insertarButon.setEnabled(false);
                    eliminarBoton.setEnabled(false);
                    consultarBoton.setEnabled(false);
                    idReceta.setEnabled(false);
                    modificarBoton.setText("CONFIRMAR");
                }
            }
            else
            {
                Toast.makeText(this, "NO SE ENCONTRÓ RESULTADO, PROBABLEMENTE NO EXISTA REGISTRO CON ESE IDENTIFICADOR", Toast.LENGTH_LONG).show();
            }
            tabla.close();
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "ERROR: NO SE PUDO", Toast.LENGTH_LONG).show();

        }
    }

    private void confirmacionActualizacion() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("ACTUALIZAR")
                .setMessage("¿Estás seguro que deseaas actualizar la receta?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actualizar();
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                idReceta.setText("");
                nombreReceta.setText("");
                ingredientesReceta.setText("");
                observacionesReceta.setText("");
                preparacionReceta.setText("");
                insertarButon.setEnabled(true);
                eliminarBoton.setEnabled(true);
                consultarBoton.setEnabled(true);
                idReceta.setEnabled(true);
                dialog.cancel();
            }
        }).show();

    }

    private void actualizar() {
        try{
            SQLiteDatabase tabla = base.getWritableDatabase();
            String SQL= "UPDATE RECETA SET NOMBRE='"+nombreReceta.getText().toString()+"', INGREDIENTES='"+ingredientesReceta.getText().toString()+"', PREPARACION='"+preparacionReceta.getText().toString()+"', OBSERVACIONES='"+observacionesReceta.getText().toString()+"' WHERE ID="+idReceta.getText().toString();
            tabla.execSQL(SQL);
            tabla.close();
            Toast.makeText(this, "SE ACTUALIZÓ CON ÉXITO", Toast.LENGTH_LONG).show();
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "NO SE PUEDE ACTUALIZAR", Toast.LENGTH_LONG).show();
        }
        idReceta.setText("");
        nombreReceta.setText("");
        ingredientesReceta.setText("");
        observacionesReceta.setText("");
        preparacionReceta.setText("");
        insertarButon.setEnabled(true);
        eliminarBoton.setEnabled(true);
        consultarBoton.setEnabled(true);
        idReceta.setEnabled(true);
    }

    private void confirmarEliminacion(String dato) {
        String datos[] = dato.split("&");
        final String id = datos[0];
        String receta = datos[1];
        String ingrediente = datos[2];
        String preparacion = datos[3];
        String observacion = datos[4];

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("CUIDADO").setMessage("¿Estás seguro que deseaas eliminar la receta: "+id+" Nombre: "+receta+" con ingredientes: " + ingrediente + " preparacion: " + preparacion + " con observacion: "+observacion + " ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminar(id);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", null).show();
    }

    private void eliminar(String id) {
        try{
            SQLiteDatabase tabla = base.getWritableDatabase();
            String SQL = "DELETE FROM RECETA WHERE ID="+id;
            tabla.execSQL(SQL);
            tabla.close();
            Toast.makeText(this, "¡BORRADO!", Toast.LENGTH_LONG).show();
            vaciarCampos();
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "NO SE PUDO ELIMINAR", Toast.LENGTH_LONG).show();
        }
    }

    private void codigoInsertar() {
        try {
            SQLiteDatabase tabla = base.getWritableDatabase();
            String SQL = "INSERT INTO RECETA VALUES(" + idReceta.getText().toString() + ",'" + nombreReceta.getText().toString() + "','" + ingredientesReceta.getText().toString() + "', '"+ preparacionReceta.getText().toString()+ "' ,'" + observacionesReceta.getText().toString() + "')";
            tabla.execSQL(SQL);
            Toast.makeText(this, "SE INSERTÓ EXITOSAMENTE", Toast.LENGTH_LONG).show();
            tabla.close();
            vaciarCampos();
        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "NO SE PUDO INSERTAR", Toast.LENGTH_LONG).show();
        }

    }

    private void vaciarCampos() {
        idReceta.setText("");
        nombreReceta.setText("");
        ingredientesReceta.setText("");
        observacionesReceta.setText("");
        preparacionReceta.setText("");
    }
}
