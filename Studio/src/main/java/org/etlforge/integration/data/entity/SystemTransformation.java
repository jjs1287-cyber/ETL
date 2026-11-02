package org.etlforge.integration.data.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.transformation.SourceBatchParams;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.util.id.KongaID;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.persist.Persistor;

public class SystemTransformation extends Transformation implements BuiltInIntegrationEntity {
   public static final SystemTransformation PASS_THROUGH = createPassThrough();
   public static final SystemTransformation SALESFORCE_QUERY_REQUEST = createSalesforceQueryRequest();
   public static final SystemTransformation SALESFORCE_STATUS_RESPONSE = createSalesforceStatusResponse();
   private boolean readOnly;

   private SystemTransformation(TransformationId var1, String var2) {
      super(var1, var2);
   }

   @Override
   public boolean isEditable() {
      return false;
   }

   @Override
   public boolean isDeletable() {
      return false;
   }

   @Override
   public boolean isRenameable() {
      return false;
   }

   @Override
   public boolean isDeployDirty() {
      return false;
   }

   @Override
   public Date getCreatedAt() {
      return null;
   }

   @Override
   public Date getModifiedAt() {
      return null;
   }

   @Override
   public void addMapping(String var1) {
      this.checkModifiable();
      super.addMapping(var1);
   }

   @Override
   public void restoreDependentData(Persistor var1, IntegrationEntityLookup var2) {
      this.checkModifiable();
      super.restoreDependentData(var1, var2);
   }

   @Override
   public void setDbInsertMode(DbInsertMode var1) {
      this.checkModifiable();
      super.setDbInsertMode(var1);
   }

   @Override
   public void setInputStructure(DataStructure var1) {
      this.checkModifiable();
      super.setInputStructure(var1);
   }

   @Override
   public void setInvalidMappings(Collection<InvalidMapping> var1) {
      this.checkModifiable();
      super.setInvalidMappings(var1);
   }

   @Override
   public void setMapList(List<String> var1) {
      this.checkModifiable();
      super.setMapList(var1);
   }

   @Override
   public void setOutputStructure(DataStructure var1) {
      this.checkModifiable();
      super.setOutputStructure(var1);
   }

   @Override
   public void setRenamedNodes(Map<String, String> var1) {
      this.checkModifiable();
      super.setRenamedNodes(var1);
   }

   @Override
   public void setSourceId(SourceId var1) {
      this.checkModifiable();
      super.setSourceId(var1);
   }

   @Override
   public void setSourceTextDocumentId(TextDocumentId var1) {
      this.checkModifiable();
      super.setSourceTextDocumentId(var1);
   }

   @Override
   public void setTargetId(TargetId var1) {
      this.checkModifiable();
      super.setTargetId(var1);
   }

   @Override
   public void setTargetTextDocumentId(TextDocumentId var1) {
      this.checkModifiable();
      super.setTargetTextDocumentId(var1);
   }

   @Override
   protected void addChild(IntegrationEntity var1) {
      this.checkModifiable();
      super.addChild(var1);
   }

   @Override
   public synchronized boolean removeChild(IntegrationEntity var1) {
      this.checkModifiable();
      return super.removeChild(var1);
   }

   @Override
   public void setCreatedAt(Date var1) {
      this.checkModifiable();
      super.setCreatedAt(var1);
   }

   @Override
   public void setDeleted(boolean var1) {
      this.checkModifiable();
      super.setDeleted(var1);
   }

   @Override
   protected void setID(KongaID var1) {
      this.checkModifiable();
      super.setID(var1);
   }

   @Override
   public void setModifiedAt(Date var1) {
      this.checkModifiable();
      super.setModifiedAt(var1);
   }

   @Override
   public void setName(String var1) throws IllegalNameException {
      this.checkModifiable();
      super.setName(var1);
   }

   @Override
   public void setParent(IntegrationEntity var1) {
      this.checkModifiable();
      super.setParent(var1);
   }

   @Override
   protected void setProperty(String var1, String var2) {
      this.checkModifiable();
      super.setProperty(var1, var2);
   }

   @Override
   public void setDescription(String var1) {
      this.checkModifiable();
      super.setDescription(var1);
   }

   @Override
   public void setSourceBatchParams(SourceBatchParams var1) {
      this.checkModifiable();
      super.setSourceBatchParams(var1);
   }

   private void checkModifiable() {
      if (this.readOnly) {
         throw new UnsupportedOperationException("This Transformation is built-in and cannot be modified");
      }
   }

   private static SystemTransformation createPassThrough() {
      SystemTransformation var0 = new SystemTransformation(TransformationId.PASS_THROUGH_ID, "Pass-Through") {
         @Override
         public boolean acceptsOutputFrom(Function var1) {
            return true;
         }

         @Override
         public boolean canGetInputFrom(Source var1) {
            return true;
         }

         @Override
         public boolean canSendOutputTo(Target var1) {
            return true;
         }
      };
      var0.setInputStructure(new NoStructure());
      var0.setOutputStructure(new NoStructure());
      var0.readOnly = true;
      return var0;
   }

   private static SystemTransformation createSalesforceQueryRequest() {
      SystemTransformation var0 = new SystemTransformation(TransformationId.SALESFORCE_QUERY_REQUEST_ID, "Salesforce Query Request") {
         @Override
         public boolean acceptsOutputFrom(Function var1) {
            return false;
         }

         @Override
         public boolean canGetInputFrom(Source var1) {
            return false;
         }

         @Override
         public boolean canSendOutputTo(Target var1) {
            return false;
         }
      };
      var0.setInputStructure(new NoStructure());
      var0.setOutputStructure(new NoStructure());
      var0.readOnly = true;
      return var0;
   }

   private static SystemTransformation createSalesforceStatusResponse() {
      SystemTransformation var0 = new SystemTransformation(TransformationId.SALESFORCE_STATUS_RESPONSE_ID, "Salesforce Status Response") {
         @Override
         public boolean acceptsOutputFrom(Function var1) {
            return var1 instanceof WebServiceCall && ((WebServiceCall)var1).isPartOfSalesforcePartnerWizard();
         }

         @Override
         public boolean canGetInputFrom(Source var1) {
            return false;
         }

         @Override
         public boolean canSendOutputTo(Target var1) {
            return var1.getLocation().isFileBased();
         }
      };
      var0.setInputStructure(new NoStructure());
      var0.setOutputStructure(new NoStructure());
      var0.readOnly = true;
      return var0;
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
   }
}
