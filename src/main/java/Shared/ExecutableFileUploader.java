package Shared;

import Client.ManageFileWidget;

import java.beans.Expression;
import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ExecutableFileUploader implements Runnable {
    private static Logger log = Logger.getLogger(ExecutableFileUploader.class.getName());
    private DataOutputStream dos;
    private File file;
    public ExecutableFileUploader(DataOutputStream dos, File file) {
        this.dos = dos;
        this.file = file;
    }
    @Override
    public void run() {
        try {
            log.info("Ready to upload");
            Path path = this.file.toPath();
            byte[] bin = Files.readAllBytes(path);

            dos.writeUTF("<file>");
            dos.writeUTF(path.getFileName().toString());
            dos.writeInt(bin.length);
            dos.write(bin);
            log.info("Uploaded");
        } catch (Exception e) {
            log.log(Level.SEVERE, "UploadFile: ", e);
        }
    }
}
