package org.etlforge.application.ui.viewer.log.tree;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class LogTreeModelFactory {
   private final DateFormat sessionDayFormat;
   private final LogFileRepository repository;
   private final KongaTreeNode root;
   private final DefaultKongaTreeModel model;

   LogTreeModelFactory(LogFileRepository var1) {
      this.repository = var1;
      this.sessionDayFormat = new SimpleDateFormat("yyyy-MM-dd");
      this.root = this.createRoot();
      this.model = new DefaultKongaTreeModel(this.root);
   }

   KongaTreeNode getRoot() {
      return this.root;
   }

   DefaultKongaTreeModel getModel() {
      return this.model;
   }

   private KongaTreeNode createRoot() {
      DefaultKongaTreeNode var1 = new DefaultKongaTreeNode(PackageResources.Nodes.ROOT_NAME, true);
      this.populateRootNode(var1);
      return var1;
   }

   private void populateRootNode(DefaultKongaTreeNode var1) {
      HeaderNode var2 = new HeaderNode(var1, PackageResources.Nodes.CURRENT_SESSION);
      LogFileRepository.Folder[] var3 = this.repository.getSessionLogs();
      if (var3.length != 0) {
         var2.populate(var3[0]);
         this.createPreviousSessionNodes(var1, var3);
      }
   }

   private void createPreviousSessionNodes(DefaultKongaTreeNode var1, LogFileRepository.Folder[] var2) {
      HeaderNode var3 = new HeaderNode(var1, PackageResources.Nodes.PREVIOUS_SESSION);
      SessionDayNode var4 = null;

      for (int var5 = 1; var5 < var2.length; var5++) {
         LogFileRepository.Folder var6 = var2[var5];
         String var7 = this.sessionDayFormat.format(var6.getSessionDate());
         if (var4 == null || !var7.equals(var4.getUserObject())) {
            var4 = new SessionDayNode(var3, var7);
         }

         new SessionLogFolderNode(var4, var2[var5]);
      }
   }
}
