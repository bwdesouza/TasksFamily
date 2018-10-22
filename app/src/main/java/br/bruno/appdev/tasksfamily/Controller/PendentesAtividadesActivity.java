package br.bruno.appdev.tasksfamily.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.widget.EditText;
import android.widget.TextView;

import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.R;
import br.bruno.appdev.tasksfamily.View.AtividadesPendentesAdapter;

public class PendentesAtividadesActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterPendente;
    private GestureDetector gestureDetector;
    private TextView txtMainLstAtvTitulo;
    private EditText edtMainLstAtvDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendentes_atividades);

        recyclerView = findViewById(R.id.lstAtividadesAprovacao);
        txtMainLstAtvTitulo = findViewById(R.id.txtMainLstAtvTitulo);
        edtMainLstAtvDescricao = findViewById(R.id.edtMainLstAtvDescricao);

        adapterPendente = new AtividadesPendentesAdapter();
        recyclerView.setAdapter(adapterPendente);
        TarefaDataStore.sharedInstance().setContext(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        /*
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);*/

    }
}
