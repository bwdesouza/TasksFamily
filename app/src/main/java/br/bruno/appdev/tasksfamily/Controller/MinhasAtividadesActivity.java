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
    private GestureDetector gestureDetector;
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

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void OnClickBtnTarefaFeita(View view){

        AlertDialog.Builder message = new AlertDialog.Builder(this);
        message.setTitle("Concluir está tarefa?");
        message.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                TarefaDataStore.sharedInstance();
                adapter.notifyDataSetChanged();
            }
        });
        message.setNegativeButton("Não", null);
        message.show();
    }

    public void OnClickBtnRejeitarTarefa(View view){
        String b = "";
    }
}


/*



        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                return (view != null && gestureDetector.onTouchEvent(motionEvent));
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent e) {

//                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                int position = recyclerView.getChildAdapterPosition(view);
//                TarefaDataStore.sharedInstance().removeCity(position);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerView.getChildAdapterPosition(view);

                Tarefas tarefa = TarefaDataStore.sharedInstance().getTarefa(position);
                long eventID = tarefa.getEventID();

                Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(uri);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {

//                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                int position = recyclerView.getChildAdapterPosition(view);
//
//                Intent intent = new Intent(MainActivity.this, AddEditCity.class);
//                intent.putExtra("position", position);
//                startActivity(intent);

                return true;
            }
        });

*/
