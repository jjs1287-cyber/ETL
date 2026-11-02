package org.etlforge.integration.ldap.structure;

import com.google.common.base.Objects;
import org.apache.commons.lang.StringUtils;

class LdapStructureAttributeImpl implements LdapStructureAttribute {
   private final NumericOid oid;
   private final String name;
   private final String description;
   private final String usageDescription;
   private final AttributeSyntaxInfo syntaxInfo;
   private final AttributeUse use;
   private final AttributeCardinality cardinality;
   private final boolean readOnly;

   LdapStructureAttributeImpl(
      NumericOid var1, String var2, String var3, String var4, AttributeSyntaxInfo var5, AttributeUse var6, AttributeCardinality var7, boolean var8
   ) {
      this.oid = var1;
      this.name = var2;
      this.description = var3;
      this.usageDescription = var4;
      this.syntaxInfo = var5;
      this.use = var6;
      this.cardinality = var7;
      this.readOnly = var8;
   }

   @Override
   public NumericOid getOid() {
      return this.oid;
   }

   @Override
   public AttributeCardinality getCardinality() {
      return this.cardinality;
   }

   @Override
   public String getUsageDescription() {
      return this.usageDescription;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public AttributeSyntaxInfo getSyntaxInfo() {
      return this.syntaxInfo;
   }

   @Override
   public AttributeUse getUse() {
      return this.use;
   }

   @Override
   public boolean isReadOnly() {
      return this.readOnly;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof LdapStructureAttribute)) {
         return false;
      }

      LdapStructureAttribute var2 = (LdapStructureAttribute)var1;
      return this.oid.equals(var2.getOid()) && Objects.equal(this.name, var2.getName());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.oid, this.name);
   }

   @Override
   public String toString() {
      return StringUtils.isNotEmpty(this.name) ? this.name : this.oid.toString();
   }
}
