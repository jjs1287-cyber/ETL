package org.etlforge.integration.salesforce.wsdl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public final class DefaultSObjectSchema implements SObjectSchema {
   private static final long serialVersionUID = 6259167426381430652L;
   private final String name;
   private volatile String label;
   private volatile String labelPlural;
   private volatile boolean custom;
   private volatile boolean queryable = true;
   private volatile boolean updatable = true;
   private volatile boolean creatable = true;
   private volatile boolean deletable = true;
   private final Set<SObjectSchema.Field> fields = Collections.synchronizedSet(new TreeSet<>(new DefaultFieldOrder()));
   private final Set<SObjectSchema.Parent> parents = Collections.synchronizedSet(new HashSet<>());
   private final Set<SObjectSchema.Child> children = Collections.synchronizedSet(new HashSet<>());

   public DefaultSObjectSchema(String var1) {
      this.name = var1;
   }

   public DefaultSObjectSchema(SObjectSchema var1) {
      this.name = var1.getName();
      this.label = var1.getLabel();
      this.labelPlural = var1.getLabelPlural();
      this.queryable = var1.isQueryable();
      this.updatable = var1.isUpdatable();
      this.creatable = var1.isCreatable();
      this.deletable = var1.isDeletable();
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public void setLabelPlural(String var1) {
      this.labelPlural = var1;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public ExpandedName getExpandedName() {
      XmlNameFactory var1 = XmlNameFactory.conforming();
      return var1.newExpandedName("urn:sobject.partner.soap.sforce.com", this.name);
   }

   @Override
   public String getLabel() {
      return this.label;
   }

   @Override
   public String getLabelPlural() {
      return this.labelPlural;
   }

   @Override
   public boolean isCustom() {
      return this.custom;
   }

   public void setCustom(boolean var1) {
      this.custom = var1;
   }

   @Override
   public boolean isQueryable() {
      return this.queryable;
   }

   public void setQueryable(boolean var1) {
      this.queryable = var1;
   }

   @Override
   public boolean isUpdatable() {
      return this.updatable;
   }

   public void setUpdatable(boolean var1) {
      this.updatable = var1;
   }

   @Override
   public boolean isCreatable() {
      return this.creatable;
   }

   public void setCreatable(boolean var1) {
      this.creatable = var1;
   }

   @Override
   public boolean isDeletable() {
      return this.deletable;
   }

   public void setDeletable(boolean var1) {
      this.deletable = var1;
   }

   @Override
   public List<SObjectSchema.Field> getFields() {
      return Lists.newArrayList(this.fields);
   }

   @Override
   public Iterable<SObjectSchema.Child> getChildren() {
      return ImmutableList.copyOf(this.children);
   }

   public void addField(SObjectSchema.Field var1) {
      this.fields.add(var1);
   }

   public void addChild(SObjectSchema.Child var1) {
      this.children.add(Preconditions.checkNotNull(var1));
   }

   public void addParent(SObjectSchema.Parent var1) {
      this.parents.add(Preconditions.checkNotNull(var1));
   }

   @Override
   public Iterable<SObjectSchema.Parent> getParents() {
      return ImmutableList.copyOf(this.parents);
   }

   @Override
   public String toString() {
      return this.name;
   }

   public static final class BooleanAttributes extends AbstractBooleanAttributes {
      public BooleanAttributes(SObjectSchema var1) {
         this(var1.isCustom(), var1.isQueryable(), var1.isCreatable(), var1.isUpdatable(), var1.isDeletable());
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

      public void applyTo(DefaultSObjectSchema var1) {
         var1.setCustom(this.values[0]);
         var1.setQueryable(this.values[1]);
         var1.setCreatable(this.values[2]);
         var1.setUpdatable(this.values[3]);
         var1.setDeletable(this.values[4]);
      }

      public static DefaultSObjectSchema.BooleanAttributes deserializeFromInt(int var0) {
         boolean[] var1 = deserializeArray(var0, 5);
         return new DefaultSObjectSchema.BooleanAttributes(var1);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof DefaultSObjectSchema.BooleanAttributes) {
            DefaultSObjectSchema.BooleanAttributes var2 = (DefaultSObjectSchema.BooleanAttributes)var1;
            return Arrays.equals(this.values, var2.values);
         } else {
            return false;
         }
      }
   }
}
