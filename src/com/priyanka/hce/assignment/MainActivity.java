package com.priyanka.hce.assignment;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

	
    private static final  String MY_CARDIO_APP_TOKEN = "4d2847d79d634301964482a736078b99";
	private int MY_SCAN_REQUEST_CODE = 100; 
	private TextView resultTextView;
	private Button scanButton;


    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		resultTextView = (TextView)findViewById(R.id.resultTextView);
		scanButton = (Button)findViewById(R.id.scanButton);
		resultTextView.setText("card.io library version: " + CardIOActivity.sdkVersion() + "\nBuilt: " + CardIOActivity.sdkBuildDate());
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();

		if (CardIOActivity.canReadCardWithCamera(this)) {
			scanButton.setText("Scan a credit card with card.io");
		}
		else {
			scanButton.setText("Enter credit card information");
		}
	}
	
	
	
	public void onScanPress(View v) {
	    Intent scanIntent = new Intent(this, CardIOActivity.class);

	    // required for authentication with card.io
	    scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, MY_CARDIO_APP_TOKEN);

	    // customize these values to suit your needs.
	    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
	    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
	    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

	    // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
	    startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}
			
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String resultStr;
		if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
			CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

			// Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
			resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

			// Do something with the raw number, e.g.:
			// myService.setCardNumber( scanResult.cardNumber );

			if (scanResult.isExpiryValid()) {
				resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n"; 
			}

			if (scanResult.cvv != null) { 
				// Never log or display a CVV
				resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
			}

			if (scanResult.postalCode != null) {
				resultStr += "Postal Code: " + scanResult.postalCode + "\n";
			}
		}
		else {
			resultStr = "Scan was canceled.";
		}
		resultTextView.setText(resultStr);

	}


	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
