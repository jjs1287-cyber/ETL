package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceEDTListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.TransformationFileStore;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.debug.client.file.DataFilesInfo;

public final class ClearTestDataAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.RemoveData";
   private final TransformationPage page;
   private final MappingTreeDataServiceListener listener;

   public ClearTestDataAction(TransformationPage var1) {
      super("action.ic.tx.RemoveData", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
      this.listener = new MappingTreeDataServiceEDTListener(new ClearTestDataAction.Listener());
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            ClearTestDataAction.this.listenToFileStore();
            DataFilesInfo var1x = ClearTestDataAction.this.getCurrentFileInfo();
            ClearTestDataAction.this.updateEnabledState(var1x);
         }
      });
   }

   private void listenToFileStore() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.addListener(this.listener);
   }

   private DataFilesInfo getCurrentFileInfo() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      TransformationFileStore var2 = var1.getDataFileStore();
      return var2.getCurrentFilesInfo();
   }

   private void updateEnabledState(DataFilesInfo var1) {
      this.setEnabledSafely(var1 != null);
   }

   @Override
   public void execute(ActionEvent var1) {
      (new BusyWorker(this.page.getWaitService()) {
         @Override
         protected void doWork() {
            ClearTestDataAction.this.removeTestData();
         }
      }).run();
   }

   private void removeTestData() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.removeAllTestData();
      this.clearShowDataNodesOnlyFlag();
   }

   private void clearShowDataNodesOnlyFlag() {
      TreeMapper var1 = this.page.getTreeMapper();
      var1.m_sourceTreeComponent.setShowDataNodesOnly(false);
      var1.m_targetTreeComponent.setShowDataNodesOnly(false);
   }

   @Override
   public final void dispose() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.removeListener(this.listener);
   }

   private class Listener implements MappingTreeDataServiceListener {
      private Listener() {
      }

      @Override
      public void allDataRemoved() {
         ClearTestDataAction.this.updateEnabledState(null);
      }

      @Override
      public void loadSourceDataAvailable(DataFilesInfo var1) {
         ClearTestDataAction.this.updateEnabledState(var1);
      }

      @Override
      public void testOperationDataAvailable(DataFilesInfo var1) {
         ClearTestDataAction.this.updateEnabledState(var1);
      }

      @Override
      public void targetDataRemoved() {
         DataFilesInfo var1 = ClearTestDataAction.this.getCurrentFileInfo();
         ClearTestDataAction.this.updateEnabledState(var1);
      }

      @Override
      public void testTransformationDataAvailable(DataFilesInfo var1) {
         ClearTestDataAction.this.updateEnabledState(var1);
      }
   }
}
