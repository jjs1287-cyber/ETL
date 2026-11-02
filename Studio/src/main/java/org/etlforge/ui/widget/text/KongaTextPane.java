package org.etlforge.ui.widget.text;

import com.google.common.base.Supplier;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.lang.exception.ExceptionHandlers;
import org.etlforge.ui.DimensionUtils;

public class KongaTextPane extends JTextPane implements KongaTextComponent {
   private final KongaTextComponentSupport support = new KongaTextComponentSupport(this);
   private boolean wrapping;

   public KongaTextPane() {
   }

   public KongaTextPane(String var1) {
      this.setText(var1);
   }

   public KongaTextPane(StyledDocument var1) {
      super(var1);
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

   public final boolean isWrapping() {
      return this.wrapping;
   }

   public final void setWrapping(boolean var1) {
      this.wrapping = var1;
   }

   @Override
   public boolean getScrollableTracksViewportWidth() {
      return this.wrapping ? this.getSize().width < this.getParent().getSize().width : super.getScrollableTracksViewportWidth();
   }

   @Override
   public void setSize(Dimension var1) {
      if (this.wrapping) {
         DimensionUtils.ensureMinimumWidth(var1, this.getParent().getSize().width);
      }

      super.setSize(var1);
   }

   public void insertStyledText(String var1, String var2, ExceptionHandler<BadLocationException> var3) {
      StyledDocument var4 = this.getStyledDocument();

      try {
         var4.insertString(var4.getLength(), var1, var4.getStyle(var2));
      } catch (BadLocationException var6) {
         var3.caught(var6);
      }
   }

   public void insertStyledText(String var1, String var2) {
      this.insertStyledText(var1, var2, ExceptionHandlers.toStandardErr());
   }

   public KongaTextPane disableTabs() {
      return KongaTextComponentCustomizer.disableTabs(this);
   }

   public void scrollToRow(int var1) {
      this.support.scrollToRow(var1);
   }

   public Supplier<String> asStringSupplier() {
      return new Supplier<String>() {
         public String get() {
            return KongaTextPane.this.getText();
         }
      };
   }

   public Supplier<String> asTrimmingStringSupplier() {
      return new Supplier<String>() {
         public String get() {
            return KongaTextPane.this.getText().trim();
         }
      };
   }
}
