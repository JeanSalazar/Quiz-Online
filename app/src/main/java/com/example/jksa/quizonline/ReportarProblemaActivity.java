package com.example.jksa.quizonline;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jksa.quizonline.Clases.Mail;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class ReportarProblemaActivity extends AppCompatActivity {

    private EditText edt_contenido;
    private TextView tv_correo_de;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_problema);

        edt_contenido = (EditText)findViewById(R.id.edt_contenido);
        tv_correo_de = (TextView)findViewById(R.id.tv_de);
    }


    // Metodo que servira para enviar un email del problema encontrado.
    public void enviarCorreo(View view){
        if(!edt_contenido.getText().toString().isEmpty()){
            sendEmail();
        }else{
            Toast.makeText(getApplicationContext(),"Tienes que contarnos cual es el problema, escribelo!!!",Toast.LENGTH_SHORT).show();
        }
    }

    public void limpiarControles(){
        edt_contenido.setText("");
    }


    // Metodo que envia correos electronicos en segundo plano.
    private void sendEmail() {
        String[] recipients = { "jkristofersa@gmail.com" };
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.activity = this;
        email.m = new Mail("jeankristofersa@gmail.com","test2018.");
        email.m.set_from("jkristofersa@gmail.com");
        email.m.setBody(edt_contenido.getText().toString());
        email.m.set_to(recipients);
        email.m.set_subject("App Test Online UCSM: Problema detectado");
        email.execute();
    }


    // Metodo para mostrar un Toast.
    public void displayMessage(String message,int opc) {
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        };

        if(opc == 1){
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.GREEN)
                    .setAction("Reenviar", mOnClickListener).show();
        }else{
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}


// Clase que se ejecutara en segundo plano para enviar el Email.
class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    Mail m;
    ReportarProblemaActivity activity;

    public SendEmailAsyncTask() {
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (m.send()) {
                activity.displayMessage("Correo enviado correctamente.",0);
                activity.limpiarControles();
                Log.d("enviado","Correo enviado correctamente.");
            } else {
                activity.displayMessage("Correo no se pudo enviar.",1);
                Log.d("enviado","Correo no se pudo enviar.");
            }

            return true;
        } catch (AuthenticationFailedException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
            e.printStackTrace();
            activity.displayMessage("Authentication failed.",1);
            return false;
        } catch (MessagingException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
            e.printStackTrace();
            activity.displayMessage("Email failed to send.",1);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            activity.displayMessage("Unexpected error occured.",1);
            return false;
        }
    }
}