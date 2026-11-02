package org.etlforge.integration.structure.crom;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import org.etlforge.integration.structure.mapping.JtrXmlFormatUtils;
import org.etlforge.integration.structure.mapping.StringIndexMap;
import org.xml.sax.Attributes;

public class CROM_Attr extends CROM_ext {
   public static final String ELEMENT = "Attr";
   public static final String NAMESPACE_QUALIFIED = "NSQual";
   public static final String FIELD_POSITION = "FieldPos";
   public static final String FIELD_LENGTH = "FieldLength";
   public static final String DB_COL_TYPE = "DbColType";
   public static final String DB_COL_SIZE = "DbColSize";
   public static final String DB_DEC_DIGITS = "DbDecDigits";
   public static final String VALUE = "Value";
   public static final String VALUE_TYPE = "ValType";
   public static final String DEFAULT_VALUE = "DefVal";
   public static final String FORMAT = "Format";
   public static final String DOMAIN = "Domain";
   public static final String LINK_TO_PRIMARY_KEY = "LinkToPrimaryKey";
   public static final String NAMESPACE = "NS";
   public static final String SCHEMA_TYPE = "Type";
   public static final String SCHEMA_TYPE_NAMESPACE = "TypeNS";
   public static final String SOURCE_EXPR = "SourceExpr";
   private short fieldPos = -1;
   private short fieldLength = -1;
   private int dbColType;
   private int dbColSize;
   private int dbDecimalDigits;
   private String value;
   private String valueType;
   private String defaultValue;
   private String format;
   private String domain;
   private String linkToPrimaryKey;
   private String sourceExpr;
   private ExtraAttribute extra;

   public CROM_Attr() {
   }

   public CROM_Attr(Attributes var1, Map<Integer, String> var2) {
      this.setNSQualify(JtrXmlFormatUtils.getBool(var1, "NSQual", false));
      this.setFieldPos(JtrXmlFormatUtils.getShort(var1, "FieldPos", -1));
      this.setFieldLength(JtrXmlFormatUtils.getShort(var1, "FieldLength", -1));
      this.setDbColType(JtrXmlFormatUtils.getInt(var1, "DbColType", 0));
      this.setDbColSize(JtrXmlFormatUtils.getInt(var1, "DbColSize", 0));
      this.setDbDecimalDigits(JtrXmlFormatUtils.getInt(var1, "DbDecDigits", 0));
      this.setValue(JtrXmlFormatUtils.getString(var1, "Value"));
      this.setValueType(JtrXmlFormatUtils.lookupIndexedString(var1, "ValType", var2));
      this.setDefaultValue(JtrXmlFormatUtils.getString(var1, "DefVal", ""));
      this.setFormat(JtrXmlFormatUtils.getString(var1, "Format"));
      this.setDomain(JtrXmlFormatUtils.getString(var1, "Domain"));
      this.setLinkToPrimaryKey(JtrXmlFormatUtils.getString(var1, "LinkToPrimaryKey"));
      this.setSourceExpr(JtrXmlFormatUtils.getString(var1, "SourceExpr", ""));
      this.setNamespace(JtrXmlFormatUtils.lookupIndexedString(var1, "NS", var2));
      this.setSchemaType(JtrXmlFormatUtils.lookupIndexedString(var1, "Type", var2));
      this.setSchemaTypeNamespace(JtrXmlFormatUtils.lookupIndexedString(var1, "TypeNS", var2));
   }

   @Override
   public void setFieldPos(short var1) {
      this.fieldPos = var1;
   }

   @Override
   public void setFieldLength(short var1) {
      this.fieldLength = var1;
   }

   @Override
   public void setDbColType(int var1) {
      this.dbColType = var1;
   }

   @Override
   public void setDbColSize(int var1) {
      this.dbColSize = var1;
   }

   @Override
   public void setDbDecimalDigits(int var1) {
      this.dbDecimalDigits = var1;
   }

   @Override
   public void setValue(String var1) {
      this.value = var1;
   }

   @Override
   public void setFormat(String var1) {
      this.format = var1;
   }

   @Override
   public void setLinkToPrimaryKey(String var1) {
      this.linkToPrimaryKey = var1;
   }

   @Override
   public void setSourceExpr(String var1) {
      this.sourceExpr = var1;
   }

   @Override
   public void setValueType(String var1) {
      this.valueType = var1;
   }

   @Override
   public void setDefaultValue(String var1) {
      this.defaultValue = var1;
   }

