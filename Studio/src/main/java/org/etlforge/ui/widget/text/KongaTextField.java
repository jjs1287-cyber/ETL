package org.etlforge.ui.widget.text;

import com.google.common.base.Objects;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;

public class KongaTextField extends JTextField implements KongaTextComponentWithIconSupport, KongaWidget {
   private final KongaTextComponentSupport support = new KongaTextComponentSupport(this);
   private boolean fixedSize;
   private String textWhenEmpty;
   private KongaTextComponentWithIconSupport.Wrapper iconWrapper;
   private ComponentPainter painter;
   private FocusListener repaintTrigger;

   public KongaTextField() {
   }

   public KongaTextField(String var1) {
      super(var1);
   }

   public KongaTextField(Size.Columns var1) {
      this(var1.get());
   }

   public KongaTextField(int var1) {
      super(var1);
   }

   public KongaTextField(String var1, int var2) {
      super(var1, var2);
   }

   public KongaTextField(Document var1, String var2, int var3) {
      super(var1, var2, var3);
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

   @Override
   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      if (this.getLabel() != null) {
         this.getLabel().setEnabled(var1);
      }
   }

   public void setFixedSize(boolean var1) {
      this.fixedSize = var1;
   }

   public boolean isFixedSize() {
      return this.fixedSize;
   }

   @Override
   public Dimension getMaximumSize() {
      return this.fixedSize ? this.getPreferredSize() : super.getMaximumSize();
   }

   @Override
   public Dimension getMinimumSize() {
      return this.fixedSize ? this.getPreferredSize() : super.getMinimumSize();
   }

   public void setTextWhenEmpty(String var1) {
      String var2 = this.textWhenEmpty;
      this.textWhenEmpty = var1;
      if (!Objects.equal(var2, var1)) {
         if (StringUtils.isBlank(var1)) {
            this.removeFocusListener(this.repaintTrigger);
            this.repaintTrigger = null;
         } else {
            this.addFocusListener(this.repaintTrigger = new KongaTextField.RepaintTrigger());
         }

         this.firePropertyChange("textWhenEmpty", var2, var1);
         this.repaint();
      }
   }

   public String getTextWhenEmpty() {
      return this.textWhenEmpty;
   }

   @Override
   public void setIcon(Icon var1) {
      if (this.iconWrapper == null) {
         this.iconWrapper = new KongaTextComponentWithIconSupport.Wrapper(this);
      }

      Icon var2 = this.iconWrapper.getIcon();
      if (this.iconWrapper.setIcon(var1)) {
         this.firePropertyChange("icon", var2, var1);
      }
   }

   @Override
   public Icon getIcon() {
      return this.iconWrapper == null ? null : this.iconWrapper.getIcon();
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

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         this.paintMe(var1);
      } else {
         this.applyPainter((Graphics2D)var1);
      }
   }

   private void paintMe(Graphics var1) {
      super.paintComponent(var1);
      if (this.textWhenEmptyNeedsPainting()) {
         this.paintTextWhenEmpty(var1);
      }
   }

   private boolean textWhenEmptyNeedsPainting() {
      return this.isEnabled() && StringUtils.isNotBlank(this.textWhenEmpty) && this.getDocument().getLength() == 0 && (!this.hasFocus() || !this.isEditable());
   }

   private void paintTextWhenEmpty(Graphics var1) {
      try {
         FontMetrics var2 = var1.getFontMetrics();
         int var3 = var2.getAscent();
         int var4 = var3 + var2.getDescent();
         int var5 = var3 + (this.getHeight() - var4) / 2;
         Color var6 = this.getForeground();
         var1.setColor(this.getDisabledTextColor());
         var1.drawString(this.textWhenEmpty, this.getInsets().left, var5);
         var1.setColor(var6);
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   private void applyPainter(Graphics2D var1) {
      assert this.painter != null;
      if (!this.painter.preRendering(this, var1)) {
         this.paintMe(var1);
         this.painter.postRendering(this, var1);
      }
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      return this.painter != null ? this.painter.getPreferredSize(var1) : var1;
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

   public void setColumns(Size.Columns var1) {
      super.setColumns(var1.get());
   }

   public void layoutWithLabel(TwoColumnLayout var1) {
      var1.addRow(this.getLabel(), this);
   }

   private final class RepaintTrigger implements FocusListener {
      private RepaintTrigger() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         KongaTextField.this.repaint();
      }

      @Override
      public void focusLost(FocusEvent var1) {
         KongaTextField.this.repaint();
      }
   }
}
