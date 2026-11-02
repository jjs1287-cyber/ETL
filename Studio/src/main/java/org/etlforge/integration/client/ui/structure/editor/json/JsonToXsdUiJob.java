package org.etlforge.integration.client.ui.structure.editor.json;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.structure.editor.xsd.CreateXsdUiJob;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFile;
import org.etlforge.integration.client.ui.structure.editor.xsd.SampleFileFactory;
import org.etlforge.integration.json.xml.JsonToXsdGenerator;
import org.etlforge.io.Folder;
import org.etlforge.xml.xsd.GeneratedXsds;

final class JsonToXsdUiJob extends CreateXsdUiJob {
   public JsonToXsdUiJob(SampleFileFactory var1) {
      super(var1);
   }

   @Override
   protected GeneratedXsds generateXsds(SampleFile var1) throws Exception {
      JsonToXsdGenerator var2 = new JsonToXsdGenerator();
      this.applyUserXsdName(var2);
      File var3 = var1.getFile();
      File var4 = var2.generateXsd(var3, Folder.parentOf(var3));
      if (this.useAutomaticXsdName()) {
         var4 = this.tweakAutomaticallyGeneratedName(var4);
      }

      return new GeneratedXsds(var4);
   }

   private void applyUserXsdName(JsonToXsdGenerator var1) {
      if (!this.useAutomaticXsdName()) {
         String var2 = this.getXsdFileName();
         var2 = appendXsdSuffixIfNecessary(var2);
         var2 = appendXsdExtensionIfNecessary(var2);
         var1.setXsdName(var2);
      }
   }

   private static String appendXsdSuffixIfNecessary(String var0) {
      return var0.toLowerCase().startsWith("etlforge.json.".toLowerCase()) ? var0 : "etlforge.json." + var0;
   }

   private static String appendXsdExtensionIfNecessary(String var0) {
      return var0.toLowerCase().endsWith(".xsd") ? var0 : var0 + ".xsd";
   }

   private boolean useAutomaticXsdName() {
      String var1 = this.getXsdFileName();
      return StringUtils.isEmpty(var1);
   }

   private File tweakAutomaticallyGeneratedName(File var1) throws Exception {
      String var2 = var1.getName();
      String var3 = appendXsdSuffixIfNecessary(var2);
      var3 = appendXsdExtensionIfNecessary(var3);
      if (var3.equals(var2)) {
         return var1;
      }

      File var4 = new File(var1.getParent(), var3);
      FileUtils.copyFile(var1, var4);
      var1.delete();
      return var4;
   }
}
