package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.entity.ProjectItemTransform;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.httpendpoint.OperationRunMode;
import org.etlforge.integration.data.entity.httpendpoint.ResponseType;
import org.etlforge.integration.data.entity.httpendpoint.TriggerableOperationFilter;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperation;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperations;

public final class OperationToHttpEndpointFactory extends ProjectItemTransform {
   public OperationToHttpEndpointFactory(UiEntityFactory var1) {
      super(var1, EntityType.HttpEndpoint);
   }

   @Override
   public boolean isSupported(IntegrationEntity var1, Optional<Folder> var2) {
      return TriggerableOperationFilter.FILTER.apply(var1);
   }

   @Override
   protected void configureDestination(IntegrationEntity var1, IntegrationEntity var2) {
      Operation var3 = (Operation)var1;
      HttpEndpoint var4 = (HttpEndpoint)var2;
      ResponseType var5 = this.configureResponseType(var3, var4);
      this.configureOperationsToRun(var3, var4, var5);
   }

   private ResponseType configureResponseType(Operation var1, HttpEndpoint var2) {
      ResponseType var3 = var1.getTargetId() == null ? ResponseType.NONE : ResponseType.OPERATION;
      var2.setResponseType(var3);
      return var3;
   }

   private void configureOperationsToRun(Operation var1, HttpEndpoint var2, ResponseType var3) {
      TriggeredOperations var4 = new TriggeredOperations();
      OperationRunMode var5 = var3 == ResponseType.OPERATION ? OperationRunMode.SYNCHRONOUS : OperationRunMode.ASYNCHRONOUS;
      var4.addOperation(new TriggeredOperation(var1.getID(), null, var5, var5 == OperationRunMode.SYNCHRONOUS, this.isUsingRequestAsSource(var1)));
      var2.setTriggeredOperations(var4);
   }

   private boolean isUsingRequestAsSource(Operation var1) {
      return SystemSource.FROM_HTTP_ENDPOINT.getID().equals(var1.getSourceId());
   }
}
