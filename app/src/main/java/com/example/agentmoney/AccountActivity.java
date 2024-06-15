package com.example.agentmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// date 和 money 能正常顯示，type就交給各位了
public class AccountActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "agent_money";
    private SQLiteDatabase productDatabase;
    private static final String TABLE_NAME = "account";
    private static final String CREATE_PRODUCT_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(_id INTEGER PRIMARY KEY, date TEXT, money INTEGER)";

    private EditText etDate;
    private EditText etMoney;
    private Button btnInput;
    private Button btnClean;
    private EditText etType;
    private ListView listv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        productDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        productDatabase.execSQL(CREATE_PRODUCT_TABLE_SQL);

        etDate = findViewById(R.id.et_date);
        etMoney = findViewById(R.id.et_money);
        btnInput = findViewById(R.id.btn_input);
        etType = findViewById(R.id.et_type);
        listv = findViewById(R.id.list);
        btnClean = findViewById(R.id.btn_clean);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_input) {
                    String inputDate = etDate.getText().toString();
                    String eachMoney = etMoney.getText().toString();
                    String type = etType.getText().toString();

                    String insertSql = "INSERT INTO " + TABLE_NAME + "(date, money) VALUES ('" + inputDate + "'," + eachMoney + " )";
                    productDatabase.execSQL(insertSql);
                } else if (v.getId() == R.id.btn_clean) {      // 清除
                    String deleteAllSql = "DELETE FROM " + TABLE_NAME;
                    productDatabase.execSQL(deleteAllSql);
                }
                etMoney.setText("");
                etType.setText("");
                etDate.setText("");
                listAllProducts();
            }
        };

        btnInput.setOnClickListener(listener);
        btnClean.setOnClickListener(listener);

    }

    // android.R.layout.simple_list_item_2 只能插兩個值(date, money)，
    // 要嘛自己創個adapter調整，或是用別的方法
    private void listAllProducts(){
        Cursor cursor = productDatabase.query(TABLE_NAME, new String[] {"_id", "date", "money"},
                null, null, null, null, null, null);

        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(AccountActivity.this, android.R.layout.simple_list_item_2,
                cursor, new String[]{"date", "money"}, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        listv.setAdapter(scAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productDatabase.close();
    }
}