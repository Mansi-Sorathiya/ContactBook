package com.example.contact;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class List_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Recycler_Adapter adapter;
    String TAG="YYY";

    ArrayList<DataModel> dataList=new ArrayList<>();
    FloatingActionButton fab;
    MyDataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        recyclerView=findViewById(R.id.recyclerview);
        fab=findViewById(R.id.fab);

        db=new MyDataBase(List_Activity.this);
        getData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(List_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getData()
    {
        Cursor cursor=db.viewData();
        Log.d(TAG, "getData: view"+db.viewData());
        while (cursor.moveToNext())//1
        {
            Integer id=cursor.getInt(0);
            String name=cursor.getString(1);
            String number=cursor.getString(2);
            byte[] imgUrl=cursor.getBlob(3);

            BitmapFactory.Options options=new BitmapFactory.Options();
            Bitmap bitmap=BitmapFactory.decodeByteArray(imgUrl,0,imgUrl.length,options);

            DataModel model=new DataModel(id,name,number,bitmap);
            dataList.add(model);
//            Log.d(TAG, "getData: data="+model.getName());

        }
//        Log.d(TAG, "onCreate: ID="+idList);
//        Log.d(TAG, "onCreate: Name="+nameList);
//        Log.d(TAG, "onCreate: Number="+numList);
//        Log.d(TAG, "onCreate: Number="+imgList);

        adapter=new Recycler_Adapter(List_Activity.this,dataList,db);
        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}