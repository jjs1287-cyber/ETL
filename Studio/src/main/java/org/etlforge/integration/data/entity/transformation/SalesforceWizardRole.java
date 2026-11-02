package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Preconditions;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class SalesforceWizardRole {
   public final SfdcMethod method;
   public final SalesforceWizardRole.Position position;

   public static SalesforceWizardRole request(SfdcMethod var0) {
      return new SalesforceWizardRole(var0, SalesforceWizardRole.Position.REQUEST);
   }

   public static SalesforceWizardRole response(SfdcMethod var0) {
      return new SalesforceWizardRole(var0, SalesforceWizardRole.Position.RESPONSE);
   }

   public SalesforceWizardRole(SfdcMethod var1, SalesforceWizardRole.Position var2) {
      this.method = Preconditions.checkNotNull(var1, "method");
      this.position = Preconditions.checkNotNull(var2, "role");
   }

   public boolean isRequest() {
      return this.position == SalesforceWizardRole.Position.REQUEST;
   }

   public boolean isResponse() {
      return this.position == SalesforceWizardRole.Position.RESPONSE;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SalesforceWizardRole)) {
         return false;
      }

      SalesforceWizardRole var2 = (SalesforceWizardRole)var1;
      return this.method == var2.method && this.position == var2.position;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.method.hashCode();
      return 31 * var1 + this.position.hashCode();
   }

   @Override
   public String toString() {
      return this.method.name() + "|" + this.position.name();
   }

   public static SalesforceWizardRole fromString(String var0) {
      int var1 = var0.lastIndexOf(124);
      Preconditions.checkArgument(var1 > 0 && var1 < var0.length() - 1, "Invalid string: the separator is missing or in an invalid position");
      String var2 = var0.substring(0, var1);
      String var3 = var0.substring(var1 + 1);
      return new SalesforceWizardRole(SfdcMethod.valueOf(var2), SalesforceWizardRole.Position.valueOf(var3));
   }

   public enum Position {
      REQUEST,
      RESPONSE;
   }
}
