package com.sime.demoweather;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class CAFData {

    private static final int bufferSize = 100 * 1024; // ~130K.
    private ByteBuffer byteData = null;

    //Constructors
    public CAFData(byte[] bytes, int length) {

        byteData = ByteBuffer.wrap(bytes, 0, length);
    }

    public CAFData(String path) {
        File inFile = new File(path);
        InputStream inStream = null;
        //byteData = null;
        try {
            inStream = new FileInputStream(inFile);
            byteData = getBytesFromInputStream(inStream);
            inStream.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (IOException e) {

        }

    }

    public CAFData(URL url) {
        InputStream inStream = null;
        int response = -1;
        URLConnection conn = null;

        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    //try {
                    inStream = httpConn.getInputStream();
                    byteData = getBytesFromInputStream(inStream);
                    inStream.close();
                    //} catch (){

                    //}
                }
                httpConn.disconnect();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Private methods
    private static ByteBuffer getBytesFromInputStream(InputStream inStream) {
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = null;
        ByteBuffer byteData = null;

        try {
            outStream = new ByteArrayOutputStream(buffer.length);
            int bytesRead = 0;
            while (bytesRead != -1) {
                bytesRead = inStream.read(buffer);
                if (bytesRead > 0) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }
            byteData = ByteBuffer.wrap(outStream.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            byteData = null;
        }

        return byteData;
    }

    // Public methods
    public static CAFData dataWithContentsOfURL(URL url) {

        CAFData data = new CAFData(url);
        if (data != null && data.length() == 0) {
            data = null;
        }

        return data;
    }

    public static CAFData dataWithContentsOfFile(String fullFilename) {

        CAFData data = new CAFData(fullFilename);
        if (data != null && data.length() == 0) {
            data = null;
        }

        return data;
    }

    // Write content in local file
    public boolean writeToFile(String path, boolean atomically) {
        boolean wasWritten = false;
        File inFile = null;
        FileOutputStream fis = null;

        try {
            if (atomically) {
                inFile = File.createTempFile("tmp", ".tmp");
                fis = new FileOutputStream(inFile);
            } else {
                inFile = new File(path);
            }

            fis.write(byteData.array());
            //Log.d("CAFDataEx",inFile.getName()+" ("+byteData.array().length+" bytes) saved.");
            fis.close();

            if (atomically) {
                if (inFile.renameTo(new File(path))) {
                    //Log.d("CAFDataEx","Renamed to "+path);
                }

            }

            //Log.d("CAFDataEx","Bytes writed = "+byteData.array().length);
            wasWritten = true;
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        return wasWritten;
    }

    public String toText() {
        return new String(byteData.array(), Charset.forName("UTF-8"));
    }

    public int length() {
        int length = 0;

        if (byteData != null) {
            length = byteData.array().length;
        }

        return length;
    }
}
