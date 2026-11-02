package org.etlforge.ui.widget.text;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.Empty;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;

public final class LineNumberBar {
   private static final int PREFERRED_ICON_WIDTH = 8;
   private static final int MARGIN = 8;
   private static final int ICON_MARGIN = 2;
   private static final Color BACKGROUND = new Color(244, 244, 244);
   private final JTextComponent tc;
   private final JScrollPane scroll;
   private final JComponent bar;
   private final LineNumberBar.Listener listener;
   private int digits;
   private int width;
   private final List<LineMarker> markers;
   private ListMultimap<Integer, LineMarker> markersPerRow;

   public static LineNumberBar install(JTextComponent var0, JScrollPane var1) {
      LineNumberBar var2 = new LineNumberBar(var0, var1);
      var1.setRowHeaderView(var2.bar);
      return var2;
   }

   private LineNumberBar(JTextComponent var1, JScrollPane var2) {
      this.tc = var1;
      this.scroll = var2;
      this.bar = new LineNumberBar.Bar();
      this.markers = new ArrayList<>();
      this.listener = this.listenToChanges();
      this.updateMarkersPerRow();
      this.updateMaxWidth(false);
   }

   public void uninstall() {
      this.scroll.setRowHeaderView(null);
      this.tc.getDocument().removeDocumentListener(this.listener);
      this.tc.removePropertyChangeListener(this.listener);
      this.markers.clear();
   }

   public void addMarker(LineMarker var1) {
      this.markers.add(var1);
      this.forceWidthUpdateAndRepaint();
   }

   private void forceWidthUpdateAndRepaint() {
      this.digits = -1;
      this.updateMarkersPerRow();
      this.updateMaxWidth(true);
      this.bar.repaint();
   }

   public void removeMarker(LineMarker var1) {
      if (this.markers.remove(var1)) {
         this.forceWidthUpdateAndRepaint();
      }
   }

   private LineNumberBar.Listener listenToChanges() {
      LineNumberBar.Listener var1 = new LineNumberBar.Listener();
      this.tc.getDocument().addDocumentListener(var1);
      this.tc.addPropertyChangeListener(var1);
      return var1;
   }

   private void updateMarkersPerRow() {
      this.markersPerRow = ArrayListMultimap.create();

      for (LineMarker var2 : this.markers) {
         int var3 = DocumentUtils.getRowNumber(this.tc.getDocument(), var2.getPosition().getOffset());
         this.markersPerRow.put(var3, var2);
      }
   }

   private void updateMaxWidth(boolean var1) {
      int var2 = DocumentUtils.getRowNumber(this.tc.getDocument(), this.tc.getDocument().getLength());
      int var3 = getDigitsForRow(var2);
      if (var3 != this.digits) {
         this.digits = var3;
         this.width = this.getWidthOfDigits(var3);
         this.width += 10;
         if (var1) {
            this.bar.revalidate();
         }
      }
   }

   private int getWidthOfDigits(int var1) {
      StringBuilder var2 = new StringBuilder();

      for (int var3 = 0; var3 < var1; var3++) {
         var2.append("8");
      }

      return this.bar.getFontMetrics(this.bar.getFont()).stringWidth(var2.toString()) + 16;
   }

   private static int getDigitsForRow(int var0) {
      return (int)Math.log10(var0) + 1;
   }

   private class Bar extends JComponent {
      public Bar() {
         this.setFont(LineNumberBar.this.tc.getFont());
         this.setOpaque(true);
      }

      @Override
      protected void paintComponent(Graphics var1) {
         Color var2 = var1.getColor();
         this.drawBackground(var1);
         Rectangle var3 = var1.getClipBounds();
         FontMetrics var4 = this.getFontMetrics(this.getFont());
         int var5 = var4.getAscent();
         int var6 = DocumentUtils.getRowNumber(LineNumberBar.this.tc.getDocument(), LineNumberBar.this.tc.getDocument().getLength());
         int var7 = this.getStartRow(var3);
         int var8 = this.getEndRow(var3, var6);

         for (int var9 = var7; var9 <= var8; var9++) {
            try {
               String var10 = String.valueOf(var9);
               int var11 = DocumentUtils.getRowStart(LineNumberBar.this.tc.getDocument(), var9);
               Rectangle var12 = LineNumberBar.this.tc.modelToView(var11);
               int var13 = this.getWidth() - 8 - var4.stringWidth(var10) - 2 - 8;
               int var14 = var12.y + var12.height - 2 - (var12.height - var5) / 2;
               var1.drawString(var10, var13, var14);
               Icon var15 = this.getMarkerIconForRow(var9);
               var13 = this.getWidth() - var15.getIconWidth() - 2;
               var14 -= var15.getIconHeight();
               var15.paintIcon(this, var1, var13, var14);
            } catch (BadLocationException var16) {
               var16.printStackTrace();
            }
         }

         var1.setColor(var2);
      }

      private Icon getMarkerIconForRow(int var1) {
         Iterator var2 = LineNumberBar.this.markersPerRow.get(var1).iterator();
         if (var2.hasNext()) {
            LineMarker var3 = (LineMarker)var2.next();
            return var3.getIcon();
         } else {
            return Empty.icon(0, 0);
         }
      }

      @Override
      public String getToolTipText(MouseEvent var1) {
         return super.getToolTipText(var1);
      }

      private void drawBackground(Graphics var1) {
         var1.setColor(LineNumberBar.BACKGROUND);
         var1.fillRect(0, 0, this.getWidth(), this.getHeight());
         var1.setColor(Color.BLACK);
         var1.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight());
      }

      private int getEndRow(Rectangle var1, int var2) {
         int var3 = LineNumberBar.this.tc.viewToModel(new Point(0, var1.y + var1.height));
         return Math.min(DocumentUtils.getRowNumber(LineNumberBar.this.tc.getDocument(), var3) + 1, var2);
      }

      private int getStartRow(Rectangle var1) {
         int var2 = LineNumberBar.this.tc.viewToModel(new Point(0, var1.y));
         return Math.max(DocumentUtils.getRowNumber(LineNumberBar.this.tc.getDocument(), var2) - 1, 1);
      }

      @Override
      public Dimension getPreferredSize() {
         Dimension var1 = LineNumberBar.this.tc.getPreferredSize();
         return new Dimension(LineNumberBar.this.width, var1.height);
      }
   }

   private class Listener extends DocumentContentChangeListener implements PropertyChangeListener {
      private Listener() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         this.removeDeadMarkers();
         LineNumberBar.this.updateMarkersPerRow();
         LineNumberBar.this.updateMaxWidth(true);
         LineNumberBar.this.bar.repaint();
      }

      private void removeDeadMarkers() {
         Iterator var1 = LineNumberBar.this.markers.iterator();

         while (var1.hasNext()) {
            if (!((LineMarker)var1.next()).isAlive()) {
               var1.remove();
            }
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("font".equals(var2)) {
            LineNumberBar.this.bar.setFont(LineNumberBar.this.tc.getFont());
            LineNumberBar.this.updateMaxWidth(true);
            LineNumberBar.this.bar.repaint();
         } else if ("document".equals(var2)) {
            ((Document)var1.getOldValue()).removeDocumentListener(this);
            ((Document)var1.getNewValue()).addDocumentListener(this);
            LineNumberBar.this.updateMarkersPerRow();
            LineNumberBar.this.updateMaxWidth(true);
            LineNumberBar.this.bar.repaint();
         }
      }
   }
}
