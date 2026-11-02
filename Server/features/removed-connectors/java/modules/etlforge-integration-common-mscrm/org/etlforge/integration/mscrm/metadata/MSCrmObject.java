package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public final class MSCrmObject implements ConnectorObject {
   private static final long serialVersionUID = 6670986609143925490L;
   private final BasicEntityDetails basicDetails;
   private final ImmutableList<MSCrmAttribute> attributes;
   private final ImmutableList<OneToManyRelationship> oneToManyRelationships;
   private final ImmutableList<ManyToOneRelationship> manyToOneRelationships;
   private final ImmutableList<ManyToManyRelationship> manyToManyRelationships;

   public static MSCrmObject.Builder builder() {
      return new MSCrmObject.Builder();
   }

   public MSCrmObject(
      BasicEntityDetails var1, List<MSCrmAttribute> var2, List<OneToManyRelationship> var3, List<ManyToOneRelationship> var4, List<ManyToManyRelationship> var5
   ) {
      this.basicDetails = Preconditions.checkNotNull(var1, "basicDetails");
      this.attributes = ImmutableList.copyOf(Preconditions.checkNotNull(var2, "attributes"));
      this.oneToManyRelationships = ImmutableList.copyOf(Preconditions.checkNotNull(var3, "oneToManyRelationships"));
      this.manyToOneRelationships = ImmutableList.copyOf(Preconditions.checkNotNull(var4, "manyToOneRelationships"));
      this.manyToManyRelationships = ImmutableList.copyOf(Preconditions.checkNotNull(var5, "manyToManyRelationships"));
   }

   @Override
   public String getName() {
      return this.basicDetails.getName();
   }

   @Override
   public String getDescription() {
      return this.basicDetails.getDescription();
   }

   public BasicEntityDetails getBasicDetails() {
      return this.basicDetails;
   }

   public String getPrimaryIdAttribute() {
      return this.basicDetails.getPrimaryIdAttribute();
   }

   public ImmutableList<MSCrmAttribute> getAttributes() {
      return this.attributes;
   }

   public ImmutableList<OneToManyRelationship> getOneToManyRelationships() {
      return this.oneToManyRelationships;
   }

   public ImmutableList<ManyToOneRelationship> getManyToOneRelationships() {
      return this.manyToOneRelationships;
   }

   public ImmutableList<ManyToManyRelationship> getManyToManyRelationships() {
      return this.manyToManyRelationships;
   }

   public boolean hasRelationships() {
      return !this.oneToManyRelationships.isEmpty() || !this.manyToOneRelationships.isEmpty() || !this.manyToManyRelationships.isEmpty();
   }

   @Override
   public String toString() {
      return this.getName();
   }

   public static final class Builder {
      private BasicEntityDetails details;
      private final List<MSCrmAttribute> attributes = Lists.newArrayList();
      private final List<OneToManyRelationship> oneToMany = Lists.newArrayList();
      private final List<ManyToOneRelationship> manyToOne = Lists.newArrayList();
      private final List<ManyToManyRelationship> manyToMany = Lists.newArrayList();

      public Builder() {
      }

      public Builder(BasicEntityDetails var1) {
         this.details = Preconditions.checkNotNull(var1, "details");
      }

      public MSCrmObject build() {
         Preconditions.checkState(this.details != null, "Must provide the BasicEntityDetails before building");
         return new MSCrmObject(this.details, this.attributes, this.oneToMany, this.manyToOne, this.manyToMany);
      }

      public MSCrmObject.Builder withDetails(BasicEntityDetails var1) {
         this.details = Preconditions.checkNotNull(var1, "details");
         return this;
      }

      public MSCrmObject.Builder addAttribute(MSCrmAttribute var1) {
         this.attributes.add(Preconditions.checkNotNull(var1, "a"));
         return this;
      }

      public MSCrmObject.Builder addAttributes(Iterable<MSCrmAttribute> var1) {
         Iterables.addAll(this.attributes, var1);
         return this;
      }

      public MSCrmObject.Builder addOneToMany(OneToManyRelationship var1) {
         this.oneToMany.add(Preconditions.checkNotNull(var1, "r"));
         return this;
      }

      public MSCrmObject.Builder addOneToMany(Iterable<OneToManyRelationship> var1) {
         Iterables.addAll(this.oneToMany, var1);
         return this;
      }

      public MSCrmObject.Builder addManyToOne(ManyToOneRelationship var1) {
         this.manyToOne.add(Preconditions.checkNotNull(var1, "r"));
         return this;
      }

      public MSCrmObject.Builder addManyToOne(Iterable<ManyToOneRelationship> var1) {
         Iterables.addAll(this.manyToOne, var1);
         return this;
      }

      public MSCrmObject.Builder addManyToMany(ManyToManyRelationship var1) {
         this.manyToMany.add(Preconditions.checkNotNull(var1, "r"));
         return this;
      }

      public MSCrmObject.Builder addManyToMany(Iterable<ManyToManyRelationship> var1) {
         Iterables.addAll(this.manyToMany, var1);
         return this;
      }
   }
}
