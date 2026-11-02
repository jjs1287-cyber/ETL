package org.etlforge.integration.client.ui.structure.editor.xsd;

import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.action.OpenLocalFolderAction;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.ui.widget.tree.NodeStatus;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.util.clipboard.ClipboardUtils;
import org.etlforge.xml.xsd.GeneratedXsds;

public final class XsdFileReview implements UiProvider {
   private final KongaTree tree = this.createTree();
   private final XsdFileReview.OpenParentFolderAction openFolderAction = new XsdFileReview.OpenParentFolderAction();

   public XsdFileReview() {
      this.installCopyPathAction();
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree();
      var1.setRootVisible(false);
      var1.setVisibleRowCount(6);
      var1.setShowsRootHandles(false);
      return var1;
   }

   private void installCopyPathAction() {
      XsdFileReview.CopyPathAction var1 = new XsdFileReview.CopyPathAction();
      TreeSelectionSource var2 = new TreeSelectionSource(this.tree);
      var2.setNodeObjectConverter(new XsdFileReview.SelectionConverter());
      var1.setSelectionSource(var2);
      PopupMenuRequestHandlers.install(this.tree, var1);
   }

   public void displayResult(CreateXsdResult var1) {
      if (var1 == null) {
         DefaultKongaTreeModel var2 = new DefaultKongaTreeModel(new DefaultKongaTreeNode("root", false));
         this.tree.setModel(var2);
         this.tree.setRootVisible(false);
      } else {
         KongaTreeModel var3 = this.createTreeModel(var1);
         this.tree.setModel(var3);
         this.tree.setCellRenderer(new XsdFileReview.Renderer(var1.getMainXsd()));
         this.tree.setRootVisible(true);
         this.tree.expandAll();
      }

      this.openFolderAction.setResult(var1);
   }

   private KongaTreeModel createTreeModel(CreateXsdResult var1) {
      GeneratedXsds var2 = var1.getXsds();
      File var3 = var2.getMainXsd();
      XsdFileReview.FileNode var4 = new XsdFileReview.FileNode(var3.getParentFile());
      var4.addChild(new XsdFileReview.FileNode(var3));
      ArrayList var5 = Lists.newArrayList(var2.getAdditionalXsds());
      Collections.sort(var5);

      for (Object __etlItem83 : var5) {
         File var7 = (File) __etlItem83;
         var4.addChild(new XsdFileReview.FileNode(var7));
      }

      return new DefaultKongaTreeModel(var4);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 4);
      var2.north(this.layoutToolbar()).center(this.tree);
      var2.decorate(var1);
   }

   private JComponent layoutToolbar() {
      BoxBuilder var1 = BoxBuilder.horizontal().bottomAligned();
      var1.add("The following XSD files will be uploaded to the server:");
      if (SystemUtils.IS_OS_WINDOWS) {
         var1.glue().add(this.openFolderAction);
      }

      return var1.container();
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   private static class CopyPathAction extends AbstractSingleObjectSelectionAction<File> {
      public CopyPathAction() {
         super(File.class, "Copy Path");
      }

      protected void run(File var1, ActionEvent var2) {
         ClipboardUtils.copyToSystemClipboard(var1.getAbsolutePath());
      }
   }

   private static class FileNode extends DefaultKongaTreeNode {
      public FileNode(File var1) {
         super(var1, var1.isDirectory());
      }

      public File getUserObject() {
         return (File)super.getUserObject();
      }

      @Override
      public String getDisplayName() {
         File var1 = this.getUserObject();
         return var1.isDirectory() ? var1.getAbsolutePath() : var1.getName();
      }

      @Override
      public Icon getNodeIcon(NodeStatus var1) {
         File var2 = this.getUserObject();
         if (var2.isDirectory()) {
            return var1 == NodeStatus.OPENED ? CommonIcons.FOLDER_OPEN_16 : CommonIcons.FOLDER_CLOSED_16;
         } else {
            return null;
         }
      }
   }

   private static class OpenParentFolderAction extends OpenLocalFolderAction {
      private File folder;

      public OpenParentFolderAction() {
         super("OpenXsdParentFolder", "&Open Folder...", null);
         this.setEnabled(false);
      }

      public void setResult(CreateXsdResult var1) {
         if (var1 == null) {
            this.setFolder(null);
         } else {
            this.setFolder(var1.getParentFolder());
         }
      }

      private void setFolder(File var1) {
         this.folder = var1;
         this.setEnabled(var1 != null);
      }

      @Override
      protected File getFolderToOpen() {
         return this.folder;
      }
   }

   private static class Renderer extends DefaultTreeCellRenderer {
      private final File mainFile;
      private final TextStyle mainFileStyle;
      private final TextStyle nodeStyle;

      public Renderer(File var1) {
         this.mainFile = var1;
         this.nodeStyle = TextStyles.TreeLeafText;
         this.mainFileStyle = TextStyle.newStyle(this.nodeStyle, 1);
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         if (var2 instanceof XsdFileReview.FileNode) {
            XsdFileReview.FileNode var8 = (XsdFileReview.FileNode)var2;
            this.setIcon(var8.getNodeIcon(var4 ? NodeStatus.OPENED : NodeStatus.CLOSED));
            TextStyle var9 = var8.getUserObject() == this.mainFile ? this.mainFileStyle : this.nodeStyle;
            this.setFont(var9.getFont());
         }

         return this;
      }
   }

   private static class SelectionConverter implements NodeObjectConverter {
      private SelectionConverter() {
      }

      @Override
      public Object toObject(TreeNode var1) {
         return var1 instanceof XsdFileReview.FileNode ? ((XsdFileReview.FileNode)var1).getUserObject() : null;
      }

      @Override
      public TreeNode toNode(Object var1) {
         return null;
      }
   }
}
