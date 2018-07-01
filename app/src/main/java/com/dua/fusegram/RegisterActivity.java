package com.dua.fusegram;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    ImageButton datePickImgBtn;
    EditText edtDOB;
    int year_x, month_x, date_x;
    static final int DIALOG_ID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtDOB = (EditText) findViewById(R.id.edtDOB);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        date_x = cal.get(Calendar.DAY_OF_MONTH);

        showDatePickerDialog();
    }

    public void showDatePickerDialog(){

        datePickImgBtn = findViewById(R.id.datePickImgBtn);
        datePickImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID)
            return new DatePickerDialog(this,datePickerListener,year_x,month_x,date_x);
        return null;

    }

    private DatePickerDialog.OnDateSetListener datePickerListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            year_x=year;
            month_x=monthOfYear+1;
            date_x=dayOfMonth;
            edtDOB.setText(date_x+"-"+month_x+"-"+year_x);

        }
    };
}
