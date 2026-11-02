package org.etlforge.application.ui.window.editor.service;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;

public class ComponentNotification {
   public static final int DELAY = 500;
   private final Component component;
   private final Color background;
   private final Color foreground;
   private Timer timer;
   private boolean on;
   private final boolean savedOpaque;

   public ComponentNotification(Component var1) {
      this.component = var1;
      this.savedOpaque = var1.isOpaque();
      this.background = var1.getBackground();
      this.foreground = var1.getForeground();
      this.timer = new Timer(500, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ComponentNotification.this.switchState();
         }
      });
      this.timer.setRepeats(true);
   }

   public final void start() {
      this.timer.start();
   }

   public final void stop() {
      if (this.timer != null) {
         this.timer.stop();
         this.decorateComponent(this.foreground, this.background, false);
         this.timer = null;
      }
   }

   private void switchState() {
      if (this.timer != null && !this.isCanceled()) {
         this.on = !this.on;
         Color var1;
         Color var2;
         if (this.on) {
            var1 = Color.WHITE;
            var2 = Color.BLACK;
         } else {
            var1 = this.foreground;
            var2 = this.background;
         }

         this.decorateComponent(var1, var2, this.on);
      } else {
         this.stop();
      }
   }

   protected boolean isCanceled() {
      return false;
   }

   private void decorateComponent(Color var1, Color var2, boolean var3) {
      if (this.component instanceof JComponent) {
         boolean var4 = var3 ? true : this.savedOpaque;
         ((JComponent)this.component).setOpaque(var4);
      }

      this.component.setForeground(var1);
      this.component.setBackground(var2);
   }
}