   @Override
   public void setDomain(String var1) {
      this.domain = var1;
   }

   @Override
   public void setNSQualify(boolean var1) {
      if (this.extra == null) {
         this.extra = new ExtraAttribute();
      }

      this.extra.m_NSQualify = var1;
   }

   @Override
   public void setNamespace(String var1) {
      if (this.extra == null) {
         this.extra = new ExtraAttribute();
      }

      this.extra.m_Namespace = var1;
   }

   @Override
   public void setSchemaType(String var1) {
      if (this.extra == null) {
         this.extra = new ExtraAttribute();
      }

      this.extra.m_SchemaType = var1;
   }

   @Override
   public void setSchemaTypeNamespace(String var1) {
      if (this.extra == null) {
         this.extra = new ExtraAttribute();
      }

      this.extra.m_SchemaTypeNamespace = var1;
   }

   @Override
   public String getValue() {
      return this.value;
   }

   @Override
   public String getValueType() {
      return this.valueType;
   }

   @Override
   public int getDbColType() {
      return this.dbColType;
   }

   @Override
   public int getDbColSize() {
      return this.dbColSize;
   }

   @Override
   public int getDbDecimalDigits() {
      return this.dbDecimalDigits;
   }

   @Override
   public String getDefaultValue() {
      return this.defaultValue;
   }

   @Override
   public String getDomain() {
      return this.domain;
   }

   @Override
   public String getFormat() {
      return this.format;
   }

   @Override
   public String getLinkToPrimaryKey() {
      return this.linkToPrimaryKey;
   }

   @Override
   public boolean getNSQualify() {
      return this.extra == null ? false : this.extra.m_NSQualify;
   }

   @Override
   public String getNamespace() {
      return this.extra == null ? null : this.extra.m_Namespace;
   }

   @Override
   public String getSchemaType() {
      return this.extra == null ? null : this.extra.m_SchemaType;
   }

   @Override
   public String getSchemaTypeNamespace() {
      return this.extra == null ? null : this.extra.m_SchemaTypeNamespace;
   }

   @Override
   public String getSourceExpr() {
      return this.sourceExpr;
   }

   @Override
   public short getFieldLength() {
      return this.fieldLength;
   }

   @Override
   public short getFieldPos() {
      return this.fieldPos;
   }

   @Override
   public boolean isMapped() {
      return this.sourceExpr != null && !this.sourceExpr.isEmpty();
   }

   @Override
   public void writeXml(Writer var1, StringIndexMap var2) throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append('<').append("Attr");
      JtrXmlFormatUtils.addAttribute(var3, "NSQual", this.getNSQualify(), false);
      JtrXmlFormatUtils.addAttribute(var3, "FieldPos", this.fieldPos, -1);
      JtrXmlFormatUtils.addAttribute(var3, "FieldLength", this.fieldLength, -1);
      JtrXmlFormatUtils.addAttribute(var3, "DbColType", this.dbColType, 0);
      JtrXmlFormatUtils.addAttribute(var3, "DbColSize", this.dbColSize, 0);
      JtrXmlFormatUtils.addAttribute(var3, "DbDecDigits", this.dbDecimalDigits, 0);
      JtrXmlFormatUtils.addAttribute(var3, "Value", this.getValue());
      JtrXmlFormatUtils.addAttribute(var3, "ValType", var2.store(this.getValueType()));
      JtrXmlFormatUtils.addAttribute(var3, "DefVal", this.getDefaultValue());
      JtrXmlFormatUtils.addAttribute(var3, "Format", this.format);
      JtrXmlFormatUtils.addAttribute(var3, "Domain", this.getDomain());
      JtrXmlFormatUtils.addAttribute(var3, "LinkToPrimaryKey", this.linkToPrimaryKey);
      JtrXmlFormatUtils.addAttribute(var3, "NS", var2.store(this.getNamespace()));
      JtrXmlFormatUtils.addAttribute(var3, "Type", var2.store(this.getSchemaType()));
      JtrXmlFormatUtils.addAttribute(var3, "TypeNS", var2.store(this.getSchemaTypeNamespace()));
      var3.append("/>\n");
      var1.write(var3.toString());
   }

   public CROM_Attr clone() {
      CROM_Attr var1 = (CROM_Attr)super.clone();
      if (this.extra != null) {
         var1.extra = this.extra.clone();
      }

      return var1;
   }
}
