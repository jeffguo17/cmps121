package com.example.jg.cmps121;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

// Credit Card Form reference from:
//   https://github.com/yekmer/credit_card_lib

//Edit Text format reference from:
//   https://stackoverflow.com/questions/11790102/format-credit-card-in-edit-text-in-android


public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        EditText cardNumberField = (EditText) findViewById(R.id.credit_card_edit_text);
        cardNumberField.addTextChangedListener(
                new TextWatcher() {

                    private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
                    private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
                    private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
                    private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
                    private static final char DIVIDER = '-';

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                            s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                        }
                    }


                    private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                        for (int i = 0; i < s.length(); i++) { // chech that every element is right
                            if (i > 0 && (i + 1) % dividerModulo == 0) {
                                isCorrect &= divider == s.charAt(i);
                            } else {
                                isCorrect &= Character.isDigit(s.charAt(i));
                            }
                        }
                        return isCorrect;
                    }

                    private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
                        final StringBuilder formatted = new StringBuilder();

                        for (int i = 0; i < digits.length; i++) {
                            if (digits[i] != 0) {
                                formatted.append(digits[i]);
                                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                                    formatted.append(divider);
                                }
                            }
                        }

                        return formatted.toString();
                    }

                    private char[] getDigitArray(final Editable s, final int size) {
                        char[] digits = new char[size];
                        int index = 0;
                        for (int i = 0; i < s.length() && index < size; i++) {
                            char current = s.charAt(i);
                            if (Character.isDigit(current)) {
                                digits[index] = current;
                                index++;
                            }
                        }
                        return digits;
                    }
                }
        );


        EditText cardExpirationField = (EditText) findViewById(R.id.credit_card_expiration_date_editText);
        cardExpirationField.addTextChangedListener(
                new TextWatcher() {

                    private static final int TOTAL_SYMBOLS = 5; // size of pattern 01/19
                    private static final int TOTAL_DIGITS = 4; // max numbers of digits in pattern: 0000 x 4
                    private static final int DIVIDER_MODULO = 3; // means divider position is every 5th symbol beginning with 1
                    private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
                    private static final char DIVIDER = '/';

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                            s.replace(0, s.length(), buildCorrecntString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                        }
                    }


                    private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                        for (int i = 0; i < s.length(); i++) { // chech that every element is right
                            if (i > 0 && (i + 1) % dividerModulo == 0) {
                                isCorrect &= divider == s.charAt(i);
                            } else {
                                isCorrect &= Character.isDigit(s.charAt(i));
                            }
                        }
                        return isCorrect;
                    }

                    private String buildCorrecntString(char[] digits, int dividerPosition, char divider) {
                        final StringBuilder formatted = new StringBuilder();

                        for (int i = 0; i < digits.length; i++) {
                            if (digits[i] != 0) {
                                formatted.append(digits[i]);
                                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                                    formatted.append(divider);
                                }
                            }
                        }

                        return formatted.toString();
                    }

                    private char[] getDigitArray(final Editable s, final int size) {
                        char[] digits = new char[size];
                        int index = 0;
                        for (int i = 0; i < s.length() && index < size; i++) {
                            char current = s.charAt(i);
                            if (Character.isDigit(current)) {
                                digits[index] = current;
                                index++;
                            }
                        }
                        return digits;
                    }
                }
        );
    }

    public void submitCard(View view) {
        // TODO: replace with your own test key
        final String publishableApiKey = BuildConfig.DEBUG ?
                "pk_test_6pRNASCoBOKtIshFeQd4XMUh" :
                getString(R.string.com_stripe_publishable_key);

        EditText cardNumberField = (EditText) findViewById(R.id.credit_card_edit_text);
        EditText expirationDate = (EditText) findViewById(R.id.credit_card_expiration_date_editText);
        EditText cvcField = (EditText) findViewById(R.id.credit_card_cvc);

        Card card = new Card(cardNumberField.getText().toString(),
                Integer.valueOf(expirationDate.getText().toString().substring(0,1)),
                Integer.valueOf(expirationDate.getText().toString().substring(3)),
                cvcField.getText().toString());

        if (!card.validateNumber()) {
            cardNumberField.getBackground()
                    .setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC);
        } else {
            cardNumberField.getBackground().clearColorFilter();
        }

        if (!card.validateExpiryDate()) {
            expirationDate.getBackground()
                    .setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC);
        } else {
            expirationDate.getBackground().clearColorFilter();
        }

        if (!card.validateCVC()) {
            cvcField.getBackground()
                    .setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC);
        } else {
            cvcField.getBackground().clearColorFilter();
        }

        Stripe stripe = new Stripe();
        stripe.createToken(card, publishableApiKey, new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                Toast.makeText(
                        getApplicationContext(),
                        "Token created: " + token.getId(),
                        Toast.LENGTH_LONG).show();
            }

            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }
}
