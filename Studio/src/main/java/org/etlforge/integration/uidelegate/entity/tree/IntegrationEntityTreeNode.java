package org.etlforge.integration.uidelegate.entity.tree;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import javax.swing.Icon;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public class IntegrationEntityTreeNode extends DefaultKongaTreeNode {
   private static final Comparator<KongaTreeNode> NODE_SORTER = new ProjectTreeNodeOrder();
   private int sortOrdinal;
   private String displayName;

   public IntegrationEntityTreeNode(KongaTreeNode var1, IntegrationEntity var2) {
      this(var1, var2, canHaveChildren(var2));
   }

   public IntegrationEntityTreeNode(KongaTreeNode var1, IntegrationEntity var2, boolean var3) {
      super(var1, var2, var3);
      Preconditions.checkNotNull(var2, "object");
   }

   public IntegrationEntityTreeNode(IntegrationEntity var1, boolean var2) {
      this(null, var1, var2);
   }

   public IntegrationEntityTreeNode(IntegrationEntity var1) {
      this(null, var1, canHaveChildren(var1));
   }

   private static boolean canHaveChildren(IntegrationEntity var0) {
      return var0 instanceof Folder || var0 instanceof IntegrationProject;
   }

   public IntegrationEntity getUserObject() {
      return (IntegrationEntity)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return this.displayName != null ? this.displayName : this.getUserObject().getName();
   }

   @Override
   public String toString() {
      return this.getDisplayName();
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public final void setSortOrdinal(int var1) {
      this.sortOrdinal = var1;
   }

   protected final int getSortOrdinal() {
      return this.sortOrdinal;
   }

   @Override
   public Icon getNodeIcon(NodeStatus var1) {
      return this.getDefaultIcon();
   }

   public final Icon getDefaultIcon() {
      return IconStore.defaultIcon(this.getUserObject());
   }

   public final EntityIcons getIcons() {
      return EntityIcons.forType(this.getUserObject().getEntityType());
   }

   @Override
   public final boolean isFolder() {
      return this.getUserObject() instanceof Folder;
   }

   public final boolean isRenamable() {
      return this.getUserObject().isRenameable();
   }

   @Override
   protected Comparator<KongaTreeNode> getNodeComparator() {
      return NODE_SORTER;
   }

   public boolean hasMatchingChild(EntityFilter var1) {
      for (KongaTreeNode var3 : this.children()) {
         if (var3 instanceof IntegrationEntityTreeNode) {
            IntegrationEntityTreeNode var4 = (IntegrationEntityTreeNode)var3;
            if (var1.apply(var4.getUserObject())) {
               return true;
            }

            if (var4.hasMatchingChild(var1)) {
               return true;
            }
         }
      }

      return false;
   }
}
