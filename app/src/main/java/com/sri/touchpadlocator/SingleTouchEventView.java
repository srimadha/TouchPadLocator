package com.sri.touchpadlocator;

/**
 * Created by smadhava on 5/2/15.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleTouchEventView extends View {
    private static final String TAG = "SingleTouchEventView";
    ServerDiscovery sd;
    public SingleTouchEventView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    long stime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        Log.d(TAG, eventX + "");
        long etime;
        boolean leftClick = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                etime = System.currentTimeMillis();
                if(etime - stime < 1000){
                    //Left Click.
                    leftClick = true;
                }
                break;
            default:
                return false;
        }
        String actionCode = "0";
        if(leftClick){
            actionCode = "1";
        }
        new ServerDiscovery().execute("nw-8677lm", eventX + "", eventY + "",actionCode);
        return true;
    }

    private class ServerDiscovery extends AsyncTask<String, Void, String> {
        Socket socket = null;
        PrintWriter out;
        public ServerDiscovery(){



        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                socket = new Socket("nw-8677lm", 9001);
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(urls[1]+","+urls[2]+","+ urls[3]+",0");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

                return "success";

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }
}
