package com.example.jksa.quizonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OpcionesDocenteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_docente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public void onClickDocente(View view){
        Intent intent = new Intent(OpcionesDocenteActivity.this,DocenteActivity.class);
        startActivity(intent);
    }

    public void onClickEstadisticas(View view){
        Intent intent = new Intent(OpcionesDocenteActivity.this,PieChartActivity.class);
        startActivity(intent);
    }

}
