package com.example.locator;

import android.os.Bundle;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;


import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityAddQuest extends ActivityBase implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_IMAGE = 2;

    private Button btnCamera, btnAttach, btnAddItem, btnAddQuest;
    private List<Button> pageButtons = new ArrayList<>(), outlineButtons = new ArrayList<>();
    private EditText editName, editQeustion, editLocation, editHint;
    private ImageView imageView;
    private List<Item> items = new ArrayList<>();
    private List<EditText> answers = new ArrayList<>();
    private Item shownItem = new Item();
    private int currentItemIndex = 0;
    private boolean removeItem = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quest);
        initializeComponents();
    }

    public void addItem() {
        shownItem.name = editName.getText().toString();
        shownItem.hint = editHint.getText().toString();
        shownItem.question = editQeustion.getText().toString();
        shownItem.location = editLocation.getText().toString();
        for (int i = 0; i < 3; i++) {
            shownItem.answers.add(answers.get(i).getText().toString());
        }
        items.add(shownItem);
    }

    public void controlsToItem() {
        if (currentItemIndex > items.size() - 1) {
            return;
        }
        shownItem.name = editName.getText().toString();
        shownItem.hint = editHint.getText().toString();
        shownItem.question = editQeustion.getText().toString();
        shownItem.location = editLocation.getText().toString();
        for (int i = 0; i < 3; i++) {
            shownItem.answers.set(i, answers.get(i).getText().toString());
        }
        items.set(currentItemIndex, shownItem);
    }

    public void itemToControls(Item item) {
        shownItem = item;
        editName.setText(item.name);
        editHint.setText(item.hint);
        editQeustion.setText(item.question);
        if (item.answers != null && !item.answers.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                answers.get(i).setText(item.answers.get(i));
            }
        }
        editLocation.setText(item.location);
        imageView.setImageDrawable(getDrawable(R.drawable.ok2));
        if (item.image != null && !item.image.isEmpty()) {
            imageView.setImageBitmap(StringToBitMap(item.image));
        }
    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);

                new Thread(() -> shownItem.image = (BitMapToString(imageBitmap))).start();

            } else if (requestCode == SELECT_IMAGE) {
                if (data != null) {
                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        imageView.setImageBitmap(bitmap);
                        new Thread(() -> shownItem.image = BitMapToString(bitmap)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 50) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void initializeComponents() {
        editQeustion = findViewById(R.id.item_question);
        editHint = findViewById(R.id.item_hint);
        editLocation = findViewById(R.id.item_location);
        editName = findViewById(R.id.item_name);

        btnCamera = findViewById(R.id.btn_camera);
        btnAttach = findViewById(R.id.btn_attach);
        btnAddQuest = findViewById(R.id.btn_add_quest);
        btnAddItem = findViewById(R.id.btn_add_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> ActivityAddQuest.super.onBackPressed());

        for (int i = 1; i < 4; i++) {
            EditText editText = findViewById(getResources().getIdentifier("item_answer_" + i, "id", getPackageName()));
            answers.add(editText);
        }
        imageView = findViewById(R.id.img_1);
        for (int i = 1; i < 7; i++) {
            Button button = findViewById(getResources().getIdentifier("btn_page_" + i + "_outline", "id", getPackageName()));
            outlineButtons.add(button);
            Button button1 = findViewById(getResources().getIdentifier("btn_page_" + i, "id", getPackageName()));
            button1.setOnClickListener(this);
            pageButtons.add(button1);
        }
        btnAddItem.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnAttach.setOnClickListener(this);
        btnAddQuest.setOnClickListener(this);
    }

    public void clearTexts() {
        editQeustion.setText("");
        editHint.setText("");
        editName.setText("");
        editLocation.setText("");
        imageView.setImageDrawable(getDrawable(R.drawable.ok2));
        btnAttach.setEnabled(true);
        btnCamera.setEnabled(true);
        shownItem = new Item();
        for (EditText answer : answers) {
            answer.setText("");
        }

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
                if (!emptyCheck(new EditText[]{editName, editQeustion, editHint, editLocation, answers.get(0), answers.get(1), answers.get(2)})) {
                    return;
                }
                addItem();
                if (currentItemIndex == 5) {
                    setUpPages(0);
                } else {
                    pageButtons.get(currentItemIndex).setVisibility(View.VISIBLE);
                    currentItemIndex++;
                    pageButtons.get(currentItemIndex).setEnabled(true);
                    outlineButtons.get(currentItemIndex).setVisibility(View.VISIBLE);
                    pageButtons.get(currentItemIndex).setVisibility(View.INVISIBLE);
                    clearTexts();
                }
                Tools.showMsg(getApplicationContext(), "Item added");
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
