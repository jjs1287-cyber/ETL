package org.etlforge.integration.client.ui.interchange.location.db;

import javax.swing.JComboBox;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.db.TransactionIsolationLevel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.widget.label.KongaLabel;

class TransactionIsolationLevelDefiner implements DatabaseLocationPanelPart {
   private final ComboBoxInputField selector;

   public TransactionIsolationLevelDefiner(InputPanel var1) {
      JComboBox var2 = new JComboBox<>(TransactionIsolationLevel.values());
      String var3 = LocationResources.getString("Database.TransLevel.Name");
      KongaLabel var4 = KongaLabel.withMnemonic(LocationResources.getString("Database.TransLevel.Label"));
      this.selector = InputFieldFactory.getComboBoxInputField(var3, var2, var4);
      this.selector.setSelectedItem(TransactionIsolationLevel.DEFAULT);
      var1.addInputField(this.selector);
   }

   @Override
   public void addToLayout(DatabaseLocationPanelLayout var1) {
      var1.add(InputFieldFactory.getCapsule(this.selector));
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(this.selector.getLabel(), this.selector.getInputComponent(), false);
   }

   public void applyTo(DatabaseLocation var1) throws IntegrationDataPanelException {
      TransactionIsolationLevel var2 = this.getSelectedLevel();
      var1.setTransactionIsolactionLevel(var2);
   }

   @Override
   public void dispose() {
      this.selector.dispose();
   }

   public void reset(DatabaseLocation var1) {
      this.selector.setSelectedItem(var1.getTransactionIsolationLevel());
   }

   public boolean isDefaultLevel() {
      return this.getSelectedLevel() == TransactionIsolationLevel.DEFAULT;
   }

   private TransactionIsolationLevel getSelectedLevel() {
      TransactionIsolationLevel var1 = (TransactionIsolationLevel)this.selector.getSelectedItem();
      if (var1 == null) {
         var1 = TransactionIsolationLevel.DEFAULT;
      }

      return var1;
   }
}
