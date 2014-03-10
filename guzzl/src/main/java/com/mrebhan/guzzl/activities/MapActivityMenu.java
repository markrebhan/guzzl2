package com.mrebhan.guzzl.activities;


import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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
        MenuItem searchItem = menu.findItem(R.id.menu_action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                // DO SOMETHING When Expanded
                return true; // return true to expand view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                // DO Something when Collapsed
                return true; // return true to collapse view
            }
        });

        // Configure the search info and add any event listeners
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
                FragmentTransactions.replaceFuelGauge(this);
                FragmentTransactions.removeInfoBar(this);
                return true;
            case R.id.menu_fuel_stop:
                FragmentTransactions.replaceFuelStop(this);
                FragmentTransactions.removeInfoBar(this);
                return true;
            default:
                return false;
        }
    }

}
