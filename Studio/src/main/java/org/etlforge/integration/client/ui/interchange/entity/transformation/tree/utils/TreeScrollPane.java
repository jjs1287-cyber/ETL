package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import javax.swing.JScrollPane;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration.Migration;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public class TreeScrollPane extends JScrollPane {
   final Migration m_MapperMigration;
   public final TreeComponent m_tree;
   String m_dtdFile;
   boolean m_isInput;

   public TreeScrollPane(Migration var1, TreeComponent var2, MappingTreeStructure var3, String var4) {
      super(20, 30);
      this.m_MapperMigration = var1;
      this.m_tree = var2;
      this.setViewportView(this.m_tree);
      this.m_tree.m_scroll = this;
      this.m_tree.readMappingStructure(var3, var4);
      this.m_tree.init();
   }

   public void adjustComponentSize() {
      byte var1 = 0;
      byte var2 = 60;
      int var3 = this.getParent().getSize().width;
      int var4 = this.getParent().getSize().height - var2;
      var4 -= this.m_MapperMigration.m_scroll.getVerticalScrollBar().getValue();
      if (this.m_tree.m_title.equals("new SOURCE")) {
         this.setBounds(var1, var2, var3, var4);
      }

      this.doLayout();
   }

   public TreeComponent getTreeComponent() {
      return this.m_tree;
   }

   public void refresh() {
      this.m_tree.refresh();
      this.doLayout();
   }
}
