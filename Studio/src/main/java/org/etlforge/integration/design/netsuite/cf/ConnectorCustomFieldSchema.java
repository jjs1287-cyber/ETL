package org.etlforge.integration.design.netsuite.cf;

import com.google.common.base.Objects;
import org.etlforge.integration.design.util.XmlElementNameSanitizer;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class ConnectorCustomFieldSchema {
   private final String customFieldXmlName;
   private final String netsuiteId;
   private final String netsuiteType;
   private final String fieldType;
   private final boolean usingScriptId;
   private final String customFieldInternalIDXmlName;

   public ConnectorCustomFieldSchema(String var1, boolean var2, String var3, String var4) {
      this(var1, var2, var3, var4, null);
   }

   public ConnectorCustomFieldSchema(String var1, boolean var2, String var3, String var4, String var5) {
      this.customFieldXmlName = var1;
      this.netsuiteId = var3;
      this.netsuiteType = var4;
      this.fieldType = var5 == null ? var4 : var5;
      this.usingScriptId = var2;
      this.customFieldInternalIDXmlName = XmlElementNameSanitizer.getSanitizedName(this.netsuiteId);
   }

   public String getCustomFieldXmlName() {
      return this.customFieldXmlName;
   }

   public String getNetsuiteId() {
      return this.netsuiteId;
   }

   public String getNetSuiteIdName() {
      return this.usingScriptId ? "scriptId" : "internalId";
   }

   public String getNetsuiteType() {
      return this.netsuiteType;
   }

   public XsdGlobalElementName getNetsuiteType(String var1) {
      return XsdGlobalElementName.newType(var1, this.netsuiteType);
   }

   public String getCustomFieldTypeRefName(String var1) {
      return var1 + this.customFieldInternalIDXmlName + this.fieldType;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("customFieldXmlName", this.customFieldXmlName)
         .add("netsuiteId", this.netsuiteId)
         .add("fieldType", this.fieldType)
         .add("netsuiteType", this.netsuiteType)
         .add("usingScriptId", this.usingScriptId)
         .toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof ConnectorCustomFieldSchema) {
         ConnectorCustomFieldSchema var2 = (ConnectorCustomFieldSchema)var1;
         return this.toString().equals(var2.toString());
      } else {
         return false;
      }
   }
}
