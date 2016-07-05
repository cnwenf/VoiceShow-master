package bupt.sse.thread;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import config.IPConfig;

/**
 * Created by TongXinSummer on 2016/5/24.
 */
public class toRegOrLogin extends Thread{
    public String userName;
    public String password;
    private Handler mHandler;
    private int type;
    public toRegOrLogin(String userName, String password, Handler mHandler,int type){
        this.userName = userName;
        this.password = password;
        this.mHandler = mHandler;
        this.type = type;
    }

    public void run(){
        try {
            Socket socket = null;
            socket = new Socket(IPConfig.IP, IPConfig.Port);
            //发送获取素材视频
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            String out="";
            if(type==0){
                try {
                JSONObject outJSON = new JSONObject();
                outJSON.put("cmd","0");
                outJSON.put("userName",userName);
                outJSON.put("password",password);
                out = outJSON.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{
                try {
                JSONObject outJSON = new JSONObject();
                outJSON.put("cmd","1");
                outJSON.put("userName",userName);
                outJSON.put("password",password);
                out = outJSON.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            os.println(out);
            os.flush();
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String msg = is.readLine();
            JSONObject inJSON = null;
            try {
                inJSON = new JSONObject(msg);
                msg = inJSON.getString("cmd");
            }catch (JSONException e) {
            e.printStackTrace();
            }
            if(msg.equals("0")){
                //TODO 注册/登陆失败
                mHandler.sendEmptyMessage(0);
            }
            else if(msg.equals("1")){
                //TODO 注册/登陆成功
                mHandler.sendEmptyMessage(1);
            }
        } catch (IOException e) {
            mHandler.sendEmptyMessage(2);
            e.printStackTrace();
        } finally {
        }
    }
}
