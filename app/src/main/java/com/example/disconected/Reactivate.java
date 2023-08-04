package com.example.disconected;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Reactivate extends AppCompatActivity {

    EditText pass;
    Button buttonActivate,buttonLogout;
    TextView status,user;
    Handler handler = new Handler();

    Api api = new Api();

    String emailValue ="";
    String   passSystem = "";

    @Override
    protected void onStart() {
        status = findViewById(R.id.statusActive);
        super.onStart();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Api.postAPI(getApplicationContext(), emailValue, new Api.ApiResultCallback(){
                    @Override
                    public void onApiResult(String password, boolean isActive) {
                        // Atualizar o valor de passSystem com o valor retornado da API
                        passSystem = password;
                        status.setText(isActive ? "Status : Inativo" : "Status : Ativo");
                    }
                });

                handler.postDelayed(this, 6000);
            }
        };
        handler.postDelayed(runnable, 3000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactivate);
        pass = findViewById(R.id.senha);
        buttonActivate = findViewById(R.id.buttonActive);
//        buttonLogout = findViewById(R.id.buttonLogout);

        user = findViewById(R.id.user);



        HandleProps handleProps = new HandleProps();

        buttonActivate.setOnClickListener(view->{
            if (passSystem.equals(pass.getText().toString().trim())) {
                Toast.makeText(this, "Wifi Liberado", Toast.LENGTH_SHORT).show();
                handleProps.write("persist.control.wifi.service", Boolean.toString(false));
                pass.setText("");
            } else {
                Toast.makeText(this, "A senha está incorreta.", Toast.LENGTH_SHORT).show();
            }

        });


//        buttonLogout.setOnClickListener(view->{
//            // Excluir todos os dados do banco de dados antes de ir para a MainActivity
//            DatabaseHelper dbHelper = new DatabaseHelper(Reactivate.this);
//            dbHelper.deleteAllDataFromDatabase();
//            // Iniciar a MainActivity
//            Intent logout = new Intent(Reactivate.this, MainActivity.class);
//            startActivity(logout);
//
//            // Finalizar a atividade atual (Reactivate)
//            finish();
//
//
//        });

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
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
            int isActiveIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_ACTIVE);
            int passIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD);

            emailValue = cursor.getString(emailIndex);
            int isActiveValue = cursor.getInt(isActiveIndex);
            String passValue = cursor.getString(passIndex);

            cursor.close();



            if (passValue != null) { // Verificar se passValue não é null antes de atribuir à variável passSystem
                passSystem = passValue;
            }


            user.setText(emailValue);
            Log.d("email", emailValue);
            Log.d("iSActive", String.valueOf(isActiveValue));
        } else {
            // Caso não haja dados retornados, você pode lidar com essa situação aqui
        }
        db.close(); // Não esqueça de fechar o banco de dados após o uso
    }
}