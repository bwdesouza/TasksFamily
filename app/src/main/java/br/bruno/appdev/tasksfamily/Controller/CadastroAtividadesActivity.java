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
import br.bruno.appdev.tasksfamily.Model.ConfiguracaoFireBase;
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
    private int btnClicado = 0;
    private int selectedHour, selectedMinute;
    private long IDCal;
    private FirebaseAuth autenticacao;
    FirebaseUser usuarioFirebase;


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

        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(CadastroAtividadesActivity.this,
                new String[]{Manifest.permission.READ_CALENDAR}, 1);

        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(CadastroAtividadesActivity.this,
                new String[]{Manifest.permission.WRITE_CALENDAR}, 2);

        Spinner spinner = findViewById(R.id.atv_repetir_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.atv_repetir_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        dtAtvInicio = findViewById(R.id.dtAtvInicio);
        dtAtvFim = findViewById(R.id.dtAtvFim);
        dtAtvHoraIni = findViewById(R.id.dtAtvHoraIni);
        dtAtvHoraFim = findViewById(R.id.dtAtvHoraFim);
        edtAtvTitulo = findViewById(R.id.edtAtvTitulo);
        swAtvTodoDia = findViewById(R.id.swAtvTodoDia);
        edtAtvDescricao = findViewById(R.id.edtAtvDescricao);

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

    public void OnClickBtnAtvCancelar(View view){
//        iniciaEventoGoogle();
        long eventID = IDCal;

        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(uri);
        startActivity(intent);
//        finish();
    }

    public void OnClickBtnAtvSalvar(View view){

//        iniciaEventoGoogle();

        criarEventoCalendarioGoogle();

        // Run query
//        Cursor cur = null;
//        ContentResolver cr = getContentResolver();
//        Uri uri = CalendarContract.Calendars.CONTENT_URI;
//        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
//        String[] selectionArgs = new String[] {"bwdesouza@gmail.com", "com.google",
//                "bwdesouza@gmail.com"};
//        // Submit the query and get a Cursor object back.
//        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
//
//        // Use the cursor to step through the returned records
//
//        long calID = 0;
//        String displayName = null;
//        String accountName = null;
//        String ownerName = null;
//
//        while (cur.moveToNext()) {
//            calID = 0;
//            displayName = null;
//            accountName = null;
//            ownerName = null;
//
//            // Get the field values
//            calID = cur.getLong(PROJECTION_ID_INDEX);
//            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
//            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
//            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
//
//            // Do something with the values...
//
//        }
//
//
//        // Construct event details
//        long startMillis = 0;
//        long endMillis = 0;
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2018, 9, 13, 12, 00);
//        startMillis = beginTime.getTimeInMillis();
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(2018, 10, 13, 23, 45);
//        endMillis = endTime.getTimeInMillis();
//
//        Calendar untilDate = Calendar.getInstance();
//        endTime.set(2018, 10, 13, 23, 45);
//
//        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyymmdd");
//        Calendar dt = Calendar.getInstance();
//
//        //where untilDate is a date instance of your choice,for example 30/01/2012
//        dt.setTime(new Date("2018/10/13"));
//
//        //if you want the event until 30/01/2012 we add one day from our day because UNTIL in RRule sets events Before the last day want for event
//        //dt.add(Calendar.DATE, 1);
//        String dtUntill = yyyymmdd.format(dt.getTime());
//
//        ContentValues values = new ContentValues();
//
//        values.put(CalendarContract.Events.DTSTART, startMillis);
//        values.put(CalendarContract.Events.TITLE, "BRUNOOOOOOOOOOOOOOOOOOO");
//        values.put(CalendarContract.Events.DESCRIPTION, "teste para funcionar");
//
//        TimeZone timeZone = TimeZone.getDefault();
//        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
//
//        // default calendar
//        values.put(CalendarContract.Events.CALENDAR_ID, calID);
//
//        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;UNTIL="
//                + dtUntill);
//        //for one hour
//        values.put(CalendarContract.Events.DURATION, "+P1H");
//
//        values.put(CalendarContract.Events.HAS_ALARM, 1);

        // insert event to calendar
       //uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        //---------------------------------------------------

        //String eventID = uri.getLastPathSegment();
//        values = new ContentValues();
//        values.put(CalendarContract.Reminders.MINUTES, 5);
//        values.put(CalendarContract.Reminders.EVENT_ID, eventID);
//        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
//        uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);


        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(CadastroAtividadesActivity.this,
//                Manifest.permission.WRITE_CALENDAR)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(CadastroAtividadesActivity.this,
//                    Manifest.permission.WRITE_CALENDAR)) {
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(CadastroAtividadesActivity.this,
//                        new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
//
//
//                uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
//                // Retrieve ID for new event
//
//                //long eventID = Long.parseLong(uri.getLastPathSegment());
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//
////            uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
////            // Retrieve ID for new event
////            String eventID = uri.getLastPathSegment();
////
////            values = new ContentValues();
////            values.put(CalendarContract.Reminders.MINUTES, 5);
////            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
////            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
////            uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
//        }


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

            // Insert Event
            cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, edtAtvTitulo.getText().toString());
            values.put(CalendarContract.Events.DESCRIPTION, edtAtvDescricao.getText().toString());
            values.put(CalendarContract.Events.CALENDAR_ID, 3);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC/GMT -3:00");
            uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            String eventID = uri.getLastPathSegment();
            IDCal = Long.parseLong(eventID);

            Tarefas tarefa = new Tarefas(IDCal, edtAtvTitulo.getText().toString(), edtAtvDescricao.getText().toString(),"",
                    email,false, "");

            tarefa.SalvarTask();

            Toast.makeText(CadastroAtividadesActivity.this,"Tarefa cadastrada com sucesso!", Toast.LENGTH_LONG).show();

            finish();
        }
    }
}
