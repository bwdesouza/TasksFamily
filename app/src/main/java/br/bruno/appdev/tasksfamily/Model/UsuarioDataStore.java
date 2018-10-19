package br.bruno.appdev.tasksfamily.Model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.bruno.appdev.tasksfamily.Entities.Usuarios;

public class UsuarioDataStore {

    private FirebaseAuth autenticacao;
    FirebaseUser usuarioFirebase;

    private static UsuarioDataStore instance = null;

    private List<Usuarios> usuarios = new ArrayList<>();
    private Context context;

    protected UsuarioDataStore() {}

    public static UsuarioDataStore sharedInstance() {

        if (instance == null)
            instance = new UsuarioDataStore();

        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public Usuarios getUsuarios(int position) {
        Usuarios us = usuarios.get(position);
        return us;
    }

    public Usuarios getUsuarios(String email) {
        Usuarios us = null;
        for (Usuarios usua : usuarios) {
            if(usua.getEmail().toString().equals(email)){
                return usua;
            }
        }
        return us;
    }

    public List<Usuarios> getAll() {
        return usuarios;
    }

    public void carregaUsuarios(){
        usuarios = new ArrayList<>();

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        usuarioFirebase = autenticacao.getCurrentUser();
        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

        String email = usuarioFirebase.getEmail().toString();
        String uid = usuarioFirebase.getUid();

        Query tarefasUsuario = raiz.child("Users");

        tarefasUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Usuarios taref = postSnapshot.getValue(Usuarios.class);
                    usuarios.add(taref);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void atualizarUsuario(Usuarios usuario){
        try {
            autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
            usuarioFirebase = autenticacao.getCurrentUser();
            DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

            raiz.child("Users").child(usuario.getId()).setValue(usuario);

        }catch (Exception e){
        }
    }
}
