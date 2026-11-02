package org.etlforge.integration.client.project.persistor.v10;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectFile;

final class PipelinePluginInfoWriter {
   private final ManagedProject project;
   private final MutableAppliedPipelinePluginStore pluginStore;

   public PipelinePluginInfoWriter(ManagedProject var1) {
      this.project = var1;
      this.pluginStore = var1.getPipelinePlugins().getAppliedPlugins();
   }

   public void write() throws IOException {
      File var1 = this.getFile();
      OutputStreamWriter var2 = new OutputStreamWriter(new FileOutputStream(var1), Charsets.UTF_8);

      try {
         this.pluginStore.persist(var2);
      } finally {
         var2.close();
      }
   }

   private File getFile() {
      ProjectFile var1 = this.project.getProjectFile();
      String var2 = var1.getProperty("pipeline-plugin-file");
      return new File(this.project.getLocation(), var2);
   }
}
