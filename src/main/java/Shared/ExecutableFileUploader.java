//package Shared;
//
//import java.io.DataOutputStream;
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//
//public class ExecutableFileUploader implements Runnable {
//    private DataOutputStream dos;
//    private File file;
//    public ExecutableFileUploader(DataOutputStream dos, File file) {
//        this.dos = dos;
//        this.file = file;
//    }
//    @Override
//    public void run() {
//        try {
//            Log("Ready to upload");
//            Path path = this.file.toPath();
//            byte[] bin = Files.readAllBytes(path);
//
//            dos.writeUTF("<file>");
//            dos.writeUTF(path.getFileName().toString());
//            dos.writeInt(bin.length);
//            dos.write(bin);
//            Log("Uploaded");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
