package bupt.sse.thread;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import config.IPConfig;
import config.PathConfig;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Feng on 2016/5/22.
 */
public class sendMP4 extends  Thread{
    private Handler mHandler;
    private String userName;
    private String dataName;

    public sendMP4(Handler mHandler, String userName,String dataName){
        this.mHandler = mHandler;
        this.userName = userName;
        this.dataName = dataName;
    }

    public void run(){
        Socket socket = null;
        try {
            socket = new Socket(IPConfig.IP, IPConfig.Port);
            //发送获取上传命令
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            JSONObject outJSON = new JSONObject();
            try{
                outJSON.put("cmd","4");
                outJSON.put("userName",userName);
                outJSON.put("fileName",dataName.split("_")[0]);
            }catch (JSONException e){}
            os.println(outJSON.toString());
            os.flush();
            //获取返回结果
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String msg ="1";//is.readLine()
            System.out.println(msg);
            if(msg.equals("-1")){
                mHandler.sendEmptyMessage(2);
                return;
            }
            String path = PathConfig.DATA_PATH+"/" + dataName+".mp4";
            System.out.println(path);
            FileInputStream fis = new FileInputStream(path);
            //获得输出流
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            int size = fis.available();
            System.out.println("size = "+size);
            dos.writeInt(size);
            int length = 0;
            byte[] sendBytes = new byte[10240];
            while ((length = fis.read(sendBytes, 0, sendBytes.length)) >0) {
                dos.write(sendBytes, 0, length);
                dos.flush();
            }
            dos.close();
            fis.close();
            mHandler.sendEmptyMessage(1);

        } catch (IOException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(0);
        } finally {
        }
    }
}
