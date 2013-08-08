package main.basket.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/** Created by martin on 6.8.13. */
public class Serializer {

  private static Serializer instance;
  protected String errString = "";

  private Serializer() {
  }

  public static Serializer getInstance() {
    if (instance == null) {
      instance = new Serializer();
    }
    return instance;
  }

  public String getErrString() {
    return errString;
  }

  public boolean serializeToJson(String filePath, Object obj, Class clazz) {
    JsonWriter writer = null;
    try {
      FileOutputStream stream = new FileOutputStream(filePath);
      writer = new JsonWriter(new OutputStreamWriter(stream, "UTF-8"));
      writer.setIndent("  ");
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(obj, clazz, writer);
      writer.close();
      return true;
    } catch (JsonIOException | IOException e) {
      errString = e.getLocalizedMessage();
    }
    return false;
  }

  public Object deserializeFromJson(String filePath, Class clazz) {
    JsonReader reader = null;
    try {
      reader = new JsonReader(new FileReader(filePath));
      return new Gson().fromJson(reader, clazz);
    } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
      errString = e.getLocalizedMessage();
    }
    return null;
  }

}
