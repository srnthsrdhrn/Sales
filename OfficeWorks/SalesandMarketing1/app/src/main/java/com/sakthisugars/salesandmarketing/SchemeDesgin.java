package com.sakthisugars.salesandmarketing;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SchemeDesgin extends AppCompatActivity {
    Button add_item_main, add_item_offer;
    RadioButton percent, amount;
    EditText value_offer, qty_main, qty_offer, scheme_id;
    ListView listView_main, listView_offer;
    Spinner spinner_main, spinner_offer;
    Button save, update, cancel, delete;
    Database_handler database_handler;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);

        //Initializing Database
        database_handler = new Database_handler(this, Database_handler.DATABASE_NAME, null, Database_handler.DATABASE_VERSION);
        db = database_handler.getWritableDatabase();

        //Initializing views
        add_item_main = (Button) findViewById(R.id.add_item_main);
        add_item_offer = (Button) findViewById(R.id.add_item_offer);
        percent = (RadioButton) findViewById(R.id.percent);
        amount = (RadioButton) findViewById(R.id.amount);
        value_offer = (EditText) findViewById(R.id.value);
        qty_main = (EditText) findViewById(R.id.quantity_main);
        qty_main.setText("0");
        qty_offer = (EditText) findViewById(R.id.quantity_offer);
        qty_offer.setText("0");
        listView_main = (ListView) findViewById(R.id.listView);
        listView_offer = (ListView) findViewById(R.id.listView_offer);
        spinner_main = (Spinner) findViewById(R.id.spinner_main);
        spinner_offer = (Spinner) findViewById(R.id.spinner_offer);
        scheme_id = (EditText) findViewById(R.id.scheme_id);
        //Setting adapter for spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.item_stocks, R.layout.list_view_row);
        spinner_main.setAdapter(adapter);
        spinner_offer.setAdapter(adapter);

        //initializing views for the footer buttons
        save = (Button) findViewById(R.id.save);
        update = (Button) findViewById(R.id.update);
        cancel = (Button) findViewById(R.id.cancel);
        delete = (Button) findViewById(R.id.delete);

        //Setting adapter for the listView_main
        final ArrayList<String> list_main = new ArrayList<>();
        final ArrayAdapter<String> list_view_main_adapter = new ArrayAdapter<String>(this, R.layout.list_view_row, list_main);
        listView_main.setAdapter(list_view_main_adapter);


        //Setting adapter for the listView_offer
        final ArrayList<String> list_offer = new ArrayList<>();
        final ArrayAdapter<String> list_view_offer_adapter = new ArrayAdapter<String>(this, R.layout.list_view_row, list_offer);
        listView_offer.setAdapter(list_view_offer_adapter);

        //setting on click listener for the add item button in the main section in the top
        add_item_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = spinner_main.getSelectedItem().toString();
                try {
                    int quantity = Integer.parseInt(qty_main.getText().toString());
                    String text = item + "-" + quantity;
                    String check_quantity;
                    for(int i=0;i<list_main.size();i++){
                        String text_item=list_main.get(i);
                        String check="";
                        for(int j=0;j<text_item.length();j++){
                            if(text_item.charAt(j)=='-'){
                                check=text_item.substring(0,j);
                                check_quantity=text_item.substring(j+1);
                                int qty=Integer.parseInt(check_quantity);
                                if(check.equals(item)){
                                    quantity+=qty;
                                    text= item + "-"+ quantity;
                                    break;
                                }
                            }
                        }


                    }
                    list_main.add(text);
                    list_view_main_adapter.notifyDataSetChanged();
                    qty_main.setText("0");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(SchemeDesgin.this, "Please Enter a Quantity", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //Onclick listener for the add item button in the offer section in the bottom
        add_item_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = spinner_offer.getSelectedItem().toString();
                try {
                    int quantity = Integer.parseInt(qty_offer.getText().toString());
                    String text = item + "-" + quantity;
                    list_offer.add(text);
                    list_view_offer_adapter.notifyDataSetChanged();
                    qty_offer.setText("0");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(SchemeDesgin.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }/*
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scheme_data scheme_data = new Scheme_data();
                for (int i = 0; i < list_main.size(); i++) {
                    scheme_data.main_items += list_main.get(i) + ",";
                    list_main.remove(i);
                }
                for (int i = 0; i < list_offer.size(); i++) {
                    scheme_data.offer_items += list_offer.get(i) + ",";
                    list_offer.remove(i);
                }
                scheme_data.discount_value = Integer.parseInt(value_offer.getText().toString());
                value_offer.setText("");
                if (percent.isChecked()) {
                    scheme_data.discount_value_type = 1;
                    percent.setChecked(false);
                } else {
                    scheme_data.discount_value_type = 0;
                    amount.setChecked(false);
                }
                int id = Integer.parseInt(scheme_id.getText().toString());
                database_handler.Scheme_update(db, scheme_data, id);
            }
        });


        //Checking while the value is entered in the scheme id field
        scheme_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = scheme_id.getText().toString();
                if (!text.equals("")) {
                    try {
                        long id = Integer.parseInt(text);
                        Scheme_data scheme_data = database_handler.Scheme_retrieve(db, id);
                        if (scheme_data != null) {
                            String[] scheme_main_items = string_to_item(scheme_data.main_items);
                            for (int i = 0; i < scheme_main_items.length; i++) {
                                list_main.add(i, scheme_main_items[i]);
                            }
                            list_view_main_adapter.notifyDataSetChanged();
                            String[] scheme_offer_items = string_to_item(scheme_data.offer_items);
                            for (int i = 0; i < scheme_offer_items.length; i++) {
                                list_offer.add(i, scheme_offer_items[i]);
                            }
                            list_view_offer_adapter.notifyDataSetChanged();
                            value_offer.setText(scheme_data.discount_value);
                            if (scheme_data.discount_value_type == 1) {
                                percent.setChecked(true);
                            } else {
                                amount.setChecked(true);
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scheme_data scheme_data = new Scheme_data();
                for (int i = 0; i < list_main.size(); i++) {
                    scheme_data.main_items += list_main.get(i) + ",";
                }
                list_main.clear();
                list_view_main_adapter.notifyDataSetChanged();
                for (int i = 0; i < list_offer.size(); i++) {
                    scheme_data.offer_items += list_offer.get(i) + ",";
                }
                list_offer.clear();
                list_view_offer_adapter.notifyDataSetChanged();
                scheme_data.discount_value = Integer.parseInt(value_offer.getText().toString());
                value_offer.setText("");
                if (percent.isChecked()) {
                    scheme_data.discount_value_type = 1;
                    percent.setChecked(false);
                } else {
                    scheme_data.discount_value_type = 0;
                    amount.setChecked(false);
                }
                long id = database_handler.Scheme_insertions(db, scheme_data);
                Toast.makeText(Scheme.this, "Scheme ID is " + id, Toast.LENGTH_LONG).show();
            }
        });
    }
    public String[] string_to_item(String s){
        int count = 0;
        for( int i=0; i<s.length(); i++ ) {
            if( s.charAt(i) == ',' ) {
                count++;
            }
        }
        String[] items = new String[count];
        int item_counter=0;
        int occurrence_counter=0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)==','){
                items[item_counter]=s.substring(occurrence_counter+1,i-1);
                occurrence_counter++;
            }
        }
        return items;
    }*/
}

