package br.bruno.appdev.tasksfamily.Entities;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;
import br.bruno.appdev.tasksfamily.Model.UsuarioDataStore;

public class Tarefas {

    private long eventID;
    private String Titulo;
    private String Descricao;
    private String EmailsDestinatarios;
    private String EmailCriador;
    private String GrauParentesco;
    private boolean TarefaValidada;
    private boolean TarefaFeita;
    private boolean TarefaConcluida;
    private boolean TarefaAprovada;

    public Tarefas(){

    }

    public Tarefas(long eventID, String titulo, String descricao, String emailsDestinatarios,
                   String emailCriador, boolean tarefaValidada, String grauParentesco,
                   boolean tarefaFeita, boolean tarefaConcluida) {

        this.eventID = eventID;
        Titulo = titulo;
        Descricao = descricao;
        EmailsDestinatarios = emailsDestinatarios;
        EmailCriador = emailCriador;
        TarefaValidada = tarefaValidada;
        GrauParentesco = grauParentesco;
        TarefaFeita = tarefaFeita;
        TarefaConcluida = tarefaConcluida;
    }

    public void SalvarTask(){
        DatabaseReference referencia = ConfiguracaoFireBase.getFirebase();
        String email = getEmailCriador();
        Usuarios us = UsuarioDataStore.sharedInstance().getUsuarios(email);

        referencia.child("Tasks").child(us.getGuidTarefa()).child(String.valueOf(getEventID())).setValue(this);
    }


    public String getEmailsDestinatarios() {
        return EmailsDestinatarios;
    }

    public void setEmailsDestinatarios(String emailsDestinatarios) {
        EmailsDestinatarios = emailsDestinatarios;
    }

    public String getGrauParentesco() {
        return GrauParentesco;
    }

    public void setGrauParentesco(String grauParentesco) {
        GrauParentesco = grauParentesco;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getEmailCriador() {
        return EmailCriador;
    }

    public void setEmailCriador(String emailCriador) {
        EmailCriador = emailCriador;
    }

    public boolean isTarefaValidada() {
        return TarefaValidada;
    }

    public void setTarefaValidada(boolean tarefaValidada) {
        TarefaValidada = tarefaValidada;
    }

    public boolean isTarefaFeita() {
        return TarefaFeita;
    }

    public void setTarefaFeita(boolean tarefaFeita) {
        TarefaFeita = tarefaFeita;
    }

    public boolean isTarefaConcluida() {
        return TarefaConcluida;
    }

    public void setTarefaConcluida(boolean tarefaConcluida) {
        TarefaConcluida = tarefaConcluida;
    }

    public boolean isTarefaAprovada() {
        return TarefaAprovada;
    }

    public void setTarefaAprovada(boolean tarefaAprovada) {
        TarefaAprovada = tarefaAprovada;
    }
}
