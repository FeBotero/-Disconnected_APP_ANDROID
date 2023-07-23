package com.example.disconected;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{
    private Api api;
    EditText email;
    Button buttonSubmit;
    ProgressBar progressBar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onStart() {
        super.onStart();

        // Criar uma instância do DatabaseHelper para acessar o banco de dados
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Coluna que você deseja recuperar do banco de dados (email)
        String[] projection = {
                DatabaseHelper.COLUMN_EMAIL
        };

        // Realizar a consulta no banco de dados
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_API_DATA,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Verificar se há dados retornados da consulta
        if (cursor != null && cursor.moveToFirst()) {
            // Email cadastrado encontrado no banco de dados
            cursor.close(); // Fechar o cursor antes de redirecionar

            Intent intent = new Intent(getApplicationContext(), Reactivate.class);
            startActivity(intent);
            finish(); // Finalizar a MainActivity para que o usuário não possa voltar a ela pressionando o botão "Back"
        }

        db.close(); // Não esqueça de fechar o banco de dados após o uso
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        buttonSubmit = findViewById(R.id.acessar);
        progressBar = findViewById(R.id.progressbar);
        databaseHelper = new DatabaseHelper(this);


        buttonSubmit.setOnClickListener(view -> {
            String emailContent = email.getText().toString();

            if (!emailContent.isEmpty()) {
                boolean success = databaseHelper.saveEmailToDatabase(emailContent);

                if (success) {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonSubmit.setVisibility(View.GONE);

                    Intent intent = new Intent(getApplicationContext(), Reactivate.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Favor digitar novamente", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    buttonSubmit.setVisibility(View.VISIBLE);
                }
            } else {
                // Handle the case when the email is empty, if needed
                Toast.makeText(this, "Favor inserir um e-mail válido", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                buttonSubmit.setVisibility(View.VISIBLE);
            }
        });

    }
}