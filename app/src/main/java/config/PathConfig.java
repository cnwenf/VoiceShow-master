package config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Feng on 2016/5/12.
 */
public class PathConfig {
    private static final String SD_ROOT = Environment.getExternalStorageDirectory().getPath();
    public static final String APP_EXTERNAL_ROOT_PATH = SD_ROOT + "/peiyinhe1.0";
    public static final String TEMP_FILE_PATH = APP_EXTERNAL_ROOT_PATH + "/temp";
    public static final String TEMP_AUDIO_PATH = TEMP_FILE_PATH + "/audio";
    public static final String TEMP_PICTURE_PATH = TEMP_FILE_PATH+"/pictures";
    public static final String RECORD_AUDIO_PATH = APP_EXTERNAL_ROOT_PATH + "/audio";
    public static final String DATA_PATH = APP_EXTERNAL_ROOT_PATH+ "/DATA";

    public PathConfig(){
        createStoreDirs();
    }

    private void createStoreDirs(){
        File tempAudioDir = new File(TEMP_AUDIO_PATH);
        if(!tempAudioDir.exists()){
            tempAudioDir.mkdirs();
        }
        File tempPictureDir = new File(TEMP_PICTURE_PATH);
        if(!tempPictureDir.exists()){
            tempPictureDir.mkdirs();
        }

        File recordAudioPath = new File(RECORD_AUDIO_PATH);
        if(!recordAudioPath.exists()){
            recordAudioPath.mkdir();
        }

        File dataPath = new File(DATA_PATH);
        if(!dataPath.exists()){
            dataPath.mkdir();
        }

    }
}
