package aleksey.sheyko.sgbp.ui.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.ui.fragments.StoreListFragment;

public class CategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getActionBar().setTitle(getIntent().getStringExtra("category"));

        StoreListFragment storeListFragment = new StoreListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_container, storeListFragment, null);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
