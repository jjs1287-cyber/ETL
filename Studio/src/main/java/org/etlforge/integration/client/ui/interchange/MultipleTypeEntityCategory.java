package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.application.ui.window.toolbar.ToolBarEntry;
import org.etlforge.integration.client.ui.interchange.core.action.NewEntityOptionButtonFactory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tree.TopLevelCategoryTreeNode;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.StaticOptionButton;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class MultipleTypeEntityCategory extends TopLevelEntityCategory {
   private final ImmutableList<EntityType> types;
   private String menuItemName;
   private Action defaultToolBarAction;
   private EntityType defaultType;

   public MultipleTypeEntityCategory(String var1, Iterable<EntityType> var2) {
      this(var1, var2, NO_ICONS);
   }

   public MultipleTypeEntityCategory(String var1, Iterable<EntityType> var2, TopLevelEntityCategory.IconProvider var3) {
      super(var1, var3);
      this.types = ImmutableList.copyOf(var2);
      ArgCheck.checkNotEmpty(this.types, "types");
      this.menuItemName = var1;
      this.defaultType = this.getTypes().get(0);
   }

   @Override
   public ImmutableList<EntityType> getTypes() {
      return this.types;
   }

   @Override
   public KongaTreeNode buildTree(IntegrationProject var1) {
      TopLevelCategoryTreeNode var2 = this.createRootNode();

      for (EntityType var4 : this.getTypes()) {
         KongaTreeNode var5 = this.createRootFolderNode(var1, var4);
         var2.addChild(var5);
      }

      return var2;
   }

   private TopLevelCategoryTreeNode createRootNode() {
      TopLevelCategoryTreeNode var1 = new TopLevelCategoryTreeNode(this.getName());
      Icon var2 = this.getIcon(EntityIcons.Size.SMALL, EntityIcons.Category.VALID);
      if (var2 != null) {
         var1.setNodeIcon(var2);
      }

      return var1;
   }

   @Override
   public Optional<? extends JPopupMenu> getRootMenu(EntityConfigLookup var1) {
      List var2 = this.buildCreateNewItemsAction(var1);
      KongaPopupMenu var3 = KongaPopupMenu.fromItems(var2);
      return Optional.of(var3);
   }

   private List<Action> buildCreateNewItemsAction(final EntityConfigLookup var1) {
      return FluentIterable.from(this.getTypes()).transform(new Function<EntityType, Action>() {
         public Action apply(EntityType var1x) {
            return MultipleTypeEntityCategory.this.newEntityAction(var1, var1x);
         }
      }).toImmutableList();
   }

   @Override
   public JMenuItem getNewEntityMenuItem(EntityConfigLookup var1) {
      Icon var2 = this.getIcon(EntityIcons.Size.SMALL, EntityIcons.Category.VALID);
      KongaMenu var3 = new KongaMenu(this.menuItemName, var2);

      for (EntityType var5 : this.getTypes()) {
         ApplicationAction var6 = this.newEntityAction(var1, var5);
         var3.add(new KongaMenuItem(var6));
      }

      return var3;
   }

   private ApplicationAction newEntityAction(EntityConfigLookup var1, EntityType var2) {
      EntityConfig var3 = var1.getEntityConfig(var2);
      return var3.newEntityAction();
   }

   public void setMenuItemName(String var1) {
      this.menuItemName = ArgCheck.checkNotEmpty(var1, "name");
   }

   public void setDefaultToolBarAction(Action var1) {
      this.defaultToolBarAction = Preconditions.checkNotNull(var1, "action");
   }

   public void setDefaultType(EntityType var1) {
      Preconditions.checkArgument(var1 != null && this.getTypes().contains(var1));
      this.defaultType = var1;
   }

   @Override
   public List<? extends ToolBarEntry> getNewEntityToolBarButtons(EntityConfigLookup var1) {
      String var2 = null;
      Icon var3 = this.getIcon(EntityIcons.Size.MEDIUM, EntityIcons.Category.VALID);
      if (var3 == null) {
         var2 = this.getName();
      }

      NewEntityOptionButtonFactory var4 = new NewEntityOptionButtonFactory(var1, var3, var2, this.getTypes(), this.defaultType);
      if (this.defaultToolBarAction != null) {
         var4.setDefaultAction(this.defaultToolBarAction);
      } else {
         var4.setDefaultAction(new MultipleTypeEntityCategory.DefaultToolBarAction(this, var1));
      }

      StaticOptionButton var5 = var4.getButton();
      var5.setKeepDefaultActionFixed(true);
      return ImmutableList.of(ToolBarEntry.of(var5.getMainActionButton()), ToolBarEntry.of(var5.getArrowButton()));
   }

   private static class DefaultToolBarAction extends AbstractAction {
      private final MultipleTypeEntityCategory category;
      private final EntityConfigLookup entityConfigs;
      private KongaDialog dialog;
      private final ActionListener dialogCloser = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (DefaultToolBarAction.this.dialog != null) {
               DefaultToolBarAction.this.dialog.dispose();
            }
         }
      };

      public DefaultToolBarAction(MultipleTypeEntityCategory var1, EntityConfigLookup var2) {
         this.category = var1;
         this.entityConfigs = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DefaultToolBarAction.this.openDialog();
            }
         });
      }

      private void openDialog() {
         UiProvider var1 = this.layoutButtons();
         JPanel var2 = BorderLayoutBuilder.fillWith(var1);
         var2.setOpaque(true);
         var2.setBorder(Empty.border(10));
         UiUtils.setPanelBackgrounds(var2, Colors.NEUTRAL_BACKGROUND);
         this.dialog = new WizardStyleDialog(ApplicationUi.getAppWinFrame(), "Create New...", var2, KongaDialog.CLOSE_ONLY, true);
         this.dialog.standardLayout(var1, KongaDialog.CLOSE_ONLY);
         this.dialog.setLocationRelativeTo(null);
         this.dialog.setVisible(true);
         this.dialog = null;
      }

      private UiProvider layoutButtons() {
         TopLevelEntityCategory.ButtonGrid var1 = this.category.toButtonGrid(this.entityConfigs);
         var1.addActionListener(this.dialogCloser);
         return var1;
      }
   }
}
