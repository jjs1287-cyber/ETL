package org.etlforge.ui.property;

import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public class TextBinding extends AbstractBinding<String> {
   private final JTextComponent component;
   private final TextBinding.UiListener uiListener;

   public static TextBinding bind(AbstractPropertyBasedModel var0, String var1, JTextComponent var2) {
      Property var3 = var0.getProperty(var1);
      return bind(var3, var2);
   }

   public static TextBinding bindAndSyncUi(AbstractPropertyBasedModel var0, String var1, JTextComponent var2) {
      Property var3 = var0.getProperty(var1);
      return bindAndSyncUi(var3, var2);
   }

   public static TextBinding bind(Property<String> var0, JTextComponent var1) {
      return new TextBinding(var0, var1);
   }

   public static TextBinding bindAndSyncUi(Property<String> var0, JTextComponent var1) {
      TextBinding var2 = bind(var0, var1);
      var2.syncUi();
      return var2;
   }

   public TextBinding(AbstractPropertyBasedModel var1, String var2, JTextComponent var3) {
      this(var1.getProperty(var2), var3);
   }

   public TextBinding(Property<String> var1, JTextComponent var2) {
      super(var1);
      this.component = var2;
      this.uiListener = new TextBinding.UiListener();
      var2.getDocument().addDocumentListener(this.uiListener);
   }

   @Override
   protected void disposeUiListener() {
      this.component.getDocument().removeDocumentListener(this.uiListener);
   }

   protected String getValueFromUi() {
      return this.component.getText();
   }

   protected void updateUiWithValue(String var1) {
      this.component.setText(var1);
      this.component.setCaretPosition(0);
   }

   protected final JTextComponent getTextComponent() {
      return this.component;
   }

   private class UiListener extends DocumentContentChangeListener {
      private UiListener() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         if (TextBinding.this.isUiToModelEnabled()) {
            TextBinding.this.syncModel();
         }
      }
   }
}
