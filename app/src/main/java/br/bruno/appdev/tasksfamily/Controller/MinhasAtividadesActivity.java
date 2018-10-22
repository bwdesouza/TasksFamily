package br.bruno.appdev.tasksfamily.Controller;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.bruno.appdev.tasksfamily.Entities.Tarefas;
import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.R;
import br.bruno.appdev.tasksfamily.View.AtividadesAdapter;

public class MinhasAtividadesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView txtLstAtvTitulo;
    private EditText edtLstAtvDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_atividades);

        recyclerView = findViewById(R.id.lstAtividades);
        txtLstAtvTitulo = findViewById(R.id.txtLstAtvTitulo);
        edtLstAtvDescricao = findViewById(R.id.edtLstAtvDescricao);

        adapter = new AtividadesAdapter();
        recyclerView.setAdapter(adapter);
        TarefaDataStore.sharedInstance().setContext(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}