package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Optional;
import java.util.ResourceBundle;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.structure.editor.text.DelimiterInputField;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.QualifierMode;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.text.KongaTextField;

public final class DelimiterAndQualifierPanel extends TextDocumentDefiner {
   public static final String VALUES = "DelimiterAndQualifierPanel.VALUES";
   private static final ResourceBundle RESOURCES = TextDocumentPage.getResources();
   private final DelimiterInputField delimiterField;
   private final TextInputField stringQualifierField;
   private final QualifierModePanel modePanel;
   private final JCheckBox edifactBox;

   public DelimiterAndQualifierPanel(TextDocument var1) {
      this(var1.getDelimiter(), var1.getStringQualifier(), var1.getQualifierMode(), isEdifact(var1));
      if (this.edifactBox != null) {
         this.edifactBox.setSelected(var1.isEdifact());
      }
   }

   private static Optional<Boolean> isEdifact(TextDocument var0) {
      return var0.isSupportingEdifact() ? Optional.of(var0.isEdifact()) : Optional.absent();
   }

   public DelimiterAndQualifierPanel(FieldDelimiter var1, String var2, QualifierMode var3, Optional<Boolean> var4) {
      this.delimiterField = new DelimiterInputField(var1);
      this.stringQualifierField = new DelimiterAndQualifierPanel.QualifierInputField(RESOURCES.getString("Label.StringQualifier"));
      this.stringQualifierField.setValue(var2);
      this.modePanel = new QualifierModePanel(this);
      this.modePanel.setEnabled(StringUtils.isNotEmpty(var2));
      this.modePanel.setSelectedMode(var3);
      if (var4.isPresent()) {
         this.edifactBox = new KongaCheckBox("EDIFACT", (Boolean)var4.get());
         this.addInputField(new CheckBoxInputField("EDIFACT", this.edifactBox));
      } else {
         this.edifactBox = null;
      }

      this.addInputField(this.delimiterField, this.delimiterField, this.delimiterField);
      this.addInputField(this.stringQualifierField);
      this.stringQualifierField.addInputChangeListener(new DelimiterAndQualifierPanel.QualiferModePanelEnabler());
      this.setOpaque(false);
      this.layoutComponents();
   }

   public void setEnabled(boolean var1) {
      this.delimiterField.setEnabled(var1);
      this.delimiterField.setActive(var1);
      this.stringQualifierField.setEnabled(var1);
      this.stringQualifierField.setActive(var1);
      if (this.edifactBox != null) {
         this.edifactBox.setEnabled(var1);
      }

      this.modePanel.setEnabled(var1 && !this.getQualifer().isEmpty());
      UiUtils.setEnabledRecursively(this.getUi(), var1);
   }

   public boolean isEdifact() {
      return this.edifactBox != null && this.edifactBox.isSelected();
   }

   public void setEdifact(boolean var1) {
      if (this.edifactBox == null) {
         throw new UnsupportedOperationException("EDIFACT is not supported");
      }

      this.edifactBox.setSelected(var1);
   }

   private void layoutComponents() {
      UiProvider var1 = this.getFieldsBox();
      this.wrap(var1);
   }

   private UiProvider getFieldsBox() {
      JComponent var1 = InputFieldFactory.getLabeledComponent(this.delimiterField.getInputComponent(), RESOURCES.getString("Label.Delimiter"), 0);
      JComponent var2 = InputFieldFactory.getCapsule(this.stringQualifierField, 0);
      return Horizontal.box()
         .addAllWithSpace(10, var1, var2, this.modePanel, this.edifactBox)
         .withBorder(StyleSheet.getDefaultTitleBorder(RESOURCES.getString("Caption.Delimiter")));
   }

   @Override
   public void stopEditing() {
   }

   @Override
   public void validateInput() throws InputPanelException {
      String var1 = this.delimiterField.getValue();
      if (var1.length() == 0) {
         throw new InputPanelException(RESOURCES.getString("Error.DelimiterMissing"));
      }

      super.validateInput();
   }

   @Override
   public void applyToTextDocument(TextDocument var1) {
      var1.setDelimiter(this.delimiterField.getDelimiter());
      var1.setStringQualifier(this.getQualifer());
      var1.setQualifierMode(this.modePanel.getSelectedMode());
      var1.setEdifact(this.isEdifact());
   }

   private String getQualifer() {
      return this.stringQualifierField.getValue().trim();
   }

   private final class QualiferModePanelEnabler implements InputChangeListener {
      private QualiferModePanelEnabler() {
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         DelayedJob.createAndSchedule(500, new Runnable() {
            @Override
            public void run() {
               DelimiterAndQualifierPanel.this.modePanel.setEnabled(!DelimiterAndQualifierPanel.this.getQualifer().isEmpty());
            }
         });
      }
   }

   private static final class QualifierInputField extends TextInputField2 {
      public QualifierInputField(String var1) {
         super(var1, createTextField(), new JLabel(var1));
      }

      @Override
      public boolean isInputValid(String var1) {
         return var1.length() == 0 || TextDocument.getQualifierFromString(var1) != 0;
      }

      @Override
      public String getInvalidMessage(String var1) {
         return DelimiterAndQualifierPanel.RESOURCES.getString("Error.InvalidQualifier");
      }

      private static JTextComponent createTextField() {
         return new KongaTextField(2);
      }
   }
}
