package org.etlforge.application.ui.viewer.log.tree;

import java.io.File;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

abstract class LogFilesParentNode extends DefaultKongaTreeNode {
   public LogFilesParentNode(KongaTreeNode var1, Object var2) {
      super(var1, var2, true);
   }

   public final void populate(LogFileRepository.Folder var1) {
      File var2 = var1.getSystemOut();
      if (var2 != null) {
         new SystemOutNode(this, var2);
      }

      File var3 = var1.getSystemErr();
      if (var3 != null) {
         new SystemErrNode(this, var3);
      }

      for (File var7 : var1.getLogFiles()) {
         new LogFileNode(this, var7);
      }
   }
}
