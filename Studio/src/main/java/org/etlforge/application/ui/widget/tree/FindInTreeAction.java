package org.etlforge.application.ui.widget.tree;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.find.SearchTextDialog;
import org.etlforge.ui.util.find.TextSearchModel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.TreeSearchTarget;

public final class FindInTreeAction extends AbstractAction {
   private static TextSearchModel sharedModel;
   private final KongaTree tree;
   private final String title;

   public FindInTreeAction(KongaTree var1) {
      this(var1, null);
   }

   public FindInTreeAction(KongaTree var1, String var2) {
      Preconditions.checkNotNull(var1, "tree");
      ActionDecorators.FIND.decorate(this);
      this.tree = var1;
      this.title = var2;
   }

   public final void setToolTip(String var1) {
      this.putValue("ShortDescription", var1);
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            FindInTreeAction.this.openDialog();
         }
      });
   }

   private void openDialog() {
      TextSearchModel var1 = this.getSearchModel();
      TreeSearchTarget var2 = new TreeSearchTarget(this.tree);
      SearchTextDialog var3 = new SearchTextDialog(var1, var2);
      var3.setTitle(this.title);
      Window var4 = this.getDialogOwner();
      var3.show(var4);
   }

   private TextSearchModel getSearchModel() {
      if (sharedModel == null) {
         sharedModel = new TextSearchModel();
      }

      return sharedModel;
   }

   private Window getDialogOwner() {
      Window var1 = SwingUtilities.getWindowAncestor(this.tree);
      if (var1 == null) {
         var1 = UiUtils.getActiveWindow();
      }

      return var1;
   }
}
