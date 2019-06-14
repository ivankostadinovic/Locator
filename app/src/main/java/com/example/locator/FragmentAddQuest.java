package com.example.locator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class FragmentAddQuest extends Fragment {




    private User user;
    private Button btnCamera,btnAttach,btnAddItem,btnAddQuest;
    private List<Button> pageButtons,outlineButtons;
    private EditText editName,editDescr,editLocation,editHint;
    private List<ImageView> imageViews;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static  final int SELECT_IMAGE=2;
    private List<Item> items;
    private int currentItem;
    private Quest quest;
    private boolean ok=false;
    public FragmentAddQuest() {
        // Required empty public constructor
    }

    public static FragmentAddQuest newInstance(User user) {
        FragmentAddQuest fragment = new FragmentAddQuest();
        Bundle args = new Bundle();
        args.putSerializable("User",user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user=(User)getArguments().getSerializable("User");
        }
    }

    private View.OnClickListener pageListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_page_1:
                    pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                    controlsToItem();
                    pageButtons.get(0).setVisibility(View.INVISIBLE);
                    currentItem=0;
                    itemToControls(items.get(0));
                    break;
                case R.id.btn_page_2:
                    pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                    controlsToItem();
                    pageButtons.get(1).setVisibility(View.INVISIBLE);
                    currentItem=1;
                    itemToControls(items.get(1));
                    break;
                case R.id.btn_page_3:
                    pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                    controlsToItem();
                    pageButtons.get(2).setVisibility(View.INVISIBLE);
                    currentItem=2;
                    itemToControls(items.get(2));
                    break;
                case R.id.btn_page_4:
                    pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                    controlsToItem();
                    pageButtons.get(3).setVisibility(View.INVISIBLE);
                    currentItem=3;
                    itemToControls(items.get(3));
                    break;
                case R.id.btn_page_5:
                    pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                    controlsToItem();
                    pageButtons.get(4).setVisibility(View.INVISIBLE);
                    currentItem=4;
                    itemToControls(items.get(4));
                    break;
                case R.id.btn_page_6:
                    pageButtons.get(currentItem).setVisibility(View.VISIBLE);
                    controlsToItem();
                    pageButtons.get(5).setVisibility(View.INVISIBLE);
                    currentItem=5;
                    itemToControls(items.get(5));
                    break;
            }

        }
    };

    public void controlsToItem()
    {
        items.get(currentItem).setName(editName.getText().toString());
        items.get(currentItem).setHint(editHint.getText().toString());
        items.get(currentItem).setDescription(editDescr.getText().toString());
        items.get(currentItem).setLocation(editLocation.getText().toString());

    }
    public void itemToControls(Item item)
    {
        editName.setText(item.getName());
        editHint.setText(item.getHint());
        editDescr.setText(item.getDescription());
        editLocation.setText(item.getLocation());
        int i=0;
        if(item.getImages().size()!=0)
            for (String b:items.get(currentItem).getImages())
            {
                imageViews.get(i).setImageBitmap(StringToBitMap(b));
                byte bt[]=b.getBytes();
               int p= bt.length;
                i++;
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        outlineButtons=new ArrayList<Button>();
        pageButtons=new ArrayList<Button>();
        imageViews=new ArrayList<ImageView>();
        items= new ArrayList<Item>();
        View view= inflater.inflate(R.layout.fragment_fragment_add_quest, container, false);
        currentItem=0;
        editDescr=(EditText) view.findViewById(R.id.item_description);
        editHint=(EditText) view.findViewById(R.id.item_hint);
        editLocation=(EditText) view.findViewById(R.id.item_location);
        editName=(EditText) view.findViewById(R.id.item_name);

        btnCamera=(Button) view.findViewById(R.id.btn_camera);
        btnAttach=(Button) view.findViewById(R.id.btn_attacah);
        for(int i=1;i<4;i++)
        {
            ImageView imgView=(ImageView) view.findViewById(getResources().getIdentifier("img_"+Integer.toString(i), "id", getActivity().getPackageName()));
            imageViews.add(imgView);
        }
       for(int i=1;i<7;i++)
       {
           items.add(new Item());
           Button button=(Button) view.findViewById(getResources().getIdentifier("btn_page_"+Integer.toString(i)+"_outline", "id", getActivity().getPackageName()));
           outlineButtons.add(button);
           Button button1=(Button) view.findViewById(getResources().getIdentifier("btn_page_"+Integer.toString(i), "id", getActivity().getPackageName()));
           button1.setOnClickListener(pageListener);
           pageButtons.add(button1);

       }




       btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 50);////////////////////////////////////////////////
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

       btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);

            }
        });

       return  view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK)
            if(requestCode==REQUEST_IMAGE_CAPTURE)
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageViews.get(items.get(currentItem).getImages().size()).setImageBitmap(imageBitmap);
                items.get(currentItem).addImage(BitMapToString(imageBitmap));

            }
        else if(requestCode==SELECT_IMAGE)
            {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        imageViews.get(items.get(currentItem).getImages().size()).setImageBitmap(bitmap);
                        items.get(currentItem).addImage(BitMapToString(bitmap));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        else if (requestCode==50)
            {

            }



    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }






}
