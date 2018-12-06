package com.example.dell.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {

    Button invertButton;
    ImageView imageView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS ) {
                // now we can call opencv code !
                helloworld();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        invertButton = findViewById(R.id.invert_image_button);
        imageView = findViewById(R.id.image_view);
    }

    @Override
    public void onResume() {;
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5,this, mLoaderCallback);
        // you may be tempted, to do something here, but it's *async*, and may take some time,
        // so any opencv call here will lead to unresolved native errors.
    }

    public void helloworld() {

         Bitmap Imagebitmap = null;
        Bundle bundle=this.getIntent().getExtras();

        if (getIntent().hasExtra("Input Image")){
            Imagebitmap = (Bitmap) getIntent().getParcelableExtra("Input Image");
            imageView.setImageBitmap(Imagebitmap);
        }else if( getIntent().hasExtra("byteArray") ){
            Bitmap _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            imageView.setImageBitmap(_bitmap);
        }

        final Bitmap finalImagebitmap = Imagebitmap;
        invertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mat imgToProcess = new Mat (finalImagebitmap.getWidth(), finalImagebitmap.getHeight(), CvType.CV_8UC3);
                Utils.bitmapToMat(finalImagebitmap,imgToProcess);

                Core.flip(imgToProcess,imgToProcess,0);
                Bitmap bitmap = Bitmap.createBitmap(imgToProcess.cols(),imgToProcess.rows(),Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(imgToProcess,bitmap);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this,"Image Inverted",Toast.LENGTH_LONG).show();
            }
        });
    }



}
