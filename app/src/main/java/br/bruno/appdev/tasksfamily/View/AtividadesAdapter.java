package br.bruno.appdev.tasksfamily.View;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;
import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.R;

public class AtividadesAdapter extends RecyclerView.Adapter<AtividadesAdapter.AtividadeHolder> {

    private List<Tarefas> tarefas = TarefaDataStore.sharedInstance().getAll();

    @NonNull
    @Override
    public AtividadeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycleview_atividades, viewGroup, false);

        return new AtividadeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeHolder holder, int position) {
        Tarefas task = tarefas.get(position);

        holder.txtLstAtvTitulo.setText(task.getTitulo());
        holder.edtLstAtvDescricao.setText(task.getDescricao());
    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    public class AtividadeHolder extends RecyclerView.ViewHolder {

//        ImageView imgUserTarefa;
        TextView txtLstAtvTitulo;
        EditText edtLstAtvDescricao;

        public AtividadeHolder(@NonNull View itemView) {
            super(itemView);

//            imgUserTarefa = itemView.findViewById(R.id.imgUserTarefa);
            txtLstAtvTitulo = itemView.findViewById(R.id.txtLstAtvTitulo);
            edtLstAtvDescricao = itemView.findViewById(R.id.edtLstAtvDescricao);
        }
    }
}
