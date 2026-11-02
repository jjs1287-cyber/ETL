package org.etlforge.integration.client.ui.interchange.entity.target;

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
import org.etlforge.integration.client.ui.interchange.entity.target.page.TargetPage;
import org.etlforge.integration.client.ui.interchange.entity.target.page.TargetPageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class TargetConfig extends AbstractEntityConfig {
   private final ServerManager serverManager;
   private final ApplicationAction testConnectionAction;
   private Action assignPluginAction;
   private DataLocationValueCaches valueCaches;

   public TargetConfig(ServerManager var1, ApplicationAction var2) {
      super(EntityType.Target);
      this.serverManager = var1;
      this.testConnectionAction = var2;
      this.setSearchAndReplaceMetaData(new TargetSearchAndReplaceProperties());
      this.setSearchValuesCollector(new TargetSearchValuesCollector());
   }

   public void setValueCaches(DataLocationValueCaches var1) {
      this.valueCaches = var1;
   }

   public void setAssignPluginAction(Action var1) {
      this.assignPluginAction = var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new TargetPage((Target)var1);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      TargetPageConfig var4 = new TargetPageConfig(var1, var3, this.serverManager);
      var4.setTestConnectionAction(this.testConnectionAction);
      var4.setAssignPluginAction(this.assignPluginAction);
      return var4;
   }

   @Override
   protected ApplicationAction createNewEntityAction() {
      NewEntityAction var1 = (NewEntityAction)super.createNewEntityAction();
      NewTargetCallback var2 = new NewTargetCallback(new TypeSelectorUiFunction());
      var1.setCallbackProvider(Suppliers.ofInstance(var2));
      return var1;
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new TargetToolTip((Target)var1);
   }
}
