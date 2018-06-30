package com.example.jksa.quizonline;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jksa.quizonline.Clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegistroActivity extends AppCompatActivity {

    private EditText edt_registro_fec_nac,edt_registro_nombre,edt_registro_apellidos,edt_registro_correo,edt_registro_contra,edt_registro_contra_repe;
    private Spinner sp_genero,sp_rol;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        edt_registro_fec_nac = (EditText)findViewById(R.id.edt_registro_fec_nac);
        edt_registro_nombre = (EditText)findViewById(R.id.edt_registro_nombre);
        edt_registro_apellidos = (EditText)findViewById(R.id.edt_registro_apellidos);
        edt_registro_correo = (EditText)findViewById(R.id.edt_registro_correo);
        edt_registro_contra = (EditText)findViewById(R.id.edt_registro_contra);
        edt_registro_contra_repe = (EditText)findViewById(R.id.edt_registro_contra_repe);
        sp_genero = (Spinner)findViewById(R.id.sp_genero);
        sp_rol = (Spinner)findViewById(R.id.sp_rol);

        sp_genero.setSelection(0);
        sp_rol.setSelection(0);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }


    public void onClickMostrarCalendario(View view){
        showDatePickerDialog();
    }


    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }


    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroActivity.this, R.style.Theme_AppCompat_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                edt_registro_fec_nac.setText(selectedDate);
            }
        }, year, month, day);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-18);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }


    public void onClickSalir(View view){
        finish();
    }


    public void onClickRegistrar(View view){
        // Verificamos que todos los controles esten llenos.
        if(verificarControles()){
            registro();
        }else{
            Toast.makeText(getApplicationContext(),"Los datos Ingresados son incorrectos, vuelva a intentarlo",Toast.LENGTH_SHORT).show();
        }
    }


    private boolean verificarControles(){
        if(!isValidEmail(edt_registro_correo.getText().toString())){
            return false;
        }else if(edt_registro_nombre.getText().toString().isEmpty()){
            return false;
        }else if(edt_registro_apellidos.getText().toString().isEmpty()){
            return false;
        }else if(!validarContrasena()){
            return false;
        }else if(edt_registro_fec_nac.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }


    private void registro() {
        final String email = edt_registro_correo.getText().toString();
        final String contrasena = edt_registro_contra.getText().toString();
        final String nombre = edt_registro_nombre.getText().toString();
        final String apellidos = edt_registro_apellidos.getText().toString();
        final String rol = sp_rol.getSelectedItem().toString();
        final String genero = sp_genero.getSelectedItem().toString();
        final String fec_nac = edt_registro_fec_nac.getText().toString();

        auth.createUserWithEmailAndPassword(email, contrasena)
                .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Usuario u = new Usuario();
                            u.setCorreo(email);
                            u.setNombre(nombre);
                            u.setGenero(genero);
                            u.setRol(rol);
                            u.setApellidos(apellidos);
                            u.setFecha_nac(fec_nac);

                            FirebaseUser currentUser = auth.getCurrentUser();
                            DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
                            reference.setValue(u);

                            //Toast.makeText(RegistroActivity.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                            mostrarDialogoRegistro(email,contrasena);
                            limpiarControles();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistroActivity.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void mostrarDialogoRegistro(final String correo, final String password) {
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCredenciales(correo,password);
                finish();
            }
        };

        Snackbar.make(findViewById(android.R.id.content), "Usted se registro correctamente", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.GREEN)
                    .setAction("Ingresar", mOnClickListener).show();
    }


    // Metodo para guardar el usuario y password en concreto.
    private void guardarCredenciales(String email,String contra){
        SharedPreferences sharedPreferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USUARIO",email);
        editor.putString("PASSWORD",contra);
        editor.commit();
    }


    public final static boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validarContrasena(){
        String contraseña,contrasenRepetida;
        contraseña = edt_registro_contra.getText().toString();
        contrasenRepetida= edt_registro_contra_repe.getText().toString();

        if(contraseña.equals(contrasenRepetida)){
            if(contraseña.length() >= 6 && contraseña.length() <= 30){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }


    private void limpiarControles(){
        edt_registro_nombre.setText("");
        edt_registro_apellidos.setText("");
        edt_registro_correo.setText("");
        edt_registro_contra.setText("");
        edt_registro_contra_repe.setText("");
        edt_registro_fec_nac.setText("");
        sp_rol.setSelection(0);
        sp_genero.setSelection(0);
    }
}
