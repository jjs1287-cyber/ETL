package org.etlforge.ui.widget;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.wait.WaitIndicator;

public class CircleProgressBarWithMessage implements WaitIndicator {
   private final CircleProgressBar circle;
   private final JComponent ui;
   private final JLabel messageLabel;

   public CircleProgressBarWithMessage(String var1) {
      this(new CircleProgressBar(), var1);
   }

   public CircleProgressBarWithMessage(CircleProgressBar var1, String var2) {
      this.circle = var1;
      this.messageLabel = new JLabel(var2);
      this.ui = this.createUi();
   }

   private JComponent createUi() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      var1.north(this.messageLabel).center(this.circle);
      var1.withBorder(new BorderBuilder().line().empty(10));
      var1.withBackground(javax.swing.UIManager.getColor("Panel.background"));
      return var1.container();
   }

   public void setMessageStyle(TextStyle var1) {
      var1.makeOver(this.messageLabel);
   }

   public void setBackground(Color var1) {
      this.ui.setBackground(var1);
   }

   public void setBorder(Border var1) {
      this.ui.setBorder(var1);
   }

   public void setOpaque(boolean var1) {
      this.ui.setOpaque(var1);
   }

   @Override
   public void start() {
      this.circle.start();
   }

   @Override
   public void stop() {
      this.circle.stop();
   }

   @Override
   public JComponent getUi() {
      return this.ui;
   }
}

