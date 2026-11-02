package org.etlforge.integration.client.connector.schema;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.upload.RequiredFiles;
import org.etlforge.integration.filetransfer.client.upload.RequiredFilesProvider;

final class ImportedFilesProvider implements RequiredFilesProvider {
   @Nullable
   @Override
   public List<RequiredFiles> locateRequiredFiles(List<RequiredFiles> var1, File var2) {
      ArrayList var3 = Lists.newArrayList();

      for (RequiredFiles var5 : var1) {
         ServerFile var6 = var5.getParentFile();
         ImmutableList var7 = this.findFilesOnDisk(var2, var5, var6);
         var3.add(new RequiredFiles(var6, var7));
      }

      return var3;
   }

   private ImmutableList<File> findFilesOnDisk(File var1, RequiredFiles var2, ServerFile var3) {
      ArrayList var4 = Lists.newArrayList();

      for (File var6 : var2.getRequiredFiles()) {
         File var7 = new File(var1, var6.getName());
         if (!var7.exists()) {
            String var8 = String.format(
               "Could not find the file \"%s\" which is imported by \"%s\". Looked for it here: %s", var6.getName(), var3.getName(), var1.getAbsolutePath()
            );
            throw new RuntimeException(var8);
         }

         var4.add(var7);
      }

      return ImmutableList.copyOf(var4);
   }
}
