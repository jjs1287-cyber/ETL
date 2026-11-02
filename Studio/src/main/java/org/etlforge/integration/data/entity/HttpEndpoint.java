package org.etlforge.integration.data.entity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.httpendpoint.HttpEndpointOperationLink;
import org.etlforge.integration.data.entity.httpendpoint.HttpEndpointReferenceReplacer;
import org.etlforge.integration.data.entity.httpendpoint.HttpEndpointValidator;
import org.etlforge.integration.data.entity.httpendpoint.ResponseType;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperations;
import org.etlforge.integration.data.entity.id.HttpEndpointId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public final class HttpEndpoint extends IntegrationEntity {
   @IdProperty
   public static final String HTTP_ENDPOINT_ID = "httpendpoint_id";
   @NameProperty
   public static final String HTTP_ENDPOINT_NAME = "httpendpoint_name";
   public static final String URL = "endpoint_url";
   public static final String REQUEST_VARIABLE = "request_variable";
   public static final String RESPONSE_VARIABLE = "response_variable";
   public static final String RESPONSE_TYPE = "response_type";
   public static final String OPERATIONS = "operations";
   private static final Version VERSION_INTRODUCED = VersionFactory.newVersion(3, 0, 0, 32);
   private TriggeredOperations operations = new TriggeredOperations();

   @Override
   public String[] getPropertyKeys() {
      return new String[]{"httpendpoint_id", "httpendpoint_name", "Description", "endpoint_url", "response_type", "request_variable", "response_variable"};
   }

   public HttpEndpoint(String var1) {
      this(null, var1);
   }

   public HttpEndpoint(HttpEndpointId var1, String var2) {
      super(EntityType.HttpEndpoint, var1, var2);
   }

   public HttpEndpointId getID() {
      return (HttpEndpointId)super.getID();
   }

   public String getUrl() {
      synchronized (this.getDataLock()) {
         return this.getProperty("endpoint_url", "");
      }
   }

   public void setUrl(String var1) {
      this.setStringProperty("endpoint_url", var1);
   }

   public String getRequestVariable() {
      synchronized (this.getDataLock()) {
         return this.getProperty("request_variable");
      }
   }

   public void setRequestVariable(String var1) {
      this.setStringProperty("request_variable", var1);
   }

   public String getResponseVariable() {
      synchronized (this.getDataLock()) {
         return this.getProperty("response_variable");
      }
   }

   public void setResponseVariable(String var1) {
      this.setStringProperty("response_variable", var1);
   }

   public ResponseType getResponseType() {
      synchronized (this.getDataLock()) {
         int var2 = this.getIntProperty("response_type", -1);
         return EnumWithIdLookup.lookupOr(var2, ResponseType.NONE);
      }
   }

   public void setResponseType(ResponseType var1) {
      this.setIntProperty("response_type", var1.getId());
   }

   public boolean isSynchronous(HttpVerb var1) {
      synchronized (this.getDataLock()) {
         return this.operations.isSynchronous(var1);
      }
   }

   public TriggeredOperations getTriggeredOperations() {
      synchronized (this.getDataLock()) {
         return new TriggeredOperations(this.operations);
      }
   }

   public void setTriggeredOperations(TriggeredOperations var1) {
      TriggeredOperations var2;
      synchronized (this.getDataLock()) {
         var2 = this.operations;
         this.operations = new TriggeredOperations(var1);
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("operations", var2, var1);
      }
   }

   public boolean isTriggering(Operation var1) {
      synchronized (this.getDataLock()) {
         return this.operations.contains(var1);
      }
   }

   public OperationId getResponseGeneratingOperationId(HttpVerb var1) {
      synchronized (this.getDataLock()) {
         return this.operations.getResponseGeneratingOperationId(var1);
      }
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      ArrayList var2 = Lists.newArrayList();
      synchronized (this.getDataLock()) {
         for (OperationId var5 : this.operations.getOperationIds()) {
            Operation var6 = var1.getEntity(var5, Operation.class);
            if (var6 != null) {
               var2.add(new HttpEndpointOperationLink(var6, this));
            }
         }

         return var2;
      }
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      synchronized (this.getDataLock()) {
         HttpEndpointReferenceReplacer var3 = new HttpEndpointReferenceReplacer(this, var1);
         var3.replaceReferences();
      }
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      new HttpEndpointValidator(this, var1).run();
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      this.operations = new TriggeredOperations(((HttpEndpoint)var1).operations);
   }

   public HttpEndpoint clone() {
      synchronized (this.getDataLock()) {
         HttpEndpoint var2 = (HttpEndpoint)super.clone();
         var2.operations = new TriggeredOperations(this.operations);
         return var2;
      }
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      this.writeOperations(var1);
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      this.restoreOperations(var1);
   }

   private void writeOperations(Persistor var1) {
      this.operations.writeTo(var1.createChild("Operations"));
   }

   private void restoreOperations(Persistor var1) {
      this.operations = new TriggeredOperations();
      Persistor var2 = var1.getFirstChild("Operations");
      if (var2 != null) {
         this.operations.restoreFrom(var2);
      }
   }

   @Override
   public Version getRequiredVersion() {
      return VERSION_INTRODUCED;
   }
}
