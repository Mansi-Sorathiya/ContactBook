package com.example.contact;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.Holder>
{
    List_Activity listActivity;
    ArrayList<DataModel> dataList;
    MyDataBase db;
    private android.util.Base64 Base64;


    public Recycler_Adapter(List_Activity listActivity, ArrayList<DataModel> dataList, MyDataBase db) {
        this.listActivity=listActivity;
        this.dataList=dataList;
        this.db=db;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(listActivity).inflate(R.layout.list_item,parent,false);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txtName.setText(""+dataList.get(position).getName());
        holder.txtNum.setText(""+dataList.get(position).getNumber());
//        loadImageFromStorage(String.valueOf(dataList.get(position)),holder.imageView);

        //Glide.with(holder.itemView).load(dataList.get(position).getImgUrl()).into(holder.imageView);
        holder.imageView.setImageBitmap(dataList.get(position).getImgUrl());

        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(listActivity,holder.optionMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.update) {
                            Dialog dialog = new Dialog(listActivity);
                            dialog.setContentView(R.layout.update);
                            EditText txt1, txt2;
                            TextView add;
                            txt1 = dialog.findViewById(R.id.updatename);
                            txt2 = dialog.findViewById(R.id.updatenumber);

                            ImageView updateimage=dialog.findViewById(R.id.updateimage);

                            txt1.setText(dataList.get(position).getName());
                            txt2.setText(dataList.get(position).getNumber());
                            updateimage.setImageBitmap(dataList.get(position).getImgUrl());

                            dialog.show();

                            Button updateButton = dialog.findViewById(R.id.btnAdd);
                            updateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MyDataBase mydataBase1 = new MyDataBase(listActivity.getApplicationContext());
                                    mydataBase1.updatedata(dataList.get(position).getId(),txt1.getText().toString(),txt2.getText().toString(),dataList.get(position).getImgUrl());

                                    Intent intent = new Intent(listActivity,MainActivity.class);
                                    listActivity.startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                            updateimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });


                        }
                        if(menuItem.getItemId()==R.id.delete) {
                            db.deleteData(dataList.get(position).getId());
                            dataList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(listActivity, "Item Deleted", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtName,txtNum;
        ImageView imageView;
        ImageView optionMenu;
        public Holder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.name);
            txtNum=itemView.findViewById(R.id.number);
            imageView=itemView.findViewById(R.id.item_Img);
            optionMenu=itemView.findViewById(R.id.optionMenu);
        }
    }

    private void loadImageFromStorage(String path, ImageView imageView) {

        try {
            File f = new File(path);
            Log.d("AAA", "loadImageFromStorage: ImgPath=" + path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            // ImageView img=(ImageView)findViewById(R.id.imgPicker);
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
