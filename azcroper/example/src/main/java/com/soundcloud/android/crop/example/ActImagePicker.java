package com.soundcloud.android.crop.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

/**
 * Created by prashant.chovatiya on 3/15/2018.
 */

public class ActImagePicker extends Activity {


    String strTagChooseImage = "Choose image";
    String strTagGallery = "Gallery";
    String strTagCamera = "Camera";


    Uri mImageCaptureUri;
    int CAMERA_REQESTCODE = 104;
    public static final int REQUEST_PICK = 9162;

    Bitmap profilePicBitmap = null;


    Button btnPick;
    ImageView ivPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        btnPick = (Button) findViewById(R.id.btnPick);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);


        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetPhoto();
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetPhoto();
            }
        });
    }



    ///////////// PROFILE PIC - Select option - GALLERY/CAMERA ////////////
    private void openBottomSheetPhoto() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_sheet_getimage, null, false);

        TextView header = (TextView) view.findViewById(R.id.header);
        header.setText(strTagChooseImage);

        TextView tvGallery = (TextView) view.findViewById(R.id.tvGallery);
        TextView tvCamera = (TextView) view.findViewById(R.id.tvCamera);

        tvGallery.setText(strTagGallery);
        tvCamera.setText(strTagCamera);


        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ActImagePicker.this, R.style.BottomSheetDialog);
        mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);

        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                ivPhoto.setImageDrawable(null);

                startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_PICK);

               // Crop.pickImage(ActImagePicker.this);
            }
        });

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                ivPhoto.setImageDrawable(null);
                getImageFromCamera();
            }
        });
    }


    /////////// CAPTURE PROFILE PIC FROM CAMERA - FUNCTION //////////
    private void getImageFromCamera() {

        try {


            File file2 = new File(App.getAppFolderBasePath() + "/" + App.APP_FOLDERNAME + "");
            if (!file2.exists()) {
                if (!file2.mkdirs()) {
                    //System.out.println("==Create Directory "+App.APP_FOLDERNAME+"====");
                } else {
                    //System.out.println("==No--1Create Directory "+App.APP_FOLDERNAME+"====");
                }
            } else {
                //System.out.println("== already created---No--2Create Directory "+App.APP_FOLDERNAME+"====");
            }


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString(), "/"+App.APP_FOLDERNAME+"/tempimage.jpg"));
            mImageCaptureUri = Uri.fromFile(new File(App.getAppFolderBasePath(), "/" + App.APP_FOLDERNAME + "/ProfPhoto.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CAMERA_REQESTCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void beginCrop(Uri source) {
       /* Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);*/


        // start cropping activity for pre-acquired image saved on the device
        CropImage.activity(source)
                .setAspectRatio(1, 1)
                .start(this);
    }




    private void setImageAndBitmap(Uri uri) {
        try {
            ivPhoto.setImageURI(uri);
            profilePicBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        { // Image set to profile code
            if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) {

                App.showLog("===requestCode==Crop.REQUEST_PICK==onActivityResult====" + requestCode);
                beginCrop(data.getData());

            } else if (requestCode == CAMERA_REQESTCODE && resultCode == RESULT_OK) {

                App.showLog("===CAMERA_REQESTCODE==onActivityResult====" + requestCode);
                beginCrop(mImageCaptureUri);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                App.showLog("===CROP_IMAGE_ACTIVITY_REQUEST_CODE=====");

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK && result.getUri() != null) {

                    App.showLog("===CROP_IMAGE_ACTIVITY_REQUEST_CODE====RESULT_OK===");
                    setImageAndBitmap(result.getUri());

                } else {
                    App.showLog("===CROP_IMAGE_ACTIVITY_REQUEST_CODE====Not - RESULT_OK=====resultCode==" + resultCode);

                    if(profilePicBitmap !=null)
                    ivPhoto.setImageBitmap(profilePicBitmap);
                }

                /* else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    App.showLog("===CROP_IMAGE_ACTIVITY_REQUEST_CODE====CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE===");
                   // Exception error = result.getError();
                }*/
            } else {
                App.showLog("===ELSE==onActivityResult====" + requestCode);
                if(profilePicBitmap !=null)
                    ivPhoto.setImageBitmap(profilePicBitmap);
            }
        }

    }


}
