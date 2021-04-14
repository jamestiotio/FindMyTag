package com.example.findmytag.algorithms.randomforest;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Serialize {
    /**
     * Writes an object to a temp file and returns the path of temp file.
     * The temp file will be deleted when the VM exits.
     */
    public static Path write(Object o) throws IOException {
        Path temp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            temp = Files.createTempFile("rf-test", ".tmp");
        }
        OutputStream file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            file = Files.newOutputStream(temp);
        }
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(o);
        out.close();
        file.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            temp.toFile().deleteOnExit();
        }
        return temp;
    }

    /** Reads an object from a (temp) file. */
    public static Object read(Path path) throws IOException, ClassNotFoundException {
        InputStream file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            file = Files.newInputStream(path);
        }
        ObjectInputStream in = new ObjectInputStream(file);
        Object o = in.readObject();
        in.close();
        file.close();
        return o;
    }
}
