package br.bruno.appdev.tasksfamily.Controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;
import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.Model.UsuarioDataStore;
import br.bruno.appdev.tasksfamily.R;
import br.bruno.appdev.tasksfamily.View.AtividadesAdapter;
import br.bruno.appdev.tasksfamily.View.AtividadesPendentesAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth firebase;
    FirebaseUser user;

    Dialog dialogPontuacaoAtual;
    TextView txt_nav_header_title;
    TextView txt_nav_header_subTitle;
    TextView txtPontuacaoTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyUserLogged();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialogPontuacaoAtual = new Dialog(this);

        /*
        txt_nav_header_title = findViewById(R.id.txt_nav_header_title);
        txt_nav_header_subTitle = findViewById(R.id.txt_nav_header_subTitle);

        firebase = ConfiguracaoFireBase.getFirebaseAutenticacao();
        user = firebase.getCurrentUser();

        if(user != null) {
            txt_nav_header_title.setText("Bruno");
            txt_nav_header_subTitle.setText("bwdesouza@gmail.com");
        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_novo_usuario) {
            Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
            intent.putExtra("tela", "menu");
            startActivity(intent);
        } else if (id == R.id.nav_nova_tarefa) {
            Intent intent = new Intent(MainActivity.this, CadastroAtividadesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_minhas_tarefas) {
            Intent intent = new Intent(MainActivity.this, MinhasAtividadesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_atividades_pendentes) {
            Intent intent = new Intent(MainActivity.this, PendentesAtividadesActivity.class);
            startActivity(intent);
           // ShowPopupPontuacao();
        } else if (id == R.id.nav_familia) {

        } else if (id == R.id.nav_sair) {
            firebase.signOut();
            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ShowPopupPontuacao() {
        TextView txtClosePopupPontuacao;
        dialogPontuacaoAtual.setContentView(R.layout.popup_pontuacao);
        txtClosePopupPontuacao = dialogPontuacaoAtual.findViewById(R.id.txtClosePopupPontuacao);
        //txtClosePopupPontuacao.setText("M");

        txtClosePopupPontuacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPontuacaoAtual.dismiss();
            }
        });

        dialogPontuacaoAtual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPontuacaoAtual.show();
    }

    private void verifyUserLogged(){
        firebase = ConfiguracaoFireBase.getFirebaseAutenticacao();
        user = firebase.getCurrentUser();

       if(user == null){
           Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
           startActivity(intentLogin);
       }
       else {
           /*
           txt_nav_header_title.setText(user.getDisplayName().toString());
           txt_nav_header_subTitle.setText(user.getEmail().toString());
           */

           TarefaDataStore.sharedInstance().carregaTarefas();
           UsuarioDataStore.sharedInstance().carregaUsuarios();
       }
    }
}



    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout){
            firebase.signOut();
            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        }

        return super.onOptionsItemSelected(item);
    }
    */
