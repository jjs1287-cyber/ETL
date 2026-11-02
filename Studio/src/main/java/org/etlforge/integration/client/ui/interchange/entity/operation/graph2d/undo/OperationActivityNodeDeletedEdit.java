package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.UIManager;
import javax.swing.undo.CompoundEdit;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.operation.flow.EmailWrapper;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.Route;

public class OperationActivityNodeDeletedEdit extends CompoundEdit {
   private final OperationGraphController controller;
   private final String presentationName;

   private OperationActivityNodeDeletedEdit(OperationGraphController var1, String var2) {
      this.controller = var1;
      this.presentationName = GraphResources.getString(var2);
   }

   public OperationActivityNodeDeletedEdit(OperationGraphController var1, OperationWrapperNode var2) {
      this(var1, "DeleteOperationEdit");
      this.collectRouteEdits(var2.getDataObject());
      this.end();
   }

   public OperationActivityNodeDeletedEdit(OperationGraphController var1, EmailWrapperNode var2) {
      this(var1, "DeleteEmailEdit");
      this.collectRouteEdits(var2.getDataObject());
      this.end();
   }

   @Override
   public String getPresentationName() {
      return this.presentationName;
   }

   @Override
   public String getUndoPresentationName() {
      return getPresentationNameImpl("AbstractUndoableEdit.undoText", this.getPresentationName());
   }

   @Override
   public String getRedoPresentationName() {
      return getPresentationNameImpl("AbstractUndoableEdit.redoText", this.getPresentationName());
   }

   private static String getPresentationNameImpl(String var0, String var1) {
      if (!"".equals(var1)) {
         var1 = UIManager.getString(var0) + " " + var1;
      } else {
         var1 = UIManager.getString(var0);
      }

      return var1;
   }

   private void collectRouteEdits(OperationWrapper var1) {
      for (Route var4 : var1.getIncomingRoutes()) {
         switch (var4.getType()) {
            case SUCCESS:
            case FAILURE:
               this.addEdit(new NextOperationEdit(this.controller, var4.getFrom(), var4.getType(), null));
               break;
            case SIBLING:
               this.addEdit(SiblingOperationEdit.siblingRemoved(this.controller, var4.getFrom(), var1.getActivity()));
               break;
            case SCRIPTED:
               throw new IllegalStateException("Scripted Operations cannot be deleted from the graph, so this should not happen.");
         }
      }
   }

   private void collectRouteEdits(EmailWrapper var1) {
      for (Route var3 : var1.getIncomingRoutes()) {
         this.addEdit(new NextEmailEdit(this.controller, var3.getFrom(), var3.getType(), null));
      }
   }
}
