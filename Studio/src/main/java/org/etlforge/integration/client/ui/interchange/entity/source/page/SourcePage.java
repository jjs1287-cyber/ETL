package org.etlforge.integration.client.ui.interchange.entity.source.page;

import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.entity.LocatablePage;
import org.etlforge.integration.client.ui.interchange.entity.LocatablePageHelpIdProvider;
import org.etlforge.integration.client.ui.interchange.location.db.DatabaseLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.local.LocalFileLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.LocationDecoratedIcons;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

public final class SourcePage extends IntegrationEntityPage implements LocatablePage<Source> {
   private static final String DEFAULT_HELP_ID = LocatablePageHelpIdProvider.getDefaultHelpId(SourceTarget.Source);
   private DataLocationType currentLocationType;
   private ServerManager serverManager;
   private Action testConnection;

   public SourcePage(Source var1) {
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

   public Source getLocatable() {
      return this.getObject();
   }

   protected SourcePanel createDataPanel() {
      SourcePanel var1 = new SourcePanel(this.getWindow(), this.getObject(), this.testConnection) {
         @Override
         protected void configureNewLocationPanel(DataLocationPanel var1) {
            SourcePage.this.getPageModel().bindProperties(var1.getBoundPropertyControls());
            if (var1 instanceof DatabaseLocationPanel) {
               ((DatabaseLocationPanel)var1).setServerManager(SourcePage.this.serverManager);
            }

            if (var1 instanceof LocalFileLocationPanel) {
               ((LocalFileLocationPanel)var1).setServerManager(SourcePage.this.serverManager);
            }
         }
      };
      var1.addTypeSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            OrderedSelection var2 = (OrderedSelection)var1.getSelection();
            DataLocationType var3 = (DataLocationType)var2.head();
            SourcePage.this.setCurrentLocationType(var3);
         }
      });
      return var1;
   }

   protected SourcePanel getDetailsPanel() {
      return (SourcePanel)super.getDetailsPanel();
   }

   public Source getObject() {
      return (Source)super.getObject();
   }

   @Override
   public Icon getSmallIcon() {
      if (this.currentLocationType != null) {
         Icon var1 = LocationDecoratedIcons.forSource().getIcon(this.currentLocationType, EntityIcons.Size.SMALL);
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
         this.setHelpId(var1 != null ? LocatablePageHelpIdProvider.getHelpId(SourceTarget.Source, var1) : DEFAULT_HELP_ID);
         this.setTitleIcon(this.getSmallIcon());
         this.firePropertyChange("locationType", var2, var1);
      }
   }

   @Override
   public DataLocationType getSelectedLocationType() {
      return this.getDetailsPanel().getSelectedLocationType();
   }
}
