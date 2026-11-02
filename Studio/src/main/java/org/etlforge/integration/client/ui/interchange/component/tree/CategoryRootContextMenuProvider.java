package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Function;
import javax.annotation.Nullable;
import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.uidelegate.project.ProjectNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class CategoryRootContextMenuProvider implements Function<KongaTreeNode, JPopupMenu> {
   private final ContentViewerCategory category;
   private final EntityConfigLookup configLookup;

   public CategoryRootContextMenuProvider(ContentViewerCategory var1, EntityConfigLookup var2) {
      this.category = var1;
      this.configLookup = var2;
   }

   @Nullable
   public JPopupMenu apply(KongaTreeNode var1) {
      return this.isRecognizedNode(var1) ? this.category.getRootMenu(this.configLookup).orNull() : null;
   }

   private boolean isRecognizedNode(KongaTreeNode var1) {
      return var1.getParent() instanceof ProjectNode && var1.getDisplayName().equals(this.category.getName());
   }
}
