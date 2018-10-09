package br.bruno.appdev.tasksfamily.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.bruno.appdev.tasksfamily.Entities.Usuarios;
import br.bruno.appdev.tasksfamily.Helper.Base64Custom;
import br.bruno.appdev.tasksfamily.Helper.Preferencias;
import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;
import br.bruno.appdev.tasksfamily.R;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText edtCadNome;
    private EditText edtCadSobrenome;
    private EditText edtCadAniversario;
    private EditText edtCadEmail;
    private EditText edtCadSenha;
    private EditText edtCadConfSenha;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;
    private Usuarios usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        edtCadNome = findViewById(R.id.edtCadNome);
        edtCadSobrenome = findViewById(R.id.edtCadSobrenome);
        edtCadAniversario = findViewById(R.id.edtCadAniversario);
        edtCadEmail = findViewById(R.id.edtCadEmail);
        edtCadSenha = findViewById(R.id.edtCadSenha);
        edtCadConfSenha = findViewById(R.id.edtCadConfSenha);
        rbMasculino = findViewById(R.id.rbCadMasculino);
        rbFeminino = findViewById(R.id.rbCadFeminino);
    }


    public void OnClickBtnCadCancelar(View view){
        finish();
    }

    public void OnClickBtnCadSalvar(View view){
        if(edtCadSenha.getText().toString().equals(edtCadConfSenha.getText().toString())){
            usuario = new Usuarios();
            usuario.setNome(edtCadNome.getText().toString());
            usuario.setSobrenome(edtCadSobrenome.getText().toString());
            usuario.setAniversario(edtCadAniversario.getText().toString());
            usuario.setEmail(edtCadEmail.getText().toString());
            usuario.setSenha(edtCadSenha.getText().toString());

            if(rbFeminino.isChecked()){
                usuario.setSexo("Feminino");
            }else{
                usuario.setSexo("Masculino");
            }

            CadastrarUsuario();
        }else{
            Toast.makeText(this,"As senhas não são correspondentes!", Toast.LENGTH_LONG).show();
        }

    }

    private void CadastrarUsuario(){
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this,"Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(identificadorUsuario);
                    usuario.Salvar();

                    Preferencias preferencia = new Preferencias(CadastroUsuarioActivity.this);
                    preferencia.SalvarUsuarioPreferenciar(identificadorUsuario, usuario.getNome());

                    finish();
                }else {
                    String erroExcessao = "";

                    try{

                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcessao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres contendo letras e números!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcessao = "O e-mail digitado é inválida, por favor digite outro e-mail!";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcessao = "Esse e-mail já está cadastrado no sistema, por favor informe outro e-mail!";
                    }catch (Exception e){
                        erroExcessao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this,"Erro: " + erroExcessao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    public void AbrirLoginUsuario(){
//        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
//        startActivity(intent);
//
//
//    }
}
