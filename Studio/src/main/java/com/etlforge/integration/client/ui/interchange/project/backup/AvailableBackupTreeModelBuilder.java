package com.etlforge.integration.client.ui.interchange.project.backup;

import java.util.Calendar;
import java.util.Date;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

class AvailableBackupTreeModelBuilder {
   private AvailableBackupTreeModelBuilder.DayNode currentDayNode;
   private KongaTreeNode root;

   public DefaultKongaTreeModel buildModel(AvailableBackup[] var1) {
      this.currentDayNode = null;
      this.root = new DefaultKongaTreeNode("Available Backups", true);
      if (var1.length > 0) {
         this.addBackupNodes(var1);
      } else {
         new DefaultKongaTreeNode(this.root, "[No backups are available]", false);
      }

      return new DefaultKongaTreeModel(this.root);
   }

   private void addBackupNodes(AvailableBackup[] var1) {
      for (AvailableBackup var5 : var1) {
         AvailableBackupTreeModelBuilder.DayNode var6 = this.getDayNode(var5);
         new AvailableBackupTreeModelBuilder.BackupNode(var6, var5);
      }
   }

   private AvailableBackupTreeModelBuilder.DayNode getDayNode(AvailableBackup var1) {
      if (this.currentDayNode == null || !this.currentDayNode.isSameDayAs(var1)) {
         this.currentDayNode = new AvailableBackupTreeModelBuilder.DayNode(this.root, var1);
      }

      return this.currentDayNode;
   }

   private static class BackupNode extends DefaultKongaTreeNode implements AvailableBackup {
      public BackupNode(AvailableBackupTreeModelBuilder.DayNode var1, AvailableBackup var2) {
         super(var1, var2, false);
      }

      @Override
      public ProjectLocation getLocation() {
         return this.getUserObject().getLocation();
      }

      @Override
      public Date getTimestamp() {
         return this.getUserObject().getTimestamp();
      }

      public AvailableBackup getUserObject() {
         return (AvailableBackup)super.getUserObject();
      }
   }

   private static class DayNode extends DefaultKongaTreeNode implements DayOfBackup {
      public DayNode(KongaTreeNode var1, AvailableBackup var2) {
         super(var1, var2.getTimestamp(), true);
      }

      @Override
      public Date getDate() {
         return this.getUserObject();
      }

      public boolean isSameDayAs(AvailableBackup var1) {
         Calendar var2 = Calendar.getInstance();
         var2.setTime(this.getUserObject());
         Calendar var3 = Calendar.getInstance();
         var3.setTime(var1.getTimestamp());
         return var2.get(1) == var3.get(1) && var2.get(6) == var3.get(6);
      }

      public Date getUserObject() {
         return (Date)super.getUserObject();
      }
   }
}
