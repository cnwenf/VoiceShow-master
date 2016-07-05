package bupt.sse.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import config.IPConfig;
import config.PathConfig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;


public class getHotsPic extends  Thread {
    private Handler mHandler = null;
    private List<String> hots_list;
    public getHotsPic(Handler mHandler, List<String> hots_list){
        this.mHandler = mHandler;
        this.hots_list = hots_list;
    }
    public void run(){
        for(String pic : hots_list){
            File file = new File(PathConfig.TEMP_PICTURE_PATH+"/"+pic+".png");
            if(file.exists()){
                continue;
            }
            Socket socket = null;
            try {
                FileOutputStream fos = new FileOutputStream(file);
                socket = new Socket(IPConfig.IP, IPConfig.Port);
                //发送获取热门素材图片命令
                PrintWriter os=new PrintWriter(socket.getOutputStream());
                JSONObject outJSON = null;
                try {
                    outJSON = new JSONObject();
                    outJSON.put("cmd","3");
                    outJSON.put("fileName","素材/"+pic+"/"+pic+".png");
                }catch (JSONException e){}
                String out = outJSON.toString();
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
        }
        mHandler.sendEmptyMessage(1);
    }

}
