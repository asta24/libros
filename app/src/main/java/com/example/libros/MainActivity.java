package com.example.libros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_codigo, et_descripcion, et_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_codigo = (EditText) findViewById(R.id.txt_codigo);
        et_descripcion = (EditText) findViewById(R.id.txt_descripcion);
        et_precio = (EditText) findViewById(R.id.txt_precio);
    }
    //Método para dar de alta a los productos
    public void Registrar (View view){
        // Instanciamos la base de datos con los cuatro argumentos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        // Abrimos la base de datos en modo lectura y escritura
        SQLiteDatabase libros = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            ContentValues registro = new ContentValues();

            // Guardamos en la base de datos los valores que el usuario ha escrito
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            // Los insertamos dentro de la tabla "articulos" que hemos creado
            libros.insert("libros1", null, registro);

            // Cerramos la base de datos
            libros.close();

            //Limpiamos los campos
            et_codigo.setText("");
            et_descripcion.setText("");
            et_precio.setText("");

            Toast.makeText(this, "El libro se ha grabado de forma correcta", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Buscar artículos
    public void Buscar (View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        // Abrimos la base de datos en modo lectura y escritura
        SQLiteDatabase libros = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()){
            //Seleccionamos los registros con el código
            Cursor fila = libros.rawQuery("select descripcion, precio from libros1 where codigo =" + codigo, null);

            // Revisamos si la consulta contiene valores
            if(fila.moveToFirst()){
                et_descripcion.setText(fila.getString(0)); //Se pone el cero por que es el primer valor que vamos a mostrar
                et_precio.setText(fila.getString(1));
                libros.close();
            }else{
                Toast.makeText(this, "El libro no existe", Toast.LENGTH_SHORT).show();
                libros.close();
            }
        }else{
            Toast.makeText(this, "Debes introducir el código del libro", Toast.LENGTH_SHORT).show();
        }

    }

    // Método para elimiar un producto
    public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase libros = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()){
            int cantidad = libros.delete("libros1", "codigo=" + codigo, null);
            libros.close();

            et_codigo.setText("");
            et_descripcion.setText("");
            et_precio.setText("");

            if(cantidad == 1){
                Toast.makeText(this, "El libro se ha borrado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El libro no existe", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Debes introducir el libro del código", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para modificar artículo
    public void Modificar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase libros = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cantidad = libros.update("libros1", registro, "codigo=" + codigo, null);
            libros.close();

            if(cantidad == 1){
                Toast.makeText(this, "El libro se ha modificado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El libro no existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para limpiar la pantalla de datos
    public void Limpiar(View view){
        et_codigo.setText("");
        et_descripcion.setText("");
        et_precio.setText("");
    }

}
