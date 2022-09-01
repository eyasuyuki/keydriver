package org.javaopen.keydriver;

import org.javaopen.keydriver.reader.Reader;
import org.javaopen.keydriver.reader.ReaderFactory;

import java.io.IOException;

public class App {
    public static void main(String args[]) {
        Reader reader = ReaderFactory.getReader(args[0]);
        try {
            reader.read(args[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
