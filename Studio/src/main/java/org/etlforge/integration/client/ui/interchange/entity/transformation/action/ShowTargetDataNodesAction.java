package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.ui.action.ToggleAction;

public final class ShowTargetDataNodesAction extends AbstractShowDataNodesOnlyAction implements ToggleAction {
   public static final String ID = "action.ic.tx.ShowTargetData";

   public ShowTargetDataNodesAction(TransformationPage var1) {
      super("action.ic.tx.ShowTargetData", var1);
   }

   @Override
   protected TreeComponent getTreeComponent(TreeMapper var1) {
      return var1.m_targetTreeComponent;
   }

   @Override
   protected MappingTreeDataServiceListener createDataServiceListener() {
      return new ShowTargetDataNodesAction.Listener();
   }

   @Override
   protected boolean isAvailable() {
      return this.hasTargetData();
   }

   private boolean hasTargetData() {
      return this.page.getTreeDataService().hasTargetData();
   }

   private class Listener extends MappingTreeDataServiceListener.Adapter {
      private Listener() {
      }

      @Override
      public void allDataRemoved() {
         ShowTargetDataNodesAction.this.setEnabled(false);
      }

      @Override
      public void testTransformationDataAvailable(DataFilesInfo var1) {
         this.checkEnabledState(var1);
      }

      private void checkEnabledState(DataFilesInfo var1) {
         boolean var2 = var1.containsFileOfType(DataFileType.TARGET);
         ShowTargetDataNodesAction.this.setEnabled(var2);
      }

      @Override
      public void testOperationDataAvailable(DataFilesInfo var1) {
         this.checkEnabledState(var1);
      }
   }
}
