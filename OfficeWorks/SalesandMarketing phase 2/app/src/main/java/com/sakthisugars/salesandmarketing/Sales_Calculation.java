package com.sakthisugars.salesandmarketing;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 7/25/2016.
 */
public class Sales_Calculation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button ok,offersale,regularsale;
    String[] Sales_Total_amt;
    ArrayList<String> list_total;
    TextView tot1;
    TextView Schemename,offeritem,itemqty,itemname,regular_itemname,itemrate;
    String name,item_qty;
    String scheme_name,parent_item,offer_items,rate;
   int totalval;
    DrawerLayout drawer;
    Database_handler database_handler;
    SQLiteDatabase db;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salescalculation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //....................................................................................
        String main_list;
        String total="",result = "";

        Bundle b = getIntent().getExtras();
        String[] resultArr = b.getStringArray("selectedItems");
        //.....
        Intent intentval = getIntent();
        userid = intentval.getStringExtra("userid");
        tot1=(TextView)findViewById(R.id.tot);
        /* String temp = resultArr.toString();
        String qusChoice = temp.substring(1, temp.length() - 1);
        String[] arrayList = qusChoice.split("~");
        ArrayList choiceList = new ArrayList<String>();
       for (int i = 0; i < arrayList.length; i++) {
           // choiceList.add(arrayList[2]);
            String value = arrayList[2];
            String tot = "";
            tot = tot + value;
            tot1.setText(tot);
        }*/

        final ListView lstview=(ListView)findViewById(R.id.listview);

      //  ListView lsttxt=(ListView)findViewById(R.id.txtlist);
       // ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
               // android.R.layout.simple_list_item_1, resultArr);
       // lstview.setAdapter(adapter1);
        // Inflate header view
       // LayoutInflater inflater = getLayoutInflater();
       // ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, lsttxt, false);
       //ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.header, lstview, false);
        // Add header view to the ListView
      // lstview.addHeaderView(headerView);
        // Get the string array defined in strings.xml file
        //String[] items=getResources().getStringArray(R.array.lstview);
       // String[] items=resultArr;
        // Create an adapter to bind data to the ListView
        LstViewAdapter adapter=new LstViewAdapter(this,R.layout.rowlayout,R.id.txtname,resultArr);
        // Bind data to the ListView
        lstview.setAdapter(adapter);
        List<String> newList = Arrays.asList(resultArr);
      //  list_total.addAll(newList);
        if(newList!=null) {
            for (int i = 0; i < newList.size(); i++) {
                main_list = newList.get(i);
               String[] separated = main_list.split("~");
               total = separated[2];
                int val=Integer.parseInt(total);
                totalval=totalval+val;
                result= String.valueOf(totalval);

            }
        }
            tot1.setText(result);


            ok = (Button) findViewById(R.id.ok);
       /*offersale = (Button) findViewById(R.id.offer);
        regularsale = (Button) findViewById(R.id.regular);
        Schemename=(TextView)findViewById(R.id.off_SchemeName);
        itemname=(TextView)findViewById(R.id.off_item_name);
        offeritem=(TextView)findViewById(R.id.offer_item);
        itemqty=(TextView)findViewById(R.id.item_qty);
        itemrate=(TextView)findViewById(R.id.item_rate);
        regular_itemname=(TextView)findViewById(R.id.item_name);*/

            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(lstview==null){
                        Toast.makeText(Sales_Calculation.this, "NotYet to purchased,Please purchase ", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent1 = new Intent(Sales_Calculation.this, Payment_Type.class);
                        intent1.putExtra("userid", userid);
                        startActivity(intent1);
                    }
                }
            });
          /*  offersale.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            regularsale.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle itemdata = getIntent().getExtras();
                    String myVal = itemdata.getString("Purchase_item");
                    String[] array = myVal.split("~");
                    for (int i = 0; i < array.length; i++) {
                        name = array[0];
                        item_qty = array[1];
                        itemname.setText(name);
                        itemqty.setText(item_qty);

                    }
                }
            });*/
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
                Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_LONG).show();
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
                Intent intent2 = new Intent(Sales_Calculation.this, CustomerPage.class);
                intent2.putExtra("userid", userid);
                startActivity(intent2);
               // startActivity(new Intent(Sales_Calculation.this,SchemeSelection.class));
                break;
           /* case R.id.logout:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    }






