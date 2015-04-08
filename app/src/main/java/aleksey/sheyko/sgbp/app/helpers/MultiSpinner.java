package aleksey.sheyko.sgbp.app.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiSpinner extends Spinner implements
        OnMultiChoiceClickListener, OnCancelListener {

    private SharedPreferences mSharedPrefs;
    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    private String spinnerText;

    public MultiSpinner(Context context, AttributeSet attrSet) {
        super(context, attrSet);

        setSpinnerAdapter(new String[]{"Grade level"});

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    private void setSpinnerAdapter(String[] strings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, strings);
        setAdapter(adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selected[which] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuilder spinnerBuffer = new StringBuilder();
        boolean someUnselected = false;

        Set<String> selectedGradePositions = new HashSet<>();
        for (int i = 0; i < items.size(); i++) {
            if (selected[i]) {
                selectedGradePositions.add(
                        String.valueOf(i));
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        mSharedPrefs.edit()
                .putStringSet("selectedGradePositions", selectedGradePositions)
                .apply();

        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        setSpinnerAdapter(new String[]{spinnerText});
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        if (items == null) return false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        Set<String> selectedGradePositions =
                mSharedPrefs.getStringSet("selectedGradePositions", new HashSet<String>());

        selected = new boolean[items.size()];
        if (selectedGradePositions.size() > 0) {
            for (String gradePositionStr : selectedGradePositions) {
                int gradePosition = Integer.parseInt(gradePositionStr);
                selected[gradePosition] = true;
            }
        }

        // all text on the spinner
        setSpinnerAdapter(new String[]{allText});
        listener.onItemsSelected(selected);
    }

    public interface MultiSpinnerListener {
        void onItemsSelected(boolean[] selected);
    }

    public boolean isMultipleSelected() {
        return spinnerText.contains(",");
    }

    public String getSpinnerText() {
        return spinnerText;
    }
}
