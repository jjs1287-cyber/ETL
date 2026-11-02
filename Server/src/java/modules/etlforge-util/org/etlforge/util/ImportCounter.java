package org.etlforge.util;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.common.primitives.Ints;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.io.Folder;
import org.etlforge.io.MatchingFilesProcessor;
import org.etlforge.system.callback.Receiver;

public final class ImportCounter {
   private final List<ImportCounter.ImportCount> counts = Lists.newArrayList();

   public static void main(String[] var0) {
      ImportCounter var1 = new ImportCounter();
      var1.run();
   }

   public void run() {
      Folder var1 = new Folder("Z:\\etlforge\\trunk\\public\\integration\\java");
      MatchingFilesProcessor var2 = new MatchingFilesProcessor(new ImportCounter.Filter(), new ImportCounter.FileVisitor());
      var2.processRecursively(var1);
      Collections.sort(this.counts);
      this.report();
   }

   private void report() {
      for (int var1 = 0; var1 < Math.min(100, this.counts.size()); var1++) {
         ImportCounter.ImportCount var2 = this.counts.get(var1);
         System.out.println(String.format("%-50s%3d", var2.getName(), var2.count));
      }
   }

   private static class Counter {
      private final File file;
      private int count;

      public Counter(File var1) {
         this.file = var1;
      }

      public ImportCounter.ImportCount count() throws IOException {
         return Files.readLines(
            this.file,
            java.nio.charset.StandardCharsets.UTF_8,
            new LineProcessor<ImportCounter.ImportCount>() {
               @Override
               public boolean processLine(String var1) throws IOException {
                  if (!var1.startsWith("/**")
                     && !var1.startsWith("public")
                     && !var1.startsWith("final")
                     && !var1.startsWith("class")
                     && !var1.startsWith("interface")) {
                     if (var1.startsWith("import ")) {
                        ++Counter.this.count;
                     }

                     return true;
                  } else {
                     return false;
                  }
               }

               public ImportCounter.ImportCount getResult() {
                  return new ImportCounter.ImportCount(Counter.this.file, Counter.this.count);
               }
            }
         );
      }
   }

   private class FileVisitor implements Receiver<File> {
      private FileVisitor() {
      }

      public void handle(File var1) {
         try {
            ImportCounter.Counter var2 = new ImportCounter.Counter(var1);
            ImportCounter.ImportCount var3 = var2.count();
            ImportCounter.this.counts.add(var3);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }
   }

   private static class Filter implements FileFilter {
      private Filter() {
      }

      @Override
      public boolean accept(File var1) {
         return var1.isFile() && var1.getName().endsWith(".java");
      }
   }

   private static class ImportCount implements Comparable<ImportCounter.ImportCount> {
      public final File file;
      public final int count;

      public ImportCount(File var1, int var2) {
         this.file = var1;
         this.count = var2;
      }

      public String getName() {
         return StringUtils.substringBeforeLast(this.file.getName(), ".java");
      }

      public int compareTo(ImportCounter.ImportCount var1) {
         return -1 * Ints.compare(this.count, var1.count);
      }
   }
}
