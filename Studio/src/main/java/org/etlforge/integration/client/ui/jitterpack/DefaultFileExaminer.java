package org.etlforge.integration.client.ui.jitterpack;

import java.io.File;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.jitterpack.ExtractDescriptorCallback;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;

public final class DefaultFileExaminer implements SelectedFileExaminer {
   private final ApplicationWindow window;
   private final JitterPackerUi packerUi;
   private final ImporterFactory importerFactory;

   public DefaultFileExaminer(ApplicationWindow var1, JitterPackerUi var2, ImporterFactory var3) {
      this.window = var1;
      this.packerUi = var2;
      this.importerFactory = var3;
   }

   @Override
   public void examine(File var1, ImportParameters var2) {
      DefaultFileExaminer.ExtractorCallbackImpl var3 = new DefaultFileExaminer.ExtractorCallbackImpl(this.window, var1);
      if (var1.isDirectory()) {
         var3.failed("The path \"" + var1.getAbsolutePath() + "\" is a folder. You need to select a file.", null);
      } else if (var1.exists()) {
         Importer var4 = this.importerFactory.newImporter(var2);
         this.packerUi.extractDescriptor(var1, var2, var4, var3);
      } else {
         var3.failed("The file \"" + var1.getAbsolutePath() + "\" does not exist.", null);
      }
   }

   private static class ExtractorCallbackImpl implements ExtractDescriptorCallback {
      private final ApplicationWindow window;
      private final File file;

      public ExtractorCallbackImpl(ApplicationWindow var1, File var2) {
         this.window = var1;
         this.file = var2;
      }

      @Override
      public void descriptorExtracted(JitterPackDescriptor var1) {
         DescriptorDialog.show(this.window, this.file, var1);
      }

      @Override
      public void failed(String var1, Throwable var2) {
         String var3 = "";
         if (StringUtils.isNotEmpty(var1)) {
            var3 = var3 + var1;
         }

         if (var2 != null) {
            if (!var3.isEmpty()) {
               var3 = var3 + "\n\n";
            }

            var3 = var3 + var2.getMessage();
         }

         if (StringUtils.isBlank(var3)) {
            var3 = Strings.getJitterPackString("Examiner.NotValid");
         }

         Alert.error(var3, ApplicationResources.getString("Error"));
      }
   }
}

