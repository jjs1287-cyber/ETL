package org.etlforge.integration.salesforce.wsdl;

import java.util.List;

final class ConstantField implements SObjectSchema.Field {
   private static final long serialVersionUID = 3556920068241763815L;
   private final SObjectSchema.Field wrappee;

   static SObjectSchema.Field createConstantIdField() {
      DefaultSObjectField var0 = new DefaultSObjectField("Id", SObjectSchema.Field.Type.ID, SObjectSchema.Field.SoapType.ID, false, true);
      var0.setUpdateable(false);
      var0.setCreateable(false);
      return new ConstantField(var0);
   }

   public ConstantField(SObjectSchema.Field var1) {
      this.wrappee = var1;
   }

   @Override
   public String getName() {
      return this.wrappee.getName();
   }

   @Override
   public String getLabel() {
      return this.wrappee.getLabel();
   }

   @Override
   public SObjectSchema.Field.Type getType() {
      return this.wrappee.getType();
   }

   @Override
   public SObjectSchema.Field.SoapType getSoapType() {
      return this.wrappee.getSoapType();
   }

   @Override
   public boolean isExternalId() {
      return this.wrappee.isExternalId();
   }

   @Override
   public boolean isIdLookup() {
      return this.wrappee.isIdLookup();
   }

   @Override
   public boolean isCreateable() {
      return this.wrappee.isCreateable();
   }

   @Override
   public boolean isUpdateable() {
      return this.wrappee.isUpdateable();
   }

   @Override
   public boolean isFilterable() {
      return this.wrappee.isFilterable();
   }

   @Override
   public Picklist getPicklist() {
      return this.wrappee.getPicklist();
   }

   @Override
   public List<String> getReferenceTo() {
      return this.wrappee.getReferenceTo();
   }

   @Override
   public String getRelationshipName() {
      return this.wrappee.getRelationshipName();
   }

   @Override
   public int hashCode() {
      return this.wrappee.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return this.wrappee.equals(var1);
   }

   @Override
   public String toString() {
      return this.wrappee.toString();
   }
}
