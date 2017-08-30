package com.emperor2018.www.emperor;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);

    // Example of a call to a native method
    TextView tv = (TextView) findViewById(R.id.sample_text);
    tv.setText(stringFromJNI());

                playButton=(Button)findViewById(R.id.playButton);
        stopButton=(Button)findViewById(R.id.stopButton);

        //播放MP3
        playButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(playButton.getText().toString().equals("播放")){
                    boolean createState=false;
                    if(mediaPlayer==null){
                        mediaPlayer=createLocalMp3();
                        createState=true;
                    }
                    //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
                    //以便其他应用程序可以使用该资源:
                    mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();//释放音频资源
                            stopButton.setEnabled(false);
                            setTitle("资源已经被释放了");
                        }
                    });
                    try {
                        //在播放音频资源之前，必须调用Prepare方法完成些准备工作
                        if(createState) mediaPlayer.prepare();
                        //开始播放音频
                        mediaPlayer.start();
                        playButton.setText("暂停");
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(playButton.getText().toString().equals("暂停")){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();//暂停
                        playButton.setText("播放");
                    }
                }
                stopButton.setEnabled(true);
            }
        });

        //停止
        stopButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null){
                    mediaPlayer.stop();//停止播放
                    mediaPlayer.release();//释放资源
                    mediaPlayer=null;
                    playButton.setText("播放");
                    stopButton.setEnabled(false);
                }
            }
        });

        android.support.constraint.ConstraintLayout cLayout = (android.support.constraint.ConstraintLayout)findViewById(R.id.conStraintLayout);

        cLayout.addView(new RenderView(this));

    }

    private Button playButton;
    private Button stopButton;
    private MediaPlayer mediaPlayer;

    class RenderView extends View{
        Bitmap bob565;
        Bitmap bob4444;
        Rect dst = new Rect();

        public RenderView(Context context){
            super(context);

            try{
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open("China_FE_MainMenu.jpg");
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

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(new RenderView(this));
//

//    }

    /**
     * 创建网络mp3
     * @return
     */
    public MediaPlayer createNetMp3(){
        String url="http://192.168.1.100:8080/media/beatit.mp3";
        MediaPlayer mp=new MediaPlayer();
        try {
            mp.setDataSource(url);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalStateException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return mp;
    }
    /**
     * 创建本地MP3
     * @return
     */
    public MediaPlayer createLocalMp3(){
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
        MediaPlayer mp=MediaPlayer.create(this,R.raw.xia_1i);
        mp.stop();
        return mp;
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
