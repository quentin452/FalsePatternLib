package com.falsepattern.lib.internal;

import lombok.val;
import lombok.var;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class Internet {
    public static void connect(URL URL, Consumer<Exception> onError, Consumer<InputStream> onSuccess) {
        try {
            val connection = (HttpURLConnection) URL.openConnection();
            connection.setConnectTimeout(3500);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", Tags.MODNAME + " " + Tags.VERSION + " Internet Connector" +
                                                        " (https://github.com/FalsePattern/FalsePatternLib)");
            if (connection.getResponseCode() != 200) {
                onError.accept(new Exception("HTTP response code " + connection.getResponseCode()));
            } else {
                onSuccess.accept(connection.getInputStream());
            }
            connection.disconnect();
        } catch (Exception e) {
            onError.accept(e);
        }
    }


    public static void transferAndClose(InputStream is, OutputStream target) throws IOException {
        var bytesRead = 0;

        byte[] smallBuffer = new byte[4096];
        while ((bytesRead = is.read(smallBuffer)) >= 0) {
            target.write(smallBuffer, 0, bytesRead);
        }
        target.close();
        is.close();
    }
}
