package org.etlforge.integration.design.netsuite.cf;

import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.util.XmlElementNameSanitizer;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class ConnectorNetSuiteCustomField {
   private String customFieldXmlName;
   private String customFieldInternalIDTypeName;
   private final NetSuiteCustomField nsCustomField;
   private static final char prefix = 'f';
   private static final int XMLNAMEMAXLENGTH = 100;
   private static final int XMLNAMEWITHIDMAXLENGTH = 70;
   private static final int XMLInternalIDTypeNAMEMAXLENGTH = 25;

   public ConnectorNetSuiteCustomField(NetSuiteCustomField var1) {
      this.nsCustomField = var1;
      this.customFieldXmlName = XmlElementNameSanitizer.getSanitizedName(var1.getCustomLabel(), 'f', 100);
      this.setCustomFieldInternalIDTypeName(XmlElementNameSanitizer.getSanitizedName(this.getPrimaryKey(), 'f', 25));
   }

   public String getPrimaryKey() {
      return this.nsCustomField.getInternalId();
   }

   void setCustomFieldXmlNameWithId() {
      if (this.customFieldXmlName.length() >= 70) {
         this.customFieldXmlName = this.customFieldXmlName.substring(0, 70);
      }

      this.customFieldXmlName = this.customFieldXmlName + "_" + this.getPrimaryKey();
      this.customFieldXmlName = XmlElementNameSanitizer.getSanitizedName(this.customFieldXmlName);
   }

   public String getCustomFieldXmlName() {
      return this.customFieldXmlName;
   }

   public String getCustomFieldTypeRefName(String var1) {
      return var1 + this.getCustomFieldInternalIDTypeName() + this.nsCustomField.getFieldType();
   }

   public XsdGlobalElementName getCustomFieldSearchColumn(NetSuiteCustomFieldTypeConverter var1, String var2) {
      return XsdGlobalElementName.newType(var2, this.getNetSuiteCustomFieldTypeExist(var1).getCustomFieldTypeSearchColumn());
   }

   public XsdGlobalElementName getCustomFieldRef(NetSuiteCustomFieldTypeConverter var1, String var2) {
      return XsdGlobalElementName.newType(var2, this.getNetSuiteCustomFieldTypeExist(var1).getCustomFieldType());
   }

   public NetSuiteCustomFieldType getNetSuiteCustomFieldTypeExist(NetSuiteCustomFieldTypeConverter var1) {
      return var1.getNSCustomFieldTypeExist(this.nsCustomField.getFieldType());
   }

   @Override
   public String toString() {
      return this.nsCustomField.toString();
   }

   public NetSuiteCustomField getNetSuiteCustomField() {
      return this.nsCustomField;
   }

   public String getCustomFieldInternalIDTypeName() {
      return this.customFieldInternalIDTypeName;
   }

   void setCustomFieldInternalIDTypeName(String var1) {
      this.customFieldInternalIDTypeName = var1;
   }
}
