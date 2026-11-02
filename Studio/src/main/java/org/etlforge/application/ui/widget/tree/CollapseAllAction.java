package org.etlforge.application.ui.widget.tree;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.widget.tree.KongaTree;

public final class CollapseAllAction extends AbstractAction {
   private final KongaTree tree;

   public CollapseAllAction(KongaTree var1) {
      this(var1, null);
   }

   public CollapseAllAction(KongaTree var1, String var2) {
      super(var2, ApplicationResources.ICONS.getIcon("CollapseTree.16"));
      this.tree = var1;
      Preconditions.checkNotNull(var1, "tree");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.tree.collapseAll();
   }
}
