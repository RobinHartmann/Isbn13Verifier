package de.hss.sae.sue.isbn13verifier;

import android.content.res.Resources;

/**
 * Created by robin.hartmann on 30.01.2017.
 */
public class Isbn13Verifier {
    private static Resources mResources;

    static {
        mResources = App.getContext().getResources();
    }

    private Isbn13Verifier() {
    }

    public static VerificationResult isValid(String value) {
        if (value == null || value.equals("")) return new VerificationResult(mResources.getString(R.string.verificationResultEmptyInput));

        /*
         * A 13-digit ISBN can be separated into its parts
         * (prefix element, registration group, registrant, publication and check digit),
         * and when this is done it is customary to separate the parts with hyphens or spaces
         */
        value = value.replace("-", "").replace(" ", "");

        if (value.length() != 13) return new VerificationResult(mResources.getString(R.string.verificationResultInvalidLength));
        if (!value.matches("\\d*")) return new VerificationResult(mResources.getString(R.string.verificationResultInvalidCharacter));

        CalculateCheckDigitResult checkDigitResult = calculateCheckDigit(value.substring(0, 12));

        if (!checkDigitResult.isValid()) return new VerificationResult(checkDigitResult.getRejectionReason());

        int inputCheckDigit = Integer.parseInt(value.substring(12));
        int calculatedCheckDigit = checkDigitResult.getCheckDigit().intValue();

        if (inputCheckDigit != calculatedCheckDigit) return new VerificationResult(mResources.getString(R.string.verificationResultInvalidCheckDigit, calculatedCheckDigit, inputCheckDigit));

        return new VerificationResult(true);
    }

    public static CalculateCheckDigitResult calculateCheckDigit(String value) {
        if (value == null || value.equals("")) return new CalculateCheckDigitResult(mResources.getString(R.string.calculateCheckDigitResultEmptyInput));

        /*
         * A 13-digit ISBN can be separated into its parts
         * (prefix element, registration group, registrant, publication and check digit),
         * and when this is done it is customary to separate the parts with hyphens or spaces
         */
        value = value.replace("-", "").replace(" ", "");

        if (value.length() != 12) return new CalculateCheckDigitResult(mResources.getString(R.string.calculateCheckDigitResultInvalidLength));
        if (!value.matches("\\d*")) return new CalculateCheckDigitResult(mResources.getString(R.string.verificationResultInvalidCharacter));

        return new CalculateCheckDigitResult(calculateCheckDigitInternal(value));
    }

    private static int calculateCheckDigitInternal(String value) {
        char[] chars = value.toCharArray();
        int[] digits = new int[12];

        for (int i = 0; i < chars.length; i++) {
            digits[i] = Integer.parseInt(String.valueOf(chars[i]));
        }

        int sum = 0;

        for (int i = 0; i < digits.length; i++) {
            sum += digits[i] * Math.pow(3, i % 2);
        }

        int differenceToNextMultipleOf10 = sum % 10;
        return (10 - differenceToNextMultipleOf10) % 10;
    }

    public static class VerificationResult {
        private boolean mIsValid;
        private String mRejectionReason;

        VerificationResult(boolean isValid) {
            this(isValid, null);
        }

        VerificationResult(String rejectionReason) { this(false, rejectionReason); }

        private VerificationResult(boolean isValid, String rejectionReason) {
            if (!isValid && rejectionReason == null) throw new IllegalArgumentException(mResources.getString(R.string.Isbn13VerifierResultIllegalArgument));
            mIsValid = isValid;
            mRejectionReason = rejectionReason;
        }

        public boolean isValid() {
            return mIsValid;
        }
        public String getRejectionReason() {
            return mRejectionReason;
        }
    }

    public static class CalculateCheckDigitResult {
        private Integer mCheckDigit;
        private boolean mIsValid;
        private String mRejectionReason;

        CalculateCheckDigitResult(int checkDigit) {
            this(checkDigit, true, null);
        }

        CalculateCheckDigitResult(String rejectionReason) { this(null, false, rejectionReason); }

        private CalculateCheckDigitResult(Integer checkDigit, boolean isValid, String rejectionReason) {
            if (!isValid && rejectionReason == null) throw new IllegalArgumentException(mResources.getString(R.string.Isbn13VerifierResultIllegalArgument));
            mCheckDigit = isValid ? checkDigit : null;
            mIsValid = isValid;
            mRejectionReason = rejectionReason;
        }

        public Integer getCheckDigit() { return mCheckDigit; }
        public boolean isValid() {
            return mIsValid;
        }
        public String getRejectionReason() {
            return mRejectionReason;
        }
    }
}
