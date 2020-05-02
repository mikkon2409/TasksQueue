//package Shared;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//
//import static Utils.Logger.Log;
//
//public class ExecutableFileDownloader {
//    public static File downloadExecutableFile(DataInputStream din) {
//        try {
//            String fileName = din.readUTF();
//            int fileLength = din.readInt();
//            Log("Ready to download " + fileName + " " + fileLength + " bytes");
//            byte[] binFile = new byte[fileLength];
//            int offset = 0;
//            while (offset != fileLength)
//                offset += din.read(binFile, offset, fileLength - offset);
//
//            File file = new File(fileName);
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(file);
//                fos.write(binFile);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//            finally {
//                try {
//                    if (fos != null)
//                        fos.close();
//                    Log("Download complete");
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            file.setExecutable(true);
//            return file;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
