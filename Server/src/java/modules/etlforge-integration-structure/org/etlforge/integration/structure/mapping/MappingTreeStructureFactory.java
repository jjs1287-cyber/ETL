package org.etlforge.integration.structure.mapping;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;
import org.etlforge.io.FileUtils;

public final class MappingTreeStructureFactory {
   public static MappingTreeStructure fromFile(File var0) {
      return fromFile(var0, Charsets.UTF_8);
   }

   public static MappingTreeStructure fromFile(File var0, Charset var1) {
      return new MappingTreeStructureFactory.FileBasedMappingTreeStructure(var0, var1);
   }

   public static MappingTreeStructure fromString(String var0) {
      return new MappingTreeStructureFactory.StringBasedMappingTreeStructure(var0);
   }

   private MappingTreeStructureFactory() {
   }

   private static class FileBasedMappingTreeStructure implements MappingTreeStructure {
      private final File file;
      private final Charset charset;

      public FileBasedMappingTreeStructure(File var1, Charset var2) {
         this.file = Preconditions.checkNotNull(var1, "file");
         this.charset = Preconditions.checkNotNull(var2, "charset");
      }

      @Override
      public CROM getRoot() {
         return null;
      }

      @Override
      public Reader getReader() throws IOException {
         return this.isZipped() ? this.getReaderFromZippedFile() : new InputStreamReader(new FileInputStream(this.file), this.charset);
      }

      private boolean isZipped() {
         String var1 = StringUtils.lowerCase(FileUtils.getExtension(this.file));
         return "gz".equals(var1);
      }

      private Reader getReaderFromZippedFile() throws IOException {
         GZIPInputStream var1 = null;
         boolean var2 = false;

         try {
            var1 = new GZIPInputStream(new BufferedInputStream(new FileInputStream(this.file)));
            var2 = true;
            return new InputStreamReader(var1, this.charset);
         } finally {
            if (!var2) {
               Closeables.closeQuietly(var1);
            }
         }
      }

      @Override
      public String getString() {
         try {
            return this.isZipped() ? this.unzipToString() : Files.toString(this.file, this.charset);
         } catch (IOException var2) {
            var2.printStackTrace();
            return "";
         }
      }

      private String unzipToString() throws IOException {
         assert this.isZipped();
         return CharStreams.toString(new InputSupplier<Reader>() {
            public Reader getInput() throws IOException {
               return FileBasedMappingTreeStructure.this.getReaderFromZippedFile();
            }
         });
      }

      @Override
      public boolean outOfDate() {
         Reader var1 = null;

         try {
            var1 = this.getReader();
            LineReader var2 = new LineReader(var1);
            String var3 = var2.readLine();
            if (StringUtils.startsWith(var3, "<?xml ")) {
               var3 = var2.readLine();
            }

            if (var3 == null || !var3.startsWith("<JTR ")) {
               return true;
            }

            String var4 = getValue(var3, "Type");
            if (var4.equals("DB")) {
               String var5 = getValue(var3, "Version");
               if (var5.isEmpty()) {
                  return true;
               }

               return Integer.parseInt(var5) < 2;
            }
         } catch (IOException var10) {
            var10.printStackTrace();
         } finally {
            Closeables.closeQuietly(var1);
         }

         return false;
      }

      private static String getValue(String var0, String var1) {
         StringTokenizer var2 = new StringTokenizer(var0, " =\"", true);
         byte var3 = 0;

         while (var2.hasMoreTokens()) {
            String var4 = var2.nextToken();
            switch (var3) {
               case 0:
                  if (var4.equals(var1)) {
                     var3 = 1;
                  }
                  break;
               case 1:
                  if (var4.equals("=")) {
                     var3 = 2;
                  } else if (!var4.equals(" ")) {
                     var3 = 0;
                  }
                  break;
               case 2:
                  if (var4.charAt(0) == '"') {
                     StringBuilder var5 = new StringBuilder();

                     while (var2.hasMoreTokens()) {
                        var4 = var2.nextToken();
                        if (var4.charAt(0) == '"') {
                           break;
                        }

                        var5.append(var4);
                     }

                     return var5.toString();
                  }

                  if (!var4.equals(" ")) {
                     return "";
                  }
            }
         }

         return "";
      }

      @Override
      public CROMHeader getHeader() {
         return CROMHeader.DEFAULT_HEADER;
      }
   }

   private static final class StringBasedMappingTreeStructure implements MappingTreeStructure {
      private final String string;

      public StringBasedMappingTreeStructure(String var1) {
         Preconditions.checkNotNull(var1, "s");
         this.string = var1;
      }

      @Override
      public CROM getRoot() {
         return null;
      }

      @Override
      public Reader getReader() {
         return new StringReader(this.string);
      }

      @Override
      public String getString() {
         return this.string;
      }

      @Override
      public boolean outOfDate() {
         return false;
      }

      @Override
      public CROMHeader getHeader() {
         return CROMHeader.DEFAULT_HEADER;
      }
   }
}
