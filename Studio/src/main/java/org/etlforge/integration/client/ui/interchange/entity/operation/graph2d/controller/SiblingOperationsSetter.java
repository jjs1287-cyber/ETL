package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.undo.UndoableEdit;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.SiblingOperationEdit;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;

final class SiblingOperationsSetter {
   private final OperationGraphController controller;

   public SiblingOperationsSetter(OperationGraphController var1) {
      this.controller = var1;
   }

   public void setSiblings(OperationWrapper var1, List<Operation> var2) {
      if (this.controller.isChangeAllowed(var1)) {
         List var3 = this.getSiblingIds(var2);
         UndoableEdit var4 = this.createEdit(var1, var3);
         var1.setSiblingIds(var3);
         this.controller.fireGraphDataEdit(var4);
      }
   }

   private List<OperationId> getSiblingIds(List<Operation> var1) {
      ArrayList var2 = Lists.newArrayListWithExpectedSize(var1.size());

      for (Operation var4 : var1) {
         var2.add(var4.getID());
      }

      return var2;
   }

   private UndoableEdit createEdit(OperationWrapper var1, List<OperationId> var2) {
      return new SiblingOperationEdit(this.controller, var1, var2);
   }
}
