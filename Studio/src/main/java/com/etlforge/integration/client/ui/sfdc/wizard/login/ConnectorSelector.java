package com.etlforge.integration.client.ui.sfdc.wizard.login;

import com.google.common.base.Objects;
import com.etlforge.integration.client.ui.sfdc.connector.page.SalesforceConnectorPanel;
import org.etlforge.application.Application;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.ProjectItemSelector;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

abstract class ConnectorSelector implements UiProvider, Disposable {
   private Receiver<Boolean> validStateReceiver = NoReceiver.create();

   public static ConnectorSelector newSelector(ApplicationWindow var0, EntityExplorerSupport var1, SalesforceConnectionFactory var2) {
      if (areExistingConnectorsAvailable(var1)) {
         EditorPanel var3 = new EditorPanel(var0);
         return new ConnectorSelector.ProjectConnectors(var1, var3);
      } else {
         return new ConnectorSelector.NewConnector(var0, var2);
      }
   }

   private static boolean areExistingConnectorsAvailable(EntityExplorerSupport var0) {
      return !var0.getProject().getAllEntitiesOfClass(SalesforceConnector.class).isEmpty();
   }

   public abstract SalesforceConnector getSelectedConnector();

   public void setSelectedConnector(SalesforceConnectorId var1) {
   }

   public abstract boolean isInputValid();

   public boolean allowsRemoval() {
      return true;
   }

   public void editConnector(SalesforceConnector var1) {
   }

   public final void setValidStateReceiver(Receiver<Boolean> var1) {
      this.validStateReceiver = var1;
      this.notifyValidStateReceiver();
   }

   protected final void notifyValidStateReceiver() {
      this.validStateReceiver.handle(this.isInputValid());
   }

   private static class NewConnector extends ConnectorSelector {
      private final ApplicationWindow appWin;
      private final SalesforceConnector newConnector;
      private final SalesforceConnectorPanel newConnectorPanel;

      public NewConnector(ApplicationWindow var1, SalesforceConnectionFactory var2) {
         this.appWin = var1;
         this.newConnector = new SalesforceConnector("New Connector");
         this.newConnector.setTransient(true);
         this.newConnectorPanel = this.createNewConnectorPanel(var1, var2);
      }

      private SalesforceConnectorPanel createNewConnectorPanel(ApplicationWindow var1, SalesforceConnectionFactory var2) {
         SalesforceConnectorPanel var3 = new SalesforceConnectorPanel(var1, this.newConnector);
         var3.setTestConnectionAction(new TestConnectionAction(var2, var3));
         var3.setIncludeNameField(false);
         var3.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1) {
               NewConnector.this.notifyValidStateReceiver();
            }
         });
         ValueCache var4 = Application.getBean("Sfdc.Connector.ValueCache", ValueCache.class);
         if (var4 != null) {
            var3.setCache(var4);
         }

         var3.display();
         return var3;
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.newConnectorPanel.getUi());
      }

      @Override
      public void requestFocus() {
         this.newConnectorPanel.setInitialFocus();
      }

      @Override
      public void dispose() {
         this.newConnectorPanel.dispose();
      }

      @Override
      public SalesforceConnector getSelectedConnector() {
         try {
            this.newConnectorPanel.apply();
         } catch (IntegrationDataPanelException var2) {
            NonBlockingAlert.error(this.appWin.getFrame(), var2.getMessage(), "Error");
         }

         return this.newConnector;
      }

      @Override
      public boolean isInputValid() {
         return this.newConnectorPanel.isInputValid();
      }
   }

   private static class ProjectConnectors extends ConnectorSelector {
      private final ProjectItemSelector<SalesforceConnector> connectorSelector;

      public ProjectConnectors(EntityExplorerSupport var1, EditorPanel var2) {
         this.connectorSelector = this.createSelector(var1, var2);
         this.connectorSelector.selectOnlyAvailableItem();
      }

      private ProjectItemSelector<SalesforceConnector> createSelector(EntityExplorerSupport var1, EditorPanel var2) {
         ProjectItemSelector var3 = new ProjectItemSelector<>(SalesforceConnector.class, EntityType.SalesforceConnector, var1, var2);
         var3.addSelectionListener(new SelectionListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent var1) {
               ProjectConnectors.this.notifyValidStateReceiver();
            }
         });
         var3.setSelectorLabel("Select or create a Salesforce Org:");
         return var3;
      }

      @Override
      public void dispose() {
         this.connectorSelector.dispose();
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.connectorSelector.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.connectorSelector.requestFocus();
      }

      @Override
      public SalesforceConnector getSelectedConnector() {
         return this.connectorSelector.getSelectedItem();
      }

      @Override
      public void setSelectedConnector(SalesforceConnectorId var1) {
         SalesforceConnector var2 = this.connectorSelector.getSelectedItem();
         SalesforceConnectorId var3 = var2 == null ? null : var2.getID();
         if (!Objects.equal(var1, var3)) {
            this.connectorSelector.setSelectedItemId(var1);
         }
      }

      @Override
      public void editConnector(SalesforceConnector var1) {
         this.connectorSelector.openEditorFor(var1);
      }

      @Override
      public boolean isInputValid() {
         return this.getSelectedConnector() != null;
      }

      @Override
      public boolean allowsRemoval() {
         return this.connectorSelector.save();
      }
   }
}
