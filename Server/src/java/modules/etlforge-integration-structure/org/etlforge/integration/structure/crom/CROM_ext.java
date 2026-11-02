package org.etlforge.integration.structure.crom;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import org.etlforge.integration.structure.mapping.StringIndexMap;

public abstract class CROM_ext implements Cloneable {
   public CROM_ext clone() {
      try {
         return (CROM_ext)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException("Will not happen because we implement Cloneable");
      }
   }

   public void addTypeExtension(String var1) {
   }

   public void addSubstitutionGroup(String var1) {
   }

   public int getDbColSize() {
      return 0;
   }

   public int getDbColType() {
      return 0;
   }

   public int getDbDecimalDigits() {
      return 0;
   }

   public String getDbSchema() {
      return null;
   }

   public String getDefaultValue() {
      return null;
   }

   public String getDelimiter() {
      return "";
   }

   public String getDomain() {
      return null;
   }

   public short getFieldLength() {
      return 0;
   }

   public short getFieldPos() {
      return 0;
   }

   public String getFormat() {
      return null;
   }

   public int getIdentLength() {
      return 0;
   }

   public int getIdentPos() {
      return 0;
   }

   public String getLinkToPrimaryKey() {
      return null;
   }

   public String getNamespace() {
      return "";
   }

   public String getNumQual() {
      return "";
   }

   public boolean getNSQualify() {
      return false;
   }

   public String getSchemaType() {
      return null;
   }

   public String getSchemaTypeExpandedName() {
      return null;
   }

   public String getSchemaTypeNamespace() {
      return "";
   }

   public String getSourceExpr() {
      return null;
   }

   public String getSqlFilter() {
      return null;
   }

   public String getSqlOrderBy() {
      return null;
   }

   public String getSqlTableName() {
      return null;
   }

   public String getStrQual() {
      return "";
   }

   public Set<String> getSubsitutionGroup() {
      return null;
   }

   public Set<String> getTypeExtensions() {
      return null;
   }

   public String getValue() {
      return null;
   }

   public String getValueType() {
      return null;
   }

   public boolean isAbstract() {
      return false;
   }

   public boolean isXml() {
      return false;
   }

   public boolean hasDerivedTypes() {
      return false;
   }

   public boolean hasSubstitutionGroup() {
      return false;
   }

   public boolean isMapped() {
      return false;
   }

   public void setDbColSize(int var1) {
   }

   public void setDbColType(int var1) {
   }

   public void setDbDecimalDigits(int var1) {
   }

   public void setDbSchema(String var1) {
   }

   public void setDefaultValue(String var1) {
   }

   public void setDelimiter(String var1) {
   }

   public void setDomain(String var1) {
   }

   public void setFieldLength(short var1) {
   }

   public void setFieldPos(short var1) {
   }

   public void setFKCounter(int var1) {
   }

   public void setFormat(String var1) {
   }

   public void setIdentLength(int var1) {
   }

   public void setIdentPos(int var1) {
   }

   public void setLinkToPrimaryKey(String var1) {
   }

   public void setNamespace(String var1) {
   }

   public void setNSQualify(boolean var1) {
   }

   public void setNumQual(String var1) {
   }

   public void setSchemaType(String var1) {
   }

   public void setSchemaTypeNamespace(String var1) {
   }

   public void setSourceExpr(String var1) {
   }

   public void setSqlFilter(String var1) {
   }

   public void setSqlOrderBy(String var1) {
   }

   public void setSqlTableName(String var1) {
   }

   public void setStrQual(String var1) {
   }

   public void setValue(String var1) {
   }

   public void setValueType(String var1) {
   }

   public void writeXml(Writer var1, StringIndexMap var2) throws IOException {
   }

   public void clearSubstitutionGroupContainer() {
   }
}
