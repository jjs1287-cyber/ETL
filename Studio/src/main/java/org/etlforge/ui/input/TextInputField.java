package org.etlforge.ui.input;

import com.google.common.base.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextComponent;
import org.etlforge.ui.widget.text.KongaTextField;

public class TextInputField extends InputField {
   private TextInputFieldEditSender undoableEditSender;
   private final DocumentListener docListener = this.createDocumentListener();
   private int changeEventDelay;
   private Timer changeEventTimer;
   private boolean autoTrim;

   public TextInputField(String var1, JTextComponent var2) {
      this(var1, var2, (JLabel)null);
   }

   public TextInputField(String var1, JTextComponent var2, String var3) {
      this(var1, var2, toJLabel(var3));
   }

   private static JLabel toJLabel(String var0) {
      return var0 == null ? null : KongaLabel.withMnemonic(var0);
   }

   public TextInputField(String var1, JTextComponent var2, JLabel var3) {
      super(var1, var2, var3);
      var2.getDocument().addDocumentListener(this.docListener);
      this.undoableEditSender = new TextInputFieldEditSender(this);
   }

   public final Document getDocument() {
      return this.getInputComponent().getDocument();
   }

   public final void setDocument(Document var1) {
      if (var1 != this.getDocument()) {
         this.undoableEditSender.dispose();
         this.getDocument().removeDocumentListener(this.docListener);
         var1.addDocumentListener(this.docListener);
         this.getInputComponent().setDocument(var1);
         this.undoableEditSender = new TextInputFieldEditSender(this);
      }
   }

   public final void setChangeEventDelay(int var1) {
      this.changeEventDelay = var1;
   }

   public final void setTextWhenEmpty(String var1) {
      JTextComponent var2 = this.getInputComponent();
      if (var2 instanceof KongaTextField) {
         ((KongaTextField)var2).setTextWhenEmpty(var1);
      }
   }

   public final void addDocumentListener(DocumentListener var1) {
      this.getDocument().addDocumentListener(var1);
   }

   public final void removeDocumentListener(DocumentListener var1) {
      this.getDocument().removeDocumentListener(var1);
   }

   protected DocumentContentChangeListener createDocumentListener() {
      return new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            if (TextInputField.this.changeEventDelay <= 0) {
               TextInputField.this.fireChange();
            } else {
               TextInputField.this.fireDelayedChange();
            }
         }
      };
   }

   @Override
   public void dispose() {
      this.removeDocumentListener(this.docListener);
      this.undoableEditSender.dispose();
   }

   public JTextComponent getInputComponent() {
      return (JTextComponent)super.getInputComponent();
   }

   public String getValue() {
      String var1 = this.getInputComponent().getText();
      return this.autoTrim ? var1.trim() : var1;
   }

   public final void setValue(String var1) {
      this.getInputComponent().setText(var1);
      this.getInputComponent().setCaretPosition(0);
   }

   public final void setValue(int var1) {
      this.setValue(String.valueOf(var1));
   }

   public final void setValue(long var1) {
      this.setValue(String.valueOf(var1));
   }

   public final void setValue(Object var1) {
      this.setValue(var1 != null ? var1.toString() : "");
   }

   @Override
   public final void makeReadOnly() {
      this.setEditable(false);
   }

   public final void setAutoTrim(boolean var1) {
      this.autoTrim = var1;
   }

   public final boolean isAutoTrim() {
      return this.autoTrim;
   }

   public final void setEditable(boolean var1) {
      this.getInputComponent().setEditable(var1);
   }

   public final boolean isEditable() {
      return this.getInputComponent().isEditable();
   }

   public final boolean isEmpty() {
      return this.getDocument().getLength() == 0;
   }

   public final void setValueSilently(final String var1) {
      this.updateSilently(new Runnable() {
         @Override
         public void run() {
            TextInputField.this.setValue(var1);
         }
      });
   }

   public final void setSelectAllWhenFocused(boolean var1) {
      JTextComponent var2 = this.getInputComponent();
      if (var2 instanceof KongaTextComponent) {
         ((KongaTextComponent)var2).setSelectAllWhenFocused(var1);
      }
   }

   public final Supplier<String> asStringSupplier() {
      return new Supplier<String>() {
         public String get() {
            return TextInputField.this.getValue();
         }
      };
   }

   private void fireChange() {
      if (this.isSendingInputChangeEvents()) {
         this.fireInputChangeEvent();
      }
   }

   private void fireDelayedChange() {
      assert this.changeEventDelay > 0 : "fireDelayedChange requires positive delay";
      if (this.isSendingInputChangeEvents()) {
         if (this.changeEventTimer != null) {
            this.changeEventTimer.stop();
         }

         this.changeEventTimer = new Timer(this.changeEventDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               TextInputField.this.fireChange();
            }
         });
         this.changeEventTimer.setRepeats(false);
         this.changeEventTimer.start();
      }
   }
}
