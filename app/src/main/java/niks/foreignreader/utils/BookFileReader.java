package niks.foreignreader.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BookFileReader {

    int textLength;
    File file;
    BufferedReader bufferedReader;

    public BookFileReader(String filename, int countRows) {
        this.textLength = countRows;
        file = new File(filename);
        createBufferedReader();
    }

    private void createBufferedReader()
    {
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        String text = "";
        if (bufferedReader != null) {
            try {
                String str;
                for (; ; ) {
                    if ((str = bufferedReader.readLine()) == null) {
                        break;
                    }
                    text += str;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return text;
    }
}
