package org.etlforge.integration.client.ui.interchange.entity.target.page;

import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.entity.LocatablePage;
import org.etlforge.integration.client.ui.interchange.entity.LocatablePageHelpIdProvider;
import org.etlforge.integration.client.ui.interchange.location.db.DatabaseLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.http.HttpLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.http.ResponseTargetComboBoxSelector;
import org.etlforge.integration.client.ui.interchange.location.local.LocalFileLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.LocationDecoratedIcons;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

public final class TargetPage extends IntegrationEntityPage implements LocatablePage<Target> {
   private static final String DEFAULT_HELP_ID = LocatablePageHelpIdProvider.getDefaultHelpId(SourceTarget.Target);
   private DataLocationType currentLocationType;
   private boolean httpResponseTargetSelectorHasBeenSet;
   private ServerManager serverManager;
   private Action testConnection;

   public TargetPage(Target var1) {
      super(new IntegrationEntityPageModel(var1));
      this.setHelpId(DEFAULT_HELP_ID);
      DataLocation var2 = var1.getLocation();
      if (var2 != null) {
         this.setCurrentLocationType(var2.getDataLocationType());
      }
   }

   public void setServerManager(ServerManager var1) {
      this.serverManager = var1;
   }

   public void setTestConnectionAction(Action var1) {
      this.testConnection = var1;
   }

   public Target getLocatable() {
      return this.getObject();
   }

   protected TargetPanel createDataPanel() {
      TargetPage.TargetPanelImpl var1 = new TargetPage.TargetPanelImpl(this.getWindow(), this.getObject());
      var1.addTypeSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            OrderedSelection var2 = (OrderedSelection)var1.getSelection();
            DataLocationType var3 = (DataLocationType)var2.head();
            TargetPage.this.setCurrentLocationType(var3);
         }
      });
      return var1;
   }

   protected TargetPanel getDetailsPanel() {
      return (TargetPanel)super.getDetailsPanel();
   }

   public Target getObject() {
      return (Target)super.getObject();
   }

   @Override
   public Icon getSmallIcon() {
      if (this.currentLocationType != null) {
         Icon var1 = LocationDecoratedIcons.forTarget().getIcon(this.currentLocationType, EntityIcons.Size.SMALL);
         if (var1 != null) {
            return var1;
         }
      }

      return super.getSmallIcon();
   }

   public void setCurrentLocationType(DataLocationType var1) {
      if (var1 != this.currentLocationType) {
         DataLocationType var2 = this.currentLocationType;
         this.currentLocationType = var1;
         this.setHelpId(var1 != null ? LocatablePageHelpIdProvider.getHelpId(SourceTarget.Target, var1) : DEFAULT_HELP_ID);
         this.setTitleIcon(this.getSmallIcon());
         this.firePropertyChange("locationType", var2, var1);
      }
   }

   @Override
   public DataLocationType getSelectedLocationType() {
      return this.getDetailsPanel().getSelectedLocationType();
   }

   private void setHttpTargetResponseSelector(HttpLocationPanel var1) {
      if (!this.httpResponseTargetSelectorHasBeenSet) {
         ResponseTargetComboBoxSelector var2 = new ResponseTargetComboBoxSelector(this.getEntityExplorerSupport());
         var1.setResponseTargetSelector(var2);
         this.httpResponseTargetSelectorHasBeenSet = true;
      }
   }

   private final class TargetPanelImpl extends TargetPanel {
      private TargetPanelImpl(ApplicationWindow var2, Target var3) {
         super(var2, var3, TargetPage.this.testConnection);
      }

      @Override
      protected void configureNewLocationPanel(DataLocationPanel var1) {
         TargetPage.this.getPageModel().bindProperties(var1.getBoundPropertyControls());
         this.configureHttpUi(var1);
         this.configureDatabaseUi(var1);
         this.configureLocalFileLocationUi(var1);
      }

      private void configureHttpUi(DataLocationPanel var1) {
         if (var1 instanceof HttpLocationPanel && !TargetPage.this.httpResponseTargetSelectorHasBeenSet) {
            TargetPage.this.setHttpTargetResponseSelector((HttpLocationPanel)var1);
         }
      }

      private void configureDatabaseUi(DataLocationPanel var1) {
         if (var1 instanceof DatabaseLocationPanel) {
            ((DatabaseLocationPanel)var1).setServerManager(TargetPage.this.serverManager);
         }
      }

      private void configureLocalFileLocationUi(DataLocationPanel var1) {
         if (var1 instanceof LocalFileLocationPanel) {
            ((LocalFileLocationPanel)var1).setServerManager(TargetPage.this.serverManager);
         }
      }
   }
}
