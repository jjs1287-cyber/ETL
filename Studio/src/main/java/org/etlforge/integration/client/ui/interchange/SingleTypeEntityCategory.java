package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.toolbar.ToolBarEntry;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class SingleTypeEntityCategory extends TopLevelEntityCategory {
   private final EntityType type;

   public SingleTypeEntityCategory(EntityType var1) {
      super(var1.getPlural(), new EntityTypeIconsProvider(var1));
      this.type = var1;
   }

   @Override
   public ImmutableList<EntityType> getTypes() {
      return ImmutableList.of(this.type);
   }

   @Override
   public KongaTreeNode buildTree(IntegrationProject var1) {
      return this.createRootFolderNode(var1, this.type);
   }

   @Override
   public Optional<? extends JPopupMenu> getRootMenu(EntityConfigLookup var1) {
      return Optional.absent();
   }

   @Override
   public JMenuItem getNewEntityMenuItem(EntityConfigLookup var1) {
      EntityConfig var2 = var1.getEntityConfig(this.type);
      ApplicationAction var3 = var2.newEntityAction();
      return new KongaMenuItem(var3);
   }

   @Override
   public List<? extends ToolBarEntry> getNewEntityToolBarButtons(EntityConfigLookup var1) {
      EntityConfig var2 = var1.getEntityConfig(this.type);
      ApplicationAction var3 = var2.newEntityAction();
      KongaButton var4 = KongaButton.forToolbar(var3).withMediumActionIcon();
      return ImmutableList.of(ToolBarEntry.of(var4));
   }
}
