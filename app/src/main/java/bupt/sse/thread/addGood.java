package bupt.sse.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import config.IPConfig;

public class addGood extends Thread{
    private String defName = null;
    private String userName = null;
    private Handler mHandler = null;
    public addGood(String defName,String userName,Handler mHandler){
        this.defName = defName;
        this.userName = userName;
        this.mHandler = mHandler;
    }
    public void test1(){
        String test = null;
    }
    public void test2(){}
    public void run(){
        Socket socket = null;
        try {
            socket = new Socket(IPConfig.IP, IPConfig.Port);
            //发送获取热门命令
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            JSONObject outJSON = null;
            try{
                outJSON = new JSONObject();
                outJSON.put("cmd","61");
                outJSON.put("defName",defName);
                outJSON.put("userName",userName);
            }catch (JSONException e){}
            os.println(outJSON.toString());
            os.flush();
            //获取热门素材名称
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String msg = is.readLine();
            socket.close();
            JSONObject inJson = new JSONObject(msg);
            String cmd = inJson.getString("cmd");
            if("0".equals(cmd)){
                mHandler.sendEmptyMessage(0);
            }
            else if("1".equals(cmd)){
                Message handlerMsg = mHandler.obtainMessage();
                handlerMsg.what = 1;
                Bundle b = new Bundle();
                b.putString("num",inJson.getString("num"));
                handlerMsg.setData(b);
                handlerMsg.sendToTarget();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){}
        finally {
        }
    }
}
