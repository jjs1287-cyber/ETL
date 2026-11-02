package org.etlforge.integration.salesforce.bulkload;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.lang.Objects;

public final class CreateJobParams {
   public final String objectName;
   public final BulkAction action;
   public final String externalIdField;
   public final boolean serialMode;
   public final String assignmentRuleId;

   public static CreateJobParams forObject(SObjectSchema var0, BulkAction var1, String var2) {
      return new CreateJobParams(var0.getName(), var1, var2);
   }

   public CreateJobParams(String var1, BulkAction var2, String var3) {
      this(var1, var2, var3, false, null);
   }

   public CreateJobParams(String var1, BulkAction var2, String var3, boolean var4, String var5) {
      Preconditions.checkNotNull(var2, "action");
      this.objectName = var1;
      this.action = var2;
      if (var2.requiresExternalId()) {
         Preconditions.checkArgument(StringUtils.isNotBlank(var3), "must provide an external ID field");
         this.externalIdField = var3;
      } else {
         this.externalIdField = "";
      }

      this.serialMode = var4;
      this.assignmentRuleId = var5;
   }

   public boolean isQuery() {
      return this.action == BulkAction.QUERY;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.objectName, this.action, this.externalIdField, this.serialMode);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof CreateJobParams)) {
         return false;
      }

      CreateJobParams var2 = (CreateJobParams)var1;
      return this.objectName.equals(var2.objectName)
         && this.action == var2.action
         && Objects.equal(this.externalIdField, var2.externalIdField)
         && this.serialMode == var2.serialMode;
   }

   @Override
   public String toString() {
      return String.format(
         "Object: %s, Operation: %s, External ID: %s", this.objectName, this.action, this.externalIdField.isEmpty() ? "[none]" : this.externalIdField
      );
   }
}
