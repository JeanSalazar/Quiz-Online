package com.example.jksa.quizonline;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TITULO_INTERNET = "No tienes Internet?";
    private static final String TEXTO_INTERNET = "Debes de tener una conexion a Internet para poder usar la Aplicacion correctamente";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView img_imagen_activos = (ImageView)findViewById(R.id.img_logo_test_online);
        ImageView img_logo_ucsm = (ImageView)findViewById(R.id.img_logo_ucsm);
        ImageView img_logo_epis = (ImageView)findViewById(R.id.img_logo_epis);
        TextView tv_version = (TextView)findViewById(R.id.tv_version);

        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        img_imagen_activos.setAnimation(uptodown);
        img_logo_ucsm.setAnimation(downtoup);
        img_logo_epis.setAnimation(downtoup);
        tv_version.setAnimation(uptodown);

        // Hilo que permitira y validara el acceso a la Aplicacion
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                // LLamamos a un metodo para verificar si se puede acceder o no?
                verificarAcceso();

            }
        },1500);
    }

    // Metodo para verificar si dispone de conexion a internet.
    private boolean verificarAcceso(){
        if(isNetDisponible() && isOnlineNet()){
            // Pedir Permisos aqui.
            // ...

            //Abrimos la pantalla de LOGIN en la Aplicacion
            abrirLoginActivity();
        }else{
            // Explicamos que se debe de tener conexion a internet.
            mostrarDialogoInternet(TITULO_INTERNET,TEXTO_INTERNET);
        }
        return false;
    }


    // Metodo para abrir la pantalla de Login.
    private void abrirLoginActivity(){
        Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    // Metodo para verificar si la red esta habilitada.
    private boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }


    // Metodo para verificar si hay acceso a internet.
    public Boolean isOnlineNet() {
        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void mostrarDialogoInternet(String titulo, String texto){
        //Se crea un objeto temporal que representa el dialogo
        MaterialDialog obj = new MaterialDialog.Builder(this)
                .title(titulo)
                .content(texto)
                .titleColorRes(R.color.red_btn_bg_color)
                .positiveColorRes(R.color.colorPrimary)
                .positiveText("Aceptar")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(!verificarAcceso()){
                            finish();   // Cerramos la Aplicacion.
                        }
                    }
                })
                .show();
        obj.setCancelable(false);
        obj.setCanceledOnTouchOutside(false);
    }
}
