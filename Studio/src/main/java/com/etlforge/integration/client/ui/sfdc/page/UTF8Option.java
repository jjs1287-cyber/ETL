package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.base.Charsets;
import javax.swing.JLabel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class UTF8Option {
   private final KongaCheckBox checkBox;

   public static <T extends IntegrationEntity & SalesforceWizardEntity> UTF8Option forWizardItem(T var0) {
      EntityType var1 = var0.getEntityType();
      if (var1 == EntityType.SalesforceBulkLoad) {
         return forBulkLoad((SalesforceBulkLoad)var0);
      } else {
         return var1 == EntityType.SalesforceQuery ? forResponse((SalesforceWizardEntity)var0) : forRequest((SalesforceWizardEntity)var0);
      }
   }

   private static UTF8Option forBulkLoad(SalesforceBulkLoad var0) {
      BulkAction var1 = var0.getAction();
      UTF8Option var2 = var1.isLoad() ? forSource() : forTarget();
      return var2.withInitialEncoding(var0.getFileEncodingName());
   }

   private static UTF8Option forResponse(SalesforceWizardEntity var0) {
      UTF8Option var1 = forTarget().withInitialEncoding(var0.getFileEncodingName());
      boolean var2 = isEnabledForResponse(var0);
      var1.checkBox.setEnabled(var2);
      return var1;
   }

   private static boolean isEnabledForResponse(SalesforceWizardEntity var0) {
      WizardItemInfo var1 = new WizardItemInfo(var0);
      return var1.writesToTextFile();
   }

   private static UTF8Option forRequest(SalesforceWizardEntity var0) {
      UTF8Option var1 = forSource().withInitialEncoding(var0.getFileEncodingName());
      boolean var2 = isEnabledForRequest(var0);
      var1.checkBox.setEnabled(var2);
      return var1;
   }

   private static boolean isEnabledForRequest(SalesforceWizardEntity var0) {
      WizardItemInfo var1 = new WizardItemInfo(var0);
      return var1.readsFromTextFile();
   }

   private static UTF8Option forSource() {
      return new UTF8Option(SourceTarget.Source);
   }

   private static UTF8Option forTarget() {
      return new UTF8Option(SourceTarget.Target);
   }

   private UTF8Option(SourceTarget var1) {
      String var2 = var1 == SourceTarget.Source ? "Read" : "Write";
      this.checkBox = new KongaCheckBox(var2 + " as UTF-8");
   }

   public boolean isSelected() {
      return this.checkBox.isEnabled() && this.checkBox.isSelected();
   }

   public void layoutIn(NColumnPanel var1) {
      assert var1.getColumnCount() == 3;
      var1.addRow(null, this.checkBox, null);
   }

   public void layoutIn(TwoColumnLayout var1) {
      JLabel var2 = new JLabel(this.checkBox.getText());
      this.checkBox.setText("");
      var1.addRow(var2, this.checkBox);
   }

   public UTF8Option asChangeSourceIn(InputPanel var1) {
      CheckBoxInputField var2 = new CheckBoxInputField(this.checkBox.getText(), this.checkBox);
      var1.addInputField(var2);
      return this;
   }

   private UTF8Option withInitialEncoding(String var1) {
      boolean var2 = Charsets.UTF_8.name().equals(var1);
      this.checkBox.setSelected(var2);
      return this;
   }

   public boolean isEnabled() {
      return this.checkBox.isEnabled();
   }

   public void requestFocus() {
      this.checkBox.requestFocusInWindow();
   }
}
