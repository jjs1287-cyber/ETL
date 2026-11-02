package org.etlforge.application.ui.viewer.log.tree;

import java.text.DateFormat;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.ui.widget.tree.KongaTreeNode;

class SessionLogFolderNode extends LogFilesParentNode {
   private final String displayName;

   public SessionLogFolderNode(KongaTreeNode var1, LogFileRepository.Folder var2) {
      super(var1, var2);
      DateFormat var3 = DateFormat.getTimeInstance();
      this.displayName = var3.format(var2.getSessionDate());
      this.populate(var2);
   }

   public LogFileRepository.Folder getUserObject() {
      return (LogFileRepository.Folder)super.getUserObject();
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public String toString() {
      return this.getDisplayName();
   }
}
