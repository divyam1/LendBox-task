package com.example.lendbox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.support.design.widget.Snackbar;
import com.example.lendbox.Interface.SignupAPI;
import com.example.lendbox.Model.SignUpRequest;
import com.example.lendbox.Utils.ApiUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {
    MaterialEditText edtamountBorrow,fname,lname,username,password,mobile;
    MaterialSpinner title,loanCity,loanPurpose,loanDuration,roi;
    CheckBox c1,c2,c3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        initView();
        showSnack(checkConnection());

         edtamountBorrow = findViewById(R.id.amount_to_borrow);
         fname =  findViewById(R.id.fname);
         lname =  findViewById(R.id.lname);
         username =  findViewById(R.id.username);
         password =  findViewById(R.id.password);
         mobile =  findViewById(R.id.mobile);


        title= findViewById(R.id.title);
        loanCity = findViewById(R.id.loan_city);
        loanDuration = findViewById(R.id.loan_duration);
        loanPurpose = findViewById(R.id.loan_purpose);
        roi= findViewById(R.id.roi);

         c1 = findViewById(R.id.checkBox1);
         c2 = findViewById(R.id.checkBox2);
         c3 = findViewById(R.id.checkBox3);
        Button button = findViewById(R.id.btsignup);

        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard(view);
                    //showSnack(checkConnection());
                    checkConditions();


                }
            });

    }

    private void checkConditions() {
        boolean check=false ;
        if(TextUtils.isEmpty(edtamountBorrow.getText())||!isInRange(Integer.parseInt(edtamountBorrow.getText().toString()))) {
            edtamountBorrow.setError("Please enter a value greater than or equal to 25000");
            check=true;
        }
        if(loanPurpose == null || loanPurpose.getSelectedItem() ==null ) {
            loanPurpose.setError("Select a loan purpose");
            check=true;
        }
        if(roi == null || roi.getSelectedItem() ==null ) {
            roi.setError("Select expected interest rate");
            check=true;
        }
        if(loanDuration == null || loanDuration.getSelectedItem() ==null ) {
            loanDuration.setError("Select a loan Duration");
            check=true;
        }
        if(loanCity == null || loanCity.getSelectedItem() ==null ) {
            loanCity.setError("Select a loan city");
            check=true;
        }
        if(title == null || title.getSelectedItem() ==null ) {
            title.setError("Select a title");
             check=true;
        }
         if(TextUtils.isEmpty(fname.getText()))
        {
            fname.setError("Please enter your first name");
            check=true;
        }
         if(!isEmailValid(username.getText().toString()))
        {
            username.setError("Please enter a valid email address ");
            check=true;
        }
         if(TextUtils.isEmpty(lname.getText()))
        {
            lname.setError("Please enter your last name");
            check=true;
        }
         if(password.getText().toString().length()<6)
        {
            password.setError("Your password must be at least 6 characters long");
            check=true;
        }
         if(mobile.getText().toString().length()<10)
        {
            mobile.setError("Please enter at least 10 characters");
            check=true;
        }

        if(check!=true) {
            if (!c1.isChecked() || !c2.isChecked()) {
                if (!c1.isChecked()) {
                    c1.setError("Accept");
                } else
                    c1.setError(null, null);
                if (!c2.isChecked())
                    c2.setError("Accept");
                else
                    c2.setError(null, null);
            } else {
                c1.setError(null, null);
                c2.setError(null, null);
                sendRequest();
            }
        }

    }

    private void sendRequest() {
        SignUpRequest signUpRequest = new SignUpRequest();
        String strpurpose= loanPurpose.getSelectedItem().toString();

        if(strpurpose.equals("Appliance Purchase"))
            signUpRequest.setLoanPurpose(6);
        else if(strpurpose.equals("Business Funding"))
            signUpRequest.setLoanPurpose(11);
        else if(strpurpose.equals("Buying a Property"))
            signUpRequest.setLoanPurpose(273);
        else if(strpurpose.equals("Car/2-wheeler Purchase"))
            signUpRequest.setLoanPurpose(9);
        else if(strpurpose.equals("Credit Card Bill Consolidation"))
            signUpRequest.setLoanPurpose(261);
        else if(strpurpose.equals("Debt Consolidation"))
            signUpRequest.setLoanPurpose(8);
        else if(strpurpose.equals("Education"))
            signUpRequest.setLoanPurpose(7);
        else if(strpurpose.equals("Family Event"))
            signUpRequest.setLoanPurpose(129);
        else if(strpurpose.equals("Home Furnishing"))
            signUpRequest.setLoanPurpose(21);
        else if(strpurpose.equals("Home Improvement"))
            signUpRequest.setLoanPurpose(22);
        else if(strpurpose.equals("Medical"))
            signUpRequest.setLoanPurpose(23);
        else if(strpurpose.equals("Travel/Vacation"))
            signUpRequest.setLoanPurpose(24);
        else if(strpurpose.equals("Wedding"))
            signUpRequest.setLoanPurpose(25);

        signUpRequest.setUserType("BORROWER");


            signUpRequest.setAmount(Integer.parseInt(edtamountBorrow.getText().toString()));
            signUpRequest.setFname(fname.getText().toString());
            signUpRequest.setLname(lname.getText().toString());
            signUpRequest.setMobile((mobile.getText().toString()));
            signUpRequest.setPassword(password.getText().toString());
            signUpRequest.setUsername(username.getText().toString());
            signUpRequest.setLoanCity(loanCity.getSelectedItem().toString());
            signUpRequest.setLoanDuration(Integer.parseInt(loanDuration.getSelectedItem().toString()));

            signUpRequest.setRoi(removeLastChar(roi.getSelectedItem().toString()));
            signUpRequest.setTitle(title.getSelectedItem().toString());

         /*   SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setTitle("Mr.");
            signUpRequest.setLoanDuration(24);
            signUpRequest.setLoanCity("Jaipur");
            signUpRequest.setUserType("BORROWER");
            signUpRequest.setUsername("deanotedis@gmail.com");
            signUpRequest.setFname("Siya");
            signUpRequest.setLname("Mirza");
            signUpRequest.setLoanPurpose(24);
            signUpRequest.setAmount(125000);
            signUpRequest.setMobile("1258467469");
            signUpRequest.setRoi("12.50");*/

            /*signUpRequest.getAmount()
                    ,signUpRequest.getFname(),signUpRequest.getLname(),signUpRequest.getUsername(),signUpRequest.getPassword(),
                    signUpRequest.getMobile(),signUpRequest.getTitle(),signUpRequest.getLoanDuration(),signUpRequest.getRoi(),
                    signUpRequest.getLoanCity(),signUpRequest.getLoanPurpose(),signUpRequest.getUserType()*/
            //  Log.d("Filter", new Gson().toJson(signUpRequest));
            /**/
            // Log.d("Filter",signUpResponseCall.request().toString());
            callRetrofit(signUpRequest);


    }

    public void callRetrofit(SignUpRequest signUpRequest){

        SignupAPI service = ApiUtils.getAPIService();
        Call<String> signUpResponseCall = service.getSignupacess(signUpRequest);
        signUpResponseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Filter", String.valueOf(response.body()));
                int code = response.code();

                //  SignUpResponse signUpResponse = response.body();
                Intent intent = new Intent(SignUpActivity.this, WebViewActivity.class);
                intent.putExtra("html", response.toString());
                startActivity(intent);

                String errorBody = null;
                if (response.errorBody() != null) {
                    // Get response errorBody
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(response + "\n" + response.body());
                Log.d("Signup ", "onResponse " + code + " " + errorBody + " " + response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


                Log.d("Signup ", "onfailure: " + t.getMessage());
                Log.d("Filter ", "Call: " + call.request().toString());
            }
        });
    }


    public void initView(){

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.loan_purpose));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        loanPurpose = (MaterialSpinner) findViewById(R.id.loan_purpose);
        loanPurpose.setAdapter(adapter1);
        loanPurpose.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                hideKeyboard(v);
                return false;
            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.roi));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        roi = (MaterialSpinner) findViewById(R.id.roi);
        roi.setAdapter(adapter2);
        roi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                hideKeyboard(v);
                return false;
            }
        });
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.loan_duration));
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        loanDuration = (MaterialSpinner) findViewById(R.id.loan_duration);
        loanDuration.setAdapter(adapter3);
        loanDuration.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                hideKeyboard(v);
                return false;
            }
        });

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.loan_city));
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        loanCity= (MaterialSpinner) findViewById(R.id.loan_city);
        loanCity.setAdapter(adapter4);
        loanCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                hideKeyboard(v);
                return false;
            }
        });
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.title));
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        title = (MaterialSpinner) findViewById(R.id.title) ;
        title.setAdapter(adapter5);
        title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                hideKeyboard(v);
                return false;
            }
        });

        TextView myTextView1 = (TextView) findViewById(R.id.textView1);
        myTextView1.setText( Html.fromHtml("I have read and agree to the  " +
                "  <a href='https://www.lendbox.in/privacy-policy'>Privacy Policy </a> " + ","+
                "  <a href='https://www.lendbox.in/terms-of-use'>Terms of Use </a> "+
                "and Consent to Electronic Disclosures"));
        myTextView1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView myTextView2 = (TextView) findViewById(R.id.textView2);
        myTextView2.setText("I authorize Lendbox to make any enquiries with any finance company/bank/registered credit ");
        myTextView2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView myTextView3 = (TextView) findViewById(R.id.textView3);
        myTextView3.setText("I agree that Lendbox can share my details with affiliated NBFCs to assist in fast loan processing (Optional) ");
        myTextView3.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.relativelayout),message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    private boolean isInRange(int c) {
        return 500000 > 25000 ? c >= 25000 && c <= 500000 : c >= 25000 && c <= 500000;
    }
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}
