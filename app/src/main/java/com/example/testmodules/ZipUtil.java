package com.example.testmodules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static String SOURCE_FILE = "/Users/zhangshexin/Desktop/123.pkg";
    private static String TARGET_FILE = "/Users/zhangshexin/Desktop/hehe.zip";
    public static void main(String[] args) {
        try {
            zip_stored(TARGET_FILE,new File(SOURCE_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 按存储方式压缩
     * @param zipFileName
     * @param inputFile
     * @throws Exception
     */
    public static void zip_stored(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip_stored(out, inputFile, "");
        out.close();
    }
    /**
     * 按存储方式压缩
     * @param out
     * @param f
     * @param base
     * @throws Exception
     */
    private static void zip_stored(ZipOutputStream out, File f, String base) throws Exception {
        out.setMethod(ZipOutputStream.STORED);
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                if (fl[i].getName().indexOf(".zip") == -1) {
                    zip_stored(out, fl[i], base + fl[i].getName());
                }
            }
        } else {
            ZipEntry entry = new ZipEntry(base);
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(f.length());
            long crc = 0;
            crc = calFileCRC32(f);
            entry.setCrc(crc);
            out.putNextEntry(entry);
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            out.closeEntry();
            in.close();
        }
    }
    public static long calFileCRC32(File file) throws IOException {
        FileInputStream fi = new FileInputStream(file);
        CheckedInputStream checksum = new CheckedInputStream(fi, new CRC32());
        while (checksum.read() != -1) { }
        long temp = checksum.getChecksum().getValue();
        fi.close();
        checksum.close();
        return temp;
    }


    //-----------------

}
