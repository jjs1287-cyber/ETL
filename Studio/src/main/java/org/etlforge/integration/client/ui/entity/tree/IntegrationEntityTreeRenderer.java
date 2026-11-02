package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import java.awt.Color;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.LocationDecoratedIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.integration.uidelegate.entity.tree.TopLevelCategoryTreeNode;
import org.etlforge.integration.uidelegate.project.ProjectNode;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.ActionNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public class IntegrationEntityTreeRenderer extends DefaultTreeCellRenderer implements TreeNodeDecor {
   protected static final Color BORDER_SELECTION_COLOR = new Color(75, 75, 75);
   private static Color UNSELECTED_BACKGROUND = new Color(255, 255, 255, 0);
   protected static final TextDecorator PROJECT_STYLE = TextStyles.TreeRootText.asTextDecorator();
   protected static final TextDecorator ROOT_FOLDER_STYLE = TextStyles.TreeRootFolderText.asTextDecorator();
   protected static final TextDecorator ENTITY_STYLE = TextStyles.TreeLeafText.asTextDecorator();
   protected static final TextDecorator ITALIC_ENTITY_STYLE = TextStyle.newStyle(TextStyles.TreeLeafText, 2).asTextDecorator();
   private JTree tree;
   private boolean selected;
   private boolean expanded;
   private boolean decorateInvalidItems;
   private boolean decorateDeployDirtyItems;
   private TextDecorator textDecorator;
   private TreeValidationService validationService;
   private ValidationStatus validationStatus;

   public IntegrationEntityTreeRenderer() {
      this.setBorderSelectionColor(BORDER_SELECTION_COLOR);
      this.setBackgroundNonSelectionColor(UNSELECTED_BACKGROUND);
   }

   public JComponent getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      this.tree = var1;
      this.selected = var3;
      this.expanded = var4;
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      this.setEnabled(var1.isEnabled());
      this.updateValidationStatus(var2);
      if (var2 instanceof IntegrationEntityTreeNode) {
         this.renderEntityNode((IntegrationEntityTreeNode)var2);
      } else if (var2 instanceof TreeNodeDecorator) {
         ((TreeNodeDecorator)var2).decorate(this);
      } else if (var2 instanceof ActionNode) {
         this.renderActionNode((ActionNode)var2);
      } else if (var2 instanceof KongaTreeNode) {
         KongaTreeNode var8 = (KongaTreeNode)var2;
         this.setTextStyle(this.getNonEntityNodeStyle(var8));
         Icon var9 = this.getNonEntityNodeIcon(var8);
         this.setIcon(var9);
         this.setToolTip(var8.getDisplayName());
      } else {
         this.setIcon(null);
         this.setToolTip(null);
      }

      return this;
   }

   private void updateValidationStatus(Object var1) {
      if (this.decorateInvalidItems && this.validationService != null) {
         if (var1 instanceof IntegrationEntityTreeNode) {
            this.validationStatus = this.validationService.getValidationStatus((IntegrationEntityTreeNode)var1);
         } else if (var1 instanceof TopLevelCategoryTreeNode) {
            Iterable var2 = ((TopLevelCategoryTreeNode)var1).collectRootFolderNodes();
            Iterable var3 = Iterables.transform(var2, new Function<IntegrationEntityTreeNode, ValidationStatus>() {
               public ValidationStatus apply(IntegrationEntityTreeNode var1) {
                  return IntegrationEntityTreeRenderer.this.validationService.getValidationStatus(var1);
               }
            });
            this.validationStatus = ValidationStatus.mostSevere(var3);
         } else {
            this.validationStatus = null;
         }
      } else {
         this.validationStatus = null;
      }
   }

   private void renderEntityNode(IntegrationEntityTreeNode var1) {
      this.setText(this.getDisplayString(var1));
      this.setToolTip(ToolTipStringFactory.getToolTip(var1.getUserObject()));
      this.setIcon(this.getIcon(var1));
      this.setTextStyle(this.getNodeStyle(var1));
   }

   private String getDisplayString(IntegrationEntityTreeNode var1) {
      String var2 = var1.getDisplayName();
      if (this.decorateDeployDirtyItems && this.isDeployDirty(var1)) {
         var2 = "> " + var2;
      }

      return var2;
   }

   private boolean isDeployDirty(IntegrationEntityTreeNode var1) {
      return var1.getUserObject().isDeployDirty() ? true : var1.hasMatchingChild(EntityFilters.DEPLOY_DIRTY);
   }

   private void renderActionNode(ActionNode var1) {
      Icon var2 = this.getNonEntityNodeIcon(var1);
      this.setIcon(var2);
      this.setToolTip(var1.getToolTip());
      if (var1.isRenderItalic()) {
         this.setTextStyle(ITALIC_ENTITY_STYLE);
      } else {
         this.setTextStyle(ENTITY_STYLE);
      }
   }

   @Override
   public final JTree getTree() {
      return this.tree;
   }

   @Override
   public final void setTextStyle(TextDecorator var1) {
      this.textDecorator = var1;
      if (!this.selected) {
         if (this.decorateInvalidItems && this.validationStatus == ValidationStatus.INVALID) {
            this.setForeground(Colors.ERROR_TEXT_COLOR);
         } else if (var1 != null) {
            this.setForeground(var1.getTextColor());
         }
      }
   }

   public final void setValidationService(TreeValidationService var1) {
      this.validationService = var1;
   }

   public final void setDecorateInvalidNodes(boolean var1) {
      this.decorateInvalidItems = var1;
      this.validationStatus = null;
   }

   public final void setDecorateDeployDirtyNodes(boolean var1) {
      this.decorateDeployDirtyItems = var1;
   }

   @Override
   public final boolean isSelected() {
      return this.selected;
   }

   @Override
   public final boolean isExpanded() {
      return this.expanded;
   }

   @Override
   public final void setEnabled(boolean var1) {
      boolean var2 = this.tree != null ? this.tree.isEnabled() && var1 : var1;
      super.setEnabled(var2);
   }

   protected Icon getIcon(IntegrationEntityTreeNode var1) {
      IntegrationEntity var2 = var1.getUserObject();
      if (var2 instanceof Folder && ((Folder)var2).isEtlforgeConnectFolder()) {
         return ClientIcons.ETLFORGE_CONNECT_16;
      }

      if (this.decorateInvalidItems && this.validationStatus != null && this.validationStatus != ValidationStatus.VALID && useIcon(var1, this.validationStatus)
         )
       {
         return var1.getIcons().getIcon(EntityIcons.Category.fromValidationStatus(this.validationStatus), EntityIcons.Size.SMALL);
      }

      if (this.isRelegatedRoot(var1)) {
         EntityType var3 = ((RootFolder)var2).getItemType();
         if (var3.isWizardType()) {
            return EntityIcons.getWizardFolderIcon(var3);
         }
      }

      if (var2 instanceof Locatable) {
         Locatable var5 = (Locatable)var2;
         LocationDecoratedIcons var4 = LocationDecoratedIcons.forLocatable(var5);
         return var4.getIcon(var5, EntityIcons.Size.SMALL);
      } else {
         return var1.getDefaultIcon();
      }
   }

   private boolean isRelegatedRoot(IntegrationEntityTreeNode var1) {
      return var1.getUserObject() instanceof RootFolder && var1.getParent() instanceof TopLevelCategoryTreeNode;
   }

   private static boolean useIcon(IntegrationEntityTreeNode var0, ValidationStatus var1) {
      return var0.getUserObject() instanceof RootFolder ? var1 != ValidationStatus.VALID : true;
   }

   protected TextDecorator getNodeStyle(IntegrationEntityTreeNode var1) {
      if (var1 instanceof ProjectNode) {
         return PROJECT_STYLE;
      } else {
         return this.hasRootFolderStyle(var1) ? ROOT_FOLDER_STYLE : ENTITY_STYLE;
      }
   }

   private boolean hasRootFolderStyle(IntegrationEntityTreeNode var1) {
      return var1.getUserObject() instanceof RootFolder && !(var1.getParent() instanceof TopLevelCategoryTreeNode);
   }

   protected TextDecorator getNonEntityNodeStyle(KongaTreeNode var1) {
      return var1 instanceof TopLevelCategoryTreeNode ? ROOT_FOLDER_STYLE : ENTITY_STYLE;
   }

   protected Icon getNonEntityNodeIcon(KongaTreeNode var1) {
      return var1.getNodeIcon(NodeStatus.CLOSED);
   }

   @Override
   public final Color getTextNonSelectionColor() {
      return this.textDecorator == null ? super.getTextNonSelectionColor() : this.textDecorator.getTextColor();
   }

   @Override
   public final Font getFont() {
      return this.textDecorator == null ? super.getFont() : this.textDecorator.getFont();
   }

   @Override
   public void setToolTip(String var1) {
      this.setToolTipText(var1);
   }
}
