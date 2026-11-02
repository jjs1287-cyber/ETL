package org.etlforge.integration.client.ui.jitterpack;

import javax.swing.JCheckBox;
import org.etlforge.integration.client.project.jitterpack.DefaultPackingFileFilter;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.util.preference.KongaPreferences;

final class ExportParametersDefiner implements UiProvider {
   private static final String INCLUDE_PASSWORDS_PREFERENCE = "IncludePasswords";
   private final IntegrationProject project;
   private final JCheckBox includeTestDataSelector;
   private final JCheckBox includePasswordsSelector;
   private final JCheckBox readOnlySelector;
   private final ExportSecurityDefiner securityDefiner;

   public ExportParametersDefiner(IntegrationProject var1) {
      this.project = var1;
      this.includePasswordsSelector = this.createIncludePasswordsSelector();
      this.includeTestDataSelector = new KongaCheckBox(Strings.get("IncludeTestData"));
      this.readOnlySelector = this.createReadOnlySelector();
      this.securityDefiner = new ExportSecurityDefiner();
   }

   private JCheckBox createIncludePasswordsSelector() {
      boolean var1 = getPreferences().getBoolean("IncludePasswords", false);
      return new KongaCheckBox(Strings.get("IncludePasswords"), var1);
   }

   private static KongaPreferences getPreferences() {
      return KongaPreferences.getPreferences(PackingParametersDefiner.class);
   }

   private JCheckBox createReadOnlySelector() {
      KongaCheckBox var1 = new KongaCheckBox(Strings.get("Params.ReadOnly"));
      if (this.project.isDeployable()) {
         var1.setToolTipText(Strings.get("Params.ReadOnly.ToolTip"));
      } else {
         var1.setSelected(true);
         var1.setEnabled(false);
         var1.setToolTipText(Strings.get("Params.ReadOnly.Disabled.ToolTip"));
      }

      return var1;
   }

   public ExportParameters getParameters() {
      this.storePreferences();
      char[] var1 = this.securityDefiner.getPassword();
      boolean var2 = this.includePasswordsSelector.isSelected();
      boolean var3 = this.readOnlySelector.isSelected();
      boolean var4 = this.includeTestDataSelector.isSelected();
      DefaultPackingFileFilter var5 = new DefaultPackingFileFilter(this.project.getLocationOnDisk(), var4);
      return new ExportParameters(var5, var1, var2, var3);
   }

   private void storePreferences() {
      getPreferences().putBoolean("IncludePasswords", this.includePasswordsSelector.isSelected());
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.createDisplayer().decorate(var1);
   }

   private UiProvider createDisplayer() {
      BoxBuilder var1 = BoxBuilder.horizontal().addAllWithSpace(20, this.includePasswordsSelector, this.includeTestDataSelector, this.readOnlySelector);
      BoxBuilder var2 = BoxBuilder.vertical().leftAligned();
      var2.rigid(10).addAllWithSpace(10, var1, this.securityDefiner);
      return var2;
   }

   @Override
   public void requestFocus() {
      this.includeTestDataSelector.requestFocus();
   }
}

