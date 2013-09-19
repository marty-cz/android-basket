package main.basket.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

/**
 * Created by martin on 6.8.13.
 */
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
        } catch (Exception e) {
            errString = e.getLocalizedMessage();
        }
        return false;
    }

    public Object deserializeFromJson(String filePath, Class clazz) {
        JsonReader reader = null;
        try {
//          reader = new JsonReader(new FileReader(filePath));
            reader = new JsonReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            return new Gson().fromJson(reader, clazz);
        } catch (Exception e) {
            errString = e.getLocalizedMessage();
        }
        return null;
    }

}
