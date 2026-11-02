package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.data.entity.EmailMessage;

class EditEmailAction extends AbstractAction {
   private final OperationGraphController controller;
   private final EmailMessage email;

   public EditEmailAction(OperationGraphController var1, EmailWrapperNode var2) {
      super("Edit...");
      this.controller = var1;
      this.email = var2.getDataObject().getActivity();
      this.putValue("ShortDescription", "Edit " + this.email.getName());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.openEditor(this.email);
   }
}
