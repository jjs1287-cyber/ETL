package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.debug.client.file.DataFileType;
import org.etlforge.integration.debug.client.file.DataFilesInfo;
import org.etlforge.ui.action.ToggleAction;

public final class ShowSourceDataNodesAction extends AbstractShowDataNodesOnlyAction implements ToggleAction {
   public static final String ID = "action.ic.tx.ShowSourceData";

   public ShowSourceDataNodesAction(TransformationPage var1) {
      super("action.ic.tx.ShowSourceData", var1);
   }

   @Override
   protected TreeComponent getTreeComponent(TreeMapper var1) {
      return var1.m_sourceTreeComponent;
   }

   @Override
   protected MappingTreeDataServiceListener createDataServiceListener() {
      return new ShowSourceDataNodesAction.Listener();
   }

   @Override
   protected boolean isAvailable() {
      return this.hasSourceData();
   }

   private boolean hasSourceData() {
      return this.page.getTreeDataService().hasSourceData();
   }

   private class Listener extends MappingTreeDataServiceListener.Adapter {
      private Listener() {
      }

      @Override
      public void allDataRemoved() {
         ShowSourceDataNodesAction.this.setEnabled(false);
      }

      @Override
      public void loadSourceDataAvailable(DataFilesInfo var1) {
         ShowSourceDataNodesAction.this.setEnabled(true);
      }

      @Override
      public void testOperationDataAvailable(DataFilesInfo var1) {
         boolean var2 = var1.containsFileOfType(DataFileType.SOURCE);
         ShowSourceDataNodesAction.this.setEnabled(var2);
      }
   }
}
