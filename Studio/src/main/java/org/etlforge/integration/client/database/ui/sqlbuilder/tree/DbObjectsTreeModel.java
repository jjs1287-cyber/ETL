package org.etlforge.integration.client.database.ui.sqlbuilder.tree;

import java.util.Arrays;
import java.util.List;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.integration.client.database.ui.sqlbuilder.DbObjectsViewFilter;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

class DbObjectsTreeModel extends DefaultTreeTableModel {
   private DbObjectsViewFilter filter;

   public DbObjectsTreeModel(TreeTableNode var1) {
      super(var1, getColumnNames());
   }

   private static List<String> getColumnNames() {
      return Arrays.asList("Name", "Type", "Primary Key");
   }

   public void setFilter(DbObjectsViewFilter var1) {
      if (var1 != this.filter) {
         this.filter = var1;
         this.modelSupport.fireTreeStructureChanged(new TreePath(new Object[]{this.getRoot()}));
      }
   }

   @Override
   public Object getChild(Object var1, int var2) {
      if (this.filter == null) {
         return super.getChild(var1, var2);
      } else if (var1 == this.getRoot()) {
         return this.getTableNode(this.getRoot(), var2);
      } else {
         return var1 instanceof DatabaseTableNode ? this.getColumnNode((DatabaseTableNode)var1, var2) : null;
      }
   }

   private Object getTableNode(TreeTableNode var1, int var2) {
      int var3 = -1;

      for (TreeTableNode var5 : IterableEnumeration.of(var1.children())) {
         if (this.isTableNodeAccepted(var5)) {
            if (++var3 == var2) {
               return var5;
            }
         }
      }

      return null;
   }

   private boolean isTableNodeAccepted(TreeTableNode var1) {
      if (var1 instanceof DatabaseTableNode) {
         DatabaseObject var2 = (DatabaseObject)var1.getUserObject();
         return this.filter.acceptsTable(var2);
      } else {
         return true;
      }
   }

   private Object getColumnNode(DatabaseTableNode var1, int var2) {
      int var3 = -1;

      for (TreeTableNode var5 : IterableEnumeration.of(var1.children())) {
         if (this.isColumnNodeAccepted(var5)) {
            if (++var3 == var2) {
               return var5;
            }
         }
      }

      return null;
   }

   private boolean isColumnNodeAccepted(TreeTableNode var1) {
      if (var1 instanceof ColumnNode) {
         DatabaseColumn var2 = (DatabaseColumn)var1.getUserObject();
         return this.filter.acceptsColumn(var2);
      } else {
         return true;
      }
   }

   @Override
   public int getChildCount(Object var1) {
      if (this.filter == null) {
         return super.getChildCount(var1);
      } else if (var1 == this.getRoot()) {
         return this.countAcceptedTables();
      } else {
         return var1 instanceof DatabaseTableNode ? this.countAcceptedColumns((DatabaseTableNode)var1) : 0;
      }
   }

   private int countAcceptedTables() {
      int var1 = 0;
      TreeTableNode var2 = this.getRoot();

      for (TreeTableNode var4 : IterableEnumeration.of(var2.children())) {
         if (this.isTableNodeAccepted(var4)) {
            var1++;
         }
      }

      return var1;
   }

   private int countAcceptedColumns(DatabaseTableNode var1) {
      int var2 = 0;

      for (TreeTableNode var4 : IterableEnumeration.of(var1.children())) {
         if (this.isColumnNodeAccepted(var4)) {
            var2++;
         }
      }

      return var2;
   }

   @Override
   public int getIndexOfChild(Object var1, Object var2) {
      if (this.filter == null) {
         return super.getIndexOfChild(var1, var2);
      } else if (var1 == this.getRoot()) {
         return this.getIndexOfTableNode(this.getRoot(), var2);
      } else {
         return var1 instanceof DatabaseTableNode ? this.getIndexOfColumnNode((DatabaseTableNode)var1, var2) : -1;
      }
   }

   private int getIndexOfTableNode(TreeTableNode var1, Object var2) {
      int var3 = 0;

      for (TreeTableNode var5 : IterableEnumeration.of(var1.children())) {
         if (this.isTableNodeAccepted(var5)) {
            if (var5 == var2) {
               return var3;
            }

            var3++;
         }
      }

      return -1;
   }

   private int getIndexOfColumnNode(DatabaseTableNode var1, Object var2) {
      int var3 = 0;

      for (TreeTableNode var5 : IterableEnumeration.of(var1.children())) {
         if (this.isColumnNodeAccepted(var5)) {
            if (var5 == var2) {
               return var3;
            }

            var3++;
         }
      }

      return -1;
   }

   public boolean isPartOfModel(TreeTableNode var1) {
      for (TreeTableNode var2 = var1; var2 != null; var2 = var2.getParent()) {
         if (var2 == this.getRoot()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void removeNodeFromParent(MutableTreeTableNode var1) {
      if (this.filter == null) {
         super.removeNodeFromParent(var1);
      } else {
         MutableTreeTableNode var2 = (MutableTreeTableNode)var1.getParent();
         if (var2 == null) {
            throw new IllegalArgumentException("node does not have a parent.");
         }

         int var3 = this.getIndexOfChild(var2, var1);
         if (var3 == -1) {
            return;
         }

         var1.removeFromParent();
         this.modelSupport.fireChildRemoved(new TreePath(this.getPathToRoot(var2)), var3, var1);
      }
   }
}
