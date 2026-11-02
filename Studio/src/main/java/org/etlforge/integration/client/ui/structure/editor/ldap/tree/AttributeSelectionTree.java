package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.search.string.StringSearchModel;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.application.ui.widget.tree.TreeStringFilterDefiner;
import org.etlforge.integration.client.ldap.ui.AttributeInfoViewer;
import org.etlforge.integration.client.ldap.ui.ReadOnlyAttributesDisabledCaption;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.GraphicsState;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.dialog.ComponentDialogTrigger;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.TreeNodeDecorRenderer;
import org.etlforge.ui.widget.PostRenderingComponentPainter;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.ui.widget.tree.check.CheckBoxTree;

public final class AttributeSelectionTree implements UiProvider {
   private AttributeSelectionModel model;
   private final CheckBoxTree tree;
   private final TreeStringFilterDefiner filterUi;
   private final String title;
   private final Action selectRequired;
   private final Action selectAll;
   private final Action unselectAll;
   private Action refreshAction;
   private final SetAuxiliaryClassesAction setAuxClassesAction;
   private final SelectionAction setExtendedAttributesAction;
   private final SelectionAction viewPropertiesAction;

   public AttributeSelectionTree(AttributeSelectionModel var1, String var2) {
      Preconditions.checkNotNull(var1, "model");
      this.model = var1;
      this.title = var2;
      this.selectRequired = new AttributeSelectionTree.SelectRequiredAction(null);
      this.tree = this.createTree(var1);
      this.filterUi = new AttributeSelectionTree.FilterUi(this.tree);
      this.selectAll = this.tree.getCheckAllAction(null);
      this.unselectAll = this.tree.getUncheckAllAction(null);
      this.setAuxClassesAction = new SetAuxiliaryClassesAction(this);
      this.setExtendedAttributesAction = new SetExtendedAttributesAction(this);
      this.viewPropertiesAction = new AttributeSelectionTree.ViewPropertiesAction();
      this.registerSelectionActions();
   }

   public void setLdapCache(LdapStructureServerCache var1) {
      this.setAuxClassesAction.setLdapCache(var1);
   }

   public void applyTo(LdapStructure var1) {
      LdapStructureEntry[] var2 = this.model.getDefinedEntries();
      var1.setClassEntries(var2);
   }

   public void ensureExtendedAttributesAreSelected(NumericOid var1) {
      KongaTreeNode[] var2 = this.model.getExtendedAttributeNodes(var1);
      this.tree.addCheckedNodes(var2);
   }

   public void setRefreshAction(Action var1) {
      this.refreshAction = var1;
      ActionKeyBinding.install(var1, this.tree.getAdaptee());
   }

   public boolean hasSelectedAttributes() {
      return this.tree.hasCheckedNodes();
   }

   private Action[] getBulkChangeActions() {
      return new Action[]{this.selectRequired, this.selectAll, this.unselectAll, this.setAuxClassesAction, this.setExtendedAttributesAction};
   }

   @Override
   public void decorate(UiCanvas var1) {
      new AttributeSelectionTree.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
      this.selectFirstClassNode();
   }

   private void selectFirstClassNode() {
      KongaTreeNode var1 = this.model.getRoot();
      if (var1.getChildCount() != 0) {
         KongaTreeNode var2 = var1.getChildAt(0);
         this.tree.getAdaptee().makeNodeSelected(var2, false);
      }
   }

