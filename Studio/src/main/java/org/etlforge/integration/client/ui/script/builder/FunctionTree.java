package org.etlforge.integration.client.ui.script.builder;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.widget.tree.ExpandCollapseToggleAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionCategory;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.graphics.IconRow;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.ui.widget.tree.UserObjectNodeObjectConverter;

public class FunctionTree implements UiProvider {
   private final KongaTree tree = this.createTree();
   private final TreeSelectionSource selSource = new TreeSelectionSource(this.tree);

   public FunctionTree() {
      this.selSource.setNodeObjectConverter(new UserObjectNodeObjectConverter(Function.class));
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.createModel());
      ScriptBuilderUtils.setTreeStyle(var1);
      var1.setCellRenderer(new FunctionTree.Renderer(false));
      var1.setNodeToolTipGenerator(new FunctionTree.ToolTipGenerator());
      var1.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      return var1;
   }

   private KongaTreeModel createModel() {
      DefaultKongaTreeNode var1 = new DefaultKongaTreeNode("Functions", true);

      for (FunctionCategory var3 : FunctionRegistry.getRegistry().getCategories()) {
         FunctionTree.CategoryNode var4 = new FunctionTree.CategoryNode(null, var3);

         for (Function var6 : var3.getFunctions()) {
            if (!var6.isHidden()) {
               boolean var7 = FunctionRegistry.getRegistry().hasWizard(var6);
               new FunctionTree.FunctionNode(var4, var6, var7);
            }
         }

         if (var4.getChildCount() > 0) {
            var1.addChild(var4);
         }
      }

      for (FunctionCategory category : org.etlforge.integration.data.script.JavaScriptFunctions.categories()) {
         FunctionTree.CategoryNode node = new FunctionTree.CategoryNode(var1, category);
         for (Function function : category.getFunctions()) new FunctionTree.FunctionNode(node, function, false);
      }
      return new DefaultKongaTreeModel(var1);
   }

   public KongaTree getTree() {
      return this.tree;
   }

   public void setMode(ScriptBuilderMode var1) {
      boolean var2 = var1 == ScriptBuilderMode.BUSINESS_ANALYST;
      FunctionTree.Renderer var3 = new FunctionTree.Renderer(var2);
      this.tree.setCellRenderer(var3);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.tree));
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   public void selectNext() {
      if (this.tree.getSelectionCount() == 0) {
         this.tree.selectNextRow();
      }
   }

   public SelectionAction getFunctionAction(Receiver<Function> var1, Icon var2, String var3, boolean var4) {
      FunctionTree.FunctionAction var5 = new FunctionTree.FunctionAction(var1, var2, var3);
      if (var4) {
         this.selSource.setDefaultAction(var5);
      }

      return var5;
   }

   public ExpandCollapseToggleAction getExpansionToggleAction() {
      return new ExpandCollapseToggleAction(this.tree);
   }

   public static class CategoryNode extends DefaultKongaTreeNode {
      public CategoryNode(KongaTreeNode var1, FunctionCategory var2) {
         super(var1, var2, true);
      }

      @Override
      public String getDisplayName() {
         return ((FunctionCategory)this.getUserObject()).getName();
      }
   }

   private class FunctionAction extends AbstractSingleObjectSelectionAction<Function> {
      private final Receiver<Function> handler;

      public FunctionAction(Receiver<Function> var2, Icon var3, String var4) {
         super(Function.class, null, var3);
         this.putValue("ShortDescription", var4);
         this.handler = var2;
         this.setSelectionSource(FunctionTree.this.selSource);
      }

      protected void run(Function var1, ActionEvent var2) {
         this.handler.handle(var1);
      }
   }

   public static class FunctionNode extends DefaultKongaTreeNode {
      private final boolean hasWizard;

      public FunctionNode(FunctionTree.CategoryNode var1, Function var2, boolean var3) {
         super(var1, var2, false);
         this.hasWizard = var3;
      }

      @Override
      public String getDisplayName() {
         return this.getUserObject().getName();
      }

      public Function getUserObject() {
         return (Function)super.getUserObject();
      }

      public boolean hasWizard() {
         return this.hasWizard;
      }
   }

   private static class Renderer extends DefaultTreeCellRenderer {
      private final Icon withWizard;
      private final Icon withoutWizard;
      private final boolean doWizardRendering;

      public Renderer(boolean var1) {
         this.doWizardRendering = var1;
         if (var1) {
            Icon var2 = ClientIcons.WAND_16;
            Icon var3 = Icons.FORMULA_ICON;
            this.withoutWizard = new IconRow(Empty.iconOfSameSize(var2), var3);
            this.withWizard = new IconRow(var2, var3);
         } else {
            this.withoutWizard = Icons.FORMULA_ICON;
            this.withWizard = null;
         }
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         if (var2 instanceof FunctionTree.FunctionNode) {
            if (this.doWizardRendering && ((FunctionTree.FunctionNode)var2).hasWizard()) {
               this.setIcon(this.withWizard);
            } else {
               this.setIcon(this.withoutWizard);
            }
         }

         return this;
      }
   }

   private static class ToolTipGenerator implements com.google.common.base.Function<Object, String> {
      private ToolTipGenerator() {
      }

      public String apply(Object var1) {
         if (var1 instanceof FunctionTree.FunctionNode) {
            Function var2 = ((FunctionTree.FunctionNode)var1).getUserObject();
            FunctionHelpHtml var3 = new FunctionHelpHtml(var2);
            var3.setWidth(400);
            return var3.getSignatureAndFirstSentenceOfDocumentation();
         } else {
            return null;
         }
      }
   }
}
