package org.etlforge.integration.client.ui.interchange.location.db;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.datalocation.cache.DatabaseLocationCache;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelUtils;
import org.etlforge.integration.client.ui.interchange.location.panel.CacheBasedLoginNameHistory;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.persist.Persistor;

public final class DatabaseLocationPanel extends DataLocationPanel {
   private final IntegrationEntity locatable;
   private final DriverSelectorPart driverSelector;
   private final DatabaseLocationPanelPart timeoutDefiner;
   private final ServerDefiner serverDefiner;
   private final DatabaseDefiner databaseDefiner;
   private final DbLoginDefiner loginDefiner;
   private final PortDefiner portDefiner;
   private final FetchSizeField fetchSizeField;
   private final TransactionIsolationLevelDefiner transactionIsolationLevelDefiner;
   private final AdditionalParametersDefiner connectParamsDefiner;
   private final ManualConnectionStringDefiner manualConnectionStringDefiner;
   private DatabaseLocationCache cache;

   public DatabaseLocationPanel(IntegrationEntity var1, DatabaseLocation var2, SourceTarget var3) {
      super(var2, var3);
      this.locatable = var1;
      this.setOpaque(false);
      this.serverDefiner = new ServerDefiner(this);
      this.databaseDefiner = new DatabaseDefiner(this);
      this.driverSelector = this.createDriverSelector();
      this.loginDefiner = new DbLoginDefiner(this);
      this.connectParamsDefiner = new AdditionalParametersDefiner(this);
      this.timeoutDefiner = new TimeOutDefiner(this);
      this.portDefiner = new PortDefiner(this);
      this.fetchSizeField = new FetchSizeField(this);
      this.transactionIsolationLevelDefiner = new TransactionIsolationLevelDefiner(this);
      this.manualConnectionStringDefiner = this.createManualConnectionStringDefiner();
      this.updateFields(var2);
   }

   public void setServerManager(ServerManager var1) {
      this.driverSelector.setServerManager(var1);
   }

   public void setDataCache(DatabaseLocationCache var1) {
      this.cache = Preconditions.checkNotNull(var1);
      this.serverDefiner.installAutoCompletion(new CacheBasedServerHistory(var1));
      this.databaseDefiner.installAutoCompletion(new CacheBasedDatabaseHistory(var1));
      this.loginDefiner.installAutoCompletionInLoginField(new CacheBasedLoginNameHistory(var1));
   }

   public DatabaseLocation getDisplayedObject() {
      return (DatabaseLocation)super.getDisplayedObject();
   }

   @Override
   public void display() {
      this.driverSelector.display();
      this.layoutComponents();
      super.display();
   }

   @Override
   public void setInitialFocus() {
      if (this.driverSelector.hasDriverSelection()) {
         this.serverDefiner.requestFocus();
      } else {
         this.driverSelector.requestFocus();
      }
   }

   @Override
   public void apply() throws IntegrationDataPanelException {
      this.vaildateInput();
      DatabaseLocation var1 = this.getDisplayedObject();
      new DatabaseLocationPanel.PasswordLocationChecker().run();
      this.applyParts(var1);
      this.updateCache(var1);
   }

   private void vaildateInput() throws IntegrationDataPanelException {
      if (!this.driverSelector.hasDriverSelection()) {
         throw new IntegrationDataPanelException("Must select a driver.");
      }
   }

   @Override
   public void reset() {
      this.updateFields(this.getDisplayedObject());
   }

   @Override
   public void dispose() {
      super.dispose();
      this.loginDefiner.dispose();
      this.driverSelector.dispose();
   }

   @Override
   public void setActive(boolean var1) {
      super.setActive(var1);
      if (var1) {
         this.setManualMode(this.manualConnectionStringDefiner.isSelected());
      }
   }

   private void updateFields(DatabaseLocation var1) {
      this.driverSelector.reset(var1);
      this.timeoutDefiner.reset(var1);
      this.serverDefiner.reset(var1);
      this.databaseDefiner.reset(var1);
      this.loginDefiner.reset(var1);
      this.portDefiner.reset(var1);
      this.fetchSizeField.reset(var1);
      this.transactionIsolationLevelDefiner.reset(var1);
      this.connectParamsDefiner.reset(var1);
      this.manualConnectionStringDefiner.reset(var1);
      boolean var2 = var1.isManualMode();
      this.setManualMode(var2);
   }

   private void applyParts(DatabaseLocation var1) throws IntegrationDataPanelException {
      this.driverSelector.applyTo(var1);
      this.timeoutDefiner.applyTo(var1);
      this.serverDefiner.applyTo(var1);
      this.databaseDefiner.applyTo(var1);
      this.loginDefiner.applyTo(var1);
      this.portDefiner.applyTo(var1);
      this.fetchSizeField.applyTo(var1);
      this.connectParamsDefiner.applyTo(var1);
      this.transactionIsolationLevelDefiner.applyTo(var1);
      this.manualConnectionStringDefiner.applyTo(var1);
   }

   private void updateCache(DatabaseLocation var1) {
      if (this.cache != null) {
         this.cache.update(var1);
      }
   }

