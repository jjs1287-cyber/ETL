package org.etlforge.application;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.collections.Pair;
import org.etlforge.io.Folder;

public final class FileCounter {
   public static void main(String[] var0) {
      if (var0.length == 0) {
         throw new RuntimeException("Please provide the path to the root folder as input.");
      }

      ExecutorService var1 = Executors.newSingleThreadExecutor();
      Timer var2 = new Timer();

      try {
         Folder var3 = new Folder(var0[0]);
         FileCounter.Worker var4 = new FileCounter.Worker(var3);
         var4.setCountLines(true);
         FileCounter.Monitor var5 = new FileCounter.Monitor(var4);
         var2.scheduleAtFixedRate(var5, 2500L, 2500L);
         Future var6 = var1.submit(var4);
         FileCounter.Count var7 = (FileCounter.Count)var6.get();
         System.out.println(var3);
         System.out.println(var7.fullReport());
      } catch (InterruptedException var8) {
         var8.printStackTrace();
      } catch (ExecutionException var9) {
         var9.printStackTrace();
      }

      var1.shutdown();
      var2.cancel();
   }

   private static String reportLineCounts(EnumMap<FileCounter.Extension, Pair<FileCounter.FileCount, FileCounter.LineCount>> var0) {
      String var1 = String.format("%-20s%10s%10s%10s%10s%10s", "Language", "files", "blank", "comment", "code", "total");
      StringBuilder var2 = new StringBuilder(var1).append(SystemUtils.LINE_SEPARATOR);

      for (Pair var4 : var0.values()) {
         FileCounter.FileCount var5 = (FileCounter.FileCount)var4.first;
         FileCounter.LineCount var6 = (FileCounter.LineCount)var4.second;
         String var7 = String.format(
            "%-20s%10s%10s%10s%10s%10s",
            var5.getExtension(),
            formatCount(var5.getCount()),
            formatCount(var6.blank()),
            formatCount(var6.comments()),
            formatCount(var6.code()),
            formatCount(var6.total())
         );
         var2.append(var7).append(SystemUtils.LINE_SEPARATOR);
      }

      return var2.toString();
   }

   private static String formatCount(long var0) {
      return NumberFormat.getIntegerInstance().format(var0);
   }

   private static class Count {
      private int folderCount;
      private int fileCount;
      private final Map<FileCounter.Extension, FileCounter.FileCount> fileCounts = Maps.newHashMap();
      private final Map<FileCounter.Extension, FileCounter.LineCount> lineCounts = Maps.newHashMap();
      public volatile boolean countLines;

      private Count() {
      }

      public synchronized void update(Folder var1) throws IOException {
         this.folderCount++;
         File[] var2 = var1.listNonFolders();
         this.fileCount += var2.length;

         for (File var6 : var2) {
            FileCounter.Extension var7 = FileCounter.Extension.fromFile(var6);
            if (var7 != null) {
               this.updateFileCount(var7);
               if (this.countLines) {
                  this.updateLineCount(var6, var7);
               }
            }
         }
      }

      private void updateFileCount(FileCounter.Extension var1) {
         FileCounter.FileCount var2 = this.fileCounts.get(var1);
         if (var2 == null) {
            this.fileCounts.put(var1, new FileCounter.FileCount(var1));
         } else {
            var2.increase();
         }
      }

      private void updateLineCount(File var1, FileCounter.Extension var2) throws IOException {
         FileCounter.LineCount var3 = this.lineCounts.get(var2);
         if (var3 == null) {
            var3 = new FileCounter.LineCount(var2);
            this.lineCounts.put(var2, var3);
         }

         var3.add(var1);
      }

      public synchronized String shortReport() {
         return String.format("%d folders, %d files", this.folderCount, this.fileCount);
      }

      public synchronized String fullReport() {
         if (this.countLines) {
            EnumMap var5 = Maps.newEnumMap(FileCounter.Extension.class);

            for (FileCounter.FileCount var7 : this.fileCounts.values()) {
               FileCounter.LineCount var8 = this.lineCounts.get(var7.getExtension());
               var5.put(var7.getExtension(), Pair.of(var7, var8));
            }

            return FileCounter.reportLineCounts(var5);
         } else {
            ArrayList var1 = Lists.newArrayList(this.fileCounts.values());
            Collections.sort(var1);
            StringBuilder var2 = new StringBuilder();
            var2.append("Folder count:\t").append(this.folderCount).append("\n");
            var2.append("File count:\t").append(this.fileCount).append("\n");
            var2.append(var1.size()).append(" unique file extensions:\n");

            for (Object __etlItem149 : var1) {
               FileCounter.FileCount var4 = (FileCounter.FileCount) __etlItem149;
               var2.append(var4).append("\n");
            }

            return var2.toString();
         }
      }
   }

