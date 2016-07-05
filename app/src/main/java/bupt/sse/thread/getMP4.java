package bupt.sse.thread;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import config.IPConfig;
import config.PathConfig;

/**
 * Created by TongXinSummer on 2016/5/23.
 */
public class getMP4 extends Thread{
    private Handler mHandler = null;
    private String mp4Name;
    /*
    * 0:mp4
    * 1:wav
    * */
    private int type = 0;
    public getMP4(Handler mHandler, String hots_list,int type){
        this.mHandler = mHandler;
        this.mp4Name = hots_list;
        this.type = type;
    }
    public void run(){
        File file;
        if(this.type==0)
            file = new File(PathConfig.DATA_PATH+"/"+mp4Name+".mp4");
        else
            file = new File(PathConfig.DATA_PATH+"/"+mp4Name+".wav");
        if(file.exists()){
            if(this.type==0)
                mHandler.sendEmptyMessage(11);
            else
                mHandler.sendEmptyMessage(12);
            return;
        }
        Socket socket = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            socket = new Socket(IPConfig.IP, IPConfig.Port);
            //发送获取素材视频
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            String out;
            if(this.type==0){
                JSONObject outJSON = new JSONObject();
                try{
                    outJSON.put("cmd","3");
                    outJSON.put("fileName","素材/"+mp4Name+"/"+mp4Name+".mp4");
                }catch (JSONException e){}
                out = outJSON.toString();
            }
            else{
                JSONObject outJSON = new JSONObject();
                try{
                    outJSON.put("cmd","3");
                    outJSON.put("fileName","素材/"+mp4Name+"/"+mp4Name+".wav");
                }catch (JSONException e){}
                out = outJSON.toString();
            }
            os.println(out);
            os.flush();
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());
            int size = dataInput.readInt();
            byte[] data = new byte[size];
            byte[] inputByte = new byte[10240];
            int len = 0;
            while (len < size) {
                //len += dataInput.read(data, len, size - len);
                int recv_len=dataInput.read(inputByte, 0, inputByte.length);
                len += recv_len;
                fos.write(inputByte, 0, recv_len);
                fos.flush();
            }
            fos.close();
            //dataInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        if(this.type==0)
            mHandler.sendEmptyMessage(11);
        else
            mHandler.sendEmptyMessage(12);
    }
}
