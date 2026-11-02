package org.etlforge.integration.client.ui.interchange.location.panel;

import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.data.location.FileFilterOptions;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

public class FileFilterOptionsPanel extends InputPanel {
   private final CheckBoxInputField ignoreCaseChoice;
   private final CheckBoxInputField regExChoice;

   public FileFilterOptionsPanel(FileFilterOptions var1) {
      this.ignoreCaseChoice = this.createChoice("ignoreCase", Strings.get("File.FilterOptions.IgnoreCase"), var1.isIgnoreCase());
      this.regExChoice = this.createChoice("regEx", Strings.get("File.FilterOptions.RegEx"), var1.isRegularExpression());
      this.regExChoice.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            FileFilterOptionsPanel.this.updateIgnoreCaseEnabled();
         }
      });
      this.updateIgnoreCaseEnabled();
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
      this.doLayout();
   }

   public FileFilterOptions getOptions() {
      return new FileFilterOptions(this.ignoreCaseChoice.isEnabled() && this.ignoreCaseChoice.isSelected(), this.regExChoice.isSelected());
   }

   private CheckBoxInputField createChoice(String var1, String var2, boolean var3) {
      KongaCheckBox var4 = new KongaCheckBox(var3);
      KongaLabel var5 = new KongaLabel(ApplicationResources.MNEMONIC_FACTORY.fromString(var2));
      CheckBoxInputField var6 = new CheckBoxInputField(var1, var4, var5);
      this.addInputField(var6);
      return var6;
   }

   private void updateIgnoreCaseEnabled() {
      this.ignoreCaseChoice.setEnabled(!this.regExChoice.isSelected());
   }

   private void doLayout() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withInsets(0, 0, 10, 5);
      this.ignoreCaseChoice.layoutIn(var1);
      this.regExChoice.layoutIn(var1);
      this.wrap(var1);
   }
}
