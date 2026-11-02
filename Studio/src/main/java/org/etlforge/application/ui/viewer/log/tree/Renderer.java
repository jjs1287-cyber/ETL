package org.etlforge.application.ui.viewer.log.tree;

import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.ui.util.Fonts;

final class Renderer extends DefaultTreeCellRenderer {
   private final Set<File> dirtyFiles = Sets.newHashSet();

   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      if (var2 instanceof HeaderNode || var2 instanceof SessionDayNode || var2 instanceof SessionLogFolderNode) {
         this.decorateFolderNode(var1, var4, var2 instanceof HeaderNode);
      } else if (var2 instanceof LogFileNode) {
         this.decorateFileNode(var1, (LogFileNode)var2);
      }

      this.setToolTipText(var2.toString());
      return this;
   }

   private void decorateFolderNode(JTree var1, boolean var2, boolean var3) {
      this.setNodeFont(var1, var3);
      if (var2) {
         this.setIcon(PackageResources.Nodes.Folder.OPEN);
      } else {
         this.setIcon(PackageResources.Nodes.Folder.CLOSED);
      }
   }

   private void setNodeFont(JTree var1, boolean var2) {
      Font var3 = var1.getFont();
      if (var2) {
         var3 = Fonts.bold(var3);
      }

      this.setFont(var3);
   }

   private void decorateFileNode(JTree var1, LogFileNode var2) {
      this.setFont(var1.getFont());
      this.setIcon(PackageResources.Nodes.FILE_ICON);
      File var3 = var2.getUserObject();
      if (this.dirtyFiles.contains(var3)) {
         this.setText(var2.getDisplayName() + " *");
      }
   }

   public void setFileDirty(File var1, boolean var2) {
      if (var2) {
         this.dirtyFiles.add(var1);
      } else {
         this.dirtyFiles.remove(var1);
      }
   }
}
