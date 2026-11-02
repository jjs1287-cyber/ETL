package org.etlforge.integration.client.ui.interchange.core.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.entity.DependenciesViewFactory;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ViewDependenciesAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public static final String ID = "action.ic.ViewDependencies";
   private final DependenciesViewFactory viewFactory;

   public ViewDependenciesAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var1.getActiveObjectService(), "action.ic.ViewDependencies", InterchangeActionDictionary.DICTIONARY, IntegrationEntity.class);
      this.viewFactory = new DependenciesViewFactory(var1, var2);
   }

   protected boolean isEnabledFor(IntegrationEntity var1) {
      return !(var1 instanceof Folder) && !(var1 instanceof IntegrationProject);
   }

   protected void runOnObject(IntegrationEntity var1) {
      this.viewDependencies(var1);
   }

   private void viewDependencies(IntegrationEntity var1) {
      this.viewFactory.showDependencies(var1);
   }
}
