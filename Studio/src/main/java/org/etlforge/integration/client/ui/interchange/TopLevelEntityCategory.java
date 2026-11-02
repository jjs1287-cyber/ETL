package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.ProjectTreeFragmentCreator;
import org.etlforge.integration.client.ui.interchange.component.menu.NewEntityMenuEntry;
import org.etlforge.integration.client.ui.interchange.component.toolbar.NewEntityToolBarEntry;
import org.etlforge.integration.client.ui.interchange.component.tree.ProjectItemTransformDrop;
import org.etlforge.integration.client.ui.interchange.component.tree.TreeDropStrategy;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public abstract class TopLevelEntityCategory implements ContentViewerCategory, NewEntityMenuEntry, NewEntityToolBarEntry {
   private final String name;
   private final TopLevelEntityCategory.IconProvider iconProvider;
   public static final TopLevelEntityCategory.IconProvider EMPTY_ICONS = new TopLevelEntityCategory.IconProvider() {
      @Override
      public Icon getIcon(EntityIcons.Size var1, EntityIcons.Category var2) {
         return var1.emptyIcon();
      }
   };
   public static final TopLevelEntityCategory.IconProvider NO_ICONS = new TopLevelEntityCategory.IconProvider() {
      @Override
      public Icon getIcon(EntityIcons.Size var1, EntityIcons.Category var2) {
         return null;
      }
   };

   public TopLevelEntityCategory(String var1, TopLevelEntityCategory.IconProvider var2) {
      this.name = Preconditions.checkNotNull(var1);
      this.iconProvider = Preconditions.checkNotNull(var2);
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public final Icon getIcon(EntityIcons.Size var1, EntityIcons.Category var2) {
      return this.iconProvider.getIcon(var1, var2);
   }

   public abstract ImmutableList<EntityType> getTypes();

   protected final KongaTreeNode createRootFolderNode(IntegrationProject var1, EntityType var2) {
      IntegrationEntityTreeModel var3 = ProjectTreeFragmentCreator.createTreeModel(var1, var2);
      return var3.getRoot();
   }

   @Override
   public final List<TreeDropStrategy> getTreeDropStrategies(EntityConfigLookup var1) {
      ArrayList var2 = Lists.newArrayList();

      for (EntityType var4 : this.getTypes()) {
         EntityConfig var5 = var1.getEntityConfig(var4);

         for (ProjectItemTransform var7 : var5.getTransformGenerators()) {
            var2.add(new ProjectItemTransformDrop(var7));
         }
      }

      return var2;
   }

   @Override
   public String toString() {
      return this.getName();
   }

   public final TopLevelEntityCategory.ButtonGrid toButtonGrid(EntityConfigLookup var1) {
      return new TopLevelEntityCategory.ButtonGrid(this, var1);
   }

   public static final class ButtonGrid implements UiProvider {
      private final String name;
      private final Icon icon;
      private final JLabel titleLabel;
      private final ImmutableList<KongaButton> buttons;

      public ButtonGrid(TopLevelEntityCategory var1, final EntityConfigLookup var2) {
         this.name = var1.getName();
         this.icon = var1.getIcon(EntityIcons.Size.LARGE, EntityIcons.Category.VALID);
         this.titleLabel = getTitleLabel(this.name);
         this.buttons = FluentIterable.from(var1.getTypes()).transform(new Function<EntityType, KongaButton>() {
            public KongaButton apply(EntityType var1) {
               ApplicationAction var2x = var2.getEntityConfig(var1).newEntityAction();
               KongaButton var3 = new KongaButton(var2x).withMediumActionIcon();
               var3.setHorizontalAlignment(10);
               String var4 = (String)var2x.getValue("Name");
               if (var4.startsWith("New ")) {
                  var4 = var4.substring(4);
                  var3.setText(var4);
               }

               return var3;
            }
         }).toImmutableList();
      }

      private static JLabel getTitleLabel(String var0) {
         return WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeLabel(var0).withBorder(Empty.border(0, 16, 4, 0));
      }

      public String getName() {
         return this.name;
      }

      public Icon getIcon() {
         return this.icon;
      }

      @Override
      public void decorate(UiCanvas var1) {
         OneColumnPanel var2 = new OneColumnPanel()
            .withInsets(0, 0, 10, 0)
            .withBorder(new TitledBorder(""))
            .setWeightX(1.0)
            .setFill(GridBagLayoutPanel.Fill.BOTH)
            .addAll(this.buttons);
         Vertical.borderLayout().of(this.titleLabel, Vertical.Anchor.north(var2)).withBackground(Colors.NEUTRAL_BACKGROUND).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.buttons.get(0).requestFocusInWindow();
      }

      public void addActionListener(ActionListener var1) {
         ButtonUtils.addActionListener(var1, this.buttons);
      }
   }

   public interface IconProvider {
      Icon getIcon(EntityIcons.Size var1, EntityIcons.Category var2);
   }
}
