package org.etlforge.integration.data.entity;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.lang.Objects;
import org.etlforge.system.ArgCheck;
import org.etlforge.xml.ExpandedName;

public interface SalesforceWizardEntity {
   String getName();

   IntegrationEntityId getID();

   EntityType getEntityType();

   SfdcMethod getMethod();

   OperationId getImplementingOperationId();

   void setImplementingOperation(Operation var1);

   SalesforceConnectorId getConnectorId();

   void setConnector(SalesforceConnector var1);

   Set<SalesforceWizardEntity.Header> getSoapHeaders();

   void setSoapHeaders(Set<SalesforceWizardEntity.Header> var1);

   ExpandedName getObject();

   void setFileEncodingName(String var1);

   String getFileEncodingName();

   final class Header {
      public final String name;
      public final String value;

      public Header(String var1, String var2) {
         ArgCheck.checkNotEmpty(var1);
         Preconditions.checkNotNull(var2);
         this.name = var1;
         this.value = var2;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.name, this.value);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof SalesforceWizardEntity.Header)) {
            return false;
         }

         SalesforceWizardEntity.Header var2 = (SalesforceWizardEntity.Header)var1;
         return this.name.equals(var2.name) && this.value.equals(var2.value);
      }

      @Override
      public String toString() {
         return this.name + "=" + this.value;
      }
   }
}
