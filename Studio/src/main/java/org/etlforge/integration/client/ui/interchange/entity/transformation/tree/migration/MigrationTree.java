package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration;

import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.MenuBuilder;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.integration.data.structure.DataStructure;

public class MigrationTree extends TreeComponent {
   private final DataStructure structure;

   @Override
   protected void processSingleClick(Node var1, int var2, MouseEvent var3) {
      switch (var2) {
         case 0:
            if (this.getHighlightedNode() != null) {
               this.setHighlightedNode(null);
               this.repaint(0, this.m_ymin, this.m_xmax + 100, this.m_ymax);
            }
            break;
         case 1:
         case 2:
         case 3:
         case 4:
            this.setHighlightedNode(var1);
            this.repaint(0, this.m_ymin, this.m_xmax + 100, this.m_ymax);
      }
   }

   @Override
   protected void processRightClickOnNode(Node var1) {
      JPopupMenu var2 = new JPopupMenu();
      MenuBuilder var3 = new MenuBuilder(this.actionListener, var2);
      this.m_rightClickNode = var1;
      if (this == this.m_TreeMapperMigration.m_newTargetTree || this == this.m_TreeMapperMigration.m_oldTargetTree) {
         var3.addMenuItem("Delete translation of target nodes");
         var3.show(this, var1.m_p.x + 5 + this.m_scroll.getHorizontalScrollBar().getValue(), var1.m_p.y + Node.m_textHeight);
      } else if (this == this.m_TreeMapperMigration.m_newSourceTree || this == this.m_TreeMapperMigration.m_oldSourceTree) {
         var3.addMenuItem("Delete translation of source nodes");
         var3.show(this, var1.m_p.x + 5 + this.m_scroll.getHorizontalScrollBar().getValue(), var1.m_p.y + Node.m_textHeight);
      }
   }

   @Override
   protected void handleDeleteRequest(Node var1) {
      if (var1 != null) {
         if (var1.isFolder()) {
            for (Node var3 : var1.getChildren()) {
               this.handleDeleteRequest(var3);
            }
         }

         if (this == this.m_TreeMapperMigration.m_newTargetTree) {
            this.m_TreeMapperMigration.m_targetMapArea.removeNewNode(var1);
            this.m_TreeMapperMigration.m_targetMapArea.repaint();
            this.m_TreeMapperMigration.m_newTargetTree.refresh();
            this.m_TreeMapperMigration.m_oldTargetTree.refresh();
         } else if (this == this.m_TreeMapperMigration.m_oldTargetTree) {
            this.m_TreeMapperMigration.m_targetMapArea.removeOldNode(var1);
            this.m_TreeMapperMigration.m_targetMapArea.repaint();
            this.m_TreeMapperMigration.m_newTargetTree.refresh();
            this.m_TreeMapperMigration.m_oldTargetTree.refresh();
         } else if (this == this.m_TreeMapperMigration.m_newSourceTree) {
            this.m_TreeMapperMigration.m_sourceMapArea.removeNewNode(var1);
            this.m_TreeMapperMigration.m_sourceMapArea.repaint();
            this.m_TreeMapperMigration.m_newSourceTree.refresh();
            this.m_TreeMapperMigration.m_oldSourceTree.refresh();
         } else if (this == this.m_TreeMapperMigration.m_oldSourceTree) {
            this.m_TreeMapperMigration.m_sourceMapArea.removeOldNode(var1);
            this.m_TreeMapperMigration.m_sourceMapArea.repaint();
            this.m_TreeMapperMigration.m_newSourceTree.refresh();
            this.m_TreeMapperMigration.m_oldSourceTree.refresh();
         }
      }
   }

   MigrationTree(Migration var1, DataStructure var2) {
      super(var1, 77);
      this.structure = var2;
   }

   @Override
   protected DataStructure getRepresentedStructure() {
      return this.structure;
   }
}
