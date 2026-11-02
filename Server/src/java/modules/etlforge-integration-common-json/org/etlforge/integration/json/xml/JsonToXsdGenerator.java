package org.etlforge.integration.json.xml;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd;
import org.codehaus.jackson.JsonParseException;
import org.etlforge.io.Folder;
import org.etlforge.json.dom.JsonDomParser;
import org.etlforge.json.dom.JsonDomWriter;
import org.etlforge.json.dom.RootNode;

public final class JsonToXsdGenerator {
   private File jsonFile;
   private File trimmedJsonFile;
   private File rawXsd;
   private Folder outputFolder;
   private String xsdName;
   private boolean deleteTempFilesWhenDone = true;

   public void setXsdName(String var1) {
      this.xsdName = var1;
   }

   public void setDeleteTempFilesWhenDone(boolean var1) {
      this.deleteTempFilesWhenDone = var1;
   }

   public File generateXsd(File var1, Folder var2) throws JsonParseException, XsdGeneratorException, IOException {
      this.jsonFile = var1;
      this.outputFolder = var2;
      File var3 = null;
      Object var4 = null;

      try {
         this.deleteExistingXsds();
         this.trimJsonFile();
         var3 = this.createXml();
         return this.createXsd(var3);
      } finally {
         this.deleteTempFiles(var3, (File)var4);
      }
   }

   private void deleteExistingXsds() throws IOException {
      for (File var4 : this.outputFolder.listFiles(new JsonToXsdGenerator.XsdFileFilter(""))) {
         FileUtils.forceDelete(var4);
      }
   }

   private void trimJsonFile() throws JsonParseException, IOException {
      this.trimmedJsonFile = this.outputFolder.getFile(FilenameUtils.getBaseName(this.jsonFile.getName()) + ".trimmed.json");
      JsonDomParser var1 = new JsonDomParser();
      var1.setSkipDuplicateStructures(true);
      RootNode var2 = var1.parse(this.jsonFile);
      JsonDomWriter.writeToFile(var2, this.trimmedJsonFile);
   }

   private File createXml() throws JsonParseException, IOException {
      File var1 = this.trimmedJsonFile == null ? this.jsonFile : this.trimmedJsonFile;
      File var2 = this.outputFolder.getFile(this.jsonFile.getName() + ".xml");
      JsonToXmlGenerator var3 = new JsonToXmlGenerator();
      var3.setIncludeSchemaTypeForStrings(true);
      var3.setUseDomTree(true);
      var3.generateXml(var1, var2);
      return var2;
   }

   private File createXsd(File var1) throws XsdGeneratorException, IOException {
      this.rawXsd = this.createRawXsd(var1);
      return this.tweakRawXsd(var1, this.rawXsd);
   }

   private File createRawXsd(File var1) throws XsdGeneratorException {
      String var2 = FilenameUtils.getName(var1.getName());
      Inst2Xsd.main(new String[]{var1.getAbsolutePath(), "-design", "rd", "-enumerations", "never", "-outDir", var1.getParent(), "-outPrefix", var2});
      File var3 = this.findGeneratedXsdFile(var2);
      if (var3 == null) {
         throw new XsdGeneratorException("The XSD file was not created.");
      } else {
         return var3;
      }
   }

   private File findGeneratedXsdFile(String var1) {
      JsonToXsdGenerator.XsdFileFilter var2 = new JsonToXsdGenerator.XsdFileFilter(var1);
      File[] var3 = this.outputFolder.listFiles(var2);
      int var4 = var3.length;
      byte var5 = 0;
      return var5 < var4 ? var3[var5] : null;
   }

   private File tweakRawXsd(File var1, File var2) throws XsdGeneratorException, IOException {
      TypeEnforcer var3 = new TypeEnforcer();
      var3.enforceTypes(var1, var2);
      XsdTweaker var4 = new XsdTweaker();
      File var5 = this.getFinalXsdFile(var2);
      var4.tweak(var2, var5);
      return var5;
   }

   private File getFinalXsdFile(File var1) {
      String var2 = this.xsdName;
      if (StringUtils.isEmpty(var2)) {
         var2 = StringUtils.substringBefore(var1.getName(), ".json.xml0");
      }

      if (!var2.toLowerCase().endsWith(".xsd")) {
         var2 = var2 + ".xsd";
      }

      return this.outputFolder.getFile(var2);
   }

   private void deleteTempFiles(File var1, File var2) {
      if (this.deleteTempFilesWhenDone) {
         if (var1 != null) {
            var1.delete();
         }

         if (this.rawXsd != null && !this.rawXsd.equals(var2)) {
            this.rawXsd.delete();
         }

         if (this.trimmedJsonFile != null) {
            this.trimmedJsonFile.delete();
         }
      }
   }

   private static class XsdFileFilter implements FileFilter {
      private final String prefix;

      public XsdFileFilter(String var1) {
         this.prefix = var1.toLowerCase();
      }

      @Override
      public boolean accept(File var1) {
         String var2 = var1.getName().toLowerCase();
         return var2.startsWith(this.prefix) && var2.endsWith(".xsd");
      }
   }
}
