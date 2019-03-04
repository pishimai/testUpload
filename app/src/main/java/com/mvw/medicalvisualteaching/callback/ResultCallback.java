package com.mvw.medicalvisualteaching.callback;

import android.os.Handler;
import android.os.Message;
import com.mvw.medicalvisualteaching.bean.Result;
import com.mvw.netlibrary.callback.Callback;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Response;

/**
 *
 */
public class ResultCallback extends Callback<Result>{
    private Handler handler;
    private String sn;//流水
    private String command;//协议
    public ResultCallback(Handler handler,String sn,String command){
        this.handler = handler;
        this.sn = sn;
        this.command = command;
    }
    @Override
    public Result parseNetworkResponse(Response response, int id) throws IOException {
        String responseStr = response.body().string();
        Result result = new Result();
        result.setResponse(responseStr);
        result.setSuccess(true);
        result.setSn(sn);
        result.setCommand(command);
        Result parseResult = parseResponse(result);
        if(parseResult != null){
            return parseResult;
        }
        return result;
    }
    @Override
    public void onResponse(Result response, int id) {
        if (handler!=null){
            Message msg = Message.obtain();
            msg.obj = response;
            handler.sendMessage(msg);
        }

    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Message msg = Message.obtain();
        Result result = new Result();
        result.setSuccess(false);
        result.setSn(sn);
        result.setCommand(command);
        String responseStr = onErrorResponse();
        if(responseStr != null){
            result.setResponse(responseStr);
            result.setSuccess(true);
        }
        msg.obj = result;
        if (handler!=null){
            handler.sendMessage(msg);
        }
    }

    public Result parseResponse(Result result){
        return result;
    }

    public String onErrorResponse(){
        return null;
    }
}
