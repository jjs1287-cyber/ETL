package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

final class SourceTreeNodeTransferAction extends AbstractSingleObjectSelectionAction<Node> {
   private final Receiver<String> receiver;

   public SourceTreeNodeTransferAction(FormulaBuilderSourceTree var1, Receiver<String> var2) {
      super(Node.class, null, PackageResources.TRANSFER_LEFT);
      this.putValue("ShortDescription", "Insert the selected source node into the expression");
      this.receiver = var2;
      this.setSelectionSource(var1);
   }

   protected void run(Node var1, ActionEvent var2) {
      if (var1.getDisplay().indexOf("(Unknown") >= 0) {
         Alert.error("You can not map the selected source element because its data type is undefined.", "Error");
      } else {
         String var3 = var1.m_deName;
         this.receiver.handle(var3);
      }
   }
}
