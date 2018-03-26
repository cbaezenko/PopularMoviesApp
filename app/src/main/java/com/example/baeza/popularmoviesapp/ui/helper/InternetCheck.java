package com.example.baeza.popularmoviesapp.ui.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.example.baeza.popularmoviesapp.R;
import com.example.baeza.popularmoviesapp.ui.MainActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by baeza on 26/3/18.
 */

public class InternetCheck extends AsyncTask<Void, Void, Boolean> {
    Context context;

    public InternetCheck(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            int timeoutMs = 1500;
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

            socket.connect(socketAddress, timeoutMs);
            socket.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isConnected) {
        if (!isConnected) {
            MainActivity.showProgressBar(false);
            MainActivity.tv_error_msg.setText(context.getString(R.string.no_internet_connection));
        }
    }
}