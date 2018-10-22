package br.bruno.appdev.tasksfamily.View;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.bruno.appdev.tasksfamily.Entities.TarefaPendente;
import br.bruno.appdev.tasksfamily.Entities.Tarefas;
import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.R;

public class AtividadesPendentesAdapter extends RecyclerView.Adapter<AtividadesPendentesAdapter.AtividadesPendentesHolder> {

    private List<TarefaPendente> tarefasPendenter = TarefaDataStore.sharedInstance().getTodasPendentes();

    @NonNull
    @Override
    public AtividadesPendentesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycleview_page_init, viewGroup, false);

        return new AtividadesPendentesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadesPendentesHolder holder, final int position) {
        TarefaPendente task = tarefasPendenter.get(position);

        holder.txtMainLstAtvTitulo.setText(task.getTitulo());
        holder.edtMainLstAtvDescricao.setText(task.getDescricao());

        if(task.isTarefaValidada()){
            holder.imgMainLstTarefaNaoFeita.setVisibility(View.INVISIBLE);
            holder.imgMainLstTarefaFeita.setEnabled(false);
            if(task.isTarefaAprovada()) {
                holder.imgMainLstTarefaFeita.setImageResource(R.drawable.check_symbol);
            }else{
                holder.imgMainLstTarefaFeita.setImageResource(R.drawable.close_button);
            }
        }

        if(task.getGrauParentesco().equals("Filha")) {
            holder.imgMainLstUserTarefa.setImageResource(R.drawable.girl);
        }else if(task.getGrauParentesco().equals("Filho")) {
            holder.imgMainLstUserTarefa.setImageResource(R.drawable.boy);
        }else if(task.getGrauParentesco().equals("Pai")) {
            holder.imgMainLstUserTarefa.setImageResource(R.drawable.man);
        }else if(task.getGrauParentesco().equals("Mae")) {
            holder.imgMainLstUserTarefa.setImageResource(R.drawable.girl2);
        }
        holder.imgMainLstTarefaFeita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = TarefaDataStore.sharedInstance().getContext();
                AlertDialog.Builder message = new AlertDialog.Builder(context);
                message.setTitle("Tarefa está aprovada?");
                message.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean atualizou = TarefaDataStore.sharedInstance().aprovarTarefa(position, true);

                        if(atualizou){
                            Toast.makeText(TarefaDataStore.sharedInstance().getContext(),"Tarefa aprovada com sucesso!", Toast.LENGTH_LONG).show();
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

        holder.imgMainLstTarefaNaoFeita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = TarefaDataStore.sharedInstance().getContext();
                AlertDialog.Builder message = new AlertDialog.Builder(context);
                message.setTitle("Tem certeza que quer reprovada está tarefa?");
                message.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean atualizou = TarefaDataStore.sharedInstance().aprovarTarefa(position, false);

                        if(atualizou){
                            Toast.makeText(TarefaDataStore.sharedInstance().getContext(),"A tarefa foi reprovada com sucesso!", Toast.LENGTH_LONG).show();
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
        return tarefasPendenter != null ? tarefasPendenter.size() : 0;
    }

    public class AtividadesPendentesHolder extends RecyclerView.ViewHolder {

        ImageView imgMainLstUserTarefa;
        TextView txtMainLstAtvTitulo;
        EditText edtMainLstAtvDescricao;
        ImageView imgMainLstTarefaFeita;
        ImageView imgMainLstTarefaNaoFeita;

        public AtividadesPendentesHolder(@NonNull View itemView) {
            super(itemView);

            imgMainLstUserTarefa = itemView.findViewById(R.id.imgMainLstUserTarefa);
            txtMainLstAtvTitulo = itemView.findViewById(R.id.txtMainLstAtvTitulo);
            edtMainLstAtvDescricao = itemView.findViewById(R.id.edtMainLstAtvDescricao);
            imgMainLstTarefaFeita = itemView.findViewById(R.id.imgMainLstTarefaFeita);
            imgMainLstTarefaNaoFeita = itemView.findViewById(R.id.imgMainLstTarefaNaoFeita);
        }
    }
}
