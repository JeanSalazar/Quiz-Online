package com.example.jksa.quizonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;

public class DocenteActivity extends AppCompatActivity {

    private TextView tv_codigo_pregunta;
    private Spinner sp_tipo_pregunta;
    private EditText edt_respuesta_simple,edt_respuesta_simple_1,edt_respuesta_simple_2,edt_respuesta_simple_3;
    private LinearLayout lyt_seleccion_simple;
    private Switch switch1;
    private RadioGroup groupRadio;
    private RadioButton rb1,rb2,rb3;

    private int num_aleatorio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_codigo_pregunta = (TextView)findViewById(R.id.tv_codigo_pregunta);
        sp_tipo_pregunta = (Spinner)findViewById(R.id.sp_tipo_pregunta);
        switch1 = (Switch)findViewById(R.id.switch1);
        edt_respuesta_simple =(EditText)findViewById(R.id.edt_respuesta_simple);
        edt_respuesta_simple_1 =(EditText)findViewById(R.id.edt_respuesta_simple_1);
        edt_respuesta_simple_2 =(EditText)findViewById(R.id.edt_respuesta_simple_2);
        edt_respuesta_simple_3 =(EditText)findViewById(R.id.edt_respuesta_simple_3);
        rb1 = (RadioButton)findViewById(R.id.rb1);
        rb2 = (RadioButton)findViewById(R.id.rb2);
        rb3 = (RadioButton)findViewById(R.id.rb3);

        lyt_seleccion_simple = (LinearLayout)findViewById(R.id.lyt_seleccion_simple);
        groupRadio = (RadioGroup)findViewById(R.id.opciones_simple);

        tv_codigo_pregunta.setText(generarAleatorio()+"");
        sp_tipo_pregunta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        // Opcion Verdadero y Falso.
                        switch1.setVisibility(View.VISIBLE);
                        edt_respuesta_simple.setVisibility(View.GONE);
                        lyt_seleccion_simple.setVisibility(View.GONE);

                        break;
                    case 1:
                        // Opcion Respuesta Simple.
                        switch1.setVisibility(View.GONE);
                        edt_respuesta_simple.setVisibility(View.VISIBLE);
                        lyt_seleccion_simple.setVisibility(View.GONE);

                        break;
                    case 2:
                        // Opcion Seleccion Simple.
                        switch1.setVisibility(View.GONE);
                        edt_respuesta_simple.setVisibility(View.GONE);
                        lyt_seleccion_simple.setVisibility(View.VISIBLE);

                        groupRadio.addView(rb1);
                        groupRadio.addView(rb2);
                        groupRadio.addView(rb3);

                        break;
                    case 3:
                        // Opcion Seleccion Multiple.
                        switch1.setVisibility(View.GONE);
                        edt_respuesta_simple.setVisibility(View.GONE);
                        lyt_seleccion_simple.setVisibility(View.VISIBLE);

                        groupRadio.removeAllViews();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private int generarAleatorio(){
        Random aleatorio = new Random(System.currentTimeMillis());

        num_aleatorio = (int) Math.floor(Math.random()*(1000-9999+1)+9999);  // Valor entre M y N, ambos incluidos.
        return num_aleatorio;
    }


    public void compartirCodigo(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, num_aleatorio);
        startActivity(Intent.createChooser(intent, "Compartir codigo con"));
    }


    public void onClickAyudaPregunta(View view){
        mostrarDialogoAyudaPregunta();
    }


    private void mostrarDialogoAyudaPregunta(){
        // Se crea un objeto temporal que representa el dialogo.
        new MaterialDialog.Builder(this)
                .title("Ayuda")
                .content("Aqui usted debe de escribir la pregunta, esta sera mostrada a los Alumnos")
                .titleColorRes(R.color.red_btn_bg_color)
                .positiveColorRes(R.color.colorPrimary)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    public void onClickAyudaRespuesta(View view){
        mostrarDialogoAyudaRespuesta();
    }


    private void mostrarDialogoAyudaRespuesta(){
        // Se crea un objeto temporal que representa el dialogo.
        new MaterialDialog.Builder(this)
                .title("Ayuda")
                .content("Usted de acuerdo al tipo de pregunta escogido, deberia de poner las posibles respuestas ademas de indicar cual es la respuesta")
                .titleColorRes(R.color.red_btn_bg_color)
                .positiveColorRes(R.color.colorPrimary)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
