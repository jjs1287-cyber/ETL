package org.etlforge.ui.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.clipboard.ClipboardUtils;

public final class CopyToClipboardButton implements UiProvider {
   private final Supplier<String> textSupplier;
   private final JButton button;

   public CopyToClipboardButton(String var1) {
      this(Suppliers.ofInstance(var1));
   }

   public CopyToClipboardButton(Supplier<String> var1) {
      Preconditions.checkNotNull(var1, "textSupplier");
      this.textSupplier = var1;
      this.button = this.createButton();
   }

   public void setButtonLabel(String var1) {
      this.button.setText(var1);
   }

   public void setToolTipText(String var1) {
      this.button.setToolTipText(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.button);
   }

   @Override
   public void requestFocus() {
      this.button.requestFocusInWindow();
   }

   private JButton createButton() {
      KongaButton var1 = new KongaButton("&Copy To Clipboard");
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ClipboardUtils.copyToSystemClipboard(CopyToClipboardButton.this.textSupplier.get());
         }
      });
      return var1;
   }
}
