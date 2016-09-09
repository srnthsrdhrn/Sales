package com.sakthisugars.salesandmarketing;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    Database_handler database_handler;
    SQLiteDatabase db;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
 /*       drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.homepage).setVisible(false);
*/
        // attach to current activity;
        ResideMenu resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.customer_page_background);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = {"Master", "Transaction"};
        int icon[] = {R.drawable.ic_master, R.drawable.ic_transaction};
        final Intent[] intents = new Intent[2];
        intents[0] = new Intent(Homepage.this,Scheme_Master.class);
        intents[1]= new Intent(Homepage.this,Transaction.class);
        for (int i = 0; i < titles.length; i++) {
            final int a = i;
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                startActivity(intents[a]);
                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
            resideMenu.setMenuListener(menuListener);
            /*******************************************************************************************************************/
            database_handler = new Database_handler(this, Database_handler.DATABASE_NAME, null, Database_handler.DATABASE_VERSION);
            db = database_handler.getWritableDatabase();
            //Erase all records at the end of the day
        /*Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)==17){
            database_handler.Erase_All_Records(db);
        }*/
        }
        final Intent[] intents1 = new Intent[2];
        intents1[0]= new Intent(Homepage.this,Report.class);
        intents1[1]= new Intent(Homepage.this,Settings.class);
        String titles1[] = {"Report", "Settings"};
        int icon1[] = {R.drawable.ic_report, R.drawable.ic_setting_light};

        for (int i = 0; i < titles1.length; i++) {
            final int a = i;
            ResideMenuItem item = new ResideMenuItem(this, icon1[i], titles1[i]);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intents1[a]);
                }
            });
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_RIGHT); // or  ResideMenu.DIRECTION_RIGHT
            resideMenu.setMenuListener(menuListener);
            /*******************************************************************************************************************/
            database_handler = new Database_handler(this, Database_handler.DATABASE_NAME, null, Database_handler.DATABASE_VERSION);
            db = database_handler.getWritableDatabase();
            //Erase all records at the end of the day
        /*Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)==17){
            database_handler.Erase_All_Records(db);
        }*/
        }
    }
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(Homepage.this, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(Homepage.this, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };
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
            case R.id.master: {
                drawer.closeDrawer(GravityCompat.START);
                String[] list = new String[]{"Item", "Scheme_Master", "Employee Details"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose")
                        .setSingleChoiceItems(list,3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which) {
                                    case 0:
                                        dialog.dismiss();
                                        //startActivity(new Intent(Homepage.this, Item.class));
                                        Intent intent = new Intent(Homepage.this, Item.class);
                                        intent.putExtra("userid", userid);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        dialog.dismiss();
                                       // startActivity(new Intent(Homepage.this,SchemeDesgin.class));
                                        Intent intent1 = new Intent(Homepage.this, SchemeDesgin.class);
                                        intent1.putExtra("userid", userid);
                                        startActivity(intent1);
                                        //startActivity(new Intent(Homepage.this,SchemeDesgin.class));
                                        break;
                                    case 2:
                                        dialog.dismiss();
                                       // startActivity(new Intent(Homepage.this,EmployeeDetails.class));
                                        Intent intent2 = new Intent(Homepage.this, EmployeeDetails.class);
                                        intent2.putExtra("userid", userid);
                                        startActivity(intent2);
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            break;
            case R.id.transaction:
                Intent intent = new Intent(Homepage.this,Transaction.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
               // startActivity(new Intent(Homepage.this,Transaction.class));
                break;
            case R.id.report:
                Intent intent1 = new Intent(Homepage.this,Report.class);
                intent1.putExtra("userid", userid);
                startActivity(intent1);
                //startActivity(new Intent(Homepage.this,Report.class));
                break;
            case R.id.customer:
                Intent intent2 = new Intent(Homepage.this,CustomerPage.class);
                intent2.putExtra("userid", userid);
                startActivity(intent2);
                break;
            case R.id.settings:
                Intent intent3 = new Intent(Homepage.this,Settings.class);
                intent3.putExtra("userid", userid);
                startActivity(intent3);
               // startActivity(new Intent(Homepage.this,Settings.class));
                break;
            case R.id.logout:
                Intent intent4 = new Intent(Homepage.this, Login.class);
                startActivity(intent4);
                intent4 = new Intent(Intent.ACTION_MAIN);
                intent4.addCategory(Intent.CATEGORY_HOME);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent4);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
