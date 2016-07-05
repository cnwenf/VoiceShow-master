package bupt.sse.thread;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import config.IPConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by Feng on 2016/5/22.
 */
public class getHotsString extends Thread{
    private Handler mHandler = null;
    private List<String> hots_list;
    public getHotsString(Handler mHandler,List<String> hots_list){
        this.mHandler = mHandler;
        this.hots_list = hots_list;
    }
    public void  run(){
        Socket socket = null;
        try {
            socket = new Socket(IPConfig.IP, IPConfig.Port);
            //发送获取热门命令
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            JSONObject outJSON = null;
            try{
                outJSON = new JSONObject();
                outJSON.put("cmd","2");
            }catch (JSONException e){}
            os.println(outJSON.toString());
            os.flush();
            //获取热门素材名称
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String msg = is.readLine();
            String[] hots = msg.split("[|]");
            for(String hot : hots){
                System.out.println(hot);
                hots_list.add(hot);
            }
            mHandler.sendEmptyMessage(0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
