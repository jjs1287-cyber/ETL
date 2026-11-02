package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.ui.selection.AbstractSelectionAction;

abstract class AbstractClassNodeAction extends AbstractSelectionAction {
   private final AttributeSelectionTree tree;

   protected AbstractClassNodeAction(AttributeSelectionTree var1, String var2) {
      super(var2);
      this.tree = var1;
   }

   protected final AttributeSelectionTree getTree() {
      return this.tree;
   }

   protected final AttributeSelectionModel getModel() {
      return this.tree.getModel();
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      final ClassNameNode var2 = this.getSelectedClassNode();
      if (var2 != null) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AbstractClassNodeAction.this.runOnNode(var2);
            }
         });
      }
   }

   protected abstract void runOnNode(ClassNameNode var1);

   @Override
   protected final void handleSelectionChange() {
      this.setEnabled(this.getSelectedClassNode() != null);
   }

   private ClassNameNode getSelectedClassNode() {
      ClassNameNode var1 = null;

      for (Object var3 : this.getSelection().items()) {
         if (var3 instanceof ClassNameNode) {
            if (var1 != null) {
               return null;
            }

            var1 = (ClassNameNode)var3;
         } else if (var3 instanceof AttributeSelectionNode) {
            ClassNameNode var4 = ((AttributeSelectionNode)var3).getParent();
            if (var1 == null) {
               var1 = var4;
            } else if (var1 != var4) {
               return null;
            }
         }
      }

      return var1;
   }
}
