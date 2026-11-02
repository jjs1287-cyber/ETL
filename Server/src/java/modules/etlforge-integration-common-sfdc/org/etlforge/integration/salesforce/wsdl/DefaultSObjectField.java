package org.etlforge.integration.salesforce.wsdl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.axis.utils.StringUtils;

public final class DefaultSObjectField implements SObjectSchema.Field {
   private static final long serialVersionUID = -8043409629763914923L;
   private final String name;
   private volatile String label;
   private final SObjectSchema.Field.Type type;
   private final SObjectSchema.Field.SoapType soapType;
   private boolean externalId;
   private boolean idLookup;
   private volatile boolean createable = true;
   private volatile boolean updateable = true;
   private volatile boolean filterable = true;
   private volatile String relationshipName;
   private volatile Picklist picklist = Picklist.EMPTY;
   private final List<String> referenceTo = Collections.synchronizedList(new ArrayList<>());

   public DefaultSObjectField(String var1, SObjectSchema.Field.Type var2, SObjectSchema.Field.SoapType var3) {
      this(var1, var2, var3, false, false);
   }

   public DefaultSObjectField(String var1, SObjectSchema.Field.Type var2, SObjectSchema.Field.SoapType var3, boolean var4, boolean var5) {
      this.name = var1;
      this.label = var1;
      this.type = var2;
      this.soapType = var3;
      this.externalId = var4;
      this.idLookup = var5;
   }

   public DefaultSObjectField(SObjectSchema.Field var1) {
      this(var1, var1.getName());
   }

   public DefaultSObjectField(SObjectSchema.Field var1, String var2) {
      this.name = var2;
      this.label = var1.getLabel();
      this.type = var1.getType();
      this.soapType = var1.getSoapType();
      this.externalId = var1.isExternalId();
      this.idLookup = var1.isIdLookup();
      this.createable = var1.isCreateable();
      this.updateable = var1.isUpdateable();
      this.filterable = var1.isFilterable();
      this.relationshipName = var1.getRelationshipName();
      this.referenceTo.addAll(var1.getReferenceTo());
      this.picklist = var1.getPicklist();
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String getLabel() {
      return StringUtils.isEmpty(this.label) ? this.name : this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   @Override
   public SObjectSchema.Field.Type getType() {
      return this.type;
   }

   @Override
   public SObjectSchema.Field.SoapType getSoapType() {
      return this.soapType;
   }

   public void setExternalId(boolean var1) {
      this.externalId = var1;
   }

   @Override
   public boolean isExternalId() {
      return this.externalId;
   }

   public void setIdLookup(boolean var1) {
      this.idLookup = var1;
   }

   @Override
   public boolean isIdLookup() {
      return this.idLookup;
   }

   @Override
   public boolean isCreateable() {
      return this.createable;
   }

   public void setCreateable(boolean var1) {
      this.createable = var1;
   }

   @Override
   public boolean isUpdateable() {
      return this.updateable;
   }

   public void setUpdateable(boolean var1) {
      this.updateable = var1;
   }

   public void setFilterable(boolean var1) {
      this.filterable = var1;
   }

   @Override
   public boolean isFilterable() {
      return this.filterable;
   }

   @Override
   public Picklist getPicklist() {
      return this.picklist;
   }

   public void setPicklist(Picklist var1) {
      this.picklist = Preconditions.checkNotNull(var1);
   }

   @Override
   public List<String> getReferenceTo() {
      return Lists.newArrayList(this.referenceTo);
   }

   public void addReferenceTo(String var1) {
      this.referenceTo.add(var1);
   }

   @Override
   public String getRelationshipName() {
      return this.relationshipName;
   }

   public void setRelationshipName(String var1) {
      this.relationshipName = var1;
   }

   @Override
   public String toString() {
      return this.name;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(
         this.name,
         this.type,
         this.soapType,
         this.externalId,
         this.idLookup,
         this.filterable,
         this.createable,
         this.updateable,
         this.relationshipName,
         this.referenceTo
      );
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SObjectSchema.Field)) {
         return false;
      }

      SObjectSchema.Field var2 = (SObjectSchema.Field)var1;
      return this.name.equals(var2.getName())
         && this.type == var2.getType()
         && this.soapType == var2.getSoapType()
         && this.externalId == var2.isExternalId()
         && this.idLookup == var2.isIdLookup()
         && this.filterable == var2.isFilterable()
         && this.createable == var2.isCreateable()
         && this.updateable == var2.isUpdateable()
         && Objects.equal(this.relationshipName, var2.getRelationshipName())
         && this.referenceTo.equals(var2.getReferenceTo());
   }

   public static final class BooleanAttributes extends AbstractBooleanAttributes {
      public BooleanAttributes(SObjectSchema.Field var1) {
         this(var1.isExternalId(), var1.isIdLookup(), var1.isCreateable(), var1.isUpdateable(), var1.isFilterable());
      }

      @VisibleForTesting
      BooleanAttributes(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5) {
         this(new boolean[]{var1, var2, var3, var4, var5});
      }

      @VisibleForTesting
      BooleanAttributes(boolean[] var1) {
         super(var1);
         assert var1.length == 5;
      }

      public void applyTo(DefaultSObjectField var1) {
         var1.setExternalId(this.values[0]);
         var1.setIdLookup(this.values[1]);
         var1.setCreateable(this.values[2]);
         var1.setUpdateable(this.values[3]);
         var1.setFilterable(this.values[4]);
      }

      public static DefaultSObjectField.BooleanAttributes deserializeFromInt(int var0) {
         boolean[] var1 = deserializeArray(var0, 5);
         return new DefaultSObjectField.BooleanAttributes(var1);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof DefaultSObjectField.BooleanAttributes) {
            DefaultSObjectField.BooleanAttributes var2 = (DefaultSObjectField.BooleanAttributes)var1;
            return Arrays.equals(this.values, var2.values);
         } else {
            return false;
         }
      }
   }
}
