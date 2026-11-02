package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ComponentGlassPane {
   private final JComponent component;
   private final ComponentGlassPane.GlassPaneImpl glassPane;
   private RootPaneContainer owner;
   private Component previousGlassPane;
   private boolean hideOnClickOutside;
   private boolean hideOnEscape;
   private ComponentGlassPane.MoveHandler moveHandler;
   private boolean fadeIn;
   private boolean fadeOut;
   private Component focusWhenDisposed;

   public ComponentGlassPane(UiProvider var1) {
      this(InvisiblePanel.of(var1));
   }

   public ComponentGlassPane(JComponent var1) {
      this.component = var1;
      this.glassPane = new ComponentGlassPane.GlassPaneImpl();
      this.hideOnClickOutside = true;
      this.hideOnEscape = false;
      if (var1.getSize().equals(new Dimension())) {
         var1.setSize(var1.getPreferredSize());
      }
   }

   public void setBounds(Rectangle var1) {
      this.component.setBounds(var1);
   }

   public void setLocation(Point var1) {
      this.component.setLocation(var1);
      this.ensureComponentInsideBounds();
      this.glassPane.repaint();
   }

   public void setLocationRelativeTo(Component var1, Point var2) {
      Point var3 = SwingUtilities.convertPoint(var1, var2, this.glassPane);
      this.setLocation(var3);
   }

   public void setFocusWhenDisposed(Component var1) {
      this.focusWhenDisposed = var1;
   }

   public void setHideOnClickOutside(boolean var1) {
      this.hideOnClickOutside = var1;
   }

   public void setHideOnEscape(boolean var1) {
      this.hideOnEscape = var1;
   }

   public void setFadeIn(boolean var1) {
      Preconditions.checkState(this.owner == null, "setFadeIn must be called before this glasspane is installed");
      this.fadeIn = var1;
      if (var1) {
         this.glassPane.alpha = 0.0F;
      }
   }

   public void setFadeOut(boolean var1) {
      this.fadeOut = var1;
   }

   public void setMoveable(boolean var1) {
      if (var1) {
         if (this.moveHandler == null) {
            this.moveHandler = new ComponentGlassPane.MoveHandler();
            this.glassPane.addMouseListener(this.moveHandler);
            this.glassPane.addMouseMotionListener(this.moveHandler);
         }
      } else if (this.moveHandler != null) {
         this.glassPane.removeMouseListener(this.moveHandler);
         this.glassPane.removeMouseMotionListener(this.moveHandler);
         this.moveHandler = null;
      }
   }

   public void install(RootPaneContainer var1) {
      if (var1 != this.owner) {
         Preconditions.checkState(this.owner == null, "Already installed");
         this.previousGlassPane = var1.getGlassPane();
         var1.setGlassPane(this.glassPane);
         this.owner = var1;
         if (this.fadeIn) {
            this.fadeIn();
         }

         this.glassPane.setVisible(true);
      }
   }

   public void dispose() {
      if (this.owner != null && this.owner.getGlassPane() == this.glassPane) {
         if (this.fadeOut) {
            this.fadeOut();
         } else {
            this.removeGlassPane();
         }
      }
   }

   private void removeGlassPane() {
      this.owner.getGlassPane().setVisible(false);
      this.owner.setGlassPane(this.previousGlassPane);
      this.owner = null;
      this.checkPostDisposeFocus();
   }

   private void checkPostDisposeFocus() {
      if (this.focusWhenDisposed != null) {
         DelayedJob.createAndSchedule(100, new Runnable() {
            @Override
            public void run() {
               ComponentGlassPane.this.focusWhenDisposed.requestFocusInWindow();
            }
         });
      }
   }

   private void fadeIn() {
      this.animate(new TimingTargetAdapter() {
         @Override
         public void timingEvent(float var1) {
            ComponentGlassPane.this.glassPane.alpha = var1;
            ComponentGlassPane.this.glassPane.repaint();
         }
      });
   }

   private void fadeOut() {
      this.animate(new TimingTargetAdapter() {
         @Override
         public void timingEvent(float var1) {
            ComponentGlassPane.this.glassPane.alpha = 1.0F - var1;
            ComponentGlassPane.this.glassPane.repaint();
         }

         @Override
         public void end() {
            ComponentGlassPane.this.removeGlassPane();
         }
      });
   }

   private void animate(TimingTarget var1) {
      Animator var2 = new Animator(250, var1);
      var2.setAcceleration(0.3F);
      var2.setDeceleration(0.2F);
      var2.start();
   }

   private void ensureComponentInsideBounds() {
      Window var1 = SwingUtilities.getWindowAncestor(this.component);
      if (var1 != null) {
         Point var2 = this.component.getLocation();
         byte var3 = 20;
         int var4 = var2.x;
         int var5 = var2.y;
         if (var1.getWidth() >= this.component.getWidth() && var4 >= var3) {
            int var6 = var4 + this.component.getWidth() + var3 - var1.getWidth();
            if (var6 > 0) {
               var4 -= var6;
            }
         } else {
            var4 = var3;
         }

         if (var1.getHeight() >= this.component.getHeight() && var5 >= var3) {
            int var7 = var5 + this.component.getHeight() + var3 - var1.getHeight();
            if (var7 > 0) {
               var5 -= var7;
            }
         } else {
            var5 = var3;
         }

         this.component.setLocation(new Point(var4, var5));
      }
   }

   private class GlassPaneImpl extends JComponent {
      private float alpha = -1.0F;

      public GlassPaneImpl() {
         this.add(ComponentGlassPane.this.component);
         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent var1) {
               if (ComponentGlassPane.this.hideOnClickOutside && !ComponentGlassPane.this.component.getBounds().contains(var1.getPoint())) {
                  ComponentGlassPane.this.dispose();
               }
            }
         });
         this.getInputMap(2).put(KeyUtils.ESCAPE, "escape");
         this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               if (ComponentGlassPane.this.hideOnEscape) {
                  ComponentGlassPane.this.dispose();
               }
            }
         });
      }

      @Override
      public void paint(Graphics var1) {
         if (this.isTransparent()) {
            this.paintTransparent(var1);
         } else {
            super.paint(var1);
         }
      }

      private boolean isTransparent() {
         return this.alpha >= 0.0F && this.alpha < 1.0F;
      }

      private void paintTransparent(Graphics var1) {
         Graphics2D var2 = (Graphics2D)var1.create();

         try {
            var2.setComposite(AlphaComposite.getInstance(3, this.alpha));
            super.paint(var2);
         } finally {
            var2.dispose();
         }
      }
   }

   private class MoveHandler extends MouseAdapter {
      private Point lastPoint;

      private MoveHandler() {
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         Point var2 = var1.getPoint();
         if (ComponentGlassPane.this.component.getBounds().contains(var2)) {
            this.lastPoint = var2;
         }
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         if (this.lastPoint != null) {
            Point var2 = var1.getPoint();
            int var3 = var2.x - this.lastPoint.x;
            int var4 = var2.y - this.lastPoint.y;
            Point var5 = ComponentGlassPane.this.component.getLocation();
            var5.x += var3;
            var5.y += var4;
            ComponentGlassPane.this.component.setLocation(var5);
            this.lastPoint = var2;
         }
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         this.lastPoint = null;
      }
   }
}
