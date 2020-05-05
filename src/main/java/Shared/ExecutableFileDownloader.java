package Shared;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutableFileDownloader {
    private static Logger log = Logger.getLogger(ExecutableFileDownloader.class.getName());

    public static File downloadExecutableFile(DataInputStream din, Path pathToWorkspace) {
        try {
            String fileName = din.readUTF();
            int fileLength = din.readInt();
            log.info("Ready to download " + fileName + " " + fileLength + " bytes");
            byte[] binFile = new byte[fileLength];
            int offset = 0;
            while (offset != fileLength)
                offset += din.read(binFile, offset, fileLength - offset);

            File file = new File(pathToWorkspace + "/" + fileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(binFile);
            }
            catch (Exception e) {
                log.log(Level.SEVERE, "Write file: ", e);
            }
            finally {
                try {
                    if (fos != null)
                        fos.close();
                    log.info("Download complete");
                }
                catch (Exception e) {
                    log.log(Level.SEVERE, "Close file after download: ", e);
                }
            }
            file.setExecutable(true);
            return file;
        } catch (Exception e) {
            log.log(Level.SEVERE, "DownloadFile: ", e);
            return null;
        }
    }
}
