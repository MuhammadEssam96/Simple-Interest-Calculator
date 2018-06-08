package muhammadessam.simpleinterestcalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Muhammad Essam on 2/25/18.
 */

public class MainActivity extends AppCompatActivity {
    private static Calendar startCalendar, endCalendar;
    private RelativeLayout resultRelativeLayout;
    private static double timePeriod;
    private double interest;
    private double total;
    private static  int startDay, startMonth, startYear, endDay, endMonth, endYear;
    private EditText principleAmountOfMoneyEditText, interestRateEditText;
    private TextView resultInterest, resultTotal;
    private static final String START_DATE_TAG =  "Start Date Picker", END_DATE_TAG = "End Date Picker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        principleAmountOfMoneyEditText = findViewById(R.id.PrincipleAmountOfMoneyEditTextView);
        interestRateEditText =  findViewById(R.id.interestRateEditTextView);
        resultRelativeLayout =  findViewById(R.id.resultLayout);
        resultInterest = findViewById(R.id.resultInterestTextView);
        resultTotal = findViewById(R.id.resultTotalTextView);
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
                    Toast.makeText(MainActivity.this, "Missing Information..", Toast.LENGTH_SHORT).show();
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

    private static double setTimePeriod(double timePeriodNew) {
        timePeriod = timePeriodNew;
        return timePeriod;
    }

    private boolean isEditTextFilled(EditText editText){
        String editTextName = editText.getContentDescription().toString();
        if (editText.getText().toString().isEmpty()
                || editText.getText().toString().length() == 0
                || editText.getText().toString().equals("")){
            editText.setError(editTextName + " is missing!");
            return false;
        }
        return true;
    }

    private void calculateInterestResultData() {
        double principalAmountOfMoney = Integer.parseInt(principleAmountOfMoneyEditText.getText().toString());
        double interestRate = Integer.parseInt(interestRateEditText.getText().toString());
        interest = calculateInterest(principalAmountOfMoney, interestRate, timePeriod);
        total = principalAmountOfMoney + interest;
        showInterestResult();
    }

    private void showInterestResult() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        resultInterest.setText(String.valueOf(decimalFormat.format(interest)));
        resultTotal.setText(String.valueOf(decimalFormat.format(total)));
        resultRelativeLayout.setVisibility(View.VISIBLE);
    }

    private double calculateInterest(double principalAmountOfMoney, double interestRate, double timePeriod){
        timePeriod = timePeriod / 360;
        interestRate = interestRate /100;
        interest = principalAmountOfMoney * interestRate * timePeriod;
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
                startMonth = month + 1;
                startDay = dayOfMonth;
                TextView startDateView = getActivity().findViewById(R.id.startDateTextView);
                startDateView.setText(new StringBuilder().append(MainActivity.startDay).append("-").append(MainActivity.startMonth + 1).append("-").append(MainActivity.startYear));
                startDateView.setVisibility(View.VISIBLE);
                setStartDate(MainActivity.startDay, MainActivity.startMonth + 1, MainActivity.startYear);
            } else if (getTag().equals(END_DATE_TAG)){
                endYear = year;
                endMonth = month + 1;
                endDay = dayOfMonth;
                TextView endDateView = getActivity().findViewById(R.id.endDateTextView);
                endDateView.setText(new StringBuilder().append(MainActivity.endDay).append("-").append(MainActivity.endMonth + 1).append("-").append(MainActivity.endYear));
                endDateView.setVisibility(View.VISIBLE);
                setEndDate(MainActivity.endDay, MainActivity.endMonth + 1, MainActivity.endYear);
                long differenceInMillis = startCalendar.getTimeInMillis() - endCalendar.getTimeInMillis();
                long daysInDays = differenceInMillis / (24 * 60 * 60 * 1000);
                TextView resultDays = getActivity().findViewById(R.id.resultDays);
                resultDays.setText(String.valueOf(Math.abs(daysInDays)).concat(" ").concat(getString(R.string.days)));
                resultDays.setVisibility(View.VISIBLE);
                timePeriod = setTimePeriod(Math.abs(daysInDays));
            }
        }
    }
}