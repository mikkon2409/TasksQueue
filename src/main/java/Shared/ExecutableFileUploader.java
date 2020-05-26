package Shared;

import java.io.File;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutableFileUploader implements Runnable {
    private static final Logger log = Logger.getLogger(ExecutableFileUploader.class.getName());
    private final ObjectOutputStream oos;
    private final FileDescriptor fileDesc;

    public ExecutableFileUploader(ObjectOutputStream oos, File file) {
        this.oos = oos;
        fileDesc = new FileDescriptor(file);
    }

    @Override
    public void run() {
        try {
            log.info("Ready to upload");
            oos.writeUTF("<file>");
            oos.writeObject(fileDesc);
            log.info("Uploaded");
        } catch (Exception e) {
            log.log(Level.SEVERE, "UploadFile: ", e);
        }
    }
}
