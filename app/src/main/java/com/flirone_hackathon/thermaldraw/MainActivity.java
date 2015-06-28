package com.flirone_hackathon.thermaldraw;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQ_CODE_INT_PICK_IMAGE = 100;

    private TextView mShapeTextView;
    private Button mStartButton;
    private Button mCaptureButton;
    private int mClickCount = 0;

    private Bitmap mBitmap;

    static {
        System.loadLibrary("opencv_java");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate()");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    protected void onResume() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onResume()");
        }
        super.onResume();
    }

    private void initViews() {
        mShapeTextView = (TextView) findViewById(R.id.shape_text);
        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStartClick(v);
                mClickCount++;
            }
        });
        mCaptureButton = (Button) findViewById(R.id.capture_button);
        mCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCaptureClick(v);
                Toast.makeText(MainActivity.this, "Capture button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleStartClick(View v) {
        // Choose a shape and populate the Shape Text View
        String [] shapesArray = getResources().getStringArray(R.array.shapes_array);
        int idx = mClickCount % shapesArray.length;
        String shapeText = shapesArray[idx];
        mShapeTextView.setText(shapeText);
    }

    private void handleCaptureClick(View v) {
//        Intent intent = new Intent(this, PreviewCaptureActivity.class);
//        startActivity(intent);
        loadImage();
    }

    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_CODE_INT_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onActivityResult() -- requestCode: " + requestCode + ", resultCode: " + resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_INT_PICK_IMAGE:
                if(resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        cursor.close();
                        matchImageShape(bitmap);
                    }
                }
        }
    }

    private void matchImageShape(Bitmap bitmap) {
        // Create the Mat from the bitmap
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bitmap, imageMat);

        // Convert to black and white
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
        // Apply GaussianBlur to smooth edges
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(5, 5), 5);
        // 
    }
}
