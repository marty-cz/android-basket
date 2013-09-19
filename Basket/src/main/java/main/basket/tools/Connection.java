package main.basket.tools;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by martin on 5.8.13.
 */
public class Connection {

    private static Connection instance;
    private int responseCode;
    private String responseStr;

    public static final String URL_SERVER = "http://www.stud.fit.vutbr.cz/~xbrazd14/";
    public static final String URL_UPLOADER = URL_SERVER + "uploader.php";
    public static final String URL_DOWNLOAD_FILE = URL_SERVER + "resources/basket.json";
    public static final String CRLF = "\r\n";
    public static final String TWO_HYPHNES = "--";
    public static final String BOUNDARY = "*****";
    public static final int BUFFER_SIZE = 1024;

    private Connection() {
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public boolean receiveFile(String filePath, FileConnectionTask progress) {
        URLConnection connection = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream inputStream = null;
        byte[] buffer;

        try {
            URL url = new URL(URL_DOWNLOAD_FILE);
            connection = url.openConnection();
            connection.connect();
            // Prepare download the file
            inputStream = new BufferedInputStream(url.openStream());
            fileOutputStream = new FileOutputStream(filePath);
            buffer = new byte[BUFFER_SIZE];
            int fileLength = connection.getContentLength();
            // Downloading the file
            int total = 0;
            int count;
            progress.setProgress(0);
            progress.setMax(fileLength / 1024);
            progress.setUnit("KiB");
            while ((count = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, count);
                total += count;
                progress.setProgress(total / 1024);
            }
            // close all streams
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            // result codes
            responseCode = 0;
            responseStr = "Succesfully downloaded...";
            return true;
        } catch (IOException e) {
            responseCode = -1;
            responseStr = e.getLocalizedMessage();
        }
        return false;
    }

    public boolean sendFile(String filePath, FileConnectionTask progress) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;

        try {
            fileInputStream = new FileInputStream(new File(filePath));

            URL url = new URL(URL_UPLOADER);
            connection = (HttpURLConnection) url.openConnection();
            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            // Enable POST method
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            // Prepare stream to file upload
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(TWO_HYPHNES + BOUNDARY + CRLF);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + filePath + "\"" + CRLF);
            outputStream.writeBytes(CRLF);
            // Prepare send file
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, BUFFER_SIZE);
            buffer = new byte[bufferSize];
            int total = 0;
            int fileLength = bytesAvailable;
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            // Sending file cycle
            progress.setProgress(0);
            progress.setMax(fileLength / 1024);
            progress.setUnit("KiB");
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);

                total += bytesRead;
                progress.setProgress(total / 1024);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, BUFFER_SIZE);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(CRLF);
            outputStream.writeBytes(TWO_HYPHNES + BOUNDARY + TWO_HYPHNES + CRLF);
            // Responses from the server (code and message)
            responseCode = connection.getResponseCode();
            responseStr = connection.getResponseMessage();
            // close all streams
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) { //Exception handling
            responseCode = -1;
            responseStr = e.getLocalizedMessage();
        }
        return false;
    }

}
