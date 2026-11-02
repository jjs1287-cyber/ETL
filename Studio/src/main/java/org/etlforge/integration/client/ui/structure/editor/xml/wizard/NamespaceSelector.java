package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.combobox.KongaComboBox;

final class NamespaceSelector implements UiProvider {
   private final KongaComboBox comboBox;

   public NamespaceSelector(String[] var1) {
      assert var1 != null && var1.length > 0;
      this.comboBox = this.createComboBox(var1);
   }

   private KongaComboBox createComboBox(String[] var1) {
      ComboBoxModel var2 = this.createComboModel(var1);
      KongaComboBox var3 = new KongaComboBox(var2);
      var3.setRenderer(new DefaultListDecorRenderer());
      var3.setSelectedIndex(-1);
      var3.setLabel("Select the &namespace:");
      return var3;
   }

   private ComboBoxModel createComboModel(String[] var1) {
      Arrays.sort(var1, String.CASE_INSENSITIVE_ORDER);
      DefaultComboBoxModel var2 = new DefaultComboBoxModel();

      for (String var6 : var1) {
         var2.addElement(new NamespaceSelector.ComboItem(var6));
      }

      return var2;
   }

   public String getSelectedNamespaceUri() {
      Object var1 = this.comboBox.getSelectedItem();
      return var1 instanceof NamespaceSelector.ComboItem ? ((NamespaceSelector.ComboItem)var1).uri : null;
   }

   public void setSelectedNamespaceUri(String var1) {
      assert EventQueue.isDispatchThread() : "setSelectedNamespaceUri must run on the EDT";
      this.comboBox.setSelectedItem(new NamespaceSelector.ComboItem(var1));
   }

   public void addActionListener(ActionListener var1) {
      this.comboBox.addActionListener(var1);
   }

   public void setInitialSelection() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NamespaceSelector.this.comboBox.setSelectedIndex(0);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.comboBox.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
   }

   public void setActive(boolean var1) {
      this.comboBox.setEnabled(var1);
   }

   private static final class ComboItem implements ListDecorator {
      private final String uri;
      private final String display;

      public ComboItem(String var1) {
         assert var1 != null;
         this.uri = var1;
         this.display = getDisplayText(var1);
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.display);
         var1.setToolTip(this.uri);
      }

      private static String getDisplayText(String var0) {
         return var0 != null && var0.length() > 0 ? var0 : "[empty namespace]";
      }

      @Override
      public boolean equals(Object var1) {
         return var1 instanceof NamespaceSelector.ComboItem && this.uri.equals(((NamespaceSelector.ComboItem)var1).uri);
      }

      @Override
      public int hashCode() {
         return this.uri.hashCode();
      }
   }
}
