package aleksey.sheyko.sgbp.ui.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.ui.fragments.StoreListFragment;

public class CategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_containter);

        getActionBar().setTitle(getIntent().getStringExtra("category"));

        StoreListFragment storeListFragment = new StoreListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_container, storeListFragment, null);
        ft.commit();
    }
}
