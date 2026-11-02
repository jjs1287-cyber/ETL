package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.LinkedList;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Size;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.text.KongaTextArea;

final class DataWarningPresenter {
   private final OverlayContainer container;
   private final LinkedList<DataWarningPresenter.Warning> queue;
   private boolean isShowing;

   public DataWarningPresenter(OverlayContainer var1) {
      this.container = var1;
      this.queue = Lists.newLinkedList();
   }

   public void showWarning(String var1, String var2) {
      assert EventQueue.isDispatchThread() : "showWarning should be called from the EDT";
      DataWarningPresenter.Warning var3 = new DataWarningPresenter.Warning(var1, var2);
      this.showWarning(var3);
   }

   private void showWarning(DataWarningPresenter.Warning var1) {
      if (this.isShowing) {
         this.queue.add(var1);
      } else {
         OverlayDecorator var2 = this.createOverlay(var1);
         var2.show(this.container, new OverlayCenterPositioner());
         this.isShowing = true;
      }
   }

   private OverlayDecorator createOverlay(DataWarningPresenter.Warning var1) {
      OverlayDecorator var2 = new OverlayDecorator(var1.getViewer(), var1.title) {
         @Override
         protected void onHidden() {
            DataWarningPresenter.this.isShowing = false;
            DataWarningPresenter.this.showNextWarning();
         }
      };
      var2.setTitleIcon(CommonIcons.WARNING_24);
      var2.setBlocking(true);
      var2.useCloseOnly();
      return var2;
   }

   private void showNextWarning() {
      if (!this.queue.isEmpty()) {
         DataWarningPresenter.Warning var1 = this.queue.removeFirst();
         this.showWarning(var1);
      }
   }

   private static class Warning {
      public final String title;
      private final String text;

      public Warning(String var1, String var2) {
         this.title = var1;
         this.text = var2;
      }

      private JComponent getViewer() {
         KongaTextArea var1 = KongaTextArea.viewer(this.text, Size.rows(10), Size.columns(60));
         JScrollPane var2 = new JScrollPane(var1);
         var2.setBorder(Empty.border());
         return var2;
      }
   }
}
