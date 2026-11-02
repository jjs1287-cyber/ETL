package org.etlforge.integration.ldap.structure;

public class LdapStructureAttributeBuilder {
   private NumericOid oid;
   private String name;
   private String description;
   private String usageDescription;
   private AttributeSyntaxInfo syntaxInfo;
   private AttributeUse use = AttributeUse.OPTIONAL;
   private AttributeCardinality cardinality = AttributeCardinality.ONE;
   private boolean readOnly;

   public void setOid(NumericOid var1) {
      if (var1 == null) {
         throw new NullPointerException("oid");
      }

      this.oid = var1;
   }

   public void setName(String var1) {
      if (var1 == null) {
         this.name = "";
      } else {
         this.name = var1.trim();
      }
   }

   public void setDescription(String var1) {
      this.description = var1 != null ? var1 : "";
   }

   public void setUsageDecription(String var1) {
      this.usageDescription = var1 != null ? var1 : "";
   }

   public void setSyntaxInfo(AttributeSyntaxInfo var1) {
      this.syntaxInfo = var1;
   }

   public void setUse(AttributeUse var1) {
      if (var1 == null) {
         throw new NullPointerException("use");
      }

      this.use = var1;
   }

   public void setCardinality(AttributeCardinality var1) {
      if (var1 == null) {
         throw new NullPointerException("cardinality");
      }

      this.cardinality = var1;
   }

   public void setReadOnly(boolean var1) {
      this.readOnly = var1;
   }

   public LdapStructureAttribute getAttribute() {
      if (this.oid == null) {
         throw new IllegalStateException("Must set the OID first");
      } else if (this.syntaxInfo == null) {
         throw new IllegalStateException("Must set the syntax info first");
      } else {
         return new LdapStructureAttributeImpl(
            this.oid, this.name, this.description, this.usageDescription, this.syntaxInfo, this.use, this.cardinality, this.readOnly
         );
      }
   }
}
