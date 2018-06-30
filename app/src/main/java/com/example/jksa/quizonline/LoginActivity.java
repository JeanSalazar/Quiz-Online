package com.example.jksa.quizonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class LoginActivity extends AppCompatActivity {


    private EditText edt_email,edt_contra;
    private ImageView img_escudo;
    private CheckBox cb_recordar;
    private Button btn_ingresar;

    private MaterialDialog materialDialog;      // Objeto que representa el cuadro de Dialogo

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instanciammos los controles de la Actividad.
        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_contra = (EditText)findViewById(R.id.edt_contra);
        img_escudo = (ImageView)findViewById(R.id.img_escudo);
        btn_ingresar = (Button)findViewById(R.id.btn_ingresar);
        cb_recordar = (CheckBox)findViewById(R.id.cb_recordar);
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);

        //signInButton.setColorScheme(SignInButton.COLOR_DARK);
        //signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresarGoogle();
            }
        });

        // Efecto Splash al cargar el Login solo para el icono.
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        img_escudo.startAnimation(animation);

        // Con el siguiente metodo verficamos si hay credenciales guardadas.
        leerCredenciales();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,null)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener != null){
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    // Este metodo es el encargado para validar el acceso de los Usuarios a la Aplicacion.
    public void onClickIngresar(View view) throws InterruptedException {
        // Entonces verificamos si los campos ingresados estan llenos.
        if(verificarCamposLlenos()){
            ingresar();
        }
    }


    private void ingresar(){
        if(isValidEmail(edt_email.getText().toString()) && validarContraseña()){
            mostrarDialogoCargar();

            String email = edt_email.getText().toString();
            String password = edt_contra.getText().toString();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information.
                                //ponerEnLinea();
                                Toast.makeText(LoginActivity.this,"Se ingreso correctamente",Toast.LENGTH_SHORT).show();
                                materialDialog.dismiss();   //Detenemos el dialogo de carga
                                //limpiarControles();

                                // Abrimos la otra actividad.
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                //finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this,"Error credenciales incorrectas",Toast.LENGTH_SHORT).show();
                                materialDialog.dismiss();   //Detenemos el dialogo de carga
                            }
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this,"Los datos ingresados son incorrectos",Toast.LENGTH_SHORT).show();
        }
    }


    public void ingresarGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    //private GoogleSignInClient mGoogleSignInClient;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                mostrarDialogoCargar();
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            if(user != null){
                                Toast.makeText(LoginActivity.this,user.getDisplayName(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this,user.getEmail(),Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this,user.getPhoneNumber(),Toast.LENGTH_SHORT).show();
                            }

                            materialDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "No se puede autentificar con Google...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public final static boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public boolean validarContraseña(){
        return !(edt_contra.getText().toString()).isEmpty();
    }


    // Metodo del evento del checbox para guardar las credenciales.
    public void onClickCredenciales(View view){
        if(cb_recordar.isChecked() && verificarCamposLlenos()){
            guardarCredenciales(edt_email.getText().toString(),edt_contra.getText().toString());
        }else{
            guardarCredenciales("","");
            cb_recordar.setChecked(false);
        }
    }


    // Metodo para guardar el usuario y password en concreto.
    private void guardarCredenciales(String email,String contra){
        SharedPreferences sharedPreferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USUARIO",email);
        editor.putString("PASSWORD",contra);
        editor.commit();
    }


    private void leerCredenciales(){
        SharedPreferences  sharedPreferences = getPreferences(getApplicationContext().MODE_PRIVATE);
        String USUARIO = sharedPreferences.getString("USUARIO","");
        String PASSWORD = sharedPreferences.getString("PASSWORD","");
        edt_email.setText(USUARIO);
        edt_contra.setText(PASSWORD);
        if(USUARIO.length() != 0 && PASSWORD.length()!=0)
            cb_recordar.setChecked(true);
    }


    private boolean verificarCamposLlenos(){
        if(edt_email.length()==0 || edt_contra.length()==0) {
            Toast.makeText(getApplicationContext(), "Tienes que llenar todos los datos, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void mostrarDialogoCargar(){
        // Mostramos un Dialogo de Carga en la Actividad.
        materialDialog = new MaterialDialog.Builder(this)
                .title("Validando datos")
                .content("Por favor espere")
                .progress(true, 0)
                .contentGravity(GravityEnum.CENTER)
                .widgetColorRes(R.color.colorPrimary)
                .show();
        materialDialog.setCancelable(false);
        materialDialog.setCanceledOnTouchOutside(false);
    }


    public void onClickRegistro(View view){
        Intent intent = new Intent(this,RegistroActivity.class);
        startActivity(intent);
        //limpiarControles();
    }


    public void onClickOlvidoAcceso(View view){
        mostrarDialogoOlvido();
    }


    public void ponerEnLinea() {
        // Ponemos el URL en la Base de Datos
        FirebaseUser currentUser = auth.getCurrentUser();
        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
        reference.child("flag_en_linea").setValue(true);
    }


    // Este metodo limpia los controles de la Actividad del Logim.
    private void limpiarControles(){
        edt_email.setText("");
        edt_contra.setText("");
        cb_recordar.setChecked(false);
    }


    // Este metodo muestra un Cuadro de Dialogo para enviar un correo electronico de recuperacion de la Contraseña.
    private void mostrarDialogoOlvido(){
        //Se crea un objeto temporal que representa el dialogo
        new MaterialDialog.Builder(this)
                .title("No tienes Acceso?")
                .content("Ingresa tu Correo Electronico y si los datos son correctos se enviara un email para recuperar el Acceso")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .titleColorRes(R.color.red_btn_bg_color)
                .positiveColorRes(R.color.colorPrimary)
                .widgetColorRes(R.color.colorPrimary)
                .input("Correo Electronico", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Aqui deberiamos de IMPLEMENTAR la recuperacion por medio de un correo electronico.


                        // Temporalmente mostraremos el Correo ingresado.
                        String correo = dialog.getInputEditText().getText().toString();
                        Toast.makeText(LoginActivity.this,correo,Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}
