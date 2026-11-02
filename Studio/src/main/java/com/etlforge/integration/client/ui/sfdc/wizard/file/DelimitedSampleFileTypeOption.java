package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Optional;
import java.awt.event.FocusListener;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.structure.editor.text.DelimiterInputField;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class DelimitedSampleFileTypeOption implements SampleFileTypeOption, Focusable {
   private final boolean withHeader;
   private final DelimiterInputField delimiterField;

   public DelimitedSampleFileTypeOption(boolean var1) {
      this.withHeader = var1;
      this.delimiterField = new DelimiterInputField(FieldDelimiter.COMMA);
   }

   @Override
   public MnemonicString getText() {
      String var1 = this.withHeader ? "&Delimited file, with header" : "D&elimited file, without header";
      return UiResourceBundle.MNEMONIC_FACTORY.fromString(var1);
   }

   @Override
   public boolean isValid() {
      return this.delimiterField.isFieldValid();
   }

   @Override
   public Optional<UiProvider> getUi() {
      JPanel var1 = InvisiblePanel.of(this.delimiterField.getInputComponent());
      BoxBuilder var2 = BoxBuilder.horizontal().withEmptyBorder(0, 32, 0, 0).addAllWithSpace(5, this.delimiterField.getLabel(), var1);
      return Optional.of(var2);
   }

   @Override
   public SampleFileType getFileType() {
      FieldDelimiter var1 = this.delimiterField.getDelimiter();
      if (!var1.isValid()) {
         throw new RuntimeException("Invalid delimiter.");
      } else {
         return new DelimitedSampleFileType(var1, this.withHeader);
      }
   }

   @Override
   public void requestFocus() {
      this.delimiterField.requestFocus();
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.delimiterField.addFocusListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.delimiterField.removeFocusListener(var1);
   }

   @Override
   public boolean isGeneratedAutomatically() {
      return true;
   }
}
