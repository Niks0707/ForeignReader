package niks.foreignreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Niks on 20.03.2017.
 */

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
        try {
            String str;
            for (;;) {
                if ((str = bufferedReader.readLine()) == null) {
                    break;
                }
                text += str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
