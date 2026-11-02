package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.toolbar.ToolBarEntry;
import org.etlforge.integration.client.ui.interchange.connect.LaunchConnectDialog;
import org.etlforge.integration.client.ui.interchange.core.action.NewEntityOptionButtonFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tree.TopLevelCategoryTreeNode;
import org.etlforge.ui.widget.button.StaticOptionButton;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public abstract class TopLevelCategoryGroup extends TopLevelEntityCategory {
   private final ImmutableList<TopLevelEntityCategory> categories;
   private final ImmutableList<EntityType> types;
   private boolean useSubMenus;
   private String menuItemName;

   public TopLevelCategoryGroup(String var1, List<TopLevelEntityCategory> var2, TopLevelEntityCategory.IconProvider var3) {
      super(var1, var3);
      this.categories = ImmutableList.copyOf(var2);
      this.types = this.collectTypes();
      this.menuItemName = var1;
   }

   private ImmutableList<EntityType> collectTypes() {
      Builder var1 = ImmutableList.builder();

      for (TopLevelEntityCategory var3 : this.categories) {
         var1.addAll(var3.getTypes());
      }

      return var1.build();
   }

   public final void setUseSubMenus(boolean var1) {
      this.useSubMenus = var1;
   }

   public void setMenuItemName(String var1) {
      this.menuItemName = var1;
   }

   @Override
   public final ImmutableList<EntityType> getTypes() {
      return this.types;
   }

   @Override
   public final Optional<? extends JPopupMenu> getRootMenu(EntityConfigLookup var1) {
      TopLevelCategoryGroup.DefaultAction var2 = this.createDefaultAction(var1);
      KongaPopupMenu var3 = new KongaPopupMenu();
      var3.add(var2);
      return Optional.of(var3);
   }

   @Override
   public final KongaTreeNode buildTree(IntegrationProject var1) {
      TopLevelCategoryTreeNode var2 = new TopLevelCategoryTreeNode(this.getName());
      Icon var3 = this.getIcon(EntityIcons.Size.SMALL, EntityIcons.Category.VALID);
      var2.setNodeIcon(var3);

      for (TopLevelEntityCategory var5 : this.categories) {
         KongaTreeNode var6 = var5.buildTree(var1);
         var2.addChild(var6);
      }

      return var2;
   }

   @Override
   public final JMenuItem getNewEntityMenuItem(EntityConfigLookup var1) {
      KongaMenu var2 = new KongaMenu(this.menuItemName);
      var2.setIcon(this.getIcon(EntityIcons.Size.SMALL, EntityIcons.Category.VALID));
      if (this.useSubMenus) {
         for (TopLevelEntityCategory var4 : this.categories) {
            if (this.useSubMenus) {
               JMenuItem var5 = var4.getNewEntityMenuItem(var1);
               var2.add(var5);
            }
         }
      } else {
         boolean var8 = false;

         for (TopLevelEntityCategory var10 : this.categories) {
            if (var8) {
               var2.addSeparator();
            }

            for (EntityType var7 : var10.getTypes()) {
               var2.add(var1.getEntityConfig(var7).newEntityAction());
            }

            var8 = true;
         }
      }

      return var2;
   }

   @Override
   public final List<? extends ToolBarEntry> getNewEntityToolBarButtons(EntityConfigLookup var1) {
      if (this.useSubMenus) {
         return Collections.emptyList();
      }

      List var2 = this.getTypesForDropdownButton();
      NewEntityOptionButtonFactory var3 = new NewEntityOptionButtonFactory(
         var1, this.getToolbarButtonIcon(), this.getToolbarButtonText(), var2, (EntityType)var2.get(0)
      );
      var3.setDefaultAction(this.createDefaultAction(var1));
      StaticOptionButton var4 = var3.getButton();
      var4.setKeepDefaultActionFixed(true);
      return ImmutableList.of(ToolBarEntry.of(var4.getMainActionButton()), ToolBarEntry.of(var4.getArrowButton()));
   }

   private TopLevelCategoryGroup.DefaultAction createDefaultAction(EntityConfigLookup var1) {
      return new TopLevelCategoryGroup.DefaultAction(
         this.getName() + "...",
         this.getIcon(EntityIcons.Size.SMALL, EntityIcons.Category.VALID),
         this.categories,
         var1,
         this.getName(),
         this.getIcon(EntityIcons.Size.LARGE, EntityIcons.Category.VALID)
      );
   }

   protected Icon getToolbarButtonIcon() {
      return this.getIcon(EntityIcons.Size.MEDIUM, EntityIcons.Category.VALID);
   }

   protected String getToolbarButtonText() {
      return null;
   }

   private List<EntityType> getTypesForDropdownButton() {
      ArrayList var1 = Lists.newArrayList();
      boolean var2 = false;

      for (TopLevelEntityCategory var4 : this.categories) {
         if (var2) {
            var1.add(null);
         }

         var1.addAll(var4.getTypes());
         var2 = true;
      }

      return var1;
   }

   private static class DefaultAction extends AbstractAction {
      private final ImmutableList<TopLevelEntityCategory> categories;
      private final EntityConfigLookup entityConfigs;
      private final String consoleTitle;
      private final Icon consoleIcon;

      public DefaultAction(String var1, Icon var2, ImmutableList<TopLevelEntityCategory> var3, EntityConfigLookup var4, String var5, Icon var6) {
         super(var1, var2);
         this.categories = var3;
         this.entityConfigs = var4;
         this.consoleTitle = var5;
         this.consoleIcon = var6;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DefaultAction.this.openDialog();
            }
         });
      }

      private void openDialog() {
         LaunchConnectDialog var1 = new LaunchConnectDialog(this.entityConfigs, this.categories, this.consoleTitle, this.consoleIcon);
         var1.open(ApplicationUi.getAppWinFrame());
      }
   }
}
