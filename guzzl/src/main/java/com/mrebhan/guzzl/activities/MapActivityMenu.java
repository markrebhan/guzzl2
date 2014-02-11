package com.mrebhan.guzzl.activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mrebhan.guzzl.R;
import com.mrebhan.guzzl.utils.FragmentTransactions;

/**
 * This subclass of the base activity handles Menu item events
 */

public class MapActivityMenu extends CheckConfigurationActivity {

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
                FragmentTransactions.replaceCarPicker(this);
                FragmentTransactions.removeInfoBar(this);
                return true;
            case R.id.menu_modify_fuel:
                Bundle bundle = new Bundle();
                FragmentTransactions.replaceFuelGauge(this);
                FragmentTransactions.removeInfoBar(this);
            default:
                return false;
        }
    }

}
