package com.example.account.ui.sale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.account.R;
import com.example.account.ui.sale.DatabaseHelper;
import com.example.account.ui.sale.SaleFragment;

public class SaleActivity extends Activity {
    EditText nameText, amountText, priceText,price2Text;
    Button delButton,saveButton;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor saleCursor;
    long saleId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        nameText = findViewById(R.id.name);
        amountText = findViewById(R.id.amount);
        priceText = findViewById(R.id.price);
        price2Text = findViewById(R.id.price2);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        price2Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer s = Integer.parseInt(String.valueOf(amountText.getText())) * Integer.parseInt(String.valueOf(priceText.getText()));
                price2Text.setText(s.toString());
            }
        });
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            saleId = extras.getLong("id");
        }
        // если 0, то добавление
        if (saleId > 0) {
            // получаем элемент по id из бд
            saleCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(saleId)});
            saleCursor.moveToFirst();
            nameText.setText(saleCursor.getString(1));
            amountText.setText(String.valueOf(saleCursor.getInt(2)));
            priceText.setText(String.valueOf(saleCursor.getInt(3)));
            price2Text.setText(String.valueOf(saleCursor.getInt(4)));
            saleCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameText.getText().toString());
        cv.put(DatabaseHelper.COLUMN_AMOUNT, Integer.parseInt(amountText.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_PRICE, Integer.parseInt(priceText.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_PRICE2, Integer.parseInt(price2Text.getText().toString()));

        if (saleId > 0) {
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + saleId, null);
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(saleId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, SaleFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