   private enum Extension {
      CPP("C++", "cpp"),
      CPP_HEADER("C++ Header", "h"),
      JAVA("Java", "java");

      private final String name;
      private final String ext;

      Extension(String var3, String var4) {
         this.name = var3;
         this.ext = var4;
      }

      public static FileCounter.Extension fromFile(File var0) {
         String var1 = StringUtils.substringAfterLast(var0.getName(), ".");

         for (FileCounter.Extension var5 : values()) {
            if (var5.ext.equals(var1)) {
               return var5;
            }
         }

         return null;
      }

      @Override
      public String toString() {
         return this.name;
      }
   }

   private static class FileCount implements Comparable<FileCounter.FileCount> {
      private final FileCounter.Extension extension;
      private int count;

      public FileCount(FileCounter.Extension var1) {
         this.extension = var1;
         this.count = 1;
      }

      public int getCount() {
         return this.count;
      }

      public FileCounter.Extension getExtension() {
         return this.extension;
      }

      public void increase() {
         this.count++;
      }

      public int compareTo(FileCounter.FileCount var1) {
         return var1.count - this.count;
      }

      @Override
      public String toString() {
         return this.extension + "\t" + this.count;
      }
   }

   private static class LineCount {
      private final FileCounter.Extension extension;
      private int blank;
      private int comment;
      private int bracesOnly;
      private int other;

      public LineCount(FileCounter.Extension var1) {
         this.extension = var1;
      }

      public void add(File var1) throws IOException {
         Files.readLines(var1, Charset.defaultCharset(), new LineProcessor<Void>() {
            private boolean inBlockComment;

            public Void getResult() {
               return null;
            }

            @Override
            public boolean processLine(String var1) throws IOException {
               this.processLineImpl(var1);
               return true;
            }

            private void processLineImpl(String var1) {
               if (this.inBlockComment) {
                  ++LineCount.this.comment;
                  if (var1.contains("*/")) {
                     this.inBlockComment = false;
                  }
               } else {
                  String var2 = var1.trim();
                  if (var2.isEmpty()) {
                     ++LineCount.this.blank;
                  } else if (var2.startsWith("//")) {
                     ++LineCount.this.comment;
                  } else if (var2.contains("/*")) {
                     if (this.isSingleLineBlockComment(var2)) {
                        ++LineCount.this.comment;
                     } else if (this.isEmbeddedBlockComment(var2)) {
                        ++LineCount.this.other;
                     } else {
                        if (var2.startsWith("/*")) {
                           ++LineCount.this.comment;
                        } else {
                           ++LineCount.this.other;
                        }

                        this.inBlockComment = true;
                     }
                  } else if (!var1.equals("{") && !var1.equals("}")) {
                     ++LineCount.this.other;
                  } else {
                     ++LineCount.this.bracesOnly;
                  }
               }
            }

            private boolean isSingleLineBlockComment(String var1) {
               return var1.startsWith("/*") && var1.endsWith("*/");
            }

            private boolean isEmbeddedBlockComment(String var1) {
               int var2 = var1.indexOf("/*");
               assert var2 >= 0;
               int var3 = var1.indexOf("*/", var2);
               return var3 > 0;
            }
         });
      }

      public int total() {
         return this.blank + this.comment + this.bracesOnly + this.other;
      }

      public int code() {
         return this.bracesOnly + this.other;
      }

      public int blank() {
         return this.blank;
      }

      public int comments() {
         return this.comment;
      }

      @Override
      public String toString() {
         return this.extension + "\t" + this.total();
      }
   }

   private static class Monitor extends TimerTask {
      private final FileCounter.Worker worker;

      public Monitor(FileCounter.Worker var1) {
         this.worker = var1;
      }

      @Override
      public void run() {
         this.worker.shortReport();
      }
   }

   private static class Worker implements Callable<FileCounter.Count> {
      private final FileCounter.Count count = new FileCounter.Count();
      private final Folder root;

      public Worker(Folder var1) {
         this.root = var1;
      }

      public void setCountLines(boolean var1) {
         this.count.countLines = var1;
      }

      public FileCounter.Count call() throws Exception {
         this.processFolder(this.root);
         return this.count;
      }

      private void processFolder(Folder var1) throws IOException {
         this.count.update(var1);

         for (Folder var5 : var1.listSubFolders()) {
            if (!var5.getName().equals(".svn")) {
               this.processFolder(var5);
            }
         }
      }

      public void shortReport() {
         System.out.println(this.count.shortReport());
      }
   }
}
