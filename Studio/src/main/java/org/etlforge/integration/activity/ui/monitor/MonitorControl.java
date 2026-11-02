package org.etlforge.integration.activity.ui.monitor;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;

final class MonitorControl implements UiProvider {
   private static final String TITLE = "Operation monitor";
   private final OperationMonitor monitor;
   private final JButton close;
   private final JButton delete;
   private final JProgressBar progressBar;
   private final JLabel titleLabel;

   public MonitorControl(OperationMonitor var1) {
      this.monitor = var1;
      this.titleLabel = new JLabel("Operation monitor");
      this.delete = new JButton(var1.getDeleteQueueAction());
      this.close = new JButton(CommonIcons.CLOSE_16);
      ButtonUtils.configureButtonsForToolBar(this.delete, this.close);
      this.progressBar = new JProgressBar();
      this.progressBar.setVisible(false);
      this.listenToMonitor(var1);
   }

   private void listenToMonitor(OperationMonitor var1) {
      var1.addRunningListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            final Boolean var2 = (Boolean)var1.getNewValue();
            UiUtils.runOnEventThread(new Runnable() {
               @Override
               public void run() {
                  MonitorControl.this.updateProgressBar(var2);
               }
            });
         }
      });
   }

   private void updateProgressBar(boolean var1) {
      this.progressBar.setIndeterminate(var1);
      this.progressBar.setVisible(var1);
   }

   public void start(long var1) {
      this.monitor.start(var1);
   }

   public void stop() {
      this.monitor.stop();
   }

   public void reset(long var1) {
      this.monitor.reset(var1);
   }

   public void addCloseListener(ActionListener var1) {
      this.close.addActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal()
         .withBorder(new BorderBuilder().etched().empty(3))
         .add(this.titleLabel)
         .glue()
         .add(this.progressBar)
         .glue()
         .addAllWithSpace(10, this.delete, this.close);
      Vertical.borderLayout().of(var2, this.monitor).withBorder(new LineBorder(Color.BLACK)).decorate(var1);
   }

   @Override
   public void requestFocus() {
   }
}
