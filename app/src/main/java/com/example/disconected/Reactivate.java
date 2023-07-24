package com.example.disconected;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Reactivate extends AppCompatActivity {

    EditText pass;
    Button buttonActivate;
    TextView status,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactivate);
        pass = findViewById(R.id.senha);
        buttonActivate = findViewById(R.id.buttonActive);
        status = findViewById(R.id.statusActive);
        user = findViewById(R.id.user);

        HandleProps handleProps = new HandleProps();

        buttonActivate.setOnClickListener(view->{
            Intent intent = new Intent("wifi.action.shutdown_wifi");
            sendBroadcast(intent);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                handleProps.write("persist.control.wifi.service", Boolean.toString(true));
                Log.d("Wifi", "Desabilitando Função Wifi");
            }, 5000); // 5000 milissegundos = 5 segundos
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = {
                DatabaseHelper.COLUMN_EMAIL,
                DatabaseHelper.COLUMN_IS_ACTIVE,
                DatabaseHelper.COLUMN_PASSWORD
        };


        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { "1" };


        Cursor cursor = db.query(
                DatabaseHelper.TABLE_API_DATA,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (cursor != null && cursor.moveToFirst()) {
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL); // Get the index for the email column
            int isActiveIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_ACTIVE);
            int passIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD);


            String emailValue = cursor.getString(emailIndex); // Retrieve the email value from the cursor
            int isActiveValue = cursor.getInt(isActiveIndex);
            String passValue = cursor.getString(passIndex);

            cursor.close();

            user.setText("Usuário : " + emailValue);
            pass.setText(passValue);
            status.setText(isActiveValue == 1 ? "Ativo" : "Inativo");
        } else {
            // Caso não haja dados retornados, você pode lidar com essa situação aqui
        }

        db.close(); // Não esqueça de fechar o banco de dados após o uso
    }




}