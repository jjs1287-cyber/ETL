package org.etlforge.ui.util.rename;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import org.etlforge.ui.UiResources;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public class ItemRenamer extends JTextField {
   public static final String FONT_RESOURCE_KEY = ItemRenamer.class.getName() + "#font";
   private final ItemRenamedListener listener;
   private final Object item;
   private final String oldName;
   private boolean allowEmptyNames;
   private final FocusListener focusHandler;

   public ItemRenamer(Object var1, String var2, ItemRenamedListener var3) {
      super(var3.getItemName(var1));
      this.listener = var3;
      this.item = var1;
      this.oldName = var2 != null ? var2 : var3.getItemName(var1);
      this.focusHandler = new ItemRenamer.FocusHandler();
      Font var4 = UiResources.getDefault().getFont(FONT_RESOURCE_KEY);
      if (var4 != null) {
         this.setFont(var4);
      }

      FontMetrics var5 = this.getFontMetrics(this.getFont());
      int var6 = var5.stringWidth(this.oldName) + 25;
      this.setSize(var6, this.getPreferredSize().height);
      this.setSelectionStart(0);
      this.setSelectionEnd(this.oldName.length());
      this.installKeyBindings();
      this.addFocusListener(this.focusHandler);
   }

   private void installKeyBindings() {
      KeyBindingInstaller var1 = new KeyBindingInstaller(this);
      var1.addBinding(KeyUtils.ENTER, "enter", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ItemRenamer.this.focusHandler.focusLost(null);
         }
      });
      var1.addBinding(KeyUtils.ESCAPE, "escape", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ItemRenamer.this.listener.itemRenameCancelled();
            ItemRenamer.this.removeFocusListener(ItemRenamer.this.focusHandler);
         }
      });
      var1.install();
   }

   public ItemRenamer(Object var1, ItemRenamedListener var2) {
      this(var1, null, var2);
   }

   public void setAllowEmptyNames(boolean var1) {
      this.allowEmptyNames = var1;
   }

   public void focusGained(FocusEvent var1) {
   }

   private synchronized void fireItemRenamed(String var1) {
      ItemRenamedEvent var2 = new ItemRenamedEvent(this, this.item, var1, this.oldName);
      this.listener.itemRenamed(var2);
   }

   private class FocusHandler extends FocusAdapter {
      private FocusHandler() {
      }

      @Override
      public void focusLost(FocusEvent var1) {
         ItemRenamer.this.removeFocusListener(this);
         String var2 = ItemRenamer.this.getText();
         if (!ItemRenamer.this.allowEmptyNames && var2.length() == 0) {
            ItemRenamer.this.removeFocusListener(this);
            ItemRenamer.this.listener.itemRenameCancelled();
         } else {
            if (!var2.equals(ItemRenamer.this.oldName)) {
               ItemRenamer.this.fireItemRenamed(var2);
            } else {
               ItemRenamer.this.listener.itemRenameCancelled();
            }
         }
      }
   }
}
