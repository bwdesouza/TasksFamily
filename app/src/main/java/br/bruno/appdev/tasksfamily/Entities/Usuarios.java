package br.bruno.appdev.tasksfamily.Entities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;

public class Usuarios {

    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private String sexo;
    private String aniversario;
    private Usuarios pai;
    private Usuarios mae;
    private List<Usuarios> filhos;

    public Usuarios() {
    }

    public void Salvar(){
        DatabaseReference referencia = ConfiguracaoFireBase.getFirebase();
        referencia.child("Users").child(String.valueOf(getId())).setValue(this);
    }

//    @Exclude
//
//    public Map<String, Object> toMap(){
//        HashMap<String, Object> hasMapUsuario = new HashMap<>();
//
//        hasMapUsuario.put("id", getId());
//        hasMapUsuario.put("nome", getNome());
//        hasMapUsuario.put("sobrenome", getSobrenome());
//        hasMapUsuario.put("aniversario", getAniversario());
//        hasMapUsuario.put("email", getEmail());
//        hasMapUsuario.put("senha", getSenha());
//        hasMapUsuario.put("sexo", getSexo());
//
//        return hasMapUsuario;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getAniversario() {
        return aniversario;
    }

    public void setAniversario(String aniversario) {
        this.aniversario = aniversario;
    }

    public Usuarios getPai() {
        return pai;
    }

    public void setPai(Usuarios pai) {
        this.pai = pai;
    }

    public Usuarios getMae() {
        return mae;
    }

    public void setMae(Usuarios mae) {
        this.mae = mae;
    }

    public List<Usuarios> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<Usuarios> filhos) {
        this.filhos = filhos;
    }
}
