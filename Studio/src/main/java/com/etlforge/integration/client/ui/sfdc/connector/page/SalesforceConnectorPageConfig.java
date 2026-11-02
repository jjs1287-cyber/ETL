package com.etlforge.integration.client.ui.sfdc.connector.page;

import javax.swing.Action;
import javax.swing.JButton;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.cli.ui.ActionCommand;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SalesforceConnectorPageConfig extends DefaultPageConfig {
   private ApplicationAction testAction;
   private boolean includeNameField;
   private ValueCache<SalesforceConnector> cache;
   private SalesforceConnectorPage page;

   public SalesforceConnectorPageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      super(var1, var2);
   }

   public void setTestAction(ApplicationAction var1) {
      this.testAction = var1;
   }

   public void setIncludeNameField(boolean var1) {
      this.includeNameField = var1;
   }

   public void setCache(ValueCache<SalesforceConnector> var1) {
      this.cache = var1;
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      this.page = (SalesforceConnectorPage)var1;
      if (this.testAction != null) {
         this.installTestAction();
      }
   }

   private void installTestAction() {
      this.addToolbarButton(this.testConnectionButton(this.page));
      this.addCommand(new ActionCommand("test", this.testAction));
      this.page.setTestConnectionAction(this.getTestConnectionWrapper(this.page));
   }

   private JButton testConnectionButton(IntegrationEntityPage var1) {
      ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.testAction);
      return this.createToolbarButton(var2);
   }

   private Action getTestConnectionWrapper(SalesforceConnectorPage var1) {
      ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.testAction);
      var2.setSaveIfDirty(true);
      return var2;
   }

   @Override
   protected void postConfiguration(IntegrationEntityPage var1) {
      this.installCache();
      var1.setIncludeNameField(this.includeNameField);
   }

   private void installCache() {
      if (this.cache != null) {
         this.page.setCache(this.cache);
      }
   }
}
