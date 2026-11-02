package org.etlforge.integration.client.ui.operation;

import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.etlforge.integration.activity.ui.monitor.DefaultOperationMonitorSite;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public class OperationMonitorDialog implements OperationMonitorSite {
   private final Operation op;
   private final OperationMonitor monitor;
   private final Window owner;
   private KongaDialog dialog;
   private DefaultOperationMonitorSite impl;

   public OperationMonitorDialog(Operation var1, OperationMonitor var2, Window var3) {
      this.op = var1;
      this.monitor = var2;
      this.owner = var3;
   }

   @Override
   public void openActivityMonitor(long var1) {
      assert this.dialog == null;
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 10);
      JPanel var4 = (JPanel)var3.container();
      this.impl = this.createAndOpenMonitorSite(var4, var1);
      this.dialog = this.createDialog(var3);
      this.dialog.setVisible(true);
   }

   private DefaultOperationMonitorSite createAndOpenMonitorSite(JPanel var1, long var2) {
      DefaultOperationMonitorSite var4 = new DefaultOperationMonitorSite(this.monitor, var1, "Center");
      var4.openActivityMonitor(var2);
      return var4;
   }

   private KongaDialog createDialog(BorderLayoutBuilder var1) {
      KongaDialog var2 = new KongaDialog(this.owner, "Operation Monitor: " + this.op.getName(), ModalityType.MODELESS);
      setDialogImage(var2);
      var2.standardLayout(var1, KongaDialog.CLOSE_ONLY);
      var2.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosed(WindowEvent var1) {
            if (OperationMonitorDialog.this.impl != null) {
               OperationMonitorDialog.this.impl.closeActivityMonitor();
               OperationMonitorDialog.this.impl = null;
            }

            OperationMonitorDialog.this.dialog = null;
         }
      });
      var2.setLocationRelativeTo(null);
      var2.manageLocation(this.getClass());
      var2.setCloseButtonAsDefault();
      return var2;
   }

   private static void setDialogImage(KongaDialog var0) {
      Icon var1 = EntityIcons.forType(EntityType.Operation).getDefaultIcon(EntityIcons.Size.SMALL);
      if (var1 instanceof ImageIcon) {
         Image var2 = ((ImageIcon)var1).getImage();
         var0.setIconImage(var2);
      }
   }

   @Override
   public void closeActivityMonitor() {
      if (this.dialog != null) {
         this.dialog.setVisible(false);
         this.dialog.dispose();
         this.dialog = null;
         this.impl.closeActivityMonitor();
         this.impl = null;
      }
   }
}
