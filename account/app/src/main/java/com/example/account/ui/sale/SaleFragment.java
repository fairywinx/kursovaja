package com.example.account.ui.sale;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.account.R;


public class SaleFragment extends Fragment {
    ListView saleList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor saleCursor;
    SimpleCursorAdapter saleAdapter;
    Button mButton;
    public SaleFragment() {
        super(R.layout.fragment_sale);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saleList = view.findViewById(R.id.list);
        saleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SaleActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        mButton = view.findViewById(R.id.addButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SaleActivity.class);
                startActivity(intent);
            }
        });
        databaseHelper = new DatabaseHelper(getActivity());
    }
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        saleCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{DatabaseHelper.COLUMN_NAME,DatabaseHelper.COLUMN_AMOUNT, DatabaseHelper.COLUMN_PRICE,DatabaseHelper.COLUMN_PRICE2};
        // создаем адаптер, передаем в него курсор
        saleAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_sale,
                saleCursor, headers, new int[]{R.id.text1, R.id.text2,R.id.text3,R.id.text4}, 0);
        saleList.setAdapter(saleAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        saleCursor.close();
    }
}


