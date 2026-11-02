package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.Flow;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class XmlStructureOptionsPage extends AbstractWizardPage implements XmlStructureWizardPage {
   private final JCheckBox validationChoice = new KongaCheckBox("&Validate XML instances against the selected XSD or DTD", true);
   private final XmlStructureOptionsPage.SchemaLocationOption schemaLocation;
   private final XmlParserSelector parserSelector;

   public XmlStructureOptionsPage(Wizard var1, SourceTarget var2, String var3) {
      super(var1, var3);
      if (var2 == SourceTarget.Source) {
         this.parserSelector = new XmlParserSelector();
         this.schemaLocation = null;
      } else {
         this.parserSelector = null;
         this.schemaLocation = new XmlStructureOptionsPage.SchemaLocationOption();
      }

      this.setCanContinue(true);
   }

   public XmlStructureOptionsPage(Wizard var1, XmlStructure var2, SourceTarget var3) {
      this(var1, var3, PackageResources.OptionsPage.DEFAULT_SUB_TITLE);
      if (var2 != null) {
         this.validationChoice.setSelected(var2.isValidationEnabled());
         this.updateSchemaLocationField(var2);
         this.updateParserSelector(var2);
      }
   }

   private void updateSchemaLocationField(XmlStructure var1) {
      if (this.schemaLocation != null) {
         String var2 = var1.getFilePath();
         this.setStructureFile(var2);
         this.schemaLocation.setLocation(var1.getSchemaLocation());
      }
   }

   private void updateParserSelector(XmlStructure var1) {
      if (this.parserSelector != null) {
         this.parserSelector.setInitialState(var1);
      }
   }

   @Override
   public void applyTo(XmlStructure var1) {
      var1.setValidationEnabled(this.validationChoice.isSelected());
      if (this.schemaLocation != null) {
         this.schemaLocation.applyTo(var1);
      }

      if (this.parserSelector != null) {
         this.parserSelector.applyTo(var1);
      }
   }

   @Override
   public void requestFocus() {
      this.validationChoice.requestFocusInWindow();
   }

   public void setStructureFile(String var1) {
      if (this.schemaLocation != null) {
         this.updateSchemaLocationField(var1);
      }
   }

   private void updateSchemaLocationField(String var1) {
      boolean var2 = true;
      this.schemaLocation.setEnabled(var2);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(PackageResources.OptionsPage.MAIN_CAPTION, "");
      var1.add(this.validationChoice, true, false);
      this.layoutSchemaLocationField(var1);
      this.layoutAdvancedOptions(var1);
      var1.add(Box.createVerticalGlue(), false, true);
      return var1.asCenterBorderLayout();
   }

   private void layoutSchemaLocationField(WizardPageLayout var1) {
      if (this.schemaLocation != null) {
         this.schemaLocation.layoutIn(var1);
      }
   }

   private void layoutAdvancedOptions(WizardPageLayout var1) {
      if (this.parserSelector != null) {
         JPanel var2 = InvisiblePanel.of(this.parserSelector);
         var2.setBorder(Empty.border(4, 16, 0, 0));
         DropDownPanel var3 = new DropDownPanel(var2, "Advanced &Options", false);
         var3.setFocusWhenExpanded(this.parserSelector);
         var1.add(var3.getDisplayer(), true, false);
      }
   }

   private static class SchemaLocationOption {
      private final JLabel caption = new JLabel("(Optional) Generate an xsi:schemaLocation attribute in the target XML");
      private final TextInputField field = this.createField();

      public SchemaLocationOption() {
      }

      public void setEnabled(boolean var1) {
         this.caption.setEnabled(var1);
         this.field.setEnabled(var1);
         this.setToolTipText(var1 ? null : "This option is only available for XSD based XML files.");
      }

      public void setLocation(String var1) {
         this.field.setValue(var1);
      }

      public void applyTo(XmlStructure var1) {
         var1.setSchemaLocation(this.getLocation());
      }

      private String getLocation() {
         if (this.field.isEnabled()) {
            String var1 = this.field.getValue().trim();
            if (!var1.isEmpty()) {
               return var1;
            }
         }

         return null;
      }

      public void layoutIn(WizardPageLayout var1) {
         Flow var2 = Flow.invisible();
         var2.add(this.field.getLabel(), this.field.getInputComponent());
         OneColumnPanel var3 = new OneColumnPanel();
         var3.withInsets(0, 0, 5, 0);
         var3.setWeightX(1.0);
         var3.add(this.caption).add(var2);
         var1.add(var3, false, false);
      }

      private void setToolTipText(String var1) {
         this.field.getInputComponent().setToolTipText(var1);
         this.field.getLabel().setToolTipText(var1);
         this.caption.setToolTipText(var1);
      }

      private TextInputField createField() {
         KongaLabel var1 = new KongaLabel(ApplicationResources.MNEMONIC_FACTORY.fromString("&Schema Location: "));
         return new TextInputField("schemaLocation", new KongaTextField(40), var1);
      }
   }
}
