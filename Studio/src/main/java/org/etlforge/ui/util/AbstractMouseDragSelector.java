package org.etlforge.ui.util;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public abstract class AbstractMouseDragSelector extends MouseAdapter implements MouseMotionListener {
   private final Container parent;
   private final JPanel draggedArea;
   private boolean controlDown;
   private boolean shiftDown;
   private Point startPoint;
   private Point currentPoint;
   private boolean dragging;
   private boolean enabled;
   private boolean initialized;

   public AbstractMouseDragSelector(Container var1) {
      Preconditions.checkNotNull(var1, "parent");
      this.parent = var1;
      this.draggedArea = new JPanel();
      this.draggedArea.setBorder(BorderFactory.createLineBorder(new Color(49, 106, 197)));
      this.draggedArea.setBackground(new Color(103, 108, 243, 50));
      this.dragging = false;
      this.setEnabled(true);
   }

   public final void setEnabled(boolean var1) {
      if (var1 != this.enabled) {
         if (var1) {
            this.parent.addMouseListener(this);
            this.parent.addMouseMotionListener(this);
         } else {
            this.parent.removeMouseListener(this);
            this.parent.removeMouseMotionListener(this);
         }

         this.enabled = var1;
      }
   }

   public final boolean isControlDown() {
      return this.controlDown;
   }

   public final boolean isShiftDown() {
      return this.shiftDown;
   }

   public final Point getStartPoint() {
      return this.startPoint;
   }

   public final boolean isDragging() {
      return this.dragging;
   }

   public final Rectangle getDraggingArea() {
      return this.draggedArea.getBounds();
   }

   public final void setDraggingAreaBackground(Color var1) {
      this.draggedArea.setBackground(var1);
   }

   public final void setDraggingAreaBorder(Border var1) {
      this.draggedArea.setBorder(var1);
   }

   public final Point getCurrentPoint() {
      return this.currentPoint;
   }

   protected void onMousePressed() {
   }

   protected abstract void onInitialize();

   protected abstract boolean allowDragging();

   protected abstract void onDragging();

   protected abstract void onStopDrag();

   @Override
   public final void mousePressed(MouseEvent var1) {
      this.startPoint = new Point(var1.getX(), var1.getY());
      this.currentPoint = this.startPoint;
      this.controlDown = var1.isControlDown();
      this.shiftDown = var1.isShiftDown();
      this.initialized = false;
      this.onMousePressed();
   }

   @Override
   public final void mouseClicked(MouseEvent var1) {
      this.currentPoint = new Point(var1.getX(), var1.getY());
      if (var1.getClickCount() > 1) {
         this.dragging = false;
      }
   }

   @Override
   public final void mouseReleased(MouseEvent var1) {
      this.currentPoint = new Point(var1.getX(), var1.getY());
      if (this.dragging) {
         this.parent.remove(this.draggedArea);
         this.draggedArea.setVisible(false);
         this.parent.repaint();
         this.dragging = false;
         this.onStopDrag();
         this.initialized = false;
      }
   }

   @Override
   public final void mouseDragged(MouseEvent var1) {
      int var2 = var1.getX();
      int var3 = var1.getY();
      this.currentPoint = new Point(var2, var3);
      if (!this.dragging) {
         if (!this.allowDragging()) {
            return;
         }

         if (!this.initialized) {
            this.onInitialize();
            this.initialized = true;
         }

         this.draggedArea.setVisible(true);
         this.parent.add(this.draggedArea);
         this.dragging = true;
      }

      int var4 = this.startPoint.x;
      int var5 = this.startPoint.y;
      this.draggedArea.setBounds(Math.min(var2, var4), Math.min(var3, var5), Math.abs(var4 - var2), Math.abs(var5 - var3));
      this.parent.repaint();
      this.onDragging();
   }

   @Override
   public final void mouseMoved(MouseEvent var1) {
   }
}
