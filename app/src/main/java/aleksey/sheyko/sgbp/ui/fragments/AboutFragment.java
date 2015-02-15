package aleksey.sheyko.sgbp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aleksey.sheyko.sgbp.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.header1))
                .setText(getResources().getString(R.string.header1));
        ((TextView) view.findViewById(R.id.body1))
                .setText(getResources().getString(R.string.body1));

        ((TextView) view.findViewById(R.id.header2))
                .setText(getResources().getString(R.string.header2));
        ((TextView) view.findViewById(R.id.body2))
                .setText(getResources().getString(R.string.body2));

        ((TextView) view.findViewById(R.id.header3))
                .setText(getResources().getString(R.string.header3));
        ((TextView) view.findViewById(R.id.body3))
                .setText(getResources().getString(R.string.body3));

        ((TextView) view.findViewById(R.id.header4))
                .setText(getResources().getString(R.string.header4));
        ((TextView) view.findViewById(R.id.body4))
                .setText(getResources().getString(R.string.body4));

        ((TextView) view.findViewById(R.id.header5))
                .setText(getResources().getString(R.string.header5));
        ((TextView) view.findViewById(R.id.body5))
                .setText(getResources().getString(R.string.body5));

        ((TextView) view.findViewById(R.id.header6))
                .setText(getResources().getString(R.string.header6));
        ((TextView) view.findViewById(R.id.body6))
                .setText(getResources().getString(R.string.body6));
    }
}
