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

import br.bruno.appdev.tasksfamily.Entities.TarefaPendente;
import br.bruno.appdev.tasksfamily.Entities.Tarefas;
import br.bruno.appdev.tasksfamily.Entities.Usuarios;

public class TarefaDataStore {

    private FirebaseAuth autenticacao;
    FirebaseUser usuarioFirebase;

    private static TarefaDataStore instance = null;

    private List<Tarefas> tarefas = new ArrayList<>();
    private List<TarefaPendente> tarefasPendentes = new ArrayList<>();
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

    public TarefaPendente getTarefaPendente(int position) {
        if (tarefasPendentes.size() == 0){
            return null;
        }

        TarefaPendente ct = tarefasPendentes.get(position);

        return ct;
    }

    public Tarefas getPesquisaTarefa(long idTarefa) {
        Tarefas tarefasPesquisa = new Tarefas();

        for (Tarefas taref : tarefas){
            if(taref.getEventID() == idTarefa ){
                tarefasPesquisa = taref;
                return tarefasPesquisa;
            }
        }

        return tarefasPesquisa;
    }

    public List<Tarefas> getAll() {
        return tarefas;
    }

    public List<Tarefas> getAllMinhasTarefas() {
        return minhasTarefas(tarefas);
    }


    public List<TarefaPendente> getTodasPendentes() {
        return processaTarefasPendentes(tarefas);
    }

    public List<TarefaPendente> processaTarefasPendentes(List<Tarefas> tarefasPends){
        tarefasPendentes = new ArrayList<>();
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        usuarioFirebase = autenticacao.getCurrentUser();

        String email = usuarioFirebase.getEmail().toString();

        for (Tarefas taref : tarefasPends){
            if(taref.isTarefaConcluida()){
                if(taref.getEmailCriador().equals(email)) {
                    tarefasPendentes.add(retornaObjetoTarefPend(taref));
                }
            }
        }

        return tarefasPendentes;
    }


    public List<Tarefas> minhasTarefas(List<Tarefas> minhasTarefas){
        List<Tarefas> minhas = new ArrayList<>();
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        usuarioFirebase = autenticacao.getCurrentUser();

        String email = usuarioFirebase.getEmail().toString();

        for (Tarefas taref : minhasTarefas){
            if(!taref.isTarefaConcluida()){
                if(taref.getEmailsDestinatarios().equals(email)) {
                    minhas.add(taref);
                }
            }
        }

        return minhas;
    }


    public TarefaPendente retornaObjetoTarefPend(Tarefas tar){
        TarefaPendente tp = new TarefaPendente();

        tp.setEventID(tar.getEventID());
        tp.setTitulo(tar.getTitulo());
        tp.setDescricao(tar.getDescricao());
        tp.setEmailCriador(tar.getEmailCriador());
        tp.setEmailsDestinatarios(tar.getEmailsDestinatarios());
        tp.setGrauParentesco(tar.getGrauParentesco());
        tp.setTarefaConcluida(tar.isTarefaConcluida());
        tp.setTarefaFeita(tar.isTarefaFeita());
        tp.setTarefaValidada(tar.isTarefaValidada());

        return tp;
    }

    public boolean atualizarTarefa(int position, boolean concluida){
        try {
            List<Tarefas> minhas = minhasTarefas(tarefas);
            Tarefas trf = minhas.get(position);
            // = getTarefa(position);

            autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
            usuarioFirebase = autenticacao.getCurrentUser();
            DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

            String email = usuarioFirebase.getEmail().toString();

            Usuarios usLog = UsuarioDataStore.sharedInstance().getUsuarios(email);

            raiz.child("Tasks").child(usLog.getGuidTarefa()).child(String.valueOf(trf.getEventID())).child("tarefaConcluida").setValue(true);

            raiz.child("Tasks").child(usLog.getGuidTarefa()).child(String.valueOf(trf.getEventID())).child("tarefaFeita").setValue(concluida);

            raiz.child("Tasks").child(usLog.getGuidTarefa()).child(String.valueOf(trf.getEventID())).child("tarefaValidada").setValue(false);

            trf.setTarefaConcluida(true);
            trf.setTarefaFeita(concluida);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean aprovarTarefa(int position, boolean concluida){
        try {
            List<TarefaPendente> trfPen = getTodasPendentes();
            TarefaPendente trf = trfPen.get(position);

            autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
            usuarioFirebase = autenticacao.getCurrentUser();
            DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

            String email = usuarioFirebase.getEmail().toString();

            Usuarios usLog = UsuarioDataStore.sharedInstance().getUsuarios(email);

            Tarefas tf = getPesquisaTarefa(trf.getEventID());
            if(!concluida){
                raiz.child("Tasks").child(usLog.getGuidTarefa()).child(String.valueOf(trf.getEventID())).child("tarefaConcluida").setValue(false);

                raiz.child("Tasks").child(usLog.getGuidTarefa()).child(String.valueOf(trf.getEventID())).child("tarefaFeita").setValue(false);

                tf.setTarefaAprovada(false);
                tf.setTarefaValidada(false);
                tf.setTarefaFeita(false);
                tf.setTarefaConcluida(false);
                trf.setTarefaAprovada(false);
                trf.setTarefaValidada(false);
                trf.setTarefaFeita(false);
                trf.setTarefaConcluida(false);
            }
            else {
                raiz.child("Tasks").child(usLog.getGuidTarefa()).child(String.valueOf(trf.getEventID())).child("tarefaAprovada").setValue(true);

                raiz.child("Tasks").child(usLog.getGuidTarefa()).child(String.valueOf(trf.getEventID())).child("tarefaValidada").setValue(true);

                tf.setTarefaAprovada(true);
                tf.setTarefaValidada(true);
                trf.setTarefaAprovada(true);
                trf.setTarefaValidada(true);
            }


            processaTarefasPendentes(tarefas);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void carregaTarefas(){

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        usuarioFirebase = autenticacao.getCurrentUser();
        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();

        String email = usuarioFirebase.getEmail().toString();

        Usuarios usLog = UsuarioDataStore.sharedInstance().getUsuarios(email);

        if(usLog == null){
            return;
        }
        Query tarefasUsuario = raiz.child("Tasks").child(usLog.getGuidTarefa());

        tarefasUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tarefas = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Tarefas taref = postSnapshot.getValue(Tarefas.class);
                    tarefas.add(taref);
                }
                processaTarefasPendentes(tarefas);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*
        if(!usLog.getIdFilha().equals("")){

        }else if(!usLog.getIdFilho().equals("")){

        }else if(!usLog.getIdMae().equals("")){

        }else if(!usLog.getIdPai().equals("")){

        }

        Query tarefasUsuario = raiz.child("Tasks").child(email.split("@")[0])
                .orderByChild("emailCriador")
                .equalTo(email);
                */

    }
}
