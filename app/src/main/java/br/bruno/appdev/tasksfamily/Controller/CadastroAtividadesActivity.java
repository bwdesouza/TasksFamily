package br.bruno.appdev.tasksfamily.Controller;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import br.bruno.appdev.tasksfamily.Entities.Tarefas;
import br.bruno.appdev.tasksfamily.Entities.Usuarios;
import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;
import br.bruno.appdev.tasksfamily.Model.TarefaDataStore;
import br.bruno.appdev.tasksfamily.Model.UsuarioDataStore;
import br.bruno.appdev.tasksfamily.R;

public class CadastroAtividadesActivity extends AppCompatActivity {

    private TimeZone zone = TimeZone.getTimeZone("GMT-03:00");
    private Locale locale = new Locale("pt", "BR");
    private Calendar myCalendar = Calendar.getInstance(zone, locale);

    private EditText edtAtvTitulo;
    private Switch swAtvTodoDia;
    private EditText dtAtvInicio;
    private EditText dtAtvHoraIni;
    private EditText dtAtvFim;
    private EditText dtAtvHoraFim;
    private EditText edtAtvDescricao;
    private Spinner emails_parentescos_spinner;
    private Spinner alerta_min_spinner;
    private String emailDestinatario;
    private int btnClicado = 0;
    private int selectedHour, selectedMinute;
    private long IDCal;
    private FirebaseAuth autenticacao;
    FirebaseUser usuarioFirebase;
    private int alertaminAntes;


    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_atividades);

        TarefaDataStore.sharedInstance().carregaTarefas();

        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(CadastroAtividadesActivity.this,
                new String[]{Manifest.permission.READ_CALENDAR}, 1);

        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(CadastroAtividadesActivity.this,
                new String[]{Manifest.permission.WRITE_CALENDAR}, 2);

        alerta_min_spinner = findViewById(R.id.atv_alerta_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.atv_alerta_spinner, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        alerta_min_spinner.setAdapter(adapter);

        alerta_min_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alertaminAntes = Integer.parseInt( (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        usuarioFirebase = autenticacao.getCurrentUser();

        String emailUsuario = usuarioFirebase.getEmail();

        Usuarios usuarioLogado = UsuarioDataStore.sharedInstance().getUsuarios(emailUsuario);

        String[] items = retornarQuemEstaCadastrado(usuarioLogado);

        emails_parentescos_spinner = findViewById(R.id.emails_parentescos_spinner);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        emails_parentescos_spinner.setAdapter(adapterSpinner);

        emails_parentescos_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                emailDestinatario = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        dtAtvInicio = findViewById(R.id.dtAtvInicio);
        dtAtvFim = findViewById(R.id.dtAtvFim);
        dtAtvHoraIni = findViewById(R.id.dtAtvHoraIni);
        dtAtvHoraFim = findViewById(R.id.dtAtvHoraFim);
        edtAtvTitulo = findViewById(R.id.edtAtvTitulo);
        swAtvTodoDia = findViewById(R.id.swAtvTodoDia);
        edtAtvDescricao = findViewById(R.id.edtAtvDescricao);

        IDCal = 0;

        dtAtvInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CadastroAtividadesActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                btnClicado = 1;
            }
        });

        dtAtvFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CadastroAtividadesActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                btnClicado = 2;
            }
        });

        dtAtvHoraIni.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 new TimePickerDialog(CadastroAtividadesActivity.this, time,
                         myCalendar.get(Calendar.HOUR_OF_DAY),
                         myCalendar.get(Calendar.MINUTE), true).show();

                 btnClicado = 1;
             }
        });

        dtAtvHoraFim.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CadastroAtividadesActivity.this, time,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();

                btnClicado = 2;
            }
        });


    }

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                selectedHour = hourOfDay;
                selectedMinute = minute;
                updateLabelHora();
        }
    };

    private void updateLabelHora() {
        if(btnClicado == 1) {
            dtAtvHoraIni.setText(selectedHour + ":" + selectedMinute);
        } else {
            dtAtvHoraFim.setText(selectedHour + ":" + selectedMinute);
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelData();
        }
    };

    private void updateLabelData() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        if(btnClicado == 1) {
            dtAtvInicio.setText(sdf.format(myCalendar.getTime()));
        } else {
            dtAtvFim.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public String[] retornarQuemEstaCadastrado(Usuarios usuario){
        List<String> retorno = new ArrayList<>();
        if(usuario.getIdFilha() != null){
            if(!usuario.getIdFilha().toString().equals("")){
                String email = UsuarioDataStore.sharedInstance().getEmailUsuario(usuario.getIdFilha());
                if(email != null) {
                    retorno.add(email);
                }
            }
        }
        if(usuario.getIdFilho() != null){
            if (!usuario.getIdFilho().toString().equals("")) {
                String email = UsuarioDataStore.sharedInstance().getEmailUsuario(usuario.getIdFilho());
                if (email != null) {
                    retorno.add(email);
                }
            }
        }
        if(usuario.getIdPai() != null){
            if (!usuario.getIdPai().toString().equals("")) {
                String email = UsuarioDataStore.sharedInstance().getEmailUsuario(usuario.getIdPai());
                if (email != null) {
                    retorno.add(email);
                }
            }
        }
        if(usuario.getIdMae() != null){
            if(!usuario.getIdMae().toString().equals("")){
                String email = UsuarioDataStore.sharedInstance().getEmailUsuario(usuario.getIdMae());
                if(email != null) {
                    retorno.add(email);
                }
            }
        }

        String[] retornoEmails = new String[retorno.size() + 1];
        retornoEmails[0] = usuario.getEmail();
        for(int i = 0; i < retorno.size(); i++){
            int j = i;
            retornoEmails[j+1] = retorno.get(i);
        }

        return retornoEmails;
    }

    public void OnClickBtnAtvCancelar(View view){
//        iniciaEventoGoogle();
        //long eventID = IDCal;

//        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
//        Intent intent = new Intent(Intent.ACTION_VIEW)
//                .setData(uri);
//        startActivity(intent);

        Toast.makeText(CadastroAtividadesActivity.this,"Criação de nova tarefa foi cancelada!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void OnClickBtnAtvSalvar(View view){
        criarEventoCalendarioGoogle();
    }

    public void iniciaEventoGoogle(){

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2018, 10, 14, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 10, 14, 8, 30);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            String eventID = uri.getLastPathSegment();
            String tet = eventID;
        }
    }

    public void criarEventoCalendarioGoogle(){
        if (ContextCompat.checkSelfPermission(CadastroAtividadesActivity.this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(CadastroAtividadesActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR}, 2);
        } else {

            autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
            usuarioFirebase = autenticacao.getCurrentUser();

            String account_name = "", account_type = "", owner_account = "";

            String email = usuarioFirebase.getEmail().toString();
            account_name = email;
            account_type = "com.google";
            owner_account = email;

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
            String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] { account_name , account_type,
                owner_account};
        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        // Use the cursor to step through the returned records

        long calID = 0;

        while (cur.moveToNext()) {
            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
        }

            //Construct event details
            long startMillis = 0;
            long endMillis = 0;

            String dataIni = dtAtvInicio.getText().toString();
            String[] dataPartsIni = dataIni.split("/");
            String dayIni = dataPartsIni[0];
            String monthIni = dataPartsIni[1];
            String yearIni = dataPartsIni[2];

            String dataFim = dtAtvFim.getText().toString();
            String[] dataPartsFim = dataFim.split("/");
            String dayFim = dataPartsFim[0];
            String monthFim = dataPartsFim[1];
            String yearFim = dataPartsFim[2];

            String horaIni = dtAtvHoraIni.getText().toString();
            String[] horaPartsIni = horaIni.split(":");
            String hourIni = horaPartsIni[0];
            String minIni = horaPartsIni[1];

            String horaFim = dtAtvHoraFim.getText().toString();
            String[] horaPartsFim = horaFim.split(":");
            String hourFim = horaPartsFim[0];
            String minFim = horaPartsFim[1];

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(Integer.parseInt(yearIni), Integer.parseInt(monthIni), Integer.parseInt(dayIni), Integer.parseInt(hourIni), Integer.parseInt(minIni));
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(Integer.parseInt(yearFim), Integer.parseInt(monthFim), Integer.parseInt(dayFim),  Integer.parseInt(hourFim),  Integer.parseInt(minFim));
            endMillis = endTime.getTimeInMillis();

            Usuarios usuarioDestinatario = UsuarioDataStore.sharedInstance().getUsuarios(emailDestinatario);
            // Insert Event
            try {

                cr = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, endMillis);
                values.put(CalendarContract.Events.TITLE, edtAtvTitulo.getText().toString());
                values.put(CalendarContract.Events.DESCRIPTION, edtAtvDescricao.getText().toString());
                values.put(CalendarContract.Events.CALENDAR_ID, calID);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC/GMT -3:00");
                uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

                String eventID = uri.getLastPathSegment();
                IDCal = Long.parseLong(eventID);

                cr = getContentResolver();
                values = new ContentValues();
                values.put(CalendarContract.Attendees.ATTENDEE_NAME, usuarioDestinatario.getNome().toString());
                values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, usuarioDestinatario.getEmail().toString());
                values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE);
                values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_OPTIONAL);
                values.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
                values.put(CalendarContract.Attendees.EVENT_ID, eventID);
                uri = cr.insert(CalendarContract.Attendees.CONTENT_URI, values);

                cr = getContentResolver();
                values = new ContentValues();
                values.put(CalendarContract.Reminders.MINUTES, alertaminAntes);
                values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);



            }catch (Exception e){

            }

            Tarefas tarefa = new Tarefas(IDCal, edtAtvTitulo.getText().toString(), edtAtvDescricao.getText().toString(),emailDestinatario,
                    email, false, usuarioDestinatario.getGrauParentesco(), false, false);

            tarefa.SalvarTask();

            Toast.makeText(CadastroAtividadesActivity.this,"Tarefa cadastrada com sucesso!", Toast.LENGTH_LONG).show();

            TarefaDataStore.sharedInstance().carregaTarefas();
            finish();
        }
    }
}
