package org.etlforge.integration.client.database.ui.primarykeys;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import javax.swing.tree.TreePath;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class PrimaryKeyTreeDefinerModel extends DefaultKongaTreeModel {
   private final Map<DatabaseObject, TableNode> tableNodes = Maps.newHashMap();

   private PrimaryKeyTreeDefinerModel() {
      super(new DefaultKongaTreeNode("Tables/Views", true));
   }

   private PrimaryKeyTreeDefinerModel(PrimaryKeyTreeDefinerModel var1) {
      super(var1.getRoot());

      for (KongaTreeNode var3 : this.getRoot().children()) {
         TableNode var4 = (TableNode)var3;
         this.tableNodes.put(var4.getUserObject(), var4);
      }
   }

   private PrimaryKeyTreeDefinerModel(DatabaseObject[] var1) {
      this();
      this.createNodes(var1);
   }

   private PrimaryKeyTreeDefinerModel(PrimaryKeys[] var1) {
      this();
      this.createNodes(var1);
   }

   public static PrimaryKeyTreeDefinerModel newModel(DatabaseObject[] var0) {
      return new PrimaryKeyTreeDefinerModel(var0);
   }

   public static PrimaryKeyTreeDefinerModel newModel(PrimaryKeys[] var0) {
      return new PrimaryKeyTreeDefinerModel(var0);
   }

   public static PrimaryKeyTreeDefinerModel copyModel(PrimaryKeyTreeDefinerModel var0) {
      return new PrimaryKeyTreeDefinerModel(var0);
   }

   public void update(PrimaryKeys[] var1) {
      for (PrimaryKeys var5 : var1) {
         this.update(var5);
      }

      this.fireTreeStructureChanged(this, new TreePath(this.getRoot()));
   }

   private void update(PrimaryKeys var1) {
      DatabaseObject var2 = var1.getDatabaseObject();
      TableNode var3 = this.tableNodes.get(var2);
      if (var3 == null) {
         var3 = new TableNode(this.getRoot(), var1);
         this.tableNodes.put(var2, var3);
      } else {
         var3.setSelectedKeys(Arrays.asList(var1.getPrimaryKeys()));
      }
   }

   private void createNodes(DatabaseObject[] var1) {
      KongaTreeNode var2 = this.getRoot();

      for (DatabaseObject var6 : var1) {
         TableNode var7 = new TableNode(var2, var6);
         this.tableNodes.put(var6, var7);
      }
   }

   private void createNodes(PrimaryKeys[] var1) {
      KongaTreeNode var2 = this.getRoot();

      for (PrimaryKeys var6 : var1) {
         TableNode var7 = new TableNode(var2, var6);
         this.tableNodes.put(var6.getDatabaseObject(), var7);
      }
   }
}
