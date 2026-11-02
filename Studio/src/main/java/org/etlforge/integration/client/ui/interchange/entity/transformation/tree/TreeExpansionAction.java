package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public final class TreeExpansionAction extends AbstractAction {
   private static final Icon expandIcon = new ImageIcon(StagingPanel.m_imgExpand);
   private static final Icon partExpandIcon = new ImageIcon(StagingPanel.m_imgPartExpand);
   private static final Icon collapseIcon = new ImageIcon(StagingPanel.m_imgCollapse);
   private static final boolean removeCollapseFolder = true;
   private final TreeComponent tree;

   public TreeExpansionAction(TreeComponent var1) {
      super(null, expandIcon);
      this.putValue("ShortDescription", "Expand/Collapse Tree");
      this.tree = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.tree.changeShowChildren();
      switch (this.tree.m_expandMode) {
         case 0:
            this.changeIcon(expandIcon);
            break;
         case 1:
            Icon var3 = collapseIcon;
            this.changeIcon(var3);
            break;
         case 2:
            Icon var2 = expandIcon;
            this.changeIcon(var2);
      }

      this.tree.refresh();
   }

   private void changeIcon(Icon var1) {
      this.putValue("SmallIcon", var1);
   }
}
