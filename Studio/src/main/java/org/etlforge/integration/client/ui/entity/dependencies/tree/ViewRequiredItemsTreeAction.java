package org.etlforge.integration.client.ui.entity.dependencies.tree;

import java.awt.EventQueue;
import javax.swing.Icon;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.util.UiUtils;

public final class ViewRequiredItemsTreeAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public ViewRequiredItemsTreeAction(ActiveObjectService var1) {
      super(ViewRequiredItemsTreeAction.class.getName(), var1, "View Required Items", null, IntegrationEntity.class);
   }

   public ViewRequiredItemsTreeAction(ActiveObjectService var1, String var2, ActionDictionary var3) {
      super(var1, var2, var3, IntegrationEntity.class);
   }

   public ViewRequiredItemsTreeAction(ActiveObjectService var1, String var2, String var3, Icon var4) {
      super(var2, var1, var3, var4, IntegrationEntity.class);
   }

   protected void runOnObject(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewRequiredItemsTreeAction.this.displayRequiredItemsTree(var1);
         }
      });
   }

   private void displayRequiredItemsTree(IntegrationEntity var1) {
      DependencyStore var2 = this.getDependencies(var1);
      if (var2 != null) {
         try {
            RequiredObjectsTreeUi var3 = new RequiredObjectsTreeUi(var1, var2);
            UiProviderDialogDisplayer.display(UiUtils.getActiveWindow(), var3, "Required Objects");
         } finally {
            var2.dispose();
         }
      } else {
         Alert.error("The selected object does not belong to a project.", "Error");
      }
   }

   private DependencyStore getDependencies(IntegrationEntity var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      return var2 != null ? var2.getCurrentDependencies() : null;
   }
}
