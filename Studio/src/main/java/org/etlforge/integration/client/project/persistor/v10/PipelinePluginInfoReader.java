package org.etlforge.integration.client.project.persistor.v10;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectPipelinePluginStore;
import org.etlforge.io.KongaIoUtils;

final class PipelinePluginInfoReader {
   public static ProjectPipelinePluginStore read(ProjectFile var0) throws IOException {
      ProjectPipelinePluginStore var1 = new ProjectPipelinePluginStore();
      File var2 = getFile(var0);
      if (var2.exists()) {
         InputStreamReader var3 = new InputStreamReader(new FileInputStream(var2), Charsets.UTF_8);

         try {
            var1.load(var3);
         } finally {
            KongaIoUtils.close(var3);
         }
      }

      return var1;
   }

   private static File getFile(ProjectFile var0) {
      String var1 = var0.getProperty("pipeline-plugin-file");
      return new File(var0.getParentFolder(), var1);
   }

   private PipelinePluginInfoReader() {
   }
}
