package Server.SearchServer.Lucene;

import java.io.File;
import java.io.FileFilter;

// Filter for text files searched with Lucene
public class TextFileFilter implements FileFilter {


    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".txt");
    }
}
