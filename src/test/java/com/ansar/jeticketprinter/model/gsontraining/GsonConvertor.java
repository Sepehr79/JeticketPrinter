package com.ansar.jeticketprinter.model.gsontraining;

import com.ansar.jeticketprinter.model.pojo.ConnectionProperties;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class GsonConvertor {

    @Test
    public void testToJson(){
        TestObject object = new TestObject();

        try(Writer writer = Files.newBufferedWriter(Paths.get("object.json"));) {
            new Gson().toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFromJson() throws IOException {

        try (Reader reader = Files.newBufferedReader(Paths.get("connection.json"))){

            ConnectionProperties properties = new Gson().fromJson(reader, ConnectionProperties.class);

            System.out.println(properties);

        } catch (NoSuchFileException e) {
            e.printStackTrace();
        }

    }

}
