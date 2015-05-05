package com.sri.touchpadlocator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerConnect extends ActionBarActivity {

    public final static String serverAddr = "com.sri.touchpadlocator.serveraddr";
    public final static String errMessage = "Network operation failed!!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_connect);

        final Button button = (Button) findViewById(R.id.connectBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String serverAddress = ((EditText)findViewById(R.id.serverAddrID)).getText().toString();
                String message = "Establishing connection to server " + serverAddress;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new ServerDiscovery().execute(serverAddress);
                } else {
                    Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(getApplicationContext(), TocuhPad.class);
                intent.putExtra(serverAddr, serverAddress);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server_connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ServerDiscovery extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                Socket socket = new Socket(urls[0], 9001);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("0,0,0,0");
                socket.close();
                return "success";
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Unable to retrieve web page. URL may be invalid.", Toast.LENGTH_LONG);
                return "failure";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG);
        }
    }
}
