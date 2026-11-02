package org.etlforge.application.ui.viewer.log.tree;

import java.io.File;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.ui.widget.tree.UserObjectNodeObjectConverter;

public class LogFileRepositoryTree implements UiProvider {
   private final KongaTree tree;
   private final TreePanel treePanel;
   private final Renderer renderer = new Renderer();
   private final DefaultKongaTreeModel model;

   public LogFileRepositoryTree(LogFileRepository var1, List<? extends Action> var2, List<? extends Action> var3) {
      LogTreeModelFactory var4 = new LogTreeModelFactory(var1);
      this.model = var4.getModel();
      this.tree = this.createTree();
      this.treePanel = this.createTreePanel(var2, var3);
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.model);
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);
      var1.setCellRenderer(this.renderer);
      KongaTreeNode var2 = this.model.getRoot().getChildAt(0);
      var1.makeNodeExpanded(var2);
      var1.asSelectionSource().setNodeObjectConverter(new UserObjectNodeObjectConverter(File.class));
      return var1;
   }

   private TreePanel createTreePanel(List<? extends Action> var1, List<? extends Action> var2) {
      TreePanelFactory var3 = new TreePanelFactory(var1, var2);
      return var3.getPanel(this.tree);
   }

   public TreeSelectionSource asSelectionSource() {
      return this.tree.asSelectionSource();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.treePanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public void setPopupMenuRequestHandler(PopupMenuRequestHandler var1) {
      PopupMenuRequestHandlers.install(this.tree, var1);
   }

   public void setLogFileDirty(File var1, boolean var2) {
      this.renderer.setFileDirty(var1, var2);

      for (KongaTreeNode var4 : this.model.getRoot().depthFirstEnumeration()) {
         if (var4 instanceof LogFileNode && var4.getUserObject().equals(var1)) {
            this.model.nodeChanged(var4);
            break;
         }
      }
   }
}
