package Shared;

import java.io.*;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutableFileDownloader {
    private static final Logger log = Logger.getLogger(ExecutableFileDownloader.class.getName());

    public static File downloadExecutableFile(ObjectInputStream oin, Path pathToWorkspace) throws IOException, ClassNotFoundException {
        FileDescriptor fd = (FileDescriptor) oin.readObject();

        File file = new File(pathToWorkspace + "/" + fd.name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(fd.bin);
        }
        catch (Exception e) {
            log.log(Level.SEVERE, "Write file: ", e);
        }
        finally {
            try {
                if (fos != null)
                    fos.close();
            }
            catch (Exception e) {
                log.log(Level.SEVERE, "Close file after download: ", e);
            }
        }
        file.setExecutable(true);
        return file;
    }
}
