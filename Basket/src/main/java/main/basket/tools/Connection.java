package main.basket.tools;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/** Created by martin on 5.8.13. */
public class Connection {

  public static void receiveFile(String filePath) {
    try {
      URL url = new URL("http://www.stud.fit.vutbr.cz/~xbrazd14/resources/basket.json");
      URLConnection connection = url.openConnection();
      connection.connect();
      // this will be useful so that you can show a typical 0-100% progress bar
      int fileLength = connection.getContentLength();

      // download the file
      InputStream input = new BufferedInputStream(url.openStream());
      OutputStream output = new FileOutputStream(filePath);

      byte data[] = new byte[1024];
      long total = 0;
      int count;
      while ((count = input.read(data)) != -1) {
        total += count;
        // publishing the progress....
    //    publishProgress((int) (total * 100 / fileLength));
        output.write(data, 0, count);
      }

      output.flush();
      output.close();
      input.close();
    } catch (Exception e) {
    }
  }

  public static void sendFile(String filePath) {
    HttpURLConnection connection  = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream   = null;

    String urlServer = "http://www.stud.fit.vutbr.cz/~xbrazd14/uploader.php";
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1*1024*1024;

    try {
      FileInputStream fileInputStream = new FileInputStream(new File(filePath) );

      URL url = new URL(urlServer);
      connection = (HttpURLConnection) url.openConnection();

// Allow Inputs & Outputs
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setUseCaches(false);

// Enable POST method
      connection.setRequestMethod("POST");

      connection.setRequestProperty("Connection", "Keep-Alive");
      connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

      outputStream = new DataOutputStream( connection.getOutputStream() );
      outputStream.writeBytes(twoHyphens + boundary + lineEnd);
      outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + filePath +"\"" + lineEnd);
      outputStream.writeBytes(lineEnd);

      bytesAvailable = fileInputStream.available();
      bufferSize = Math.min(bytesAvailable, maxBufferSize);
      buffer = new byte[bufferSize];

// Read file
      bytesRead = fileInputStream.read(buffer, 0, bufferSize);

      while (bytesRead > 0) {
        outputStream.write(buffer, 0, bufferSize);
        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
      }

      outputStream.writeBytes(lineEnd);
      outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

// Responses from the server (code and message)
      int serverResponseCode = connection.getResponseCode();
      String serverResponseMessage = connection.getResponseMessage();

      fileInputStream.close();
      outputStream.flush();
      outputStream.close();
    }
    catch (Exception ex) {
//Exception handling
    }
  }

}
