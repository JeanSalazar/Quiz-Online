package com.example.jksa.quizonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class AcercaDeActivity extends AppCompatActivity {

    // Controles de la Pantalla de Inicio.
    private ImageView img_escudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Las 2 siguientes lineas sirven para que se pueda retorceder a la Actividad Anterior.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Instanciamos los controles de la Interfax.
        img_escudo = (ImageView)findViewById(R.id.img_escudo);

        // Asignamos el efecto "Desplazar hacia abajo" al cargar la Actividad.
        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        img_escudo.startAnimation(uptodown);
    }


    // Metodo para abrir la Actividad de Reportar Problema.
    public void abrirReporteProblema(View view){
        Intent intent = new Intent(this,ReportarProblemaActivity.class);
        startActivity(intent);
        //finish();
    }
}
