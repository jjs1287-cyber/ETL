package org.etlforge.ui.layout;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class DialogLayoutBuilder implements TwoColumnLayout, UiProvider {
   private final JPanel panel;

   public DialogLayoutBuilder() {
      this.panel = new JPanel(new DialogLayout());
   }

   public DialogLayoutBuilder(int var1, int var2) {
      this.panel = new JPanel(new DialogLayout(var1, var2));
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocusInWindow();
   }

   public JPanel container() {
      return this.panel;
   }

   public DialogLayoutBuilder setBorder(Border var1) {
      this.panel.setBorder(var1);
      return this;
   }

   public DialogLayoutBuilder addRow(String var1, String var2) {
      return this.addRow(var1, null, var2);
   }

   public DialogLayoutBuilder addRow(String var1, Icon var2, String var3) {
      JLabel var4 = new JLabel(var1, var2, 10);
      return this.addRow(var4, new JLabel(var3));
   }

   public DialogLayoutBuilder addRow(Component var1, Component var2) {
      this.panel.add(var1);
      this.panel.add(var2);
      return this;
   }

   @Override
   public TwoColumnLayout addRow(Component var1) {
      return this.addRow(new JLabel(" "), var1);
   }
}
