package com.example.lenovo.voiceshow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import bupt.sse.thread.getMP4;
import config.PathConfig;
import mediaCtr.AudioEncoder;
import mediaCtr.ExtAudioRecorder;
import mediaCtr.MD5Util;
import mediaCtr.MultiAudioMixer;
import mediaCtr.ViewMux;

public class VoiceActivity extends Activity implements OnClickListener {
    SurfaceView surfaceView;
    ImageButton play,  stop;
    MediaPlayer mPlayer;
    TextView t_pause;

    ImageView pause;
    // 记录当前视频的播放位置
    int position;
    File soundFile;
    ExtAudioRecorder mRecorder;
    private ProgressDialog dlgMixing;

    public String path = null;
    public String dataName = null;
    @Override


    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.voice_page);
            new PathConfig();

            Bundle bundle2 = this.getIntent().getExtras();

            //注意，此处界面跳转过程中应该取得配音文件的名字
            dataName = bundle2.getString("voice_name");

            // 获取界面中的三个按钮

        t_pause=(TextView)findViewById(R.id.t_pause);
            play = (ImageButton) findViewById(R.id.voice_record);
            pause = (ImageView) findViewById(R.id.pause);
            stop = (ImageButton) findViewById(R.id.finish);

            // 为三个按钮的单击事件绑定事件监听器
            play.setOnClickListener(this);
            pause.setOnClickListener(this);
            stop.setOnClickListener(this);
            // 创建MediaPlayer
            mPlayer = new MediaPlayer();
            mRecorder = ExtAudioRecorder.getInstanse(false);;
            surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
            // 设置播放时打开屏幕
            surfaceView.getHolder().setKeepScreenOn(true);
            surfaceView.getHolder().addCallback(new SurfaceListener());

            dlgMixing = new ProgressDialog(this);
            dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dlgMixing.setCancelable(false);
            dlgMixing.setCanceledOnTouchOutside(false);

        }
    @Override
    public void onClick(View source)
    {
        // MediaRecorder.VideoSource.SURFACE
        try
        {
            switch (source.getId())
            {
                // 播放按钮被单击
                case R.id.voice_record:
                    play();
                    break;
                // 暂停按钮被单击
                case R.id.pause:
                    if (mPlayer.isPlaying())
                    {
                        mPlayer.pause();
                        pause.setImageResource(R.drawable.start);
                        t_pause.setText("开始");

                    }
                    else
                    {
                        mPlayer.start();
                        pause.setImageResource(R.drawable.pause);
                        t_pause.setText("暂停");
                    }
                    break;
                // 停止按钮被单击
                case R.id.finish:
                    if (mPlayer.isPlaying()) mPlayer.stop();
                    mRecorder.stop();
                    mRecorder.release();
                    performMixAudio();
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void performMixAudio() {
        //if(!addAudioTracks.isEmpty()){
        dlgMixing.setMessage("拼命合成配音视频中...");
        dlgMixing.show();
        new MixAudioTask().execute();
        //}
    }
    //开始录音
    private void play() throws IOException
    {
        soundFile = new File(PathConfig.RECORD_AUDIO_PATH  + "/"+dataName+ "_ly.wav");
        /*
        // 设置录音的声音来源
        mRecorder.setAudioSource(MediaRecorder .AudioSource.MIC);
        // 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
        mRecorder.setOutputFormat(MediaRecorder .OutputFormat.THREE_GPP);
        // 设置声音编码的格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        */
        mRecorder.setOutputFile(soundFile.getAbsolutePath());
        mRecorder.prepare();
        // 开始录音
        mRecorder.start();

        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setVolume(0,0);
        // 设置需要播放的视频
        mPlayer.setDataSource(PathConfig.DATA_PATH +"/"+ dataName+ ".mp4");
        // 把视频画面输出到SurfaceView
        mPlayer.setDisplay(surfaceView.getHolder());  // ①
        mPlayer.prepare();
        // 获取窗口管理器
        WindowManager wManager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        // 获取屏幕大小
        wManager.getDefaultDisplay().getMetrics(metrics);
        // 设置视频保持纵横比缩放到占满整个屏幕
        surfaceView.setLayoutParams(new LayoutParams(metrics.widthPixels
                , mPlayer.getVideoHeight() * metrics.widthPixels
                / mPlayer.getVideoWidth()));
        mPlayer.start();
    }

    private class SurfaceListener implements SurfaceHolder.Callback
    {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format,
                                   int width, int height)
        {
        }
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            if (position > 0)
            {
                try
                {
                    // 开始播放
                    play();
                    // 并直接从指定位置开始播放
                    mPlayer.seekTo(position);
                    position = 0;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
        }
    }
    // 当其他Activity被打开时，暂停播放
    @Override
    protected void onPause()
    {
        if (mPlayer.isPlaying())
        {
            // 保存当前的播放位置
            position = mPlayer.getCurrentPosition();
            mPlayer.stop();
        }
        super.onPause();
    }
    @Override
    protected void onDestroy()
    {
        // 停止播放
        if (mPlayer.isPlaying()) mPlayer.stop();
        // 释放资源
        mPlayer.release();
        super.onDestroy();
    }

    class MixAudioTask extends AsyncTask<Void, Double, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            String rawAudioFile = null;
            File[] rawAudioFiles = new File[2];
            StringBuilder sbMix = new StringBuilder();
            rawAudioFiles[0] = new File(PathConfig.DATA_PATH+"/"+dataName+".wav");
            rawAudioFiles[1] = new File(PathConfig.RECORD_AUDIO_PATH+"/"+dataName+"_ly.wav");

            final String mixFilePath =PathConfig.TEMP_AUDIO_PATH+"/"
                    + MD5Util.getMD5Str(sbMix.toString());

            try {
                MultiAudioMixer audioMixer = MultiAudioMixer.
                        createAudioMixer();

                audioMixer.setOnAudioMixListener(new MultiAudioMixer.OnAudioMixListener() {

                    FileOutputStream fosRawMixAudio = new FileOutputStream(mixFilePath);

                    @Override
                    public void onMixing(byte[] mixBytes) throws IOException {
                        fosRawMixAudio.write(mixBytes);
                    }

                    @Override
                    public void onMixError(int errorCode) {
                        try {
                            if(fosRawMixAudio != null)
                                fosRawMixAudio.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMixComplete() {
                        try {
                            if(fosRawMixAudio != null)
                                fosRawMixAudio.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
                audioMixer.mixAudios(rawAudioFiles);
                rawAudioFile = mixFilePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AudioEncoder accEncoder =  AudioEncoder.createAccEncoder(rawAudioFile);
            String finalMixPath = PathConfig.TEMP_AUDIO_PATH+"/"+ dataName +"_hy.aac";
            accEncoder.encodeToFile(finalMixPath);
            ViewMux vm = new ViewMux(dataName);
            try{
                vm.mux();
            }catch(IOException e){}
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Toast.makeText(VoiceActivity.this, "合成配音视频成功！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(VoiceActivity.this,FinishActivity.class);
                    Bundle bundle=new Bundle();//用Bundle携带数据
                    bundle.putString("video_name",dataName);//传递type参数为position
                    intent.putExtras(bundle);
                    startActivity(intent);

            dlgMixing.cancel();
        }
    }




}
