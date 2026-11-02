package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.ui.graph2d.GraphNodeEditor;

final class EmailNodeRenamer extends AbstractNodeRenamer<EmailWrapperNode> {
   public EmailNodeRenamer(OperationGraphController var1, EmailWrapperNode var2) {
      super(var1, var2);
   }

   @Override
   protected void processNewValue(GraphNodeEditor var1) {
      EmailMessage var2 = this.getNode().getDataObject().getActivity();
      String var3 = var2.getName();
      String var4 = (String)var1.getCellEditorValue();
      if (!var3.equals(var4)) {
         this.applyNewName(var2, var4);
      }
   }
}
