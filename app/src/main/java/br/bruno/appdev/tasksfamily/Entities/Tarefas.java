package br.bruno.appdev.tasksfamily.Entities;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;

public class Tarefas {

    private long eventID;
    private String Titulo;
    private String Descricao;
    private List<String> EmailsDestinatarios;
    private String EmailCriador;
    private boolean TarefaAceita;

    public Tarefas() {
    }

    public void SalvarTask(){
        DatabaseReference referencia = ConfiguracaoFireBase.getFirebase();
        referencia.child("Tasks").child(String.valueOf(getEventID())).setValue(this);
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

    public List<String> getEmailsDestinatarios() {
        return EmailsDestinatarios;
    }

    public void setEmailsDestinatarios(List<String> emailsDestinatarios) {
        EmailsDestinatarios = emailsDestinatarios;
    }

    public String getEmailCriador() {
        return EmailCriador;
    }

    public void setEmailCriador(String emailCriador) {
        EmailCriador = emailCriador;
    }

    public boolean isTarefaAceita() {
        return TarefaAceita;
    }

    public void setTarefaAceita(boolean tarefaAceita) {
        TarefaAceita = tarefaAceita;
    }
}
