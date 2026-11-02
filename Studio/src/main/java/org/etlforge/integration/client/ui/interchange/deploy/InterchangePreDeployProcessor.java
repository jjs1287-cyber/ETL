package org.etlforge.integration.client.ui.interchange.deploy;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.deploy.DeployException;
import org.etlforge.integration.client.ui.deploy.PreDeployProcessor;
import org.etlforge.integration.client.ui.entity.EntityPageLocator;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiThreadSupplier;

final class InterchangePreDeployProcessor implements PreDeployProcessor {
   private final EntityPageLocator pageLocator;

   public InterchangePreDeployProcessor(EntityPageLocator var1) {
      Preconditions.checkNotNull(var1, "pageLocator");
      this.pageLocator = var1;
   }

   @Override
   public void process(final IntegrationEntity var1) throws DeployException {
      if (!(var1 instanceof LogFileRepository.Folder)) {
         try {
            UiThreadSupplier.call(new Supplier<Void>() {
               public Void get() {
                  InterchangePreDeployProcessor.this.saveIfNecessary(var1);
                  return null;
               }
            });
         } catch (Exception var3) {
            if (var3.getCause() instanceof SaveEditorException) {
               throw new DeployException("Failed to save the current changes made to " + var1.getName() + ".", var3.getCause());
            }
         }
      }
   }

   private void saveIfNecessary(IntegrationEntity var1) {
      Editor var2 = this.pageLocator.findOpenPage(var1);
      if (var2 != null) {
         try {
            var2.save();
         } catch (SaveEditorException var4) {
            throw new RuntimeException(var4);
         }
      }
   }
}
