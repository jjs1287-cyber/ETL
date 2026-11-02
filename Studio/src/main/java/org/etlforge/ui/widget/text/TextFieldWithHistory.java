package org.etlforge.ui.widget.text;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.StringValueHistoryCompletionContext;
import org.etlforge.ui.widget.list.ListFactory;

public class TextFieldWithHistory implements UiProvider {
   private final JTextField field;
   private final JButton button;
   private final StringValueHistoryCompletionContext context;
   private final DropDownAutoCompleter autoCompleter;

   public TextFieldWithHistory(StringValueHistory var1) {
      this(new KongaTextField(), var1);
   }

   public TextFieldWithHistory(JTextField var1, StringValueHistory var2) {
      if (var1 == null) {
         throw new NullPointerException("field cannot be null");
      }

      if (var2 == null) {
         throw new NullPointerException("history cannot be null");
      }

      this.field = var1;
      this.button = new JButton(new TextFieldWithHistory.ShowMatchingHistoryAction());
      this.context = new StringValueHistoryCompletionContext(var2);
      this.autoCompleter = new DropDownAutoCompleter(this.context);
      this.autoCompleter.install(var1);
   }

   public void refresh() {
      this.context.refresh();
   }

   public void setListFactory(ListFactory var1) {
      this.autoCompleter.setListFactory(var1);
   }

   public void setMaxWidth(int var1) {
      this.autoCompleter.setMaxWidth(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JPanel var2 = new JPanel(new TextFieldWithHistory.Layout());
      var2.add(this.field);
      var2.add(this.button);
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   public void setText(String var1) {
      this.autoCompleter.setEnabled(false);

      try {
         this.field.setText(var1);
      } finally {
         this.autoCompleter.setEnabled(true);
      }
   }

   public String getText() {
      return this.field.getText();
   }

   private static class Layout implements LayoutManager {
      private static final int ICON_GAP = 1;

      private Layout() {
      }

      @Override
      public void layoutContainer(Container var1) {
         synchronized (var1.getTreeLock()) {
            JTextField var3 = (JTextField)var1.getComponent(0);
            JButton var4 = (JButton)var1.getComponent(1);
            Insets var5 = var1.getInsets();
            Icon var6 = var4.getIcon();
            int var7 = var6.getIconWidth();
            int var8 = var6.getIconHeight();
            int var9 = var1.getWidth() - var5.left - var5.right;
            int var10 = var5.left;
            int var11 = var5.top;
            int var12 = var9 - var7 - 1;
            int var13 = Math.max(var8, var3.getPreferredSize().height);
            var3.setBounds(var10, var11, var12, var13);
            var10 += var12 + 1;
            var4.setBounds(var10, var11, var7, var13);
         }
      }

      @Override
      public Dimension minimumLayoutSize(Container var1) {
         synchronized (var1.getTreeLock()) {
            Dimension var3 = this.getIconSize(var1);
            Dimension var4 = ((JComponent)var1.getComponent(0)).getMinimumSize();
            Insets var5 = var1.getInsets();
            int var6 = var4.width + 1 + var3.width + var5.left + var5.right;
            int var7 = Math.max(var3.height, var4.height) + var5.top + var5.bottom;
            return new Dimension(var6, var7);
         }
      }

      @Override
      public Dimension preferredLayoutSize(Container var1) {
         synchronized (var1.getTreeLock()) {
            Dimension var3 = this.getIconSize(var1);
            Dimension var4 = ((JComponent)var1.getComponent(0)).getPreferredSize();
            Insets var5 = var1.getInsets();
            int var6 = var4.width + 1 + var3.width + var5.left + var5.right;
            int var7 = Math.max(var3.height, var4.height) + var5.top + var5.bottom;
            return new Dimension(var6, var7);
         }
      }

      @Override
      public void addLayoutComponent(String var1, Component var2) {
      }

      @Override
      public void removeLayoutComponent(Component var1) {
      }

      private Dimension getIconSize(Container var1) {
         Icon var2 = ((JButton)var1.getComponent(1)).getIcon();
         int var3 = var2 != null ? var2.getIconWidth() : 0;
         int var4 = var2 != null ? var2.getIconHeight() : 0;
         return new Dimension(var3, var4);
      }
   }

   private class ShowMatchingHistoryAction extends AbstractAction {
      public ShowMatchingHistoryAction() {
         super("", UiResourceBundle.getOptionButtonArrow16());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TextFieldWithHistory.this.autoCompleter.displayValueDropDown();
      }
   }
}
