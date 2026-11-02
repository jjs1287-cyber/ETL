package org.etlforge.integration.activity.ui.monitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.system.Disposable;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

public final class DefaultOperationMonitorSite implements OperationMonitorSite, Disposable {
   private final OperationMonitor monitor;
   private final JPanel container;
   private final Object layoutPosition;
   private MonitorControl activityMonitorControl;
   private JComponent activityMonitorUi;

   public DefaultOperationMonitorSite(OperationMonitor var1, JPanel var2, Object var3) {
      this.monitor = var1;
      this.container = var2;
      this.layoutPosition = var3;
   }

   @Override
   public final void dispose() {
      if (this.activityMonitorControl != null) {
         this.activityMonitorControl.stop();
      }
   }

   @Override
   public final void openActivityMonitor(long var1) {
      if (this.activityMonitorControl == null) {
         this.createAndStartActivityMonitor(var1);
      } else {
         this.activityMonitorControl.reset(var1);
      }

      if (this.activityMonitorUi == null) {
         this.displayActivityMonitor(var1);
      }
   }

   private void createAndStartActivityMonitor(long var1) {
      this.activityMonitorControl = new MonitorControl(this.monitor);
      this.activityMonitorControl.addCloseListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            DefaultOperationMonitorSite.this.closeActivityMonitor();
         }
      });
      this.activityMonitorControl.start(var1);
   }

   private void displayActivityMonitor(long var1) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder();
      var3.north(this.activityMonitorControl);
      this.activityMonitorUi = var3.container();
      this.addToUi(this.activityMonitorUi);
      this.activityMonitorControl.start(var1);
   }

   private void addToUi(JComponent var1) {
      this.container.add(var1, this.layoutPosition);
      UiUtils.revalidate(this.container);
   }

   @Override
   public final void closeActivityMonitor() {
      if (this.activityMonitorUi != null) {
         this.activityMonitorControl.stop();
         this.removeFromUi(this.activityMonitorUi);
         this.activityMonitorUi = null;
      }
   }

   private void removeFromUi(JComponent var1) {
      this.container.remove(var1);
      UiUtils.revalidate(this.container);
   }
}
