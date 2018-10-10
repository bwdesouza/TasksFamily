package br.bruno.appdev.tasksfamily.Controller;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import br.bruno.appdev.tasksfamily.R;

public class CadastroAtividadesActivity extends AppCompatActivity {

    private TimeZone zone = TimeZone.getTimeZone("GMT-03:00");
    private Locale locale = new Locale("pt", "BR");
    private Calendar myCalendar = Calendar.getInstance(zone, locale);
    private EditText dtAtvInicio;
    private EditText dtAtvFim;
    private Integer btnClicado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_atividades);

        dtAtvInicio = findViewById(R.id.dtAtvInicio);
        dtAtvFim = findViewById(R.id.dtAtvFim);

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
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        if(btnClicado == 1) {
            dtAtvInicio.setText(sdf.format(myCalendar.getTime()));
        } else {
            dtAtvFim.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void OnClickBtnAtvCancelar(View view){
        finish();
    }

    public void OnClickBtnAtvSalvar(View view){

    }
}
