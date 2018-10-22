package br.bruno.appdev.tasksfamily.View;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import br.bruno.appdev.tasksfamily.Controller.LoginActivity;
import br.bruno.appdev.tasksfamily.Controller.MinhasAtividadesActivity;
import br.bruno.appdev.tasksfamily.Entities.Tarefas;
import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;
import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.R;

public class AtividadesAdapter extends RecyclerView.Adapter<AtividadesAdapter.AtividadeHolder> {

    private List<Tarefas> tarefas = TarefaDataStore.sharedInstance().getAllMinhasTarefas();

    @NonNull
    @Override
    public AtividadeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycleview_atividades, viewGroup, false);

        return new AtividadeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeHolder holder, final int position) {
        Tarefas task = tarefas.get(position);

        holder.txtLstAtvTitulo.setText(task.getTitulo());
        holder.edtLstAtvDescricao.setText(task.getDescricao());

        if(task.isTarefaConcluida()){
            holder.imgLstTarefaNaoFeita.setVisibility(View.INVISIBLE);
            holder.imgLstTarefaFeita.setEnabled(false);
            if(task.isTarefaFeita()) {
                holder.imgLstTarefaFeita.setImageResource(R.drawable.check_symbol);
            }else{
                holder.imgLstTarefaFeita.setImageResource(R.drawable.close_button);
            }
        }

        if(task.getGrauParentesco().equals("Filha")) {
            holder.imgLstUserTarefa.setImageResource(R.drawable.girl);
        }else if(task.getGrauParentesco().equals("Filho")) {
            holder.imgLstUserTarefa.setImageResource(R.drawable.boy);
        }else if(task.getGrauParentesco().equals("Pai")) {
            holder.imgLstUserTarefa.setImageResource(R.drawable.man);
        }else if(task.getGrauParentesco().equals("Mae")) {
            holder.imgLstUserTarefa.setImageResource(R.drawable.girl2);
        }

        holder.imgLstTarefaFeita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = TarefaDataStore.sharedInstance().getContext();
                AlertDialog.Builder message = new AlertDialog.Builder(context);
                    message.setTitle("Concluiu sua tarefa?");
                message.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean atualizou = TarefaDataStore.sharedInstance().atualizarTarefa(position, true);

                        if(atualizou){
                            Toast.makeText(TarefaDataStore.sharedInstance().getContext(),"Tarefa atualizada com sucesso!", Toast.LENGTH_LONG).show();
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(TarefaDataStore.sharedInstance().getContext(),"Ocorreu um erro ao alterar sua tarefa, por favor tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                message.setNegativeButton("Cancelar", null);
                message.show();
            }
        });

        holder.imgLstTarefaNaoFeita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = TarefaDataStore.sharedInstance().getContext();
                AlertDialog.Builder message = new AlertDialog.Builder(context);
                message.setTitle("Não finalizou a sua tarefa?");
                message.setPositiveButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean atualizou = TarefaDataStore.sharedInstance().atualizarTarefa(position, false);

                        if(atualizou){
                            Toast.makeText(TarefaDataStore.sharedInstance().getContext(),"Tarefa atualizada com sucesso!", Toast.LENGTH_LONG).show();
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(TarefaDataStore.sharedInstance().getContext(),"Ocorreu um erro ao alterar sua tarefa, por favor tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                message.setNegativeButton("Cancelar", null);
                message.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tarefas != null ? tarefas.size() : 0;
    }

    public class AtividadeHolder extends RecyclerView.ViewHolder {

        ImageView imgLstUserTarefa;
        TextView txtLstAtvTitulo;
        EditText edtLstAtvDescricao;
        ImageView imgLstTarefaFeita;
        ImageView imgLstTarefaNaoFeita;

        public AtividadeHolder(@NonNull View itemView) {
            super(itemView);

            imgLstUserTarefa = itemView.findViewById(R.id.imgLstUserTarefa);
            txtLstAtvTitulo = itemView.findViewById(R.id.txtLstAtvTitulo);
            edtLstAtvDescricao = itemView.findViewById(R.id.edtLstAtvDescricao);
            imgLstTarefaFeita = itemView.findViewById(R.id.imgLstTarefaFeita);
            imgLstTarefaNaoFeita = itemView.findViewById(R.id.imgLstTarefaNaoFeita);
        }
    }
}
