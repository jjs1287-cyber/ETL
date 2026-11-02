package org.etlforge.xml.xsd;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.FileUtils;

final class GeneratedXsdNameChanger {
   private final GeneratedXsds originalXsds;
   private Map<String, String> translation;

   public GeneratedXsdNameChanger(GeneratedXsds var1) {
      this.originalXsds = var1;
   }

   public GeneratedXsds replaceNames() throws IOException {
      if (this.originalXsds.isSingleFile()) {
         return this.originalXsds;
      }

      this.translation = this.renameFiles();
      this.replaceReferences();
      return this.createResult();
   }

   private Map<String, String> renameFiles() throws IOException {
      Map var1 = this.originalXsds.getRenamedFiles();
      HashMap var2 = Maps.newHashMap();

      for (Object __etlItem41 : var1.entrySet()) {
         Entry var4 = (Entry) __etlItem41;
         File var5 = (File)var4.getKey();
         File var6 = (File)var4.getValue();
         var2.put(var5.getName(), var6.getName());
         FileUtils.copyFile(var5, var6);
         FileUtils.forceDelete(var5);
      }

      File var7 = this.originalXsds.getMainXsd();
      var2.put(var7.getName(), var7.getName());
      return var2;
   }

   private void replaceReferences() throws IOException {
      GeneratedXsdNameChanger.ReferenceReplacer var1 = new GeneratedXsdNameChanger.ReferenceReplacer(this.originalXsds.getAllFiles(), this.translation);
      var1.replaceReferences();
   }

   private GeneratedXsds createResult() {
      File var1 = this.translate(this.originalXsds.getMainXsd());
      HashSet var2 = Sets.newHashSet();

      for (File var4 : this.originalXsds.getAdditionalXsds()) {
         var2.add(this.translate(var4));
      }

      return new GeneratedXsds(var1, var2);
   }

   private File translate(File var1) {
      String var2 = this.translation.get(var1.getName());
      return new File(var1.getParent(), var2);
   }

   private static class ReferenceReplacer {
      private static final int MIN_LENGTH = 18;
      private final Iterable<File> filesToScan;
      private final Map<String, String> translation;

      public ReferenceReplacer(Iterable<File> var1, Map<String, String> var2) {
         this.filesToScan = var1;
         this.translation = var2;
      }

      public void replaceReferences() throws IOException {
         for (File var2 : this.filesToScan) {
            try {
               this.processFile(new File(var2.getParent(), this.translation.get(var2.getName())));
            } catch (RuntimeException var4) {
               if (var4.getCause() instanceof IOException) {
                  throw (IOException)var4.getCause();
               }

               throw var4;
            }
         }
      }

      private void processFile(File var1) throws IOException {
         File var2 = new File(var1.getParent(), var1.getName() + "2");
         final BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2), Charsets.UTF_8));

         try {
            Files.readLines(var1, Charsets.UTF_8, new LineProcessor<Void>() {
               @Override
               public boolean processLine(String var1) throws IOException {
                  String var2x = ReferenceReplacer.this.translateLine(var1);
                  var3.write(var2x);
                  var3.newLine();
                  return true;
               }

               public Void getResult() {
                  return null;
               }
            });
         } finally {
            var3.close();
         }

         FileUtils.forceDelete(var1);
         FileUtils.copyFile(var2, var1);
         FileUtils.forceDelete(var2);
      }

      private String translateLine(String var1) {
         if (var1.length() < 18) {
            return var1;
         }

         for (Entry var3 : this.translation.entrySet()) {
            String var4 = schemaLocation((String)var3.getKey());
            String var5 = schemaLocation((String)var3.getValue());
            var1 = var1.replace(var4, var5);
         }

         return var1;
      }

      private static String schemaLocation(String var0) {
         return "schemaLocation=\"" + var0 + "\"";
      }
   }
}
