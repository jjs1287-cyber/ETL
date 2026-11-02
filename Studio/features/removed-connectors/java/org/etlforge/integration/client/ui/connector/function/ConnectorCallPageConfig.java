package org.etlforge.integration.client.ui.connector.function;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.connector.CreateIntegrationControlPanel;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.RefreshFunctionJobFactory;
import org.etlforge.integration.client.ui.connector.action.CreateOperationAction;
import org.etlforge.integration.client.ui.connector.action.CreateTransformationAction;
import org.etlforge.integration.client.ui.connector.action.RefreshFunctionAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.wizard.FunctionCallOperationFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ConnectorCallPageConfig<F extends AbstractConnectorCall<E, ?>, E extends ConnectorEndpoint> extends DefaultPageConfig {
   private final UiEntityFactory entityFactory;
   private final FunctionCallOperationType<? super F, ?> operationType;
   private final RefreshFunctionJobFactory refreshJobFactory;
   private final ImmutableList<? extends FunctionProperty> properties;

   public ConnectorCallPageConfig(
      ApplicationWindow var1,
      ProjectPersistor var2,
      UiEntityFactory var3,
      FunctionCallOperationType<? super F, ?> var4,
      RefreshFunctionJobFactory var5,
      List<? extends FunctionProperty> var6
   ) {
      super(var1, var2);
      this.entityFactory = var3;
      this.operationType = var4;
      this.refreshJobFactory = var5;
      this.properties = ImmutableList.copyOf(var6);
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      ConnectorCallPage var2 = (ConnectorCallPage)var1;
      this.installControlPanel(var2);
      this.installRefreshAction(var2);
      var2.setFunctionProperties(this.properties);
   }

   private void installControlPanel(ConnectorCallPage<F, E> var1) {
      AbstractConnectorCall var2 = var1.getObject();
      FunctionCallOperationFactory var3 = new FunctionCallOperationFactory<>(this.entityFactory, this.operationType);
      CreateOperationAction var4 = new CreateOperationAction<>(var2, var3);
      CreateTransformationAction var5 = CreateTransformationAction.forRequest(var2, this.entityFactory);
      CreateTransformationAction var6 = CreateTransformationAction.forResponse(var2, this.entityFactory);
      CreateIntegrationControlPanel var7 = new CreateIntegrationControlPanel(var4, var5, var6);
      var1.installControlPanel(var7);
   }

   private void installRefreshAction(final ConnectorCallPage<F, E> var1) {
      this.refreshJobFactory.setPageRefresher(new Runnable() {
         @Override
         public void run() {
            var1.refreshMappingStructures();
         }
      });
      this.refreshJobFactory.setWaitService(var1.getWaitService());
      RefreshFunctionAction var2 = new RefreshFunctionAction(var1, this.refreshJobFactory);
      this.addToolbarButton(this.createToolbarButton(var2));
   }
}
