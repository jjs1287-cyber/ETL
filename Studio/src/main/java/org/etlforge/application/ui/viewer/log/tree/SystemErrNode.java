package org.etlforge.application.ui.viewer.log.tree;

import java.io.File;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class SystemErrNode extends LogFileNode {
   public SystemErrNode(KongaTreeNode var1, File var2) {
      super(var1, var2);
   }

   @Override
   public String getDisplayName() {
      return "system.err";
   }
}
