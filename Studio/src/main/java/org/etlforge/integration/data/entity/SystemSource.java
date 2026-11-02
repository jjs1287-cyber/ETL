package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class SystemSource extends Source implements BuiltInIntegrationEntity {
   public static final SystemSource SOURCE_FROM_PREVIOUS_OPERATION = new SystemSource(
      new SourceId("d1b5723a-3aaa-4205-846d-dcca925e901e"), "(Use previous operation target as source)"
   );
   public static final SystemSource WEB_SERVICE_CALL = new SystemSource(new SourceId("00c9a0f4-8e83-460f-a203-d1d11c9a7254"), "(Web Service Call)");
   public static final SystemSource FROM_HTTP_ENDPOINT = new SystemSource(new SourceId("c641c074-95e3-4b65-99a8-94e2916b1fe8"), "(Source from HTTP Endpoint)");
   private boolean addedToRootFolder = false;

   private SystemSource(SourceId var1, String var2) {
      super(var1, var2, null);
      this.setEntityType(EntityType.SystemSource);
   }

   @Override
   public boolean isDeletable() {
      return false;
   }

   @Override
   public boolean isEditable() {
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
   public void setLocation(DataLocation var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setName(String var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public synchronized void setParent(IntegrationEntity var1) {
      if (this.addedToRootFolder) {
         throw new UnsupportedOperationException();
      }

      super.setParent(var1);
   }

   @Override
   public void setDescription(String var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean canHaveHistory() {
      return false;
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
   }

   static {
      SOURCE_FROM_PREVIOUS_OPERATION.addedToRootFolder = true;
      FROM_HTTP_ENDPOINT.addedToRootFolder = true;
      WEB_SERVICE_CALL.addedToRootFolder = true;
   }
}
