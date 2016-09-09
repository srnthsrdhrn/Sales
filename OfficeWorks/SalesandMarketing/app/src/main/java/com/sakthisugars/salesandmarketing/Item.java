package com.sakthisugars.salesandmarketing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class Item extends AppCompatActivity {
    Button check;
    TextView item_description,item_stock,item_price;
    String item_uom;
    EditText item_name;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        check= (Button) findViewById(R.id.save);
        item_name= (EditText) findViewById(R.id.item_name);
        item_description= (TextView) findViewById(R.id.item_description);
        item_price= (TextView) findViewById(R.id.item_price);
        item_stock= (TextView) findViewById(R.id.item_stock);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check.setVisibility(View.INVISIBLE);
                check.setEnabled(false);

            }
        });

    }

    @Override
    protected void onDestroy() {
        if(progressDialog!=null)
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    private class connect extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Item.this);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
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
                            publishProgress(stringBuilder.length(),httpURLConnection.getContentLength());
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
                            Toast.makeText(Item.this, "Slow Internet Connection \n Check your connection and Try Again", Toast.LENGTH_SHORT).show();
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
        protected void onProgressUpdate(Integer... values) {
            int data_received=values[0];
            int total=values[1];
            int progress=data_received/total*100;
            progressDialog.setProgress(progress);
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
}
