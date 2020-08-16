package com.example.testmodules;

import android.util.Log;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;
import java.io.File;

public class ZipEntryAnne {
   private static String SOURCE_FILE = "/Users/zhangshexin/Desktop/123.pkg";
   private static String TARGET_FILE = "/Users/zhangshexin/Desktop/hehe.zip";

   public static void main2(String[] args) {
      try {
         createZipFile();
         readZipFile();
      } catch(IOException ioe) {
         System.out.println("IOException : " + ioe);
      }
   }

   private static String inPath = "/Users/zhangshexin/Desktop/tt.iso";
   private static String tag = "/Users/zhangshexin/Desktop/teamviewer_change_id";
   private static String outPath = "/Users/zhangshexin/Desktop/hehe2332.zip";

   public static void main(String[] args) throws Exception {
      // 压缩文件
//      createZipFile(SOURCE_FILE,outPath);
      deflater(SOURCE_FILE,outPath);
      // 解压文件
//      Inflater(outPath, "D:\\c.bat");

   }

   /**
    * 创建ZIP文件
    * @param targetDirPath 目标文件夹
    * @param targetZipPath 目标压缩文件
    */
   public static boolean createZipFile(String targetDirPath, String targetZipPath){
      File targetDir = new File(targetDirPath);
      File targetZip = new File(targetZipPath);
      ZipArchiveOutputStream zos = null;
      List<File> files =new ArrayList<>();
      if(targetDir.isFile()){
         files.add(targetDir);
      }else{
         File[] temps = targetDir.listFiles();
         files.addAll(Arrays.asList(temps));
      }
      try {
         zos = new ZipArchiveOutputStream(targetZip);
         zos.setUseZip64(Zip64Mode.AsNeeded);
         zos.setMethod(ZipEntry.STORED);//关键代码,设置压缩模式为存储
         for(File file: files){
            if(!file.exists() || file.isDirectory()){
               continue;
            }
            ZipArchiveEntry zae = new ZipArchiveEntry(file,file.getName());
            zos.putArchiveEntry(zae);
            FileInputStream fis=new FileInputStream(file);
            IOUtils.copy(fis,zos);
            fis.close();
            zos.closeArchiveEntry();
            zos.flush();
         }
         zos.finish();
         zos.close();
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
      return true;
   }
   /**
    *
    * @param outPath2
    *            压缩后的文件
    * @param str
    *            解压后的文件
    * @throws Exception
    */
   private static void Inflater(String outPath2, String str) throws Exception {
      FileInputStream fis = new FileInputStream(new File(outPath));
      FileOutputStream fos = new FileOutputStream(new File(str));
      InflaterOutputStream ios = new InflaterOutputStream(fos, new Inflater(true));

      byte[] b = new byte[1024];
      int len = 0;
      while ((len = fis.read(b)) != -1) {
         ios.write(b, 0, len);
      }
      fis.close();
      ios.close();
   }

   /**
    *
    * @param inPath
    *            原文件
    * @param outPath
    *            压缩后的文件
    * @throws Exception
    */
   public static void deflater(final String inPath, String outPath) throws Exception { //TODO 这个压缩有问题，在压缩后大小不对，会无法解压
      FileInputStream fis = new FileInputStream(new File(inPath));
      FileOutputStream fos = new FileOutputStream(new File(outPath));
      DeflaterOutputStream dos = new DeflaterOutputStream(fos,
              new Deflater(0,true));
      org.apache.commons.compress.utils.IOUtils.copy(fis,dos);
//      byte[] b = new byte[8192];
//      int len = 0;
//      while ((len = fis.read(b)) != -1) {
//         Log.e("TAG", "deflater:------- "+ len+"");
//         dos.write(b, 0, len);
//      }
      fis.close();
      dos.close();
      fos.close();
   }

   private static void createZipFile() throws IOException{
      FileOutputStream fout = new FileOutputStream(TARGET_FILE);
      CheckedOutputStream checksum = new CheckedOutputStream(fout, new Adler32());
      ZipOutputStream zout = new ZipOutputStream(checksum);

      FileInputStream fin = new FileInputStream(SOURCE_FILE);
      ZipEntry zipEntry = new ZipEntry(SOURCE_FILE);
      zipEntry.setMethod(ZipEntry.STORED);
      zipEntry.setCrc(21183432);
      zout.putNextEntry(zipEntry);
      int length;
      byte[] buffer = new byte[1024];
      while((length = fin.read(buffer)) > 0) {
         zout.write(buffer, 0, length);
      }

      zout.closeEntry();
      fin.close();
      zout.close();
   }

   private static void readZipFile() throws IOException{
      final ZipFile file = new ZipFile(TARGET_FILE); 
      System.out.println("Iterating over zip file : " + TARGET_FILE);
      try { 
         final Enumeration<? extends ZipEntry> entries = file.entries(); 
         while (entries.hasMoreElements()) { 
            final ZipEntry entry = entries.nextElement(); 
            System.out.printf("File: %s Size %d Modified on %TD %n", 
               entry.getName(), entry.getSize(), 
                  new Date(entry.getTime())); 
            System.out.println(entry.getMethod() == ZipEntry.DEFLATED ? "Deflate" : "Stored");
            extractFile(entry, file.getInputStream(entry)); 
         } 
         System.out.printf("Zip file %s extracted successfully.", SOURCE_FILE); 
      } 
      finally { 
         file.close(); 
      }
   }

   private static void extractFile(final ZipEntry entry, InputStream is) 
      throws IOException {
      FileOutputStream fos = null; 
      try { 
         fos = new FileOutputStream(entry.getName()); 
         final byte[] buf = new byte[1024]; 
         int read = 0; 
         int length; 
         while ((length = is.read(buf, 0, buf.length)) >= 0) { 
            fos.write(buf, 0, length); 
         } 
      } catch (IOException ioex) { 
         fos.close(); 
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
}

