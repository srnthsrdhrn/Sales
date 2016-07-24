package com.sakthisugars.salesandmarketing;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Item extends AppCompatActivity {
    ProgressDialog progressDialog;
    String Itemlist_url = "http://192.168.1.28:900/Android.svc/GetItemList/Develop/I";
    String save_url1 = "http://192.168.1.28:900/Android.svc/SaveItemStock/Develop/S";
    String save_url=" http://192.168.1.28:900/Android.svc/SaveItemStock?";
    String LOGIN_URL;
    TextView uom;
    String rateofitem,itemid,qty,uomid,rateval,rate;
    TextView txtitemcode, txtitemid, txtstockcount, txtuom, txtuomid;
    EditText txtrate,txtqty;
    Button save,cancel;
    String userid;
    Spinner mySpinner;
    public ArrayList<ItemDetails> ItemDetailsList = new ArrayList<ItemDetails>();
    public ArrayList<String> ItemList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        uom=(TextView)findViewById(R.id.uom);
        txtqty=(EditText)findViewById(R.id.qty);
        txtrate=(EditText)findViewById(R.id.rate);
        //txtitemcode = (TextView) findViewById(R.id.itemcode);
      //  txtitemid = (TextView) findViewById(R.id.itemid);
       // txtstockcount = (TextView) findViewById(R.id.stockcount);
      //  txtuom = (TextView) findViewById(R.id.uom);
       // txtuomid = (TextView) findViewById(R.id.uomid);
       // rate = (EditText) findViewById(R.id.rate);
        new connect().execute("");
        save.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                //itemid=txtitemid.getText().toString();
                // rate = String.valueOf(txtrate.getText());
                rate = txtrate.getText().toString();
                qty = txtqty.getText().toString();
                if (qty.length() != 0) {
                    if (rate.length() != 0) {
                        new save().execute("");
                        txtqty.setText("");
                        txtrate.setText("");
                    } else
                        Toast.makeText(Item.this, "Please enter the Qty", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Item.this, "Please enter the Rate", Toast.LENGTH_LONG).show();

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtqty.setText("");
                txtrate.setText("");
            }
            });

    }

    private class connect extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Item.this);

        }

        @Override
        protected String doInBackground(String... params) {
            String string = "";

            try {

                LOGIN_URL = Itemlist_url;
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
                            Toast.makeText(Item.this, "Slow Internet Connection \n Check your connection and Try Again", Toast.LENGTH_SHORT).show();
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
                JSONArray jsonArray = new JSONArray(jsonObject.optString("GetItemListResult"));
                Log.i(Item.class.getName(),
                        "Number of entries00000000000 " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    ItemDetails info = new ItemDetails();

                    info.setCode(jsonObject.optString("ItemCode"));
                    info.setItemId(jsonObject.optString("ItemId"));
                    info.setStockCount(jsonObject.optString("StockCount"));
                    info.setUOM(jsonObject.optString("UOM"));
                    info.setUOMId(jsonObject.optString("UOMId"));
                    ItemDetailsList.add(info);
                    ItemList.add(jsonObject.optString("ItemName"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mySpinner = (Spinner) findViewById(R.id.my_spinner);
            mySpinner.setAdapter(new ArrayAdapter<String>(Item.this, android.R.layout.simple_spinner_dropdown_item, ItemList));
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    // Locate the textviews in activity_main.xml
                    // txtitemcode = (TextView) findViewById(R.id.itemcode);
                     //txtitemid = (TextView) findViewById(R.id.itemid);
                     //txtstockcount = (TextView) findViewById(R.id.stockcount);
                    // txtuom = (TextView) findViewById(R.id.uom);
                    // txtuomid = (TextView) findViewById(R.id.uomid);

                    // Set the text followed by the position
                   // txtitemcode.setText(ItemDetailsList.get(position).getCode());
                   // txtitemid.setText(ItemDetailsList.get(position).getItemId());
                    itemid=ItemDetailsList.get(position).getItemId();
                 //   txtstockcount.setText(ItemDetailsList.get(position).getStockCount());
                    uom.setText(ItemDetailsList.get(position).getUOM());
                   // txtuomid.setText(ItemDetailsList.get(position).getUOMId());
                    uomid = ItemDetailsList.get(position).getUOMId();
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
           // Intent intent = getIntent();
          //  userid = intent.getStringExtra("userid");
            String string = "";
            try {

               // String Combine_txt1 = "/"+itemid + "/" + qty + "/" + uomid + "/" + rateofitem;
                String Combine_txt  = "UserId="+"develop"+"&"+ "Key=S"+"&"+"ItemId=" + itemid+"&"+"Qty="+qty+"&"+"UOMId="+uomid+"&"+"Rate="+rate;
                // UserId=Develop&Key=S&ItemId=1&Qty=111&UOMId=4&Rate=222;
                Log.v("The new URL is", String.valueOf(Combine_txt));
                LOGIN_URL = save_url + Combine_txt;
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
                            Toast.makeText(Item.this, "Slow Internet Connection \n Check your connection and Try Again", Toast.LENGTH_SHORT).show();
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
                    String value = jsonResultObject.getString("SaveItemStockResult");
                    Log.v("The Result", String.valueOf(value));
                    Toast.makeText(Item.this,value, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(Item.this, (CharSequence) e, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    boolean flag = false;
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}


