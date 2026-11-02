package org.etlforge.ui.widget.label;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.BreakIterator;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.list.KongaListLabel;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeLabel;
import org.etlforge.util.clipboard.ClipboardUtils;

public class KongaLabel extends JLabel implements KongaWidget, Label {
   private ComponentPainter painter;
   private boolean multiLineEnabled;
   private String originalText;
   private KongaLabel.ButtonTrigger buttonTrigger;
   private final KongaLabel.CopyAction copyAction = new KongaLabel.CopyAction();
   private boolean antiAlias;

   public static KongaLabel withMnemonic(String var0) {
      return new KongaLabel(UiResourceBundle.MNEMONIC_FACTORY.fromString(var0));
   }

   public KongaLabel() {
      this.init();
   }

   public KongaLabel(Icon var1, int var2) {
      super(var1, var2);
      this.init();
   }

   public KongaLabel(Icon var1) {
      super(var1);
      this.init();
   }

   public KongaLabel(String var1, Icon var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   public KongaLabel(String var1, int var2) {
      super(var1, var2);
      this.init();
   }

   public KongaLabel(String var1) {
      super(var1);
      this.init();
   }

   public KongaLabel(MnemonicString var1) {
      LabelUtils.applyMnemonics(this, var1);
   }

   private void init() {
      this.antiAlias = Boolean.getBoolean("swing.aatext");
      this.copyAction.updateEnabledState();
      PopupMenuRequestHandlers.install(this, this.copyAction);
   }

   public void setAntiAliasingEnabled(boolean var1) {
      if (var1 != this.antiAlias) {
         this.antiAlias = var1;
         this.firePropertyChange("antiAlias", !var1, var1);
      }
   }

   public void setMultiLineEnabled(boolean var1) {
      this.multiLineEnabled = var1;
      this.handleMultiLines();
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
   public void setText(String var1) {
      this.originalText = var1;
      this.handleMultiLines();
      if (this.copyAction != null) {
         this.copyAction.updateEnabledState();
      }
   }

   @Override
   public void setBounds(int var1, int var2, int var3, int var4) {
      boolean var5 = var3 != this.getWidth() || var4 != this.getHeight();
      super.setBounds(var1, var2, var3, var4);
      if (this.multiLineEnabled && var5) {
         this.setMultiLineText();
      }
   }

   @Override
   public void setBackgroundColor(Color var1) {
      this.setBackground(var1);
   }

   @Override
   public void setText(MnemonicString var1) {
      LabelUtils.applyMnemonics(this, var1);
   }

   public void setMnemonicText(String var1) {
      this.setText(UiResourceBundle.MNEMONIC_FACTORY.fromString(var1));
   }

   @Override
   public void setTextColor(Color var1) {
      this.setForeground(var1);
   }

   @Override
   public void setToolTip(String var1) {
      this.setToolTipText(var1);
   }

   public void setLabelForButton(AbstractButton var1) {
      super.setLabelFor(var1);
      if (this.buttonTrigger == null) {
         if (var1 != null) {
            this.buttonTrigger = new KongaLabel.ButtonTrigger(var1);
         }
      } else {
         this.buttonTrigger.setButton(var1);
      }
   }

   public void setLabelForList(JList var1) {
      new KongaListLabel(var1, this);
   }

   public void setLabelForTree(KongaTree var1) {
      new KongaTreeLabel(var1, this);
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      return this.painter != null ? this.painter.getPreferredSize(var1) : var1;
   }

   @Override
   public void paint(Graphics var1) {
      if (this.antiAlias) {
         Graphics2D var2 = (Graphics2D)var1;
         RenderingHints var3 = var2.getRenderingHints();
         UiUtils.makeAntiAliased(var2);
         super.paint(var1);
         var2.setRenderingHints(var3);
      } else {
         super.paint(var1);
      }
   }

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         super.paintComponent(var1);
      } else {
         this.applyPainter((Graphics2D)var1);
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
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   private void handleMultiLines() {
      if (this.multiLineEnabled) {
         this.setMultiLineText();
      } else {
         super.setText(this.originalText);
      }
   }

   private void setMultiLineText() {
      if (this.originalText != null && !this.originalText.isEmpty()) {
         String var1 = this.getMultiLineText();
         super.setText(var1);
      } else {
         super.setText(this.originalText);
      }
   }

   private int getMultiLineWidth() {
      int var1 = this.getWidth();
      Insets var2 = this.getInsets();
      var1 -= var2.left + var2.right;
      Border var3 = this.getBorder();
      if (var3 != null) {
         var2 = var3.getBorderInsets(this);
         var1 -= var2.left + var2.right;
      }

      return var1;
   }

   private String getMultiLineText() {
      assert this.multiLineEnabled;
      int var1 = this.getMultiLineWidth();
      FontMetrics var2 = this.getFontMetrics(this.getFont());
      ArrayList var3 = new ArrayList();
      StringBuilder var4 = new StringBuilder();
      var3.add(var4);
      BreakIterator var5 = BreakIterator.getWordInstance();
      var5.setText(this.originalText);
      int var6 = var5.first();

      int var7;
      while ((var7 = var5.next()) != -1) {
         String var8 = this.originalText.substring(var6, var7);
         if (var2.stringWidth(var4.toString() + var8) > var1) {
            var4 = new StringBuilder();
            var3.add(var4);
            if (var8.trim().length() > 0) {
               var4.append(var8);
            }
         } else {
            var4.append(var8);
         }

         var6 = var7;
      }

      StringBuilder var12 = new StringBuilder("<html>");
      boolean var9 = true;

      for (Object __etlItem273 : var3) {
         StringBuilder var11 = (StringBuilder) __etlItem273;
         if (var11.length() > 0) {
            if (var9) {
               var9 = false;
            } else {
               var12.append("<br>");
            }

            var12.append(var11);
         }
      }

      return var12.append("</html>").toString();
   }

   public KongaLabel withBorder(Border var1) {
      this.setBorder(var1);
      return this;
   }

   public KongaLabel makeBold() {
      Fonts.convertToBold(this);
      return this;
   }

   private class ButtonTrigger {
      private AbstractButton button;

      public ButtonTrigger(AbstractButton var2) {
         this.button = var2;
         this.installKeyTrigger();
         this.installMouseTrigger();
      }

      public void setButton(AbstractButton var1) {
         this.button = var1;
      }

      private void installKeyTrigger() {
         if (KongaLabel.this.getDisplayedMnemonic() > 0) {
            InputMap var1 = KongaLabel.this.getInputMap(2);
            KeyStroke var2 = KeyStroke.getKeyStroke(KongaLabel.this.getDisplayedMnemonic(), 8, false);
            Object var3 = var1.get(var2);
            if (var3 != null) {
               ActionMap var4 = KongaLabel.this.getActionMap();
               var4.put(var3, new AbstractAction() {
                  @Override
                  public void actionPerformed(ActionEvent var1) {
                     ButtonTrigger.this.click();
                  }
               });
            }
         }
      }

      private void installMouseTrigger() {
         KongaLabel.this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent var1) {
               ButtonTrigger.this.click();
            }

            @Override
            public void mouseEntered(MouseEvent var1) {
               this.redispatch(var1);
            }

            @Override
            public void mouseExited(MouseEvent var1) {
               this.redispatch(var1);
            }

            private void redispatch(MouseEvent var1) {
               var1.setSource(ButtonTrigger.this.button);
               ButtonTrigger.this.button.dispatchEvent(var1);
            }
         });
      }

      private void click() {
         if (this.button != null) {
            this.button.requestFocusInWindow();
            this.button.doClick();
         }
      }
   }

   private class CopyAction extends AbstractAction {
      public CopyAction() {
         super(UiResourceBundle.getString("Copy"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ClipboardUtils.copyToSystemClipboard(KongaLabel.this.originalText);
      }

      public void updateEnabledState() {
         String var1 = KongaLabel.this.getText();
         KongaLabel.this.copyAction.setEnabled(var1 != null && var1.length() > 0);
      }
   }
}
