package org.etlforge.integration.client.ui.interchange.entity.operation;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.integration.activity.serverapi.history.OperationHistoryProvider;
import org.etlforge.integration.activity.serverapi.history.SourceHistoryProvider;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.core.action.NewEntityAction;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphConfig;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationGraphActions;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPage;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPageConfig;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactory;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class OperationConfig extends AbstractEntityConfig {
   private final OperationGraphConfig graphConfig;
   private EditorDisplayer activityPageDisplayer;
   private OperationLogProvider logApi;
   private OperationQueueProvider queueApi;
   private OperationHistoryProvider opHistoryApi;
   private SourceHistoryProvider sourceHistoryApi;
   private OperationGraphActions graphActions;
   private OperationRunnerFactory opRunnerFactory;
   private OperationPreferences operationPrefs = OperationPreferences.NONE;

   public OperationConfig(OperationGraphConfig var1) {
      super(EntityType.Operation);
      this.graphConfig = var1;
      this.setSearchAndReplaceMetaData(new OperationSearchAndReplaceProperties());
   }

   public void setActivityPageDisplayer(EditorDisplayer var1) {
      this.activityPageDisplayer = var1;
   }

   public void setLogApi(OperationLogProvider var1) {
      this.logApi = var1;
   }

   public void setQueueApi(OperationQueueProvider var1) {
      this.queueApi = var1;
   }

   public void setOperationHistoryApi(OperationHistoryProvider var1) {
      this.opHistoryApi = var1;
   }

   public void setSourceHistoryApi(SourceHistoryProvider var1) {
      this.sourceHistoryApi = var1;
   }

   public void setGraphActions(List<ApplicationAction> var1) {
      this.graphActions = new OperationGraphActions(var1);
   }

   public void setOpRunnerFactory(OperationRunnerFactory var1) {
      this.opRunnerFactory = var1;
   }

   public void setOperationPreferences(OperationPreferences var1) {
      this.operationPrefs = Preconditions.checkNotNull(var1);
   }

   @Override
   protected ApplicationAction createNewEntityAction() {
      NewEntityAction var1 = (NewEntityAction)super.createNewEntityAction();
      NewOperationCallback var2 = new NewOperationCallback();
      var1.setCallbackProvider(Suppliers.ofInstance(var2));
      return var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new OperationPage((Operation)var1, this.graphConfig);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      OperationPageConfig var4 = new OperationPageConfig(var1, var3, this.getEntityFactory());
      var4.setSaveConsequenceFactories(this.getSaveConsequenceFactories());
      var4.setOpRunnerFactory(this.opRunnerFactory);
      var4.setActivityPageDisplayer(this.activityPageDisplayer);
      var4.setLogApi(this.logApi);
      var4.setQueueApi(this.queueApi);
      var4.setOperationHistoryApi(this.opHistoryApi);
      var4.setSourceHistoryApi(this.sourceHistoryApi);
      var4.setGraphActions(this.graphActions);
      return var4;
   }

   @Override
   public IntegrationEntityFactory getFactory() {
      return new OperationFactory(this.operationPrefs);
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new OperationToolTip((Operation)var1);
   }
}
