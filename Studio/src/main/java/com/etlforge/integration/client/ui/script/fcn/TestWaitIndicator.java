package com.etlforge.integration.client.ui.script.fcn;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.concurrent.Future;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.wait.WaitIndicator;
import org.etlforge.ui.widget.CircleProgressBar;

public final class TestWaitIndicator implements WaitIndicator {
   private final TestWaitIndicator.CancelAction cancel = new TestWaitIndicator.CancelAction();
   private final CircleProgressBar circle = new CircleProgressBar();
   private final JComponent ui;

   public TestWaitIndicator() {
      this.circle.setPreferredSize(new Dimension(75, 75));
      this.ui = this.createUi();
   }

   private JComponent createUi() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 5);
      var1.center(this.circle).south(this.layoutCancelButton());
      var1.opaque().withBorder(new BorderBuilder().rounded(8, 8, 2, Color.GRAY).empty(5));
      return var1.container();
   }

   private JComponent layoutCancelButton() {
      return Horizontal.box().nonOpaque().glue().add(this.cancel).glue().container();
   }

   public void setFuture(Future<?> var1) {
      this.cancel.setFuture(var1);
   }

   @Override
   public void start() {
      this.circle.start();
   }

   @Override
   public void stop() {
      this.circle.stop();
      this.cancel.setFuture(null);
   }

   @Override
   public JComponent getUi() {
      return this.ui;
   }

   private static class CancelAction extends AbstractAction {
      private Future<?> future;

      public CancelAction() {
         super("Cancel", ApplicationResources.getIcon("Stop.16"));
         this.putValue("ShortDescription", "Cancel the test");
         this.setEnabled(false);
      }

      public void setFuture(Future<?> var1) {
         this.future = var1;
         this.setEnabled(var1 != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.future != null) {
            this.future.cancel(true);
         }
      }
   }
}
