package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.ui.interchange.entity.transformation.batch.DialogBatchSizeInput;
import org.etlforge.integration.client.ui.interchange.entity.transformation.batch.SourceBatchChange;
import org.etlforge.integration.client.ui.interchange.entity.transformation.batch.SourceBatchEditor;
import org.etlforge.integration.data.entity.transformation.SourceBatchParams;
import org.etlforge.system.callback.Receiver;

final class TreeMapperSourceBatchNodeDefiner {
   private final TreeMapper treeMapper;
   private SourceBatchEditor sourceBatchEditor;

   public TreeMapperSourceBatchNodeDefiner(TreeMapper var1) {
      this.treeMapper = var1;
   }

   public void editBatchNode(String var1) {
      if (this.isSourceBatchingSupported()) {
         this.editImpl(var1);
      } else {
         NonBlockingAlert.error("The current version does not support batching when the Transformation source is XML or a web service call", "Not Supported");
      }
   }

   private void editImpl(String var1) {
      if (this.sourceBatchEditor == null) {
         this.sourceBatchEditor = this.createSourceBatchEditor();
      }

      SourceBatchChange var2 = this.sourceBatchEditor.edit(var1);
      if (var2 != null) {
         this.treeMapper.getTreeMapperContainer().registerSaveJob(var2);
         this.treeMapper.m_sourceTreeComponent.refreshBatchNode(var2.getNewParams());
      }
   }

   private boolean isSourceBatchingSupported() {
      return true;
   }

   private SourceBatchEditor createSourceBatchEditor() {
      DialogBatchSizeInput var1 = new DialogBatchSizeInput();
      SourceBatchEditor var2 = new SourceBatchEditor(this.treeMapper.getTransformation(), var1);
      var2.setUndoRedoMonitor(new Receiver<SourceBatchParams>() {
         public void handle(SourceBatchParams var1) {
            TreeMapperSourceBatchNodeDefiner.this.treeMapper.m_sourceTreeComponent.refreshBatchNode(var1);
         }
      });
      return var2;
   }
}