   private DriverSelectorPart createDriverSelector() {
      DriverSelectorPart var1 = new DriverSelectorPart(this);
      var1.setSelectionListener(new Receiver<DriverDescriptor>() {
         public void handle(DriverDescriptor var1) {
            boolean var2 = DatabaseLocationPanel.this.manualConnectionStringDefiner.isSelected();
            DatabaseLocationPanel.this.updateEnabledStateOfTheLoginDefiner(var2);
            DatabaseLocationPanel.this.updateEnabledStateOfTheDatabaseField(var2);
         }
      });
      return var1;
   }

   private ManualConnectionStringDefiner createManualConnectionStringDefiner() {
      ManualConnectionStringDefiner var1 = new ManualConnectionStringDefiner(this);
      var1.addItemListener(new DatabaseLocationPanel.ManualSelectionListener());
      return var1;
   }

   private void layoutComponents() {
      DataLocationPanelLayout var1 = new DataLocationPanelLayout();
      this.driverSelector.addToLayout(var1);
      this.serverDefiner.addToLayout(var1);
      this.databaseDefiner.addToLayout(var1);
      this.loginDefiner.addToLayout(var1);
      this.portDefiner.addToLayout(var1);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(InvisiblePanel.of(var1));
      var2.south(this.layoutOptionalFields());
      this.wrap(var2);
   }

   private JComponent layoutOptionalFields() {
      DataLocationPanelLayout var1 = new DataLocationPanelLayout();
      this.transactionIsolationLevelDefiner.addToLayout(var1);
      this.timeoutDefiner.addToLayout(var1);
      if (this.getSourceTarget() == SourceTarget.Source) {
         this.fetchSizeField.addToLayout(var1);
      }

      this.connectParamsDefiner.addToLayout(var1);
      this.manualConnectionStringDefiner.addToLayout(var1);
      JPanel var2 = new JPanel();
      var1.addToParent(var2, null);
      InterchangeViewUtils.configurePropertyFields(var2);
      boolean var3 = !this.transactionIsolationLevelDefiner.isDefaultLevel()
         || !this.fetchSizeField.isDefaultValue()
         || !this.connectParamsDefiner.isEmpty()
         || this.manualConnectionStringDefiner.isSelected();
      DropDownPanel var4 = DataLocationPanelUtils.createOptionsDropDown(var2, var3);
      return var4.getDisplayer();
   }

   private void setManualMode(boolean var1) {
      this.updateEnabledStateOfTheDatabaseField(var1);
      this.serverDefiner.setEnabled(!var1);
      this.connectParamsDefiner.setEnabled(!var1);
      this.portDefiner.setEnabled(!var1);
      this.updateEnabledStateOfTheLoginDefiner(var1);
   }

   private void updateEnabledStateOfTheLoginDefiner(boolean var1) {
      this.loginDefiner.setLoginEnabled(this.driverSelector.isJdbcDriver() || !var1);
   }

   private void updateEnabledStateOfTheDatabaseField(boolean var1) {
      this.databaseDefiner.setEnabled(!var1 && !this.driverSelector.isOracleDriver());
   }

   private class ManualSelectionListener implements ItemListener {
      private ManualSelectionListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         DatabaseLocationPanel.this.setManualMode(DatabaseLocationPanel.this.manualConnectionStringDefiner.isSelected());
      }
   }

   private class PasswordLocationChecker {
      private final Persistor preference;
      private final String preferenceKey;

      public PasswordLocationChecker() {
         ManagedProject var2 = ProjectUtils.getManagedProject(DatabaseLocationPanel.this.locatable);
         EntityResources var3 = var2.getEntityResources();
         this.preference = var3.getSettings().getEntitySettings(DatabaseLocationPanel.this.locatable);
         this.preferenceKey = "HidePassword";
      }

      public void run() {
         if (this.shouldAskAboutPasswordInConnectionString()) {
            this.askAboutPassword();
         }
      }

      private boolean shouldAskAboutPasswordInConnectionString() {
         if (DatabaseLocationPanel.this.manualConnectionStringDefiner.isSelected()
            && DatabaseLocationPanel.this.manualConnectionStringDefiner.isPasswordDefined()) {
            Boolean var1 = this.preference.getBoolean(this.preferenceKey);
            return var1 == null || var1;
         } else {
            return false;
         }
      }

      private void askAboutPassword() {
         assert DatabaseLocationPanel.this.manualConnectionStringDefiner.isSelected()
            && DatabaseLocationPanel.this.manualConnectionStringDefiner.isPasswordDefined();
         Answer var1 = Ask.yesOrNo(
            DatabaseLocationPanel.this.getUi(),
            "The connection string contains a password. It is safer to define the password in the password field.\n\nDo you want to move the password from the connection string to the password field?",
            "Password Safety",
            Alert.Option.YES,
            Alert.Type.WARNING
         );
         if (var1.isYes()) {
            this.preference.putBoolean(this.preferenceKey, true);
            this.movePasswordToPasswordField();
         } else {
            this.preference.putBoolean(this.preferenceKey, false);
         }
      }

      private void movePasswordToPasswordField() {
         String var1 = DatabaseLocationPanel.this.manualConnectionStringDefiner.removePassword();
         assert var1 != null;
         DatabaseLocationPanel.this.loginDefiner.setPassword(Optional.of(Password.encrypt(var1)));
      }
   }
}
