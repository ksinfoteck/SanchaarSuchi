package in.kamalaenterprize.commonutility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c 	= Calendar.getInstance();
		int year 			= c.get(Calendar.YEAR);
		int month 			= c.get(Calendar.MONTH);
		int day 			= c.get(Calendar.DAY_OF_MONTH); 

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		GlobalVariables.SELECTDATE = dateFormat.format(c.getTime());

		GlobalVariables.TextViewDate.setText(GlobalVariables.SELECTDATE);

	}
}