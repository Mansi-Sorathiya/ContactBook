package com.example.contact;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_CAMERA = 100;
    private static final int PICK_IMAGE_GALLERY = 200;
    EditText etxtName, etxtNum;
    Button add;
    ImageView imageView;
    private Bitmap bitmap;
    Uri ImageURI;
    String imgName;
    MyDataBase db;
    int id;
    String name, number;
    final CharSequence[] items = {"Camera", "Gallery"};

    public static File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String base64Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etxtName = findViewById(R.id.eTxtName);
        etxtNum = findViewById(R.id.eTxtNum);
        add = findViewById(R.id.btnAdd);
        imageView = findViewById(R.id.takeImage);

        db = new MyDataBase(MainActivity.this);

        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");

        String image = getIntent().getStringExtra("img");
        Bitmap b=StringToBitMap(image);


        Log.d("Image", "onCreate: ImageURI MainActivity = =" + image);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 500);

        if (getIntent().getExtras() != null) {
            etxtName.setText("" + name);
            etxtNum.setText("" + number);
            imageView.setImageBitmap(b);

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "onClick: Bitmap in Main= " + bitmap);
                if (getIntent().getExtras() == null)
                {
//                    String name1 = etxtName.getText().toString();
//                    String number1 = etxtNum.getText().toString();
//                    String image1 = ImageURI.toString();
//                    db.addData("" + name1, "" + number1, "" + image1);
                    db.addData(etxtName.getText().toString(), etxtNum.getText().toString(), bitmap);
                    Log.d("XXX", "onClick: Bitmap in Main= " + bitmap);
                }
                else
                {
                    String name1 = etxtName.getText().toString();
                    String number1 = etxtNum.getText().toString();
                    String image1 = ImageURI.toString();
                    db.updatedata(id, "" + name1, "" + number1, b);
                }
                Intent intent = new Intent(MainActivity.this, List_Activity.class);
                startActivity(intent);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectImage();
                selectImgOther();
            }
        });

    }

    private Bitmap StringToBitMap(String image) {
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private void selectImgOther() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera")) {
                    launchCamera();

                } else if (items[which].equals("Gallery")) {

                    Intent GalleryIntent = null;
                    GalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GalleryIntent.setType("image/*");
                    GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(GalleryIntent, 0);
                }
            }
        });
        builder.show();

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // If not granted, request the CAMERA permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //from gallery
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {

            Log.d("LLL", "CODE in Gallery=" + requestCode);
            Bundle extras = data.getExtras();
            Uri selectedImageUri = data.getData();
            Log.d("UUU", "onActivityResult: extras=" + selectedImageUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Log.d("XXX", "onActivityResult:Bitmap= " + bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageView.setImageBitmap(bitmap);
            //Bitmap bitmap = (Bitmap) extras.get("data");;
//            ByteArrayOutputStream bytes=new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
//            imageView.setImageBitmap(bitmap);
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.getDefault());
//            String currentDateandTime=sdf.format(new Date());
//            File downloadedFile;
//            downloadedFile = new File(file.getAbsolutePath() + "/IMG_" + currentDateandTime + ".jpg");
//            try {
//                downloadedFile.createNewFile();
//                FileOutputStream fo=new FileOutputStream(downloadedFile);
//                fo.write(bytes.toByteArray());
//                Toast.makeText(MainActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            ImageURI= Uri.parse(downloadedFile.getAbsolutePath());


        } else {
            // Handle the case where the URI is null
            Log.e("LLL", "Gallery: URI is null");
        }

        // for take image from camera
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Get the captured image from the intent's data
            Bundle extras = data.getExtras();
            if (extras != null) {
                bitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(bitmap);
//                if (bitmap != null) {
//                    // The image is in the extras as a bitmap
//                    imageView.setImageBitmap(bitmap);
//
//                    // Save the bitmap to internal storage and get the URI
//                    ImageURI = Uri.parse(saveToInternalStorage(bitmap));
//                    Log.d("MMM", "Main: URI in Main Camera==" + ImageURI);
//                    loadImageFromStorage(savedImgPath);
//                } else {
//                    // Handle the case where the bitmap is null
//                    Log.e("LLL", "Camera: Bitmap is null");
//                }
//            } else {
//                // Handle the case where extras is null
//                Log.e("LLL", "Camera: Extras is null");
            }
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 100);

        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with launching the camera
                launchCamera();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        //imgName="img_"+new Random(10000000)+".jpg";
        //imgName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date() + ".jpg");
        Random r = new Random();
        r.nextInt(1000000);
        imgName = "img_" + r.toString() + ".jpg";
        Log.d("LLL", "saveToInternalStorage: Name=" + imgName);
        File mypath = new File(directory + "/" + imgName);
        Log.d("MMM", "PATH=" + mypath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(mypath);
    }

    private void loadImageFromStorage(String path) {

        try {

            //path=path+imgName;
            Log.d("MMM", "LOOAD: path=" + path + imgName);
            File f = new File(path + imgName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            // ImageView img=(ImageView)findViewById(R.id.imgPicker);

            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
