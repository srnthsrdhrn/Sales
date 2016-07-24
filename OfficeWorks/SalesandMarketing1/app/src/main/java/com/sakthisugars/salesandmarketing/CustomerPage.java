package com.sakthisugars.salesandmarketing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.doubleclick.CustomRenderedAd;

public class CustomerPage extends AppCompatActivity {
    Button overdue,new_purchase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);
        overdue= (Button) findViewById(R.id.overdue);
        new_purchase= (Button) findViewById(R.id.new_purchase);
        new_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerPage.this,SchemeSelection.class));
            }
        });
        overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerPage.this,OverduePage.class));
            }
        });
    }
}
