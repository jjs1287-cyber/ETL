package org.etlforge.integration.client.ui.operation;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.integration.activity.ui.monitor.DefaultOperationMonitorSite;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.ui.Empty;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.widget.KongaScrollPane;

public final class OperationMonitorPageSite implements OperationMonitorSite {
   private final OperationMonitor monitor;
   private final GenericEditor page;
   private final JPanel canvas;
   private DefaultOperationMonitorSite monitorSite;

   public OperationMonitorPageSite(OperationMonitor var1, GenericEditor var2) {
      this.monitor = var1;
      this.page = var2;
      this.canvas = new JPanel(new BorderLayout());
      this.canvas.setOpaque(false);
      var2.addResource(new AbstractWindowElementResource() {
         @Override
         public void dispose(WindowElement var1) {
            if (OperationMonitorPageSite.this.monitorSite != null) {
               OperationMonitorPageSite.this.monitorSite.dispose();
            }
         }
      });
   }

   @Override
   public void openActivityMonitor(long var1) {
      if (this.monitorSite == null) {
         this.monitorSite = new DefaultOperationMonitorSite(this.monitor, this.canvas, "South");
      }

      this.monitorSite.openActivityMonitor(var1);
   }

   @Override
   public void closeActivityMonitor() {
      if (this.monitorSite != null) {
         this.monitorSite.closeActivityMonitor();
      }
   }

   public void layoutPage(JPanel var1, boolean var2, boolean var3) {
      if (var2) {
         KongaScrollPane var4 = new KongaScrollPane(var1);
         var4.makeInconspicuous();
         this.canvas.add(var4, "Center");
         if (var3) {
            var4.addMouseListener(new OperationMonitorPageSite.ContextMenuTrigger());
         }

         this.page.setEditorBorder(Empty.border());
      } else {
         if (var3) {
            var1.addMouseListener(new OperationMonitorPageSite.ContextMenuTrigger());
         }

         this.canvas.add(var1, "Center");
      }

      this.page.setMainPanel(this.canvas);
   }

   private class ContextMenuTrigger extends MouseActionListener {
      private ContextMenuTrigger() {
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         OperationMonitorPageSite.this.page.getController().processMouseEvent(OperationMonitorPageSite.this.page, var1);
      }
   }
}
