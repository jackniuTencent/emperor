package com.emperor2018.www.emperor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Launcher extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_launcher);
//
//    // Example of a call to a native method
//    TextView tv = (TextView) findViewById(R.id.sample_text);
//    tv.setText(stringFromJNI());
//    }

    class RenderView extends View{
        Bitmap bob565;
        Bitmap bob4444;
        Rect dst = new Rect();

        public RenderView(Context context){
            super(context);

            try{
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("bobrgb888.png");
                bob565 = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                Log.d("BitmapText",
                        "bobrgb888.png format: " + bob565.getConfig());

//                inputStream = assetManager.open("bobargb8888.png");
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
//                bob4444 = BitmapFactory
//                        .decodeStream(inputStream, null, options);
//                inputStream.close();
//                Log.d("BitmapText",
//                        "bobargb8888.png format: " + bob4444.getConfig());
            }catch(IOException e){
                // silently ignored, bad coder monkey, baaad!
            }finally{
                // we should really close our input streams here.
            }
        }

        protected void onDraw(Canvas canvas){
            dst.set(0, 0, 1000, 1000);
            canvas.drawBitmap(bob565, null, dst, null);
            //canvas.drawBitmap(bob4444, 100, 100, null);
            invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new RenderView(this));
    }

        /**
         * A native method that is implemented by the 'native-lib' native library,
         * which is packaged with this application.
         */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
