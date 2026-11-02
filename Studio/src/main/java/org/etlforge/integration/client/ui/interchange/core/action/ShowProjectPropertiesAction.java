package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ShowProjectPropertiesAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.ProjectProperties";
   private final CurrentProjectService currentProject;
   private final EntityPageDisplayer pageDisplayer;

   public ShowProjectPropertiesAction(CurrentProjectService var1, EntityPageDisplayer var2) {
      super("action.ic.ProjectProperties", InterchangeActionDictionary.DICTIONARY);
      this.currentProject = var1;
      this.pageDisplayer = var2;
   }

   @Override
   public void execute(ActionEvent var1) {
      IntegrationProject var2 = this.currentProject.getProject();
      if (var2 != null) {
         this.pageDisplayer.openPage(var2);
      }
   }
}
