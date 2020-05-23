package Shared;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileDescriptor implements Serializable {
    private static Logger log = Logger.getLogger(FileDescriptor.class.getName());
    String name;
    long byteSize;
    byte[] bin;
    public FileDescriptor(File file) {
        name = file.getName();
        byteSize = file.length();
        try {
            bin = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.log(Level.SEVERE, "FileDescriptor: ", e);
        }
    }
}
