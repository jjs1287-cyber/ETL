package org.etlforge.ui.util.progress;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.system.callback.Receiver;

public class MessageGlassPane extends JComponent {
   private final ProgressMessage message = new ProgressMessage("", new Receiver<String>() {
      public void handle(String var1) {
         MessageGlassPane.this.repaint();
      }
   });

   public MessageGlassPane() {
      this.setFont(new JLabel().getFont());
      this.addMouseListener(new MouseAdapter() {});
      this.addMouseMotionListener(new MouseMotionAdapter() {});
      this.addKeyListener(new KeyAdapter() {});
      this.setFocusTraversalKeysEnabled(false);
   }

   public void setMessage(String var1) {
      this.message.setText(var1);
   }

   public void start(int var1) {
      this.message.setUpdateAutomatically(var1);
   }

   public void stop() {
      this.message.stopAutomaticUpdate();
   }

   @Override
   protected void paintComponent(Graphics var1) {
      Rectangle var2 = var1.getClipBounds();
      Color var3 = new Color(1.0F, 1.0F, 1.0F, 0.65F);
      var1.setColor(var3);
      var1.fillRect(var2.x, var2.y, var2.width, var2.height);
      FontMetrics var4 = var1.getFontMetrics();
      int var5 = var4.stringWidth(this.message.getMaxMessage());
      int var6 = var4.getHeight();
      int var7 = (this.getWidth() - var5) / 2;
      int var8 = (this.getHeight() - var6) / 2;
      var1.setColor(this.getForeground());
      var1.drawString(this.message.getText(), var7, var8);
   }
}
