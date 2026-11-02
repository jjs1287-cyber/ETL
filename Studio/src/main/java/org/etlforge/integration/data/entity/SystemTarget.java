package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class SystemTarget extends Target implements BuiltInIntegrationEntity {
   public static final SystemTarget WEB_SERVICE_CALL = new SystemTarget(new TargetId("ae7d3139-d6fc-4a53-b75f-fd7ae6623933"), "(Web Service Call)");
   private boolean addedToRootFolder;

   public static SystemTarget[] getSystemTargets() {
      return new SystemTarget[]{WEB_SERVICE_CALL};
   }

   private SystemTarget(TargetId var1, String var2) {
      super(var1, var2, null);
      this.setEntityType(EntityType.SystemTarget);
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
   public void setLocation(DataLocation var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setName(String var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setParent(IntegrationEntity var1) {
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
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
   }

   static {
      WEB_SERVICE_CALL.addedToRootFolder = true;
   }
}
