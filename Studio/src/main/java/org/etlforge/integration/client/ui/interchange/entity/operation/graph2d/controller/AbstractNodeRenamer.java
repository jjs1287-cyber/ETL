package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.EntityRenameEdit;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.graph2d.GraphNodeEditor;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;

abstract class AbstractNodeRenamer<T extends AbstractGraphNode> {
   private final OperationGraphController controller;
   private final T node;

   public AbstractNodeRenamer(OperationGraphController var1, T var2) {
      this.controller = var1;
      this.node = (T)var2;
   }

   public void rename() {
      GraphNodeUi2D var1 = this.controller.getGraph().getNodeUi(this.node);
      GraphNodeEditor var2 = var1.getEditor();
      if (var2 != null) {
         var2.addCellEditorListener(new AbstractNodeRenamer.EditorListener(var2));
         this.controller.getGraph().showNodeEditor(var2);
      }
   }

   protected final OperationGraphController getController() {
      return this.controller;
   }

   protected final T getNode() {
      return this.node;
   }

   protected abstract void processNewValue(GraphNodeEditor var1);

   protected final void applyNewName(IntegrationEntity var1, String var2) {
      EntityRenameEdit var3 = new EntityRenameEdit(var1, var2);
      if (UiCompanion.of(var1).rename(var2)) {
         this.getController().fireGraphDataEdit(var3);
      }
   }

   private class EditorListener implements CellEditorListener {
      private final GraphNodeEditor editor;

      public EditorListener(GraphNodeEditor var2) {
         this.editor = var2;
      }

      @Override
      public void editingCanceled(ChangeEvent var1) {
         this.done();
      }

      @Override
      public void editingStopped(ChangeEvent var1) {
         AbstractNodeRenamer.this.processNewValue(this.editor);
         this.done();
      }

      private void done() {
         AbstractNodeRenamer.this.controller.getGraph().requestFocus();
      }
   }
}
