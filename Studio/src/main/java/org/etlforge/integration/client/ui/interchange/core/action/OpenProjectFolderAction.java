package org.etlforge.integration.client.ui.interchange.core.action;

import java.io.File;
import org.etlforge.application.ui.window.action.OpenLocalFolderAction;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectManager;

public final class OpenProjectFolderAction extends OpenLocalFolderAction {
   public static final String ID = "action.ic.OpenProjectFolder";
   private final ProjectManager projectManager;

   public OpenProjectFolderAction(ProjectManager var1) {
      super("action.ic.OpenProjectFolder", InterchangeActionDictionary.DICTIONARY);
      this.projectManager = var1;
   }

   @Override
   protected File getFolderToOpen() {
      ManagedProject var1 = this.projectManager.getCurrentProject();
      return var1 != null ? var1.getLocation() : null;
   }
}
