package br.bruno.appdev.tasksfamily.Model;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

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

import br.bruno.appdev.tasksfamily.Entities.Tarefas;

public class TarefaDataStore {

    private FirebaseAuth autenticacao;
    FirebaseUser usuarioFirebase;

    private static TarefaDataStore instance = null;

    private List<Tarefas> tarefas = new ArrayList<>();
    private Context context;

    protected TarefaDataStore() {}

    public static TarefaDataStore sharedInstance() {

        if (instance == null)
            instance = new TarefaDataStore();

        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public Tarefas getTarefa(int position) {
        Tarefas ct = tarefas.get(position);
        return ct;
    }

    public List<Tarefas> getAll() {
        return tarefas;
    }

    public boolean atualizarTarefa(int position, boolean concluida){
        try {
            Tarefas trf = getTarefa(position);

            autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
            usuarioFirebase = autenticacao.getCurrentUser();
            DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

            String email = usuarioFirebase.getEmail().toString();

            raiz.child("Tasks").child(email.split("@")[0]).child(String.valueOf(trf.getEventID())).child("tarefaFeita").setValue(concluida);

            raiz.child("Tasks").child(email.split("@")[0]).child(String.valueOf(trf.getEventID())).child("tarefaConcluida").setValue(true);

            trf.setTarefaConcluida(true);
            trf.setTarefaFeita(concluida);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void carregaTarefas(){
        tarefas = new ArrayList<>();

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        usuarioFirebase = autenticacao.getCurrentUser();
        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

        String email = usuarioFirebase.getEmail().toString();

        Query tarefasUsuario = raiz.child("Tasks").child(email.split("@")[0])
                .orderByChild("emailCriador")
                .equalTo(email);

        tarefasUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Tarefas taref = postSnapshot.getValue(Tarefas.class);
                    tarefas.add(taref);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
