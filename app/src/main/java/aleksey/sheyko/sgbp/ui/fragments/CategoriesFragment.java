package aleksey.sheyko.sgbp.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.HashMap;
import java.util.Map;

import aleksey.sheyko.sgbp.R;
import aleksey.sheyko.sgbp.adapters.CategoriesAdapter;
import aleksey.sheyko.sgbp.helpers.Constants;
import aleksey.sheyko.sgbp.ui.activities.CategoryActivity;

public class CategoriesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.categoriesGrid);
        gridView.setAdapter(new CategoriesAdapter(this.getActivity()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, String> categories = new HashMap<>();
                categories.put("0", Constants.CATEGORY_FOOD);
                categories.put("1", Constants.CATEGORY_AUTO);
                categories.put("2", Constants.CATEGORY_SOUND);
                categories.put("3", Constants.CATEGORY_BODY_CARE);
                categories.put("4", Constants.CATEGORY_HOTELS);

                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("category", categories.get(position + ""));
                startActivity(intent);
            }
        });
        return rootView;
    }
}
