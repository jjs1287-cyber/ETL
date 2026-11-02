package org.etlforge.integration.data.connector;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.StrongEntityLink;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.structure.CallStructureSchema;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XsdBasedInputStructure;
import org.etlforge.integration.data.structure.XsdBasedOutputStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.util.persist.Persistor;

public abstract class AbstractConnectorCall<E extends ConnectorEndpoint, EID extends IntegrationEntityId> extends IntegrationEntity implements ConnectorCall {
   public static final String ENDPOINT = "Endpoint";
   public static final String INPUT_SCHEMA = "input_schema";
   public static final String OUTPUT_SCHEMA = "output_schema";
   protected static final String INPUT_XSD_FILENAME = "input_xsd_filename";
   protected static final String INPUT_XSD_ROOT = "input_xsd_root";
   protected static final String OUTPUT_XSD_FILENAME = "output_xsd_filename";
   protected static final String OUTPUT_XSD_ROOT = "output_xsd_root";
   private E endpoint;
   private final String connectorTypeId;
   private final Class<E> endpointClass;
   private final Class<EID> endpointIdClass;

   public AbstractConnectorCall(EntityType var1, IntegrationEntityId var2, String var3, String var4, Class<E> var5, Class<EID> var6) {
      super(var1, var2, var3);
      this.connectorTypeId = Preconditions.checkNotNull(var4, "connectorTypeId");
      this.endpointClass = Preconditions.checkNotNull(var5, "endpointClass");
      this.endpointIdClass = Preconditions.checkNotNull(var6, "endpointIdClass");
   }

   public final String getConnectorTypeId() {
      return this.connectorTypeId;
   }

   public final E getEndpoint() {
      synchronized (this.getDataLock()) {
         return this.endpoint;
      }
   }

   public final void setEndpoint(E var1) {
      Preconditions.checkNotNull(var1);
      ConnectorEndpoint var2;
      synchronized (this.getDataLock()) {
         if (var1 == this.endpoint) {
            return;
         }

         var2 = this.endpoint;
         this.setNewEndpoint((E)var1);
      }

      this.firePropertyChange("Endpoint", var2, var1);
   }

   protected abstract String getEndpointIdPropertyName();

   private void setNewEndpoint(E var1) {
      this.endpoint = (E)var1;
      this.setProperty(this.getEndpointIdPropertyName(), var1.getID());
   }

   @Override
   public final void restoreDependentData(Persistor var1, IntegrationEntityLookup var2) {
      this.restoreEndpoint(var2);
   }

   private void restoreEndpoint(IntegrationEntityLookup var1) {
      Optional var2 = EntityUtils.readId(this.endpointIdClass, this, this.getEndpointIdPropertyName());
      ConnectorEndpoint var3 = var1.getEntity((IntegrationEntityId)var2.orNull(), this.endpointClass);
      synchronized (this.getDataLock()) {
         this.endpoint = (E)var3;
      }
   }

   @Override
   public final List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      ArrayList var2 = Lists.newArrayList();
      ConnectorEndpoint var3 = this.getEndpoint();
      if (var3 != null) {
         var2.add(new StrongEntityLink(var3, this));
      }

      return var2;
   }

   @Override
   public final void replaceReferences(ReferenceReplacementService var1) {
      synchronized (this.getDataLock()) {
         if (this.endpoint != null) {
            ConnectorEndpoint var3 = var1.getReplacement(this.endpoint.getID(), this.endpointClass);
            this.setNewEndpoint((E)var3);
         }
      }
   }

   @Override
   public final IntegrationEntityId getFunctionID() {
      return this.getID();
   }

   public final CallStructureSchema getInputSchema() {
      return this.getSchema("input_xsd_filename", "input_xsd_root");
   }

   private CallStructureSchema getSchema(String var1, String var2) {
      synchronized (this.getDataLock()) {
         String var4 = this.getProperty(var1);
         return Strings.isNullOrEmpty(var4) ? null : new CallStructureSchema(var4, this.getProperty(var2));
      }
   }

   public final void setInputSchema(CallStructureSchema var1) {
      this.setSchema(var1, "input_schema", "input_xsd_filename", "input_xsd_root");
   }

   private final void setSchema(CallStructureSchema var1, String var2, String var3, String var4) {
      CallStructureSchema var5;
      synchronized (this.getDataLock()) {
         var5 = this.getSchema(var3, var4);
         this.setProperty(var3, var1.getSchemaName());
         this.setProperty(var4, var1.getRootElement());
      }

      if (!Objects.equal(var5, var1)) {
         this.firePropertyChange(var2, var5, var1);
      }
   }

   public final CallStructureSchema getOutputSchema() {
      return this.getSchema("output_xsd_filename", "output_xsd_root");
   }

   public final void setOutputSchema(CallStructureSchema var1) {
      this.setSchema(var1, "output_schema", "output_xsd_filename", "output_xsd_root");
   }

   @Override
   public final boolean acceptsOutputFrom(Function var1) {
      XsdBasedInputStructure var2 = this.getInputStructure();
      DataStructure var3 = var1.getOutputStructure();
      return var2.equals(var3);
   }

   @Override
   public boolean hasRequestTransformation() {
      return true;
   }

   @Override
   public final XsdBasedInputStructure getInputStructure() {
      return new XsdBasedInputStructure(this.getID(), this.getInputSchema());
   }

   @Override
   public final XsdBasedOutputStructure getOutputStructure() {
      return new XsdBasedOutputStructure(this.getID(), this.getOutputSchema());
   }

   public abstract XsdNamingStrategy getXsdNamingStrategy();

   @Override
   public final void doPostImportProcessing(IntegrationEntityLookup var1) {
      this.restoreEndpoint(var1);
   }

   @Override
   protected final void importAdditionalDataFrom(IntegrationEntity var1) {
      String var2 = this.getEndpointIdPropertyName();
      Optional var3 = EntityUtils.readId(this.endpointIdClass, var1, var2);
      EntityUtils.storeId(var3, this.endpointIdClass, this, var2);
   }

   @OverridingMethodsMustInvokeSuper
   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      ConnectorEndpoint var2 = this.getEndpoint();
      if (var2 == null) {
         var1.addMessage("Must select an endpoint.", ValidationStatus.INVALID);
      }
   }
}