   public void setModel(final AttributeSelectionModel var1) {
      if (var1 == null) {
         throw new NullPointerException("model");
      }

      this.model = var1;
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            AttributeSelectionTree.this.tree.setModel(var1);
            AttributeSelectionTree.this.tree.getAdaptee().expandAll();
            AttributeSelectionTree.this.requestFocus();
         }
      });
   }

   AttributeSelectionModel getModel() {
      return this.model;
   }

   private CheckBoxTree createTree(AttributeSelectionModel var1) {
      AttributeSelectionTree.TreeImpl var2 = new AttributeSelectionTree.TreeImpl(var1);
      var2.setPainter(new AttributeSelectionTree.TreeDecorator());
      var2.setRootVisible(false);
      var2.setShowsRootHandles(true);
      var2.setCellRenderer(new TreeNodeDecorRenderer());
      CheckBoxTree var3 = new CheckBoxTree(var2);
      var3.checkAll();
      var3.setPopupMenuHandler(new AttributeSelectionTree.PopupHandler());
      return var3;
   }

   private void registerSelectionActions() {
      TreeSelectionSource var1 = new TreeSelectionSource(this.tree.getAdaptee());
      this.setAuxClassesAction.setSelectionSource(var1);
      this.setExtendedAttributesAction.setSelectionSource(var1);
      var1.setDefaultAction(this.viewPropertiesAction);
   }

   private static class FilterUi extends TreeStringFilterDefiner {
      public FilterUi(CheckBoxTree var1) {
         super(var1.getAdaptee());
         this.setCaption(PackageResources.Tree.Search.CAPTION);
         this.setToolTip(PackageResources.Tree.Search.TOOLTIP);
         this.setLargeModel(true);
      }

      @Override
      public boolean filter(KongaTreeNode var1, StringSearchModel var2) {
         String var3 = ((AttributeSelectionNode)var1).getUserObject().getName();
         return var2.test(var3);
      }

      @Override
      public boolean isSubjectToFiltering(KongaTreeNode var1) {
         return var1 instanceof AttributeSelectionNode;
      }
   }

   private class Layout {
      private final OneColumnPanel col = new OneColumnPanel();

      public Layout() {
         this.col.withInsets(0, 0, 5, 0);
         this.col.noFill(AttributeSelectionTree.this.filterUi);
         this.addButtons();
         this.addTree();
         this.addReadOnlyNote();
      }

      public void decorate(UiCanvas var1) {
         var1.display(BorderLayoutBuilder.fillWith(this.col));
      }

      private void addButtons() {
         Box var1 = Box.createHorizontalBox();

         for (Action var5 : AttributeSelectionTree.this.getBulkChangeActions()) {
            var1.add(new KongaButton(var5));
            var1.add(Box.createHorizontalStrut(5));
         }

         if (AttributeSelectionTree.this.refreshAction != null) {
            var1.add(Box.createHorizontalGlue());
            var1.add(new KongaButton(AttributeSelectionTree.this.refreshAction));
         }

         this.col.add(var1);
      }

      private void addTree() {
         AttributeSelectionTree.TreePanelImpl var1 = AttributeSelectionTree.this.new TreePanelImpl(
            AttributeSelectionTree.this.title, AttributeSelectionTree.this.tree.getAdaptee()
         );
         this.col.fillOut(var1);
      }

      private void addReadOnlyNote() {
         if (AttributeSelectionTree.this.model.getType() == SourceTarget.Target) {
            JPanel var1 = InvisiblePanel.newPanel();
            ReadOnlyAttributesDisabledCaption.addTo(var1, null);
            this.col.noFill(var1);
         }
      }
   }

   private class PopupHandler implements PopupMenuRequestHandler {
      private PopupHandler() {
      }

      @Override
      public boolean popupMenuRequested(Component var1, MouseEvent var2) {
         KongaTree var3 = (KongaTree)var1;
         Object var4 = this.getSelectedObject(var3, var2);
         if (var4 == null) {
            return false;
         }

         JPopupMenu var5 = null;
         if (var4 instanceof ClassNameNode) {
            var5 = this.getPopupMenu((ClassNameNode)var4);
         } else if (var4 instanceof AttributeSelectionNode) {
            var5 = this.getAttributeNodePopupMenu();
         }

         if (var5 != null) {
            var3.showPopupMenu(var5);
         }

         return true;
      }

      private Object getSelectedObject(KongaTree var1, MouseEvent var2) {
         TreePath var3 = var1.getPathForLocation(var2.getX(), var2.getY());
         return var3 != null ? var3.getLastPathComponent() : null;
      }

      private JPopupMenu getPopupMenu(ClassNameNode var1) {
         if (var1.getChildAt(0) instanceof UnknownStructureNode) {
            return null;
         }

         KongaPopupMenu var2 = new KongaPopupMenu();
         var2.addActions(
            AttributeSelectionTree.this.new SelectRequiredAction(var1),
            AttributeSelectionTree.this.tree.getCheckAllAction(var1),
            AttributeSelectionTree.this.tree.getUncheckAllAction(var1),
            AttributeSelectionTree.this.setAuxClassesAction,
            AttributeSelectionTree.this.setExtendedAttributesAction
         );
         return var2;
      }

      private JPopupMenu getAttributeNodePopupMenu() {
         KongaPopupMenu var1 = new KongaPopupMenu();
         var1.add(AttributeSelectionTree.this.viewPropertiesAction);
         return var1;
      }
   }

   private class SelectRequiredAction extends AbstractAction {
      private final KongaTreeNode node;

      public SelectRequiredAction(KongaTreeNode var2) {
         super(PackageResources.Tree.SelectAll.NAME);
         this.node = var2;
         this.putValue("ShortDescription", PackageResources.Tree.SelectAll.TOOLTIP);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         KongaTreeNode[] var2;
         if (this.node instanceof ClassNameNode) {
            var2 = ((ClassNameNode)this.node).getRequiredAttributes();
         } else {
            var2 = AttributeSelectionTree.this.model.getRequiredAttributes();
         }

         AttributeSelectionTree.this.tree.addCheckedNodes(var2);
      }
   }

   private static class TreeDecorator extends PostRenderingComponentPainter {
      private static final String REQUIRED = "Required attribute";
      private static final String MULTI = "* = multi-valued attribute";

      private TreeDecorator() {
      }

      @Override
      public void postRendering(Component var1, Graphics2D var2) {
         JTree var3 = (JTree)var1;
         GraphicsState var4 = new GraphicsState(var2, GraphicsState.RENDERING_HINTS, GraphicsState.COLOR, GraphicsState.FONT);
         UiUtils.makeAntiAliased(var2);
         var2.setColor(AttributeSelectionNode.REQUIRED);
         var2.setFont(var3.getFont());
         FontMetrics var5 = var3.getFontMetrics(var3.getFont());
         int var6 = Math.max(var5.stringWidth("Required attribute"), var5.stringWidth("* = multi-valued attribute"));
         int var7 = var5.getHeight();
         int var8 = var3.getWidth() - var6 - 10;
         int var9 = var7;
         var2.drawString("Required attribute", var8, var9);
         var2.setColor(Color.BLACK);
         var9 += var7 + 3;
         var2.drawString("* = multi-valued attribute", var8, var9);
         this.drawBorder(var2, var8 - 10, var9 + 8, var3.getWidth());
         var4.restore();
      }

      private void drawBorder(Graphics2D var1, int var2, int var3, int var4) {
         var1.setColor(Color.GRAY);
         var1.drawLine(var2, 0, var2, var3);
         int var5 = var2 + 2;
         int var6 = var3 + 2;
         var1.drawLine(var2, var3, var5, var6);
         var1.drawLine(var5, var6, var4, var6);
      }
   }

   private static class TreeImpl extends KongaTree {
      TreeImpl(KongaTreeModel var1) {
         super(var1);
      }

      @Override
      public Dimension getPreferredSize() {
         return DimensionUtils.withWidthAtLeast(super.getPreferredSize(), 600);
      }

      @Override
      public void addNotify() {
         super.addNotify();
         this.doHack();
      }

      private void doHack() {
         KongaTreeModel var1 = this.getModel();
         if (var1 instanceof DefaultKongaTreeModel) {
            TreePath var2 = this.getPath(var1.getRoot());
            ((DefaultKongaTreeModel)var1).fireTreeStructureChanged(this, var2);
            this.expandAll();
         }
      }
   }

   private class TreePanelImpl extends TreePanel {
      public TreePanelImpl(String var2, KongaTree var3) {
         super(var2, var3);
         this.setDropdownActions(AttributeSelectionTree.this.getBulkChangeActions());
      }

      @Override
      protected JButton[] getToolBarButtons() {
         JButton[] var1 = super.getToolBarButtons();
         JButton var2 = createToolBarButton(AttributeSelectionTree.this.viewPropertiesAction);
         JButton[] var3 = new JButton[var1.length + 1];
         if (var1.length == 0) {
            var3[0] = var2;
         } else {
            var3[0] = var1[0];
            var3[1] = var2;

            for (int var4 = 1; var4 < var1.length; var4++) {
               var3[var4 + 1] = var1[var4];
            }
         }

         return var3;
      }
   }

   private class ViewPropertiesAction extends AbstractSingleObjectSelectionAction<AttributeSelectionNode> {
      public ViewPropertiesAction() {
         super(AttributeSelectionNode.class);
         ActionDecorators.VIEW_DETAILS.decorate(this);
      }

      protected void run(AttributeSelectionNode var1, ActionEvent var2) {
         this.showDetails(var1);
      }

      private void showDetails(AttributeSelectionNode var1) {
         AttributeInfoViewer var2 = new AttributeInfoViewer(var1.getUserObject());
         ComponentDialogTrigger var3 = new ComponentDialogTrigger(AttributeSelectionTree.this.tree.getAdaptee());
         UiProviderDialogDisplayer.display(var3, var2, PackageResources.Tree.ATTRIBUTE_PROPERTIES_TITLE);
      }
   }
}
