package br.bruno.appdev.tasksfamily.Model;

import android.content.Context;

import java.util.List;

import br.bruno.appdev.tasksfamily.Entities.Tarefas;

public class TarefaDataStore {

    private static TarefaDataStore instance = null;

    private List<Tarefas> tarefas;
    private Context context;

    protected TarefaDataStore() {}

    public static TarefaDataStore sharedInstance() {

        if (instance == null)
            instance = new TarefaDataStore();

        return instance;
    }

}
