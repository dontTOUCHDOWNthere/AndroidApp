package activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by rh035578 on 7/20/15.
 */
public class PriceWatcher implements TextWatcher {

    private String current = "";
    private EditText checkPrice;

    public void setEditText(EditText price) {
        checkPrice = price;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!s.toString().equals(current)) {
            checkPrice.removeTextChangedListener(this);

            String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
            String cleanString = s.toString().replaceAll(replaceable, "");

            double parsed;
            try {
                parsed = Double.parseDouble(cleanString);
            } catch (NumberFormatException e) {
                parsed = 0.00;
            }
            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

            current = formatted;
            checkPrice.setText(formatted);
            checkPrice.setSelection(formatted.length());
            checkPrice.addTextChangedListener(this);
        }

    }
}
