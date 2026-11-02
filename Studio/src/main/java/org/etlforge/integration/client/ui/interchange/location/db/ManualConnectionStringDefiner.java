package org.etlforge.integration.client.ui.interchange.location.db;

import java.awt.event.ItemListener;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.db.ConnectionStringPasswordExtractor;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.input.ToggleButtonActivationConstraint;
import org.etlforge.ui.layout.BorderLayoutBuilder;

final class ManualConnectionStringDefiner implements DatabaseLocationPanelPart {
   private final CheckBoxInputField enabler;
   private final TextInputField2 field;

   public ManualConnectionStringDefiner(InputPanel var1) {
      String var2 = LocationResources.getString("Database.ManualConnectString.Name");
      String var3 = LocationResources.getString("Database.ManualConnectString.Label");
      this.enabler = InputFieldFactory.getCheckBoxInputField(var2, var3);
      this.field = InputFieldFactory.getNonEmptyTextInputField(var2, "", 30, true);
      var1.addInputField(this.enabler);
      var1.addInputField(this.field, this.field, this.field);
      this.addConstraint();
   }

   @Override
   public void addToLayout(DatabaseLocationPanelLayout var1) {
      var1.addRow(InputFieldFactory.getCapsule(this.enabler));
      var1.addRow(InputFieldFactory.getCapsule(this.field));
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      var1.add(InputFieldFactory.getCapsule(this.enabler, 0), false);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder().withEmptyBorder(0, 25, 0, 0);
      var2.north(this.field.getInputComponent());
      var1.add(var2.container(), true);
   }

   public void applyTo(DatabaseLocation var1) {
      var1.setManualConnectionString(this.getConnectionString());
      var1.setManualMode(this.enabler.isSelected());
   }

   public void reset(DatabaseLocation var1) {
      boolean var2 = var1.isManualMode();
      this.enabler.setSelected(var2);
      String var3 = var1.getProperty("connectstring");
      this.field.setValue(var3);
   }

   @Override
   public void dispose() {
      this.enabler.dispose();
      this.field.dispose();
   }

   public void addItemListener(ItemListener var1) {
      this.enabler.getInputComponent().addItemListener(var1);
   }

   public boolean isSelected() {
      return this.enabler.isSelected();
   }

   public boolean isPasswordDefined() {
      return new ConnectionStringPasswordExtractor(this.getConnectionString()).isDefiningPassword();
   }

   public String removePassword() {
      ConnectionStringPasswordExtractor var1 = new ConnectionStringPasswordExtractor(this.getConnectionString());
      String var2 = var1.getPassword();
      if (var2 != null) {
         this.setNewConnectionString(var1.removePassword());
      }

      return var2;
   }

   private void addConstraint() {
      ToggleButtonActivationConstraint var1 = new ToggleButtonActivationConstraint(this.enabler);
      var1.bind(this.field, true);
      this.field.addActivationConstraint(var1);
   }

   private String getConnectionString() {
      return this.field.getValue().trim();
   }

   private void setNewConnectionString(final String var1) {
      this.field.updateSilently(new Runnable() {
         @Override
         public void run() {
            ManualConnectionStringDefiner.this.field.setValue(var1);
         }
      });
   }
}
