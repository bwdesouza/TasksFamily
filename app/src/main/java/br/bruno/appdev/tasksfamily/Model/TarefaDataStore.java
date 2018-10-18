package br.bruno.appdev.tasksfamily.Model;

import android.content.Context;
import android.database.Cursor;
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


    public Tarefas getTarefa(int position) {
        Tarefas ct = tarefas.get(position);
        return ct;
    }

    public List<Tarefas> getAll() {

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        usuarioFirebase = autenticacao.getCurrentUser();
        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

        Query tarefasUsuario = raiz.child("Tasks")
                .orderByChild("emailCriador")
                .equalTo(usuarioFirebase.getEmail().toString());

        tarefasUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Tarefas tare = postSnapshot.getValue(Tarefas.class);
                    tarefas.add(tare);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return tarefas;
    }

}
