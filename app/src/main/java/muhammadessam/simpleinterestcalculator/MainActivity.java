package muhammadessam.simpleinterestcalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
    private static Calendar startCalendar, endCalendar;
    private TextView interestResult;
    private static double timePeriod;
    private double interest, total, principalAmountOfMoney, interestRate;
    public static  int startDay, startMonth, startYear, endDay, endMonth, endYear;
    private EditText principleAmountOfMoneyEditText, interestRateEditText;
    private static final String START_DATE_TAG =  "Start Date Picker", END_DATE_TAG = "End Date Picker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        principleAmountOfMoneyEditText = findViewById(R.id.PrincipleAmountOfMoneyEditTextView);
        interestRateEditText =  findViewById(R.id.interestRateEditTextView);
        interestResult =  findViewById(R.id.resultTextView);
        TextView startDateView = findViewById(R.id.startDateTextView);
        final Calendar calendar = Calendar.getInstance();
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startMonth = calendar.get(Calendar.MONTH);
        startYear = calendar.get(Calendar.YEAR);
        startDateView.setText(new StringBuilder().append(startDay).append("/").append(startMonth + 1).append("/").append(startYear));
        Button pickStartDateButton = findViewById(R.id.pickStartDateButton);
        pickStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getFragmentManager(), START_DATE_TAG);
            }
        });

        Button pickEndDateButton = findViewById(R.id.pickEndDateButton);
        pickEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getFragmentManager(), END_DATE_TAG);
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

    public static void setStartDate(int startDay, int startMonth, int startYear) {
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, startDay);
        startCalendar.set(Calendar.MONTH, startMonth);
        startCalendar.set(Calendar.YEAR, startYear);
    }

    public static  void setEndDate(int endDay, int endMonth, int endYear) {
        endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        endCalendar.set(Calendar.MONTH, endMonth);
        endCalendar.set(Calendar.YEAR, endYear);
    }

    private static double setTimePeriod(double timePeriodd) {
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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private static final String START_DATE_TAG =  "Start Date Picker", END_DATE_TAG = "End Date Picker";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (getTag().equals(START_DATE_TAG)){
                startYear = year;
                startMonth =month + 1;
                startDay = dayOfMonth;
                TextView startDateView = getActivity().findViewById(R.id.startDateTextView);
                startDateView.setText(new StringBuilder().append(MainActivity.startDay).append("-").append(MainActivity.startMonth + 1).append("-").append(MainActivity.startYear));
                startDateView.setVisibility(View.VISIBLE);
                setStartDate(MainActivity.startDay, MainActivity.startMonth + 1, MainActivity.startYear);
            } else if (getTag().equals(END_DATE_TAG)){
                endYear = year;
                endMonth =month + 1;
                endDay = dayOfMonth;
                TextView endDateView = getActivity().findViewById(R.id.endDateTextView);
                endDateView.setText(new StringBuilder().append(MainActivity.endDay).append("-").append(MainActivity.endMonth + 1).append("-").append(MainActivity.endYear));
                endDateView.setVisibility(View.VISIBLE);
                setEndDate(MainActivity.endDay, MainActivity.endMonth + 1, MainActivity.endYear);
                long differenceInMillis = startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis();
                long daysInDays = differenceInMillis / (24 * 60 * 60 * 1000);
                TextView resultDays = getActivity().findViewById(R.id.resultDays);
                resultDays.setText(String.valueOf(Math.abs(daysInDays)) + " Days.");
                resultDays.setVisibility(View.VISIBLE);
                timePeriod = setTimePeriod(Math.abs(daysInDays));
            }
        }
    }
}