package com.example.agentmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
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
    private Spinner spType;
    private ListView listv;
    private String[] types = new String[] {"請選擇項目類型", "飲食", "交通", "娛樂", "繳費", "收入", "其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        productDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        productDatabase.execSQL(CREATE_PRODUCT_TABLE_SQL);

        etDate = findViewById(R.id.et_date);
        etMoney = findViewById(R.id.et_money);
        btnInput = findViewById(R.id.btn_input);
        spType = findViewById(R.id.sp_type);
        listv = findViewById(R.id.list);
        btnClean = findViewById(R.id.btn_clean);

        listAllProducts();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_input) {
                    String inputDate = etDate.getText().toString();
                    String eachMoney = etMoney.getText().toString();
                    String type = spType.getSelectedItem().toString();
                    if (type.equals(types[0])) {
                        Toast.makeText(AccountActivity.this, "未選擇項目類型", Toast.LENGTH_LONG).show();
                    } else {

                        String productName = etDate.getText().toString();
                        int productPrice = Integer.parseInt(etMoney.getText().toString());
                        // 直接將類型附加在日期的後面
                        inputDate = inputDate + "#" + type;

                        String insertSql = "INSERT INTO " + TABLE_NAME + "(date, money) VALUES ('" + inputDate + "'," + eachMoney + ")";
                        productDatabase.execSQL(insertSql);
                    }
                } else if (v.getId() == R.id.btn_clean) {      // 清除
                    String deleteAllSql = "DELETE FROM " + TABLE_NAME;
                    productDatabase.execSQL(deleteAllSql);
                }
                etMoney.setText("");
                spType.setSelection(0);
                etDate.setText("");
                listAllProducts();
            }
        };

        btnInput.setOnClickListener(listener);
        btnClean.setOnClickListener(listener);

        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, types);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(spAdapter);


    }
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