package org.etlforge.integration.client.project;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import org.etlforge.io.FileUtils;
import org.etlforge.string.StringPredicates;

public final class ProjectFolderDetector {
   private final Map<File, File> projectFolders = Maps.newHashMap();
   private final Set<File> nonProjectFolders = Sets.newHashSet();

   public boolean isProjectFolder(File var1) {
      if (this.nonProjectFolders.contains(var1)) {
         return false;
      }

      File var2 = this.projectFolders.get(var1);
      return var2 != null ? true : this.checkPossibleProjectFolder(var1);
   }

   private boolean checkPossibleProjectFolder(File var1) {
      for (String var4 : Iterables.filter(FileUtils.listNames(var1), StringPredicates.endsWith(".jip"))) {
         File var5 = new File(var1, var4);
         if (this.checkProjectFile(var5)) {
            this.projectFolders.put(var1, var5);
            return true;
         }
      }

      return false;
   }

   private boolean checkProjectFile(File var1) {
      try {
         String var2 = Files.readLines(var1, Charset.defaultCharset(), new ProjectFolderDetector.JipReader());
         if (var2 != null) {
            File var3 = new File(var1.getParent(), var2);
            return var3.exists() && var3.isFile();
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return false;
   }

   public File getProjectFile(File var1) {
      return this.projectFolders.get(var1);
   }

   private static class JipReader implements LineProcessor<String> {
      private final String key = "structure-file=";
      private String structureFileName;

      private JipReader() {
      }

      public String getResult() {
         return this.structureFileName;
      }

      @Override
      public boolean processLine(String var1) {
         if (var1.startsWith("structure-file=")) {
            this.structureFileName = var1.substring("structure-file=".length());
            return false;
         } else {
            return true;
         }
      }
   }
}
