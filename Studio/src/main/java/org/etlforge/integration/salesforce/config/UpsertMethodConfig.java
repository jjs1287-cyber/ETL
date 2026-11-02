package org.etlforge.integration.salesforce.config;

public final class UpsertMethodConfig extends DefaultSfdcMethodConfig {
   public UpsertMethodConfig() {
      super("upsert");
      this.addProperty("TruncateFieldsHeaderPath", "root$transaction.header$AllowFieldTruncationHeader$allowFieldTruncation$");
      this.addProperty("AutoResponseEmailHeaderPath", "root$transaction.header$EmailHeader$triggerAutoResponseEmail$");
      this.addProperty("OtherEmailHeaderPath", "root$transaction.header$EmailHeader$triggerOtherEmail$");
      this.addProperty("UserEmailHeaderPath", "root$transaction.header$EmailHeader$triggerUserEmail$");
      this.addProperty("DebugLevelHeaderPath", "root$transaction.header$DebuggingHeader$debugLevel$");
      this.addProperty("ExternalIdFieldPath", "root$transaction.body$upsert$externalIDFieldName$");
      this.addProperty("ResponseSuccessPath", "root$transaction.response$body$upsertResponse$result.success$");
      this.addProperty("ResponseErrorPath", "root$transaction.response$body$upsertResponse$result.errors#.message$");
      this.addProperty("ResponseSfIdPath", "root$transaction.response$body$upsertResponse$result.id$");
      this.addProperty("ResponseExternalIdPath", "root$transaction.request$body$upsert$sObjects#.{0}${1}$");
      this.addConstantHeaderMapping(new ConstantHeaderMapping("root$transaction.header$AssignmentRuleHeader$useDefaultRule$", Boolean.TRUE));
      this.addConstantHeaderMapping(new ConstantHeaderMapping("root$transaction.header$DisableFeedTrackingHeader$disableFeedTracking$", Boolean.TRUE));
   }

   @Override
   public String getSimpleRootPathInRequest() {
      return "root$transaction.body$upsert$sObjects.";
   }

   @Override
   public String getSimpleRootPathInResponse() {
      return "root$transaction.request$body$upsert$sObjects.";
   }

   @Override
   public boolean requestHasSimpleMappingTreeRepresentation() {
      return true;
   }
}
