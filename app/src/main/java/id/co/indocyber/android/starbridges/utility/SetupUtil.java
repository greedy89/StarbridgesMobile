package id.co.indocyber.android.starbridges.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import id.co.indocyber.android.starbridges.network.StringConverter;

public class SetupUtil {

    public static void setupCurrency(final EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            StringConverter stringConverter = new StringConverter();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    editText.removeTextChangedListener(this);

                    //String cleanString = s.toString().replaceAll("[,.]", "");

                    //double parsed = Double.parseDouble(cleanString);
                    //String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
                    String formatted = stringConverter.numberFormat(s.toString());

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
