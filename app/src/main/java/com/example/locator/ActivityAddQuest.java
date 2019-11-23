package com.example.locator;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private Item shownItem;
    private int currentItemIndex;
    private int currentImage;
    private Quest quest;
    private boolean removeItem = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quest);

        initializeComponents();


    }

    public void addItem() {

        shownItem.setName(editName.getText().toString());
        shownItem.setHint(editHint.getText().toString());
        shownItem.setDescription(editDescr.getText().toString());
        shownItem.setLocation(editLocation.getText().toString());
        items.add(shownItem);
    }

    public void controlsToItem() {
        if (currentItemIndex > items.size() - 1) {
            return;
        }
        shownItem.setName(editName.getText().toString());
        shownItem.setHint(editHint.getText().toString());
        shownItem.setDescription(editDescr.getText().toString());
        shownItem.setLocation(editLocation.getText().toString());
        items.set(currentItemIndex, shownItem);

    }

    public void itemToControls(Item item) {
        shownItem = item;
        editName.setText(item.getName());
        editHint.setText(item.getHint());
        editDescr.setText(item.getDescription());
        editLocation.setText(item.getLocation());
        int i = 0;
        imageViews.get(0).setImageDrawable(getDrawable(R.drawable.ok2));
        imageViews.get(1).setImageDrawable(getDrawable(R.drawable.ok2));
        imageViews.get(2).setImageDrawable(getDrawable(R.drawable.ok2));
        if (item.getImages().size() != 0)
            for (String b : item.getImages()) {
                imageViews.get(i).setImageBitmap(StringToBitMap(b));
                i++;
            }
        currentImage = item.getImages().size();
        if (shownItem.getImages().size() == 3) {
            btnAttach.setEnabled(false);
            btnCamera.setEnabled(false);
        } else {
            btnAttach.setEnabled(true);
            btnCamera.setEnabled(true);
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
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");

                imageViews.get(currentImage++).setImageBitmap(imageBitmap);

                if (shownItem.getImages().size() == 2) {
                    btnAttach.setEnabled(false);
                    btnCamera.setEnabled(false);
                }

                new Thread(new Runnable() {
                    public void run() {
                        shownItem.addImage(BitMapToString(imageBitmap));

                    }
                }).start();

            } else if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                        imageViews.get(currentImage++).setImageBitmap(bitmap);

                        if (shownItem.getImages().size() == 2) {
                            btnAttach.setEnabled(false);
                            btnCamera.setEnabled(false);
                        }
                        new Thread(new Runnable() {
                            public void run() {
                                shownItem.addImage(BitMapToString(bitmap));
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


        outlineButtons = new ArrayList<>();
        pageButtons = new ArrayList<>();
        imageViews = new ArrayList<>();
        items = new ArrayList<>();
        shownItem = new Item();
        currentItemIndex = 0;
        editDescr = findViewById(R.id.item_description);
        editHint = findViewById(R.id.item_hint);
        editLocation = findViewById(R.id.item_location);
        editName = findViewById(R.id.item_name);

        btnCamera = findViewById(R.id.btn_camera);
        btnAttach = findViewById(R.id.btn_attach);
        btnAddQuest = findViewById(R.id.btn_add_quest);
        btnAddItem = findViewById(R.id.btn_add_item);


        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAddQuest.super.onBackPressed();
            }
        });
        for (int i = 1; i < 4; i++) {
            ImageView imgView = findViewById(getResources().getIdentifier("img_" + Integer.toString(i), "id", getPackageName()));
            imageViews.add(imgView);
        }
        for (int i = 1; i < 7; i++) {

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
        imageViews.get(0).setImageDrawable(getDrawable(R.drawable.ok2));
        imageViews.get(1).setImageDrawable(getDrawable(R.drawable.ok2));
        imageViews.get(2).setImageDrawable(getDrawable(R.drawable.ok2));
        btnAttach.setEnabled(true);
        btnCamera.setEnabled(true);
        shownItem = new Item();
        currentImage = 0;
    }

    public void setUpPages(int index) {
        if (currentItemIndex == index) {
            return;
        }
        pageButtons.get(currentItemIndex).setVisibility(View.VISIBLE);
        controlsToItem();
        pageButtons.get(index).setVisibility(View.INVISIBLE);
        currentItemIndex = index;
        if (index > items.size() - 1) {
            btnAddItem.setText("Add item");
            clearTexts();
            removeItem = false;
        } else {
            btnAddItem.setText("Remove item");
            itemToControls(items.get(index));
            removeItem = true;

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_page_1:
                setUpPages(0);
                break;
            case R.id.btn_page_2:
                setUpPages(1);
                break;
            case R.id.btn_page_3:
                setUpPages(2);
                break;
            case R.id.btn_page_4:
                setUpPages(3);
                break;
            case R.id.btn_page_5:
                setUpPages(4);
                break;
            case R.id.btn_page_6:
                setUpPages(5);
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
                if (removeItem) {
                    items.remove(currentItemIndex);
                    clearTexts();
                    removeItem = false;
                    btnAddItem.setText("Add item");
                    return;
                }
                //items.get(currentItemIndex).getImages().size()==3
                if (!emptyCheck(new EditText[]{editName, editDescr, editHint, editLocation}))
                    return;
                else if (true) {// check for 3 images
                    addItem();
                    if (currentItemIndex == 5) {
                        setUpPages(0);
                    } else {
                        pageButtons.get(currentItemIndex).setVisibility(View.VISIBLE);
                        currentItemIndex++;
                        pageButtons.get(currentItemIndex).setEnabled(true);
                        outlineButtons.get(currentItemIndex).setVisibility(View.VISIBLE);
                        pageButtons.get(currentItemIndex).setVisibility(View.INVISIBLE);
                        currentImage = 0;
                        clearTexts();
                    }
                    Tools.showMsg(getApplicationContext(), "Item added");
                } else {
                    Tools.showMsg(getApplicationContext(), "All 3 images required");
                }
                break;
            case R.id.btn_add_quest:
                if (items.size() >= 3) {
                    Intent i = new Intent(this, ActivitySubmitQuest.class);
                    LocatorData.getInstance().itemsToAdd = items;
                    startActivity(i);

                } else {
                    Tools.showMsg(getApplicationContext(), "Atleast 3 items required");
                }
                break;
        }

    }
}
