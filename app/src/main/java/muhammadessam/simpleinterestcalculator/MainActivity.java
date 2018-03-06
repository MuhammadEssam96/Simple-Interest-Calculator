package muhammadessam.simpleinterestcalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Muhammad Essam on 2/25/18.
 */

public class MainActivity extends AppCompatActivity {
    private Calendar startCalendar, endCalendar;
    private TextView startDateView, endDateView, resultDays, interestResult;
    private double timePeriod, interest, total, principalAmountOfMoney, interestRate;
    private int startDay, startMonth, startYear;
    private EditText principleAmountOfMoneyEditText, interestRateEditText;
    static final int START_DATE_DIALOG_ID = 999, END_DATE_DIALOG_ID = 998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        principleAmountOfMoneyEditText = findViewById(R.id.PrincipleAmountOfMoneyEditTextView);
        interestRateEditText =  findViewById(R.id.interestRateEditTextView);
        interestResult =  findViewById(R.id.resultTextView);
        startDateView =  findViewById(R.id.startDateTextView);
        endDateView =  findViewById(R.id.endDateTextView);
        resultDays =  findViewById(R.id.resultDays);
        final Calendar calendar = Calendar.getInstance();
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startMonth = calendar.get(Calendar.MONTH);
        startYear = calendar.get(Calendar.YEAR);
        startDateView.setText(new StringBuilder().append(startDay).append("/").append(startMonth + 1).append("/").append(startYear));
        Button pickStartDateButton = findViewById(R.id.pickStartDateButton);
        pickStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(START_DATE_DIALOG_ID);
            }
        });

        Button pickEndDateButton = findViewById(R.id.pickEndDateButton);
        pickEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(END_DATE_DIALOG_ID);
            }
        });

        Button calculateInterestResult = findViewById(R.id.calculateInterestButton);
        calculateInterestResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditTextFilled(principleAmountOfMoneyEditText) && isEditTextFilled(interestRateEditText)){
                    calculateInterestResultData();
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "Missing Information..", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                return new DatePickerDialog(this, startDatePickerListener, startYear, startMonth, startDay);
            case END_DATE_DIALOG_ID:
                return new DatePickerDialog(this, endDatePickerListener, startYear, startMonth, startDay);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            startYear = selectedYear;
            startMonth = selectedMonth;
            startDay = selectedDay;
            startDateView.setText(new StringBuilder().append(startDay).append("-").append(startMonth + 1).append("-").append(startYear));
            startDateView.setVisibility(View.VISIBLE);
            setStartDate(startDay, startMonth, startYear);
        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            endDateView.setText(new StringBuilder().append(selectedDay).append("-").append(selectedMonth + 1).append("-").append(selectedYear));
            endDateView.setVisibility(View.VISIBLE);
            setEndDate(selectedDay, selectedMonth, selectedYear);
            setDays();
        }
    };

    private void setStartDate(int startDay, int startMonth, int startYear) {
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, startDay);
        startCalendar.set(Calendar.MONTH, startMonth);
        startCalendar.set(Calendar.YEAR, startYear);
    }

    private void setEndDate(int endDay, int endMonth, int endYear) {
        endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        endCalendar.set(Calendar.MONTH, endMonth);
        endCalendar.set(Calendar.YEAR, endYear);
    }

    private void setDays() {
        long differenceInMillis = startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis();
        long daysInDays = differenceInMillis / (24 * 60 * 60 * 1000);
        resultDays.setText(String.valueOf(Math.abs(daysInDays)) + " Days.");
        resultDays.setVisibility(View.VISIBLE);
        timePeriod = setTimePeriod(Math.abs(daysInDays));
    }

    private double setTimePeriod(double timePeriodd) {
        timePeriod = timePeriodd;
        Log.v("MainActivity" , String.valueOf(timePeriod));
        return timePeriod;
    }

    private boolean isEditTextFilled(EditText editText){
        String editTextName = editText.getContentDescription().toString();
        if (editText.getText().toString().isEmpty()
                || editText.getText().toString().length() == 0
                || editText.getText().toString().equals("")){
            editText.setError(editTextName + " is missing!");
            return false;
        } else {
            return true;
        }
    }

    private void calculateInterestResultData() {
        principalAmountOfMoney = Integer.parseInt(principleAmountOfMoneyEditText.getText().toString());
        interestRate = Integer.parseInt(interestRateEditText.getText().toString());
        interest = calculateInterest(principalAmountOfMoney, interestRate, timePeriod);
        total = principalAmountOfMoney + interest;
        Log.v("MainActivity" , "Principle Amount Of Money: " + String.valueOf(principalAmountOfMoney));
        Log.v("MainActivity" , "Interest rate: " + String.valueOf(interestRate % 100));
        Log.v("MainActivity" , "Time Period: " + String.valueOf(timePeriod / 360));
        Log.v("MainActivity" , "Interest: " + String.valueOf(interest));
        Log.v("MainActivity" , "Total: " + String.valueOf(total));
        showInterestResult();
    }

    private void showInterestResult() {
        interestResult.setText("Principal Amount of Money is: " + principalAmountOfMoney + ".\nInterest is: " + interest + ".\nTotal is: " + total + ".\nInterest Rate is: " + interestRate + "%.\nTime Period is: " + timePeriod + ". ");
        interestResult.setVisibility(View.VISIBLE);
    }

    private double calculateInterest(double principalAmountOfMoney, double interestRate, double timePeriod){
        timePeriod = timePeriod / 360;
        interestRate = interestRate /100;
        interest = principalAmountOfMoney * interestRate * timePeriod;
        Log.v("MainActivity" , "Interest: " + String.valueOf(interest));
        return interest;
    }
}
