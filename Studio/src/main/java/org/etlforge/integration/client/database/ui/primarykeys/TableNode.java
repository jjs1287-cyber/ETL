package org.etlforge.integration.client.database.ui.primarykeys;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.Icon;
import org.etlforge.integration.client.database.ui.TableIcon;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

final class TableNode extends DefaultKongaTreeNode {
   private static final Icon ICON = new TableIcon();
   private Set<DatabaseColumn> keys;

   public TableNode(KongaTreeNode var1, DatabaseObject var2) {
      super(var1, var2, true);
      this.setSelectedKeys(var2.getPrimaryKeys());
   }

   public TableNode(KongaTreeNode var1, PrimaryKeys var2) {
      super(var1, var2.getDatabaseObject(), true);
      this.setSelectedKeys(Arrays.asList(var2.getPrimaryKeys()));
   }

   public void removeKey(KeyNode var1) {
      assert var1.getParent() == this;
      var1.removeFromParent();
      this.keys.remove(var1.getUserObject());
   }

   public void setSelectedKeys(Collection<DatabaseColumn> var1) {
      if (var1 == null) {
         this.keys = new LinkedHashSet<>();
      } else {
         this.keys = new LinkedHashSet<>(var1);
      }

      this.createChildNodes();
   }

   public Set<DatabaseColumn> getSelectedKeys() {
      return new LinkedHashSet<>(this.keys);
   }

   public DatabaseColumn[] getSelectedKeysAsArray() {
      return this.keys.toArray(new DatabaseColumn[this.keys.size()]);
   }

   public boolean hasKeys() {
      return !this.keys.isEmpty();
   }

   private void createChildNodes() {
      this.removeAllChildren();
      if (this.keys.isEmpty()) {
         new NoKeySelectedNode(this);
      } else {
         for (DatabaseColumn var2 : this.keys) {
            new KeyNode(this, var2);
         }
      }
   }

   @Override
   public String getDisplayName() {
      return this.getUserObject().getName();
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return ICON;
   }

   public DatabaseObject getUserObject() {
      return (DatabaseObject)super.getUserObject();
   }
}
