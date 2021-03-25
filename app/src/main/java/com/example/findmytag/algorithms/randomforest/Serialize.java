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
    @RequiresApi(api = Build.VERSION_CODES.O)
    static Path write(Object o) throws IOException {
        Path temp = Files.createTempFile("smile-test-", ".tmp");
        OutputStream file = Files.newOutputStream(temp);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(o);
        out.close();
        file.close();
        temp.toFile().deleteOnExit();
        return temp;
    }

    /** Reads an object from a (temp) file. */
    @RequiresApi(api = Build.VERSION_CODES.O)
    static Object read(Path path) throws IOException, ClassNotFoundException {
        InputStream file = Files.newInputStream(path);
        ObjectInputStream in = new ObjectInputStream(file);
        Object o = in.readObject();
        in.close();
        file.close();
        return o;
    }
}
