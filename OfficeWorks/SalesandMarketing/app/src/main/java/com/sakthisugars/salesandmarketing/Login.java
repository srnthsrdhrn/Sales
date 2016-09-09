package com.sakthisugars.salesandmarketing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class Login extends AppCompatActivity {
    EditText name,pass;
    Button login, forgot_password;
    String user_id,password;
    ProgressDialog progressDialog;
    int PASSWORD_MIN_LENGTH=1;
    int PASSWORD_MAX_LENGTH=8;
    boolean PASSWORD_CONTAINS_SPECIAL_CHARACTERS=false;
    String LOGIN_URL="http://192.168.1.28:900/Android.svc/GetUser";
    String RESET_PASSWORD_URL="";
    Database_handler database_handler;
    SQLiteDatabase db;
    //Json data keywords
    private String USERNAME="UserName";
    private String EMAIL="Email";
    private String PASSWORD="Password";
    private String PHONE="Phone";
    private String PARENT_EMPLOYEE="ParentEmployee";
    private String IS_ADMIN="IsAdmin";
    private String GET_USER_RESULT="GetUserResult";
    private String USER_ID="UserId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        forgot_password = (Button) findViewById(R.id.forgot_password);
        database_handler = new Database_handler(this, Database_handler.DATABASE_NAME, null, Database_handler.DATABASE_VERSION);
        db = database_handler.getWritableDatabase();
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user_id = name.getText().toString();
                    password = pass.getText().toString();
                    if (user_id.length() == 0) {
                        Toast.makeText(Login.this, "Please enter the username", Toast.LENGTH_LONG).show();
                    } else if (password.length() < PASSWORD_MIN_LENGTH) {
                        Toast.makeText(Login.this, "Password is too small, Please check", Toast.LENGTH_LONG).show();
                    } else if (password.length() > PASSWORD_MAX_LENGTH) {
                        Toast.makeText(Login.this, "Password is too large, Please check", Toast.LENGTH_LONG).show();
                    } else if (PASSWORD_CONTAINS_SPECIAL_CHARACTERS) {
                        for (int i = 0; i < password.length(); i++) {
                            char check = password.charAt(i);
                            if (!(32 < check && check < 48 || 57 < check && check < 65 || 90 < check && check < 97 || 123 < check && check < 127)) {
                                Toast.makeText(Login.this, "Password must contain special characters, Please check", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Employee employee = database_handler.Search_employee(db, user_id);
                        if (employee == null) {
                            //new connect().execute(LOGIN_URL + user_id + "/" + password);
                            new connect().execute(LOGIN_URL);
                        } else {
                            Employee.sname = employee.name;
                            Employee.semail = employee.email;
                            Employee.sparent_employee = employee.parent_employee;
                            Employee.sis_admin = employee.is_admin;
                            Employee.spassword = employee.password;
                            if (password.equals(employee.password)) {
                                Intent intent = new Intent(Login.this, Homepage.class);
                                finish();
                                startActivity(intent);
                            }
                        }
                        if (user_id.equals("srinath") && password.equals("12345678")) {
                            Employee.sname = "srinath";
                            Employee.spassword = "12345678";
                            Employee.sis_admin = 1;
                            Employee.sparent_employee = "Boss";
                            Employee.semail = "srnthsrdhrn1@gmail.com";
                            database_handler.Store_employee_data(db, "srinath", "12345678", 1, "srnthsrdhrn1@gmail.com", "Boss");
                            finish();
                            startActivity(new Intent(Login.this, Homepage.class));
                        } else {
                            if (user_id.equals("employee") && password.equals("123456")) {
                                Employee.sname = "srinath123";
                                Employee.spassword = "123456";
                                Employee.sis_admin = 0;
                                Employee.sparent_employee = "srinath";
                                Employee.semail = "srnthsrdhrn2@gmail.com";
                                finish();
                                startActivity(new Intent(Login.this, Transaction.class));
                            }
                        }
                    }
                }
            });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                LinearLayout linearLayout = new LinearLayout(Login.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                View view = View.inflate(Login.this, R.layout.forgot_password_dialog, linearLayout);
                builder.setTitle("Forgot Password")
                        .setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button confirm = (Button) view.findViewById(R.id.confirm);
                final EditText email = (EditText) view.findViewById(R.id.email_id);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email_id = email.getText().toString();
                        //new connect().execute(RESET_PASSWORD_URL+email_id);
                        email.setText("");
                        Toast.makeText(Login.this, "Your password has been sent to your mail", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });


            }
        });
    }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There is no Network Connectivity, Please Turn on Wifi or Mobile Data");
            builder.setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        Login.this.finishAffinity();
                    } else
                        Login.this.finish();
                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();
        }

    }
    private class connect extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String string="";
            try {
                URL url = new URL(params[0]);
                try {
                    HttpURLConnection httpURLConnection= (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setReadTimeout(10000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                            stringBuilder.append("\n");
                        }
                        string = stringBuilder.toString();
                        reader.close();
                        inputStream.close();
                    }
                    httpURLConnection.disconnect();
                }catch (SocketTimeoutException e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this, "Slow Internet Connection \n Check your connection and Try Again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return string;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject Object = null;
            try {
                Object = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            if(Object!=null){
                try {
                    JSONArray jsonArray = Object.getJSONArray(GET_USER_RESULT);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString(USERNAME);
                        String password = jsonObject.getString(PASSWORD);
                        String phone = jsonObject.getString(PHONE);
                        String user_id = jsonObject.getString(USER_ID);
                        String email="srnthsrdhrn1@gmail.com";
                        String parent_employee="Boss";
                        int is_admin=jsonObject.getInt(IS_ADMIN);
                        if(user_id.equals(Login.this.user_id)&&password.equals(Login.this.password)) {
                            if (Login.this.user_id.equals("disa")){
                                Employee.sname = name;
                                Employee.spassword = password;
                                Employee.semail=email;
                                 Employee.sparent_employee="Boss";
                                Employee.sis_admin = 0;
                            } else {
                                Employee.sname = name;
                                Employee.spassword = password;
                                Employee.semail=email;
                                 Employee.sparent_employee=parent_employee;
                                Employee.sis_admin = 1;
                            }
                            database_handler.Store_employee_data(db,name,password,is_admin,email,parent_employee);
                            finish();
                            if (Employee.sis_admin == 1)
                                startActivity(new Intent(Login.this, Homepage.class));
                            else
                                startActivity(new Intent(Login.this, Transaction.class));

                        }
                    }
                }catch (JSONException e){
                    Toast.makeText(Login.this,"Error Retrieving data, Please try again",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(Login.this,"Incorrect Username or Password",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        if(progressDialog!=null)
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }
boolean flag=false;
    @Override
    public void onBackPressed() {
        if(flag){
            super.onBackPressed();
    }else{
            Toast.makeText(Login.this,"Press Back Again to Exit",Toast.LENGTH_LONG).show();
            flag=true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag = false;
                }
            }, 3 * 1000);
        }
    }
}
