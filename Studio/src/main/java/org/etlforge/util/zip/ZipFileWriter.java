package org.etlforge.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.etlforge.io.KongaIoUtils;

public class ZipFileWriter {
   private static final int BUFFER_SIZE = 524288;
   private final String m_stZipFile;
   private ZipOutputStream m_zip;
   private byte[] m_buf;

   public ZipFileWriter(String var1) {
      this.m_stZipFile = var1;
   }

   public synchronized void closeZipFile() throws FileNotFoundException, IOException {
      this.m_zip.close();
      this.m_zip = null;
      this.m_buf = null;
   }

   public synchronized void add(byte[] var1, String var2) throws FileNotFoundException, IOException {
      this.init();
      this.m_zip.putNextEntry(new ZipEntry(var2.replace('\\', '/')));
      this.m_zip.write(var1);
      this.m_zip.closeEntry();
   }

   public synchronized void add(String var1, String var2) throws FileNotFoundException, IOException {
      this.init();
      FileInputStream var3 = new FileInputStream(var1);
      this.m_zip.putNextEntry(new ZipEntry(var2.replace('\\', '/')));

      try {
         int var4 = 0;
         if (this.m_buf == null) {
            this.m_buf = new byte[524288];
         }

         while ((var4 = var3.read(this.m_buf)) > -1) {
            this.m_zip.write(this.m_buf, 0, var4);
         }
      } finally {
         KongaIoUtils.close(var3);
         this.m_zip.closeEntry();
         Object var8 = null;
      }
   }

   private void init() throws FileNotFoundException {
      if (this.m_zip == null) {
         File var1 = new File(this.m_stZipFile);
         File var2 = var1.getParentFile();
         if (!var2.exists() && !var2.mkdirs()) {
            throw new FileNotFoundException("Could not create the folder " + var2.getAbsolutePath());
         }

         this.m_zip = new ZipOutputStream(new FileOutputStream(this.m_stZipFile));
      }
   }
}
