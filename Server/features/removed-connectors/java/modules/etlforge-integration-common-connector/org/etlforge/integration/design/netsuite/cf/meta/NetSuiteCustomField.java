package org.etlforge.integration.design.netsuite.cf.meta;

import com.google.common.base.Strings;
import java.io.Serializable;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class NetSuiteCustomField implements Serializable {
   public static final String CUSTOMLABELNAME = "label";
   public static final String INTERNALIDNAME = "internalId";
   public static final String SCRIPTIDNAME = "scriptId";
   public static final String FIELDTYPENAME = "fieldType";
   private final String customLabel;
   private final String internalId;
   private final String fieldType;
   private final String scriptId;

   public NetSuiteCustomField(String var1, String var2, String var3, String var4) {
      if (Strings.isNullOrEmpty(var2)) {
         throw new ConnectorNetSuiteDefinitionError("internalId can not be empty for custom field");
      }

      this.internalId = var2.toLowerCase();
      this.scriptId = Strings.isNullOrEmpty(var3) ? this.internalId : var3.toLowerCase();
      this.customLabel = var1;
      this.fieldType = var4;
   }

   public static boolean isCustomFieldName(String var0) {
      return var0.equals("label") || var0.equals("fieldType") || var0.equals("scriptId") || var0.equals("internalId");
   }

   public boolean isInvalid() {
      return Strings.isNullOrEmpty(this.getPrimaryKey());
   }

   public String getPrimaryKey() {
      return this.getInternalId();
   }

   @Override
   public String toString() {
      return "customLabel="
         + this.getCustomLabel()
         + ";internalId="
         + this.getInternalId()
         + ";scriptId="
         + this.getScriptId()
         + ";fieldType="
         + this.getFieldType();
   }

   public String getScriptId() {
      return this.scriptId;
   }

   public String getInternalId() {
      return this.internalId;
   }

   public String getCustomLabel() {
      return this.customLabel;
   }

   public String getFieldType() {
      return this.fieldType;
   }
}
