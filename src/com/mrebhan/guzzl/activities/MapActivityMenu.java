package com.mrebhan.guzzl.activities;




import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.mrebhan.guzzl.R;

/**
 * This subclass of the base activity handles Menu item events
 */

public class MapActivityMenu extends BaseMapActivity {

	// create a new interface that starts up showcarpicker fragment when called
	//ShowCarPicker mShowCarPicker = (ShowCarPicker) this; // this refers to a subclass of this class
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_map_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_modify_car:
			// Pop the carpicker fragment to select/mofify car settings
			Fragment fragment = new CarPickerPageViewer();
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(R.id.frameLayout_main, fragment);
			ft.addToBackStack(null);
			ft.commit();
		default:
			return false;
		}
	}

}
