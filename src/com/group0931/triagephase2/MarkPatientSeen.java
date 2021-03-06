package com.group0931.triagephase2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * An Activity that allows users to record when a patient in the ER
 * was seen by a doctor. 
 * @author Christina Chung, Angel You, Asher MindenWebb.
 *
 */
public class MarkPatientSeen extends Activity {
	
	/** The year seen by doctor.*/
	EditText year;
	/** The month seen by doctor.*/
	EditText month;
	/** The day seen by doctor.*/
	EditText day;
	/** The hour seen by doctor.*/
	EditText hour;
	/** The minute seen by doctor.*/
	EditText minute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_patient_seen);
		year = (EditText) findViewById(R.id.yearSeen);
		month = (EditText) findViewById(R.id.monthSeen);
		day = (EditText) findViewById(R.id.daySeen);
		hour = (EditText) findViewById(R.id.hourSeen);
		minute = (EditText) findViewById(R.id.minuteSeen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.make_patient_seen, menu);
		return true;
	}
	
	/**
	 * Marks a {@code Patient} as seen.
	 * @param view The View from which the method is called.
	 * @throws CurrentPatientNotSetException when a {@code CurrentPatient} is not set.
	 * @throws VisitsNotLoadedException when a {@code Patient}
	 * does not have their visits loaded
	 * @throws NoVisitsException if the {@code patient} has no visits.
	 */
	public void makePatientSeen(View view) throws CurrentPatientNotSetException, 
	VisitsNotLoadedException, NoVisitsException{
		
		TimeStamp seenTime; 
		
		System.out.println("makingseen");
		
		String seenYear = year.getText().toString();
		String seenMonth = month.getText().toString();
		String seenDay = day.getText().toString();
		String seenHour = hour.getText().toString();
		String seenMinute = minute.getText().toString();
		
		try {
			//Set seenTime with the current date and time, if 
			//the user left the fields blank.
			if (seenYear.equals("") && seenMonth.equals("") 
					&& seenDay.equals("") &&	seenHour.equals("") 
					&& seenMinute.equals("")){
				seenTime = new TimeStamp(); 	
			}else { //Set seenTime with the given seen date and time.
				seenTime = TimeStamp.FactoryTimeStampWithTime(seenYear, seenMonth, seenDay, 
						seenHour, seenMinute); 
			}
			CurrentPatient.get().getLastVisit().setSeenTime(seenTime);
			CurrentPatient.get().makeSeen();
			this.displayMessage(CurrentPatient.get().getName()
					+ " has been marked as seen at " +
							CurrentPatient.get().getLastVisit().getSeenTime() + ".");
			
		} catch(InactiveVisitException e){
			this.displayMessage("Cannot add a seen time. " 
					+ CurrentPatient.get().getName()  +
					" has already been seen by a doctor for this visit entry.");
			e.printStackTrace();
		} catch(InvalidTimeStampException e){
			this.displayMessage("Unforunately, you have not" +
					" entered a valid date and time. Please try again.");
			e.printStackTrace();
		} catch(CurrentPatientNotSetException e){
			this.displayMessage("Unfornately, an error has occured. " +
					" Seen time was not added.");
			e.printStackTrace();
		} catch(VisitsNotLoadedException e){
			this.displayMessage("Unfornately, an error has occured. " +
					" Seen time was not added.");
			e.printStackTrace();
		} catch(NoVisitsException e) {
			this.displayMessage("Unfornately, an error has occured. " +
					" Seen time was not added.");
			e.printStackTrace();
		} finally {
			this.finish();
		}
		setResult(RESULT_OK);
		this.finish();
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
	 * Displays a message to the user by launching
	 * a {@code MessageDisplayActivity}.
	 * @param message The message to be displayed.
	 */
	public void displayMessage(String message){
		Intent i = new Intent(this, MessageDisplayActivity.class);
		i.putExtra("DISPLAYMESSAGE", message);
		this.startActivity(i);
	}
	
}
