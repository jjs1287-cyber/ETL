package org.etlforge.integration.client.ui.deploy.option;

import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModelFactory;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class DeployDataPreview {
   private final IntegrationProject project;

   public DeployDataPreview(IntegrationProject var1) {
      this.project = var1;
   }

   public void show() {
      IntegrationEntityTree var1 = this.createTree();
      KongaDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
   }

   private IntegrationEntityTree createTree() {
      IntegrationEntityTreeModelFactory var1 = new IntegrationEntityTreeModelFactory();
      IntegrationEntityTreeModel var2 = var1.newModel(this.project);
      var2.setFilter(new KongaTreeFilter() {
         @Override
         public boolean apply(KongaTreeNode var1) {
            return var1.getUserObject() instanceof RootFolder ? ((RootFolder)var1.getUserObject()).getNumberOfChildren() > 0 : true;
         }
      });
      IntegrationEntityTree var3 = new IntegrationEntityTree(var2);
      var3.setRootVisible(false);
      var3.setVisibleRowCount(16);
      var3.expandAll();
      return var3;
   }

   private KongaDialog createDialog(IntegrationEntityTree var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 2);
      var2.north("The following items will be deployed:").center(var1);
      KongaDialog var3 = new KongaDialog(UiUtils.getActiveFrame(), "Items To Deploy", true);
      var3.standardLayout(var2, KongaDialog.CLOSE_ONLY);
      var3.setLocationRelativeTo(null);
      var3.manageLocation(DeployDataPreview.class);
      return var3;
   }
}
