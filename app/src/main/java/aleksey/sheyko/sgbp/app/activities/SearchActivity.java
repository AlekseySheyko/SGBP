package aleksey.sheyko.sgbp.app.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.app.fragments.StoreListFragment;


public class SearchActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_containter);
        handleIntent(getIntent());

        StoreListFragment storeListFragment = new StoreListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_container, storeListFragment, null);
        ft.commit();
    }

    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String query) {
        // get a Cursor, prepare the ListAdapter
        // and set it
    }
}
