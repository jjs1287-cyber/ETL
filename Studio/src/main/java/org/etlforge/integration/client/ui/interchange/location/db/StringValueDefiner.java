package org.etlforge.integration.client.ui.interchange.location.db;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.label.KongaLabel;

abstract class StringValueDefiner implements DatabaseLocationPanelPart, Enablable {
   private final TextInputField field;
   private final boolean spansRow;
   private TokenListCompletionSupport autoCompleteSupport;

   protected StringValueDefiner(InputPanel var1, String var2, String var3, boolean var4, int var5, boolean var6) {
      this.spansRow = var6;
      if (var4) {
         this.field = InputFieldFactory.getTextInputField(var2, KongaLabel.withMnemonic(var3), var5, false);
         var1.addInputField(this.field);
      } else {
         TextInputField2 var7 = InputFieldFactory.getNonEmptyTextInputField(var2, KongaLabel.withMnemonic(var3), var5, false);
         var1.addInputField(var7, var7, var7);
         this.field = var7;
      }
   }

   protected final void installAutoCompletion(StringValueHistory var1) {
      Preconditions.checkNotNull(var1, "h");
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }

      this.autoCompleteSupport = new TextFieldCompletionSupport(this.field, var1);
   }

   public final void applyTo(DatabaseLocation var1) {
      this.applyValue(var1, this.field.getValue().trim());
   }

   @Override
   public final void addToLayout(DatabaseLocationPanelLayout var1) {
      JComponent var2 = InputFieldFactory.getCapsule(this.field);
      if (this.spansRow) {
         var1.addRow(var2);
      } else {
         var1.add(var2);
      }
   }

   @Override
   public final void addToLayout(DataLocationPanelLayout var1) {
      var1.add(this.field, this.spansRow);
   }

   public final void requestFocus() {
      this.field.requestFocus();
   }

   @Override
   public final void dispose() {
      this.field.dispose();
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }
   }

   @Override
   public final boolean isEnabled() {
      return this.field.isEnabled();
   }

   @Override
   public final void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
      this.field.setActive(var1);
   }

   public final boolean isEmpty() {
      return this.field.getValue().trim().isEmpty();
   }

   protected abstract void applyValue(DatabaseLocation var1, String var2);

   protected final void setValue(String var1) {
      this.field.setValue(var1);
   }

   protected final TextInputField getInputField() {
      return this.field;
   }
}
