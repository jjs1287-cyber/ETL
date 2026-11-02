package org.etlforge.application.ui.viewer.log.tree;

import java.io.File;
import org.etlforge.ui.widget.tree.KongaTreeNode;

class SystemOutNode extends LogFileNode {
   public SystemOutNode(KongaTreeNode var1, File var2) {
      super(var1, var2);
   }

   @Override
   public String getDisplayName() {
      return "system.out";
   }
}
