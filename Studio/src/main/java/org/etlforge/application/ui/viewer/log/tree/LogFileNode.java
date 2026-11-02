package org.etlforge.application.ui.viewer.log.tree;

import java.io.File;
import org.etlforge.application.ui.viewer.log.LogFileUtils;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

class LogFileNode extends DefaultKongaTreeNode {
   private final String display;

   public LogFileNode(KongaTreeNode var1, File var2) {
      super(var1, var2, false);
      this.display = LogFileUtils.getDisplayName(var2);
   }

   public File getUserObject() {
      return (File)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return this.display;
   }

   @Override
   public String toString() {
      return this.display;
   }
}
