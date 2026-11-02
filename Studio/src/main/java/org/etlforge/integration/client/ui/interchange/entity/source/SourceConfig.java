package org.etlforge.integration.client.ui.interchange.entity.source;

import com.google.common.base.Suppliers;
import javax.swing.Action;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.core.action.NewEntityAction;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.DataLocationValueCaches;
import org.etlforge.integration.client.ui.interchange.entity.TypeSelectorUiFunction;
import org.etlforge.integration.client.ui.interchange.entity.source.page.SourcePage;
import org.etlforge.integration.client.ui.interchange.entity.source.page.SourcePageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class SourceConfig extends AbstractEntityConfig {
   private final ServerManager serverManager;
   private final ApplicationAction testConnectionAction;
   private Action assignPluginAction;
   private DataLocationValueCaches valueCaches;

   public SourceConfig(ServerManager var1, ApplicationAction var2) {
      super(EntityType.Source);
      this.serverManager = var1;
      this.testConnectionAction = var2;
      this.setSearchAndReplaceMetaData(new SourceSearchAndReplaceProperties());
      this.setSearchValuesCollector(new SourceSearchValuesCollector());
   }

   public void setValueCaches(DataLocationValueCaches var1) {
      this.valueCaches = var1;
   }

   public void setAssignPluginAction(Action var1) {
      this.assignPluginAction = var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new SourcePage((Source)var1);
   }

   @Override
   protected ApplicationAction createNewEntityAction() {
      NewEntityAction var1 = (NewEntityAction)super.createNewEntityAction();
      NewSourceCallback var2 = new NewSourceCallback(new TypeSelectorUiFunction());
      var1.setCallbackProvider(Suppliers.ofInstance(var2));
      return var1;
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      SourcePageConfig var4 = new SourcePageConfig(var1, var3, this.serverManager);
      var4.setTestConnectionAction(this.testConnectionAction);
      var4.setAssignPluginAction(this.assignPluginAction);
      return var4;
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new SourceToolTip((Source)var1);
   }
}
