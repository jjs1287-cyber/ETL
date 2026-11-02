package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public final class AutoMapperAction extends AbstractAction {
   private final Supplier<TreeMapper> mappingTreeSupplier;

   public AutoMapperAction(final TransformationPage var1) {
      this(new Supplier<TreeMapper>() {
         public TreeMapper get() {
            return var1.getTreeMapper();
         }
      });
   }

   public AutoMapperAction(TreeMapper var1) {
      this(Suppliers.ofInstance(var1));
   }

   private AutoMapperAction(Supplier<TreeMapper> var1) {
      super(null, ClientIcons.AUTO_MAPPER_16);
      this.putValue("KongaMediumIconKey", ClientIcons.AUTO_MAPPER_24);
      this.putValue("ShortDescription", "Auto Map");
      this.mappingTreeSupplier = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.doAutoMap();
   }

   private void doAutoMap() {
      TreeMapper var1 = this.mappingTreeSupplier.get();
      if (var1 != null) {
         TreeMapperSourceTree var2 = var1.m_sourceTreeComponent;
         TreeMapperTargetTree var3 = var1.m_targetTreeComponent;
         Node var4 = this.getSelectedNode(var2);
         Node var5 = this.getSelectedNode(var3);
         if (var4 == null || var5 == null) {
            var4 = this.getRootTable(var2);
            var5 = this.getRootTable(var3);
         }

         if (var4 != null && var5 != null) {
            var1.doStraightMappings(var4, var5);
         }
      }
   }

   private Node getSelectedNode(TreeComponent var1) {
      Node var2 = var1.getHighlightedNode();
      return var2 != null && !var2.isFolder() ? null : var2;
   }

   private Node getRootTable(TreeComponent var1) {
      Node var2 = var1.m_root;
      return var2.getChildCount() == 1 ? var2.getChildAt(0) : null;
   }
}
