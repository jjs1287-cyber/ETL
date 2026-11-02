package com.etlforge.integration.client.ui.sfdc.wizard.soql.relational;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.etlforge.util.name.Nameables;

public final class RelationshipPath {
   private final ImmutableList<FieldOwner> path;

   public static RelationshipPath root() {
      return new RelationshipPath(ImmutableList.of());
   }

   public static RelationshipPath of(RootObject var0, FieldOwner... var1) {
      Builder var2 = ImmutableList.builder();
      var2.add(var0);
      var2.add(var1);
      return new RelationshipPath(var2.build());
   }

   public static RelationshipPath of(Iterable<FieldOwner> var0) {
      return new RelationshipPath(ImmutableList.copyOf(var0));
   }

   private RelationshipPath(ImmutableList<FieldOwner> var1) {
      this.path = var1;
   }

   public boolean isEmpty() {
      return this.path.isEmpty();
   }

   public int size() {
      return this.path.size();
   }

   public ImmutableList<FieldOwner> toList() {
      return this.path;
   }

   public ImmutableList<String> toNameList() {
      return ImmutableList.copyOf(Nameables.toNames(this.path));
   }

   public RelationshipPath getParentPath() {
      if (this.path.isEmpty()) {
         throw new UnsupportedOperationException("getParentPath cannot be called on an empty path.");
      } else {
         return this.path.size() == 1 ? root() : new RelationshipPath(this.path.subList(0, this.size() - 1));
      }
   }

   public FieldOwner getLeaf() {
      if (this.path.isEmpty()) {
         throw new UnsupportedOperationException("getLeaf cannot be called on an empty path.");
      } else {
         return this.path.get(this.size() - 1);
      }
   }

   public RelationshipPath append(FieldOwner var1) {
      if (this.path.isEmpty()) {
         Preconditions.checkArgument(var1 instanceof RootObject, "The first object in the path must be a RootObject");
      }

      Builder var2 = ImmutableList.builder();
      var2.addAll(this.path);
      var2.add(var1);
      return new RelationshipPath(var2.build());
   }

   @Override
   public int hashCode() {
      return this.toNameList().hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof RelationshipPath && this.toNameList().equals(((RelationshipPath)var1).toNameList());
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).addValue(Joiner.on(" > ").join(this.toNameList())).toString();
   }
}
