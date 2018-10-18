package br.bruno.appdev.tasksfamily.Controller;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import br.bruno.appdev.tasksfamily.Entities.Tarefas;
import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.R;
import br.bruno.appdev.tasksfamily.View.AtividadesAdapter;

public class MinhasAtividadesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_atividades);

        recyclerView = findViewById(R.id.lstAtividades);

        adapter = new AtividadesAdapter();
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

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
    }

    public void OnClickBtnTarefaFeita(View view){
        String b = "";
    }

    public void OnClickBtnRejeitarTarefa(View view){
        String b = "";
    }
}
