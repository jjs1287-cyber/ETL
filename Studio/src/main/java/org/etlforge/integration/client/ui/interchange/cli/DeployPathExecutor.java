package org.etlforge.integration.client.ui.interchange.cli;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.cli.CommandException;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class DeployPathExecutor extends AbstractProjectPathCommandExecutor {
   private final DeployFacade deployFacade;
   private final EditorService editorService;

   public DeployPathExecutor(CurrentProjectService var1, DeployFacade var2, EditorService var3) {
      super(var1);
      this.deployFacade = var2;
      this.editorService = var3;
   }

   @Override
   protected void processMatchingItems(Set<IntegrationEntity> var1) throws CommandException {
      this.deployObjects(var1);
   }

   @Override
   protected void noMatchingItems(String var1) throws CommandException {
      if (StringUtils.isBlank(var1)) {
         this.deployDisplayedObject();
      } else {
         Alert.error("Nothing matches the path \"" + var1 + "\".", "Path Not Found");
      }
   }

   private void deployDisplayedObject() throws CommandException {
      Editor var1 = this.editorService.getActiveEditor();
      if (var1 instanceof IntegrationEntityPage) {
         IntegrationEntity var2 = ((IntegrationEntityPage)var1).getObject();
         this.deployObjects(Sets.newHashSet(var2));
      } else {
         throw new CommandException("No object open to deploy.");
      }
   }

   private void deployObjects(final Set<IntegrationEntity> var1) {
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            DeployPathExecutor.this.deployFacade.deploy(var1);
         }
      });
   }
}
