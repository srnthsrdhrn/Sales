package com.sakthisugars.salesandmarketing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.List;

public class SchemeSelection extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private List<Scheme_data> content_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private Scheme_data_adapter Adapter;
    ProgressDialog progressDialog;
    Button regular_sales;
   // String Scheme_url= "http://192.168.1.28:900/Android.svc/GetSchemeList/Develop/M";
  //  String Scheme_url="http://192.168.1.28:900/Android.svc/SaveSchemeDetail?UserId=DEVELOP&SchemeName=0&Description=0&Type=0&ParentId=0&ItemId=0&Rate=0&PerDiscount=0&Discount=0&FreeItem=0&Key=C&SchemeDetId=0&SchemeUId=0";
    String Scheme_url="http://192.168.1.28:900//Android.svc/SaveSchemeDetail?UserId=DEVELOP&SchemeName=111&Description=111&Type=0&ParentId=0&ItemId=0&Rate=0&PerDiscount=0&Discount=0&FreeItem=0&Key=C&SchemeDetId=0&SchemeUId=0";
    //String Scheme_url="http://192.168.1.28:900/Android.svc/SaveSchemeDetail?UserId=DEVELOP&SchemeName=0&Description=0&Type=0&ParentId=0&ItemId=0&Rate=0&PerDiscount=0&Discount=0&FreeItem=0&Key=C&SchemeDetId=0&SchemeUId=0";
   //String Scheme_url1="http://192.168.1.28:900/Android.svc/SaveSchemeDetail?UserId=DEVELOP&SchemeName=0&Description=0&Type=0&ParentId=0&ItemId=0&Rate=0&PerDiscount=0&Discount=0&FreeItem=0&Key=C&SchemeDetId=0&SchemeUId=24";
    String LOGIN_URL,parentitemid,parentitemname,qtyvalue;
    ArrayAdapter<String> list_view_main_adapter;
    String name;
    DrawerLayout drawer;
    Database_handler database_handler;
    SQLiteDatabase db;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheme_selection_drawer);
        Intent intentval = getIntent();
        userid = intentval.getStringExtra("userid");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//........................................................................................
        new fetchitemname().execute("");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Adapter = new Scheme_data_adapter(content_list);
        //list_view_main_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, (List<String>) Adapter);
        // recyclerView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(Adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {


            @Override
           public void onClick(View view,int position){
                Scheme_data feedItem = content_list.get(position);
                Toast.makeText(SchemeSelection.this, feedItem.getScheme_name(), Toast.LENGTH_SHORT).show();
           // public void onClick (AdapterView<?> arg0, View arg1, int position, long arg3){
               // Scheme_data feedItem =new  Scheme_data();
               // Scheme_data_adapter.MyViewHolder holder = (Scheme_data_adapter.MyViewHolder) view.getTag();
               // int positions = holder.getPosition();
              // Toast.makeText(SchemeSelection.this, "Please enter is" + position, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        regular_sales = (Button) findViewById(R.id.sales);
        regular_sales.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent1 = new Intent(SchemeSelection.this, Regular_Sales.class);
                intent1.putExtra("userid", userid);
                startActivity(intent1);
            }
        });
        // prepareSchemeData("one by two","Computer-2","Laptop -1",5,1);
    }
    boolean flag = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(flag){
                super.onBackPressed();
            }else{
                Toast.makeText(this,"Press Back Again to Exit",Toast.LENGTH_LONG).show();
                flag=true;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.customerpage:
                finish();
                Intent intent2 = new Intent(SchemeSelection.this, CustomerPage.class);
                intent2.putExtra("userid", userid);
                startActivity(intent2);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void gotodetails() {
        Intent intent=new Intent(getApplicationContext(),Scheme_data.class);
    }

/*  public void prepareSchemeData(String name,String main_items, String offer_items, int discount_value, int discount_type) {
        Scheme_data content = new Scheme_data();
        content.setScheme_name(name);
        content.setMain_items(main_items);
        content.setOffer_items(offer_items);
        content.setDiscount_amt(discount_value);
        content.setDiscount_value_type(discount_type);
        content_list.add(content);
        Adapter.notifyDataSetChanged();
    }*/
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private class  fetchitemname extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SchemeSelection.this);

        }

        @Override
        protected String doInBackground(String... params) {
            String string = "";

            try {

                LOGIN_URL = Scheme_url;
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
                            Toast.makeText(SchemeSelection.this, "Slow Internet Connection \n Check your connection and Try Again", Toast.LENGTH_SHORT).show();
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
                JSONArray jsonArray = new JSONArray(jsonObject.optString("SaveSchemeDetailResult"));
                Log.i(Item.class.getName(),
                        "Number of entries00000000000 " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                  //  ItemDetails info = new ItemDetails();

                    // info.setCode(jsonObject.optString("ItemCode"));
                   // info.setItemId(jsonObject.optString("ItemId"));
                    Scheme_data content = new Scheme_data();
                    content.setScheme_name(jsonObject.optString("SchemeName"));
                    content.setScheme_id(jsonObject.optString("SchemeUId"));
                    content.setMain_items(jsonObject.optString("ParentName"));
                    content.setOffer_items(jsonObject.optString("ItemName"));
                    content.setDiscount_amt(jsonObject.optInt("ValueDiscount"));
                    content.setPer_Discount_amt(jsonObject.optInt("PercentDiscount"));
                    content.setfree_item(jsonObject.optString("FreeItem"));
                    content_list.add(content);
                    Adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //  spinner_main = (Spinner) findViewById(R.id.spinner_main);
          /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(Regular_Sales.this, android.R.layout.simple_spinner_item, Itemnamelist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            popupSpinner.setAdapter(adapter);
            popupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    parentitemname= Itemname.get(position).getName();
                    parentitemid = Itemname.get(position).getItemId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

        }

    }

}
