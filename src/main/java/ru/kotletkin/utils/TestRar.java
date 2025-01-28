package ru.kotletkin.utils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestRar {
    public static void main(String[] args) throws IOException {

//  https://www.baeldung.com/spring-boot-requestmapping-serve-zip

        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream("./messages/1.zip")))) {
            zipOut.setLevel(9);
            ArrayList<String> names = new ArrayList<>(); // get from sql names and bucket name
            names.add("0a1995bf-08af-49c9-b1e8-75cfe69f611d.json");
            names.add("3217c2ac-f005-49ca-9785-24d2e6bdface.json");
            names.add("77d7e7ea-fa32-4a12-b3e1-4812ec61180e.json");

            for (String name : names) {
                InputStream input = URI.create("http://192.168.126.129:8888/buckets/telegram-bucket/" +
                        name).toURL().openStream();
                zipOut.putNextEntry(new ZipEntry(name));
                IOUtils.copy(input, zipOut);
            }

            zipOut.closeEntry();
        }


    }
}
