package com.sakthisugars.salesandmarketing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;

public class EmployeeDetails  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    RadioButton yes,no;
    ProgressDialog progressDialog;
    Button save, cancel;
    TextView departmentid,designationid;
    EditText txtincentive;
    Spinner spn_emp,spn_head;
    public ArrayList<EmpDetails_Data> Emp_Details = new ArrayList<EmpDetails_Data>();
    public ArrayList<String>Empid_Detailslist = new ArrayList<String>();
    public ArrayList<String>Empheadid_Detailslist = new ArrayList<String>();
    String emp_details_url = "http://192.168.1.28:900/Android.svc/GetEmployeeDetail?UserId=Develop&Key=E&EmployeeId=0";
    String emp_details_save_url="http://192.168.1.28:900/Android.svc/SaveEmployeeDetail?";
    String LOGIN_URL,Deptid,Designationid,username,spn_emp_id,spn_head_id,incentive,Is_admin;
    String spn_headid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_master);
        Intent intent = getIntent();
        username = intent.getStringExtra("userid");
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        txtincentive=(EditText)findViewById(R.id.emp_incentive);
        departmentid=(TextView)findViewById(R.id.dept_id);
        designationid=(TextView)findViewById(R.id.designation_id);
        spn_emp = (Spinner)findViewById(R.id.spn_emp_id);
        spn_head = (Spinner)findViewById(R.id.spn_head_id);
        yes=(RadioButton)findViewById(R.id.yes);
        no=(RadioButton)findViewById(R.id.no);
        //...............................menu bar...........................
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_Menu = navigationView.getMenu();
        //........................manubar ..........................
        new fetchitemname().execute("");
        save.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                incentive=txtincentive.getText().toString();
                if(incentive=="")
                {
                    Toast.makeText(EmployeeDetails.this, "Enter Incentive", Toast.LENGTH_LONG).show();
                }
                else{
                    new save().execute("");
                }

            }
        });
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    boolean flag = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (flag) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
                flag = true;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.homepage:
                finish();
                startActivity(new Intent(EmployeeDetails.this, Homepage.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void RadioButtonClicked(View view) {

//This variable will store whether the user was male or female
        Is_admin = "";
// Check that the button is  now checked?
        boolean checked = ((RadioButton) view).isChecked();

// Check which radio button was clicked
        switch (view.getId()) {
            case R.id.no:
                if (checked)
                    yes.setChecked(false);
                    Is_admin = "0";
                break;
            case R.id.yes:
                if (checked)
                   no.setChecked(false);
                    Is_admin = "1";
                break;
        }
    }
    private class  fetchitemname extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EmployeeDetails.this);

        }

        @Override
        protected String doInBackground(String... params) {
            String string = "";

            try {

                LOGIN_URL = emp_details_url;
                URL url = new URL(LOGIN_URL);
                Log.v("The new URL is", String.valueOf(url));
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setReadTimeout(10000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EmployeeDetails.this, "Slow Internet Connection \n Check your connection and Try Again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                } catch (IOException e) {
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
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(jsonObject.optString("GetEmployeeDetailResult"));
                Log.i(Item.class.getName(),
                        "Number of entries00000000000 " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                   EmpDetails_Data Emp_info = new EmpDetails_Data();

                    // info.setCode(jsonObject.optString("ItemCode"));
                    Emp_info.setEmpid(jsonObject.optString("EmployeeId"));
                    Emp_info.setHeadid(jsonObject.optString("HeadId"));
                    Emp_info.setDeptid(jsonObject.optString("DepartmentId"));
                    Emp_info.setDesignationid(jsonObject.optString("DesignationId"));
                    //  info.setStockCount(jsonObject.optString("StockCount"));
                    //info.setUOM(jsonObject.optString("UOM"));
                    // info.setUOMId(jsonObject.optString("UOMId"));
                    Emp_Details.add(Emp_info);
                    Empid_Detailslist.add(jsonObject.optString("EmployeeId"));
                    Empheadid_Detailslist.add(jsonObject.optString("HeadId"));


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //  spinner_main = (Spinner) findViewById(R.id.spinner_main);
            ArrayAdapter<String> adapter_emp = new ArrayAdapter<String>(EmployeeDetails.this, android.R.layout.simple_spinner_item, Empid_Detailslist);
            adapter_emp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_emp.setAdapter(adapter_emp);
            spn_emp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    spn_emp_id = spn_emp.getSelectedItem().toString();
                    spn_headid = Emp_Details.get(position).getHeadid();
                    Log.v("The new URL is", String.valueOf(spn_headid));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ArrayAdapter<String> adapter_head = new ArrayAdapter<String>(EmployeeDetails.this, android.R.layout.simple_spinner_item, Empheadid_Detailslist);
            adapter_head.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_head.setAdapter(adapter_head);
           // spn_head.setSelection(spn_headid);
            spn_head.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    spn_head_id = spn_head.getSelectedItem().toString();
                    Deptid= Emp_Details.get(position).getDeptid();
                    Designationid = Emp_Details.get(position).getDesignationid();
                    departmentid.setText(Deptid);
                    designationid.setText(Designationid);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

    }
    private class save extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String string = "";
            try {

                // String Combine_txt1 = "/"+itemid + "/" + qty + "/" + uomid + "/" + rateofitem;
                String Combine_txt = "UserId=" + username + "&" + "Key=S" + "&" + "EmployeeId=" + spn_emp_id + "&" + "HeadId=" + spn_head_id + "&" + "Incentive=" + incentive + "&" + "IsAdmin=" + Is_admin;
                // UserId=Develop&Key=S&EmployeeId=1&HeadId=1&Incentive=252.23&IsAdmin=0"
                Log.v("The new URL is", String.valueOf(Combine_txt));
                LOGIN_URL = emp_details_save_url + Combine_txt;
                URL url = new URL(LOGIN_URL);
                Log.v("The new URL is", String.valueOf(url));
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setReadTimeout(10000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EmployeeDetails.this, "Slow Internet Connection \n Check your connection and Try Again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                } catch (IOException e) {
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
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                Log.v("The new URL is", String.valueOf(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            if (jsonObject != null) {
                try {
                    JSONObject jsonResultObject = new JSONObject(String.valueOf(jsonObject));
                    String value = jsonResultObject.getString("SaveEmployeeDetailResult");
                    Log.v("The Result", String.valueOf(value));
                    Toast.makeText(EmployeeDetails.this, value, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(EmployeeDetails.this, (CharSequence) e, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
