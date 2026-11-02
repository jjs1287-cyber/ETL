package com.etlforge.integration.client.ui.sfdc.soql.browser;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class SchemaBrowserTransferHandler extends TransferHandler {
   private final SchemaBrowserTransferHandler.NodeToString nodeToString = new SchemaBrowserTransferHandler.NodeToString();

   @Override
   protected Transferable createTransferable(JComponent var1) {
      KongaTree var2 = (KongaTree)var1;
      List var3 = var2.getSelectedNodes();
      String var4 = Joiner.on(", ").join(Iterables.transform(var3, this.nodeToString));
      return new StringSelection(var4);
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return 1;
   }

   private static class NodeToString implements Function<KongaTreeNode, String> {
      private NodeToString() {
      }

      public String apply(KongaTreeNode var1) {
         return var1 instanceof FieldNode ? ((FieldNode)var1).getSoqlRepresentation() : var1.getDisplayName();
      }
   }
}
