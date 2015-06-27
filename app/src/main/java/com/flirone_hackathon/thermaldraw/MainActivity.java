package com.flirone_hackathon.thermaldraw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mShapeTextView;
    private Button mStartButton;
    private Button mCaptureButton;
    private int mClickCount = 0;

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
}
