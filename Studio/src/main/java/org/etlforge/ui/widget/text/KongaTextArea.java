package org.etlforge.ui.widget.text;

import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;

public class KongaTextArea extends JTextArea implements KongaTextComponent, KongaWidget {
   private final KongaTextComponentSupport support = new KongaTextComponentSupport(this);
   private ComponentPainter painter;
   private String textWhenEmpty;
   private FocusListener repaintTrigger;

   public static KongaTextArea viewer() {
      KongaTextArea var0 = new KongaTextArea();
      return configureViewer(var0);
   }

   public static KongaTextArea viewer(String var0) {
      KongaTextArea var1 = new KongaTextArea(var0);
      return configureViewer(var1);
   }

   public static KongaTextArea viewer(Size.Rows var0, Size.Columns var1) {
      KongaTextArea var2 = new KongaTextArea(var0, var1);
      return configureViewer(var2);
   }

   public static KongaTextArea viewer(String var0, Size.Rows var1, Size.Columns var2) {
      KongaTextArea var3 = new KongaTextArea(var0, var1.get(), var2.get());
      return configureViewer(var3);
   }

   private static KongaTextArea configureViewer(KongaTextArea var0) {
      var0.disableTabs();
      var0.setEditable(false);
      return var0;
   }

   public KongaTextArea() {
   }

   public KongaTextArea(String var1) {
      super(var1);
   }

   public KongaTextArea(Size.Rows var1, Size.Columns var2) {
      this(var1.get(), var2.get());
   }

   public KongaTextArea(int var1, int var2) {
      super(var1, var2);
   }

   public KongaTextArea(String var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public KongaTextArea(Document var1) {
      super(var1);
   }

   public KongaTextArea(Document var1, String var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   @Override
   public void disableContextMenu() {
      this.support.disableContextMenu();
   }

   @Override
   public void addActionToContextMenu(Action var1) {
      this.support.addAdditionalAction(var1);
   }

   @Override
   public void addToContextMenu(JMenuItem var1) {
      this.support.addAdditionalMenuItem(var1);
   }

   @Override
   public void setSelectAllWhenFocused(boolean var1) {
      this.support.setSelectAllWhenFocused(var1);
   }

   @Override
   public boolean isEmpty() {
      return this.getDocument().getLength() == 0;
   }

   public KongaTextArea disableTabs() {
      return KongaTextComponentCustomizer.disableTabs(this);
   }

   public void scrollToRow(int var1) {
      this.support.scrollToRow(var1);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      boolean var2 = var1 != this.painter;
      this.painter = var1;
      if (var2) {
         this.repaint();
      }
   }

   @Override
   public UiProvider asUiProvider() {
      return this.support.asUiProvider();
   }

   public void setTextWhenEmpty(String var1) {
      String var2 = this.textWhenEmpty;
      this.textWhenEmpty = var1;
      if (!Objects.equal(var2, var1)) {
         if (StringUtils.isBlank(var1)) {
            this.removeFocusListener(this.repaintTrigger);
            this.repaintTrigger = null;
         } else {
            this.addFocusListener(this.repaintTrigger = new KongaTextArea.RepaintTrigger());
         }

         this.firePropertyChange("textWhenEmpty", var2, var1);
         this.repaint();
      }
   }

   public String getTextWhenEmpty() {
      return this.textWhenEmpty;
   }

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         super.paintComponent(var1);
         if (this.textWhenEmptyNeedsPainting()) {
            this.paintTextWhenEmpty(var1);
         }
      } else {
         this.applyPainter((Graphics2D)var1);
      }
   }

   private boolean textWhenEmptyNeedsPainting() {
      return this.isEnabled() && StringUtils.isNotBlank(this.textWhenEmpty) && this.getDocument().getLength() == 0 && !this.hasFocus();
   }

   private void paintTextWhenEmpty(Graphics var1) {
      try {
         FontMetrics var2 = var1.getFontMetrics();
         int var3 = var2.getAscent();
         Insets var4 = this.getInsets();
         int var5 = this.getWidth() - var4.left - var4.right;
         int var6 = var4.left + (var5 - var2.stringWidth(this.textWhenEmpty)) / 2;
         int var7 = var3 + var2.getDescent();
         int var8 = var3 + (this.getHeight() - var7) / 2;
         Color var9 = this.getForeground();
         var1.setColor(this.getDisabledTextColor());
         var1.drawString(this.textWhenEmpty, var6, var8);
         var1.setColor(var9);
      } catch (Exception var10) {
         var10.printStackTrace();
      }
   }

   private void applyPainter(Graphics2D var1) {
      assert this.painter != null;
      if (!this.painter.preRendering(this, var1)) {
         super.paintComponent(var1);
         this.painter.postRendering(this, var1);
      }
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      return this.painter != null ? this.painter.getPreferredSize(var1) : var1;
   }

   public Supplier<String> asStringSupplier() {
      return new Supplier<String>() {
         public String get() {
            return KongaTextArea.this.getText();
         }
      };
   }

   public Supplier<String> asTrimmingStringSupplier() {
      return new Supplier<String>() {
         public String get() {
            return KongaTextArea.this.getText().trim();
         }
      };
   }

   public void setLabel(String var1) {
      this.support.setLabel(var1);
   }

   public void setLabel(JLabel var1) {
      this.support.setLabel(var1);
   }

   public JLabel getLabel() {
      return this.support.getLabel();
   }

   private final class RepaintTrigger implements FocusListener {
      private RepaintTrigger() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         KongaTextArea.this.repaint();
      }

      @Override
      public void focusLost(FocusEvent var1) {
         KongaTextArea.this.repaint();
      }
   }
}
