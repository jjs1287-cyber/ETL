package org.etlforge.xml.xsd;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thaiopensource.relaxng.translate.Driver;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public final class XsdGenerator {
   private final File xmlFile;
   private File outputFolder;
   private String xsdFileName;
   private String inputEncoding;
   private boolean includeMainXsdNameInSecondarySchemaNames;

   public XsdGenerator(File var1) {
      this.xmlFile = var1;
      this.outputFolder = var1.getParentFile();
      this.xsdFileName = FilenameUtils.getBaseName(var1.getName()) + ".xsd";
   }

   public void setOutputFolder(File var1) {
      Preconditions.checkArgument(var1.isDirectory(), "folder is not a directory");
      this.outputFolder = var1;
   }

   public void setXsdFileName(String var1) {
      String var2 = var1.trim();
      this.xsdFileName = var2.toLowerCase().endsWith(".xsd") ? var2 : var2 + ".xsd";
   }

   public void setInputEncoding(String var1) {
      this.inputEncoding = var1;
   }

   public void setIncludeMainXsdNameInSecondarySchemaNames(boolean var1) {
      this.includeMainXsdNameInSecondarySchemaNames = var1;
   }

   public GeneratedXsds generate() throws Exception {
      Set var1 = this.collectXsdFilesInOutputFolder();
      if (!var1.isEmpty()) {
         throw new Exception("The output folder already contains XSDs. This is not supported.");
      } else {
         File var2 = new File(this.outputFolder, this.xsdFileName);
         this.runTrang(var2);
         Set var3 = this.collectXsdFilesInOutputFolder();
         if (!var3.remove(var2)) {
            throw new Exception("The requested output file was not created.");
         } else {
            GeneratedXsds var4 = new GeneratedXsds(var2, var3);
            if (this.includeMainXsdNameInSecondarySchemaNames) {
               GeneratedXsdNameChanger var5 = new GeneratedXsdNameChanger(var4);
               return var5.replaceNames();
            } else {
               return var4;
            }
         }
      }
   }

   private Set<File> collectXsdFilesInOutputFolder() {
      HashSet var1 = Sets.newHashSet();
      File[] var2 = this.xmlFile.getParentFile().listFiles(new XsdGenerator.XsdNameFilter());
      if (var2 != null) {
         var1.addAll(Arrays.asList(var2));
      }

      return var1;
   }

   private void runTrang(File var1) {
      String[] var2 = this.createArgs(var1);
      Driver var3 = new Driver();
      var3.run(var2);
   }

   private String[] createArgs(File var1) {
      ArrayList var2 = Lists.newArrayList("-I", "xml");
      if (StringUtils.isNotEmpty(this.inputEncoding)) {
         var2.add("-i");
         var2.add("encoding=" + this.inputEncoding);
      }

      var2.add("-O");
      var2.add("xsd");
      var2.add("-o");
      var2.add("encoding=utf-8");
      var2.add(this.xmlFile.getAbsolutePath());
      var2.add(var1.getAbsolutePath());
      return (String[]) var2.toArray(new String[var2.size()]);
   }

   private static class XsdNameFilter implements FileFilter {
      private XsdNameFilter() {
      }

      @Override
      public boolean accept(File var1) {
         return var1.getName().toLowerCase().endsWith(".xsd");
      }
   }
}
