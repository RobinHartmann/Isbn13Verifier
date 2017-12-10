package de.hss.sae.sue.isbn13verifier;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText mEtInput;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtInput = (EditText) findViewById(R.id.etInput);
        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
    }

    public void generateCheckDigit(View view) {
        String input = mEtInput.getText().toString();
        Isbn13Verifier.CalculateCheckDigitResult result = Isbn13Verifier.calculateCheckDigit(input);

        if (!result.isValid()) {
            mToast.setText(result.getRejectionReason());
            mToast.show();
            return;
        }

        Integer checkDigit = result.getCheckDigit();
        String separator = detectSeparator(input);

        if (input.endsWith(separator)) mEtInput.setText(input.concat(checkDigit.toString()));
        else mEtInput.setText(input.concat(separator).concat(checkDigit.toString()));

        mToast.setText(R.string.mainActivityCheckDigitGeneratedSuccessfully);
        mToast.show();
    }

    public void onVerify(View view) {
        String input = mEtInput.getText().toString();
        Isbn13Verifier.VerificationResult result = Isbn13Verifier.isValid(input);

        if (!result.isValid()) {
            mToast.setText(result.getRejectionReason());
            mToast.show();
            return;
        }

        mToast.setText(R.string.mainActivityVerifiedSuccessfully);
        mToast.show();
    }

    private String detectSeparator(String value) {
        boolean containsSpaces = value.contains(" ");
        boolean containsHyphens = value.contains("-");

        if (containsHyphens && containsSpaces) return "";
        if (containsHyphens) return "-";
        if (containsSpaces) return " ";

        return "";
    }
}
