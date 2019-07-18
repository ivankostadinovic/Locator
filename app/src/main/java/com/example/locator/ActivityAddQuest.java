package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityAddQuest extends ActivityBase implements View.OnClickListener {


    private User user;
    private Button btnCamera, btnAttach, btnAddItem, btnAddQuest;
    private List<Button> pageButtons, outlineButtons;
    private EditText editName, editDescr, editLocation, editHint;
    private List<ImageView> imageViews;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_IMAGE = 2;
    private List<Item> items;
    private int currentItem;
    private int currentImage;
    private Quest quest;
    private boolean ok = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quest);

        initializeComponents();


    }


    public void controlsToItem() {
        items.get(currentItem).setName(editName.getText().toString());
        items.get(currentItem).setHint(editHint.getText().toString());
        items.get(currentItem).setDescription(editDescr.getText().toString());
        items.get(currentItem).setLocation(editLocation.getText().toString());

    }

    public void itemToControls(Item item) {
        editName.setText(item.getName());
        editHint.setText(item.getHint());
        editDescr.setText(item.getDescription());
        editLocation.setText(item.getLocation());
        int i = 0;
        if (item.getImages().size() != 0)
            for (String b : items.get(currentItem).getImages()) {
                imageViews.get(i).setImageBitmap(StringToBitMap(b));
                byte bt[] = b.getBytes();
                int p = bt.length;
                i++;
            }
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == REQUEST_IMAGE_CAPTURE)
            {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageViews.get(currentImage++).setImageBitmap(imageBitmap);


                new Thread(new Runnable() {
                    public void run() {
                        items.get(currentItem).addImage(BitMapToString(imageBitmap));

                    }
                }).start();

            }
        else if (requestCode == SELECT_IMAGE)
        {
                if (data != null) {
                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        imageViews.get(currentImage++).setImageBitmap(bitmap);

                        new Thread(new Runnable() {
                            public void run() {
                                items.get(currentItem).addImage(BitMapToString(bitmap));

                            }
                        }).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 50) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
    }



                   /* new Thread(new Runnable() {
        public void run() {
        };
    }).start();*/

    @Override
    public void initializeComponents() {


        outlineButtons = new ArrayList<Button>();
        pageButtons = new ArrayList<Button>();
        imageViews = new ArrayList<ImageView>();
        items = new ArrayList<Item>();
        currentItem = 0;
        editDescr = (EditText) findViewById(R.id.item_description);
        editHint = (EditText) findViewById(R.id.item_hint);
        editLocation = (EditText) findViewById(R.id.item_location);
        editName = (EditText) findViewById(R.id.item_name);

        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnAttach = (Button) findViewById(R.id.btn_attach);
        btnAddQuest = (Button) findViewById(R.id.btn_add_quest);
        btnAddItem = (Button) findViewById(R.id.btn_add_item);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAddQuest.super.onBackPressed();
            }
        });
        for (int i = 1; i < 4; i++) {
            ImageView imgView = (ImageView) findViewById(getResources().getIdentifier("img_" + Integer.toString(i), "id", getPackageName()));
            imageViews.add(imgView);
        }
        for (int i = 1; i < 7; i++) {
            items.add(new Item());
            Button button = (Button) findViewById(getResources().getIdentifier("btn_page_" + Integer.toString(i) + "_outline", "id", getPackageName()));
            outlineButtons.add(button);
            Button button1 = (Button) findViewById(getResources().getIdentifier("btn_page_" + Integer.toString(i), "id", getPackageName()));
            button1.setOnClickListener(this);
            pageButtons.add(button1);

        }

        btnAddItem.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnAttach.setOnClickListener(this);
        btnAddQuest.setOnClickListener(this);

    }

    public void clearTexts() {
        editDescr.setText("");
        editHint.setText("");
        editName.setText("");
        editLocation.setText("");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_page_1:
                pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                controlsToItem();
                pageButtons.get(0).setVisibility(View.INVISIBLE);
                currentItem = 0;
                itemToControls(items.get(0));
                break;
            case R.id.btn_page_2:
                pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                controlsToItem();
                pageButtons.get(1).setVisibility(View.INVISIBLE);
                currentItem = 1;
                itemToControls(items.get(1));
                break;
            case R.id.btn_page_3:
                pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                controlsToItem();
                pageButtons.get(2).setVisibility(View.INVISIBLE);
                currentItem = 2;
                itemToControls(items.get(2));
                break;
            case R.id.btn_page_4:
                pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                controlsToItem();
                pageButtons.get(3).setVisibility(View.INVISIBLE);
                currentItem = 3;
                itemToControls(items.get(3));
                break;
            case R.id.btn_page_5:
                pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                controlsToItem();
                pageButtons.get(4).setVisibility(View.INVISIBLE);
                currentItem = 4;
                itemToControls(items.get(4));
                break;
            case R.id.btn_page_6:
                pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                controlsToItem();
                pageButtons.get(5).setVisibility(View.INVISIBLE);
                currentItem = 5;
                itemToControls(items.get(5));
                break;

            case R.id.btn_attach:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                break;
            case R.id.btn_camera:
                if (ContextCompat.checkSelfPermission(ActivityAddQuest.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(ActivityAddQuest.this, new String[]{Manifest.permission.CAMERA}, 50);
                else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.btn_add_item:
                controlsToItem();
                //items.get(currentItem).getImages().size()==3
                if (!emptyCheck(new EditText[]{editName, editDescr, editHint, editLocation}))
                    return;
                else if (true) {
                    pageButtons.get(currentItem + 1).setEnabled(true);
                    outlineButtons.get(currentItem + 1).setVisibility(View.VISIBLE);
                    pageButtons.get(currentItem + 1).setVisibility(View.INVISIBLE);
                    pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                    currentItem++;
                    currentImage=0;
                    clearTexts();
                    Toast.makeText(ActivityAddQuest.this, "Item added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ActivityAddQuest.this, "All 3 images required", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.btn_add_quest:
                if (items.size() > 3) {
                    Intent i = new Intent(this, ActivitySubmitQuest.class);
                    i.putExtra("Items", (Serializable) items);
                    startActivity(i);

                } else {
                    Toast.makeText(ActivityAddQuest.this, "Atleast 3 items required", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}
