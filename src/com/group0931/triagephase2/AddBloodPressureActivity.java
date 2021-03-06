package com.group0931.triagephase2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * An activity that allows the user to add a blood pressure reading to the most
 * recent visit of the current patient.
 * @author Christina Chung, Angel You, Asher Minden Webb.
 *
 */
public class AddBloodPressureActivity extends Activity {

	/** The systolic pressure of this reading.*/
	private EditText systolic;
	/** The diastolic pressure of this reading. */
	private EditText diastolic;
	/** THe year of this reading. */
	private EditText year;
	/** The month of this reading. */
	private EditText month;
	/** The day of this reading. */
	private EditText day;
	/** The hour of this reading. */
	private EditText hour;
	/** The minute of this reading. */
	private EditText minute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_blood_pressure);
		
		systolic = (EditText) findViewById(R.id.blood_pressure_reading_systolic);
		diastolic = (EditText) findViewById(R.id.blood_pressure_reading_diastolic);
		year = (EditText) findViewById(R.id.blood_pressure_reading_year);
		month = (EditText) findViewById(R.id.blood_pressure_reading_month);
		day = (EditText) findViewById(R.id.blood_pressure_reading_day);
		hour = (EditText) findViewById(R.id.blood_pressure_reading_hour);
		minute = (EditText) findViewById(R.id.blood_pressure_reading_minute);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_blood_pressure, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Adds a new blood pressure reading to the most recent visit of the 
	 * current patient.
	 * @param view The View form which the method is called.
	 * @throws CurrentPatientNotSetException When there isn't a current patient 
	 * to add a visit entry to. 
	 * @throws VisitsNotLoadedException When the current patient doesn't have a 
	 * most recent visit. 
	 * @throws NoVisitsException 
	 * @throws InvalidTimeStampException 
	 */
	public void addNewBloodPressure(View view) throws CurrentPatientNotSetException,
	VisitsNotLoadedException, NoVisitsException{
		try {	
			if (CurrentPatient.get().hasVisited()) {
				TimeStamp readingTime = TimeStamp.FactoryTimeStampWithTime(
						year.getText().toString(), month.getText().toString(), 
						day.getText().toString(), hour.getText().toString(),
						minute.getText().toString());
				BloodPressureReading newBloodPressure = 
						BloodPressureReading.FactoryBloodPressureReading(
								systolic.getText().toString(), 
								diastolic.getText().toString(),
								readingTime);		
				CurrentPatient.get().getLastVisit().addBloodPressureReading(newBloodPressure);
				this.displayMessage("A new blood pressure reading of " + 
				CurrentPatient.get().getLastVisit().getMostRecentBloodPressureReading() + 
					" for " + CurrentPatient.get().getName() + " has been entered.");
			} else {
				throw new InactivePatientException(); 
			}
		} catch (InvalidTimeStampException e) {
			this.displayMessage("Unfortunately, the date your entered is invalid. " +
					"Please try again");
		} catch (InactivePatientException e){
			this.displayMessage("Unfortunately, " + CurrentPatient.get().getName() + 
					" does not have an active visit to add vital sign readings to.");
		} catch (InvalidVitalSignException e){
			this.displayMessage("Unfortunately, you did not enter a valid blood pressure.");
		} finally {
			this.finish();
		}
		this.finish();
	}
	
	/**
	 * Displays a message to the user by launching a {@code MessageDisplayActivity}.
	 * @param message The message to be displayed.
	 */
	public void displayMessage(String message){
		Intent i = new Intent(this, MessageDisplayActivity.class);
		i.putExtra("DISPLAYMESSAGE", message);
		this.startActivity(i);
	}
}
