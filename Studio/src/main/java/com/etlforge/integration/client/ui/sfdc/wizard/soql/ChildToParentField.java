package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public final class ChildToParentField implements Comparable<ChildToParentField> {
   private final ImmutableList<String> path;

   public ChildToParentField(Iterable<String> var1) {
      this.path = ImmutableList.copyOf(var1);
      Preconditions.checkArgument(this.path.size() >= 2, "The path must have at least two parts");
   }

   public ChildToParentField(String... var1) {
      Preconditions.checkArgument(var1.length > 0, "Empty path not allowed");
      this.path = ImmutableList.copyOf(var1);
   }

   public String getName() {
      return this.path.get(this.path.size() - 1);
   }

   public ImmutableList<String> getParentPath() {
      return this.path.subList(0, this.path.size() - 1);
   }

   public String toSoql() {
      return Joiner.on('.').join(this.path);
   }

   @Override
   public int hashCode() {
      return this.path.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof ChildToParentField && this.path.equals(((ChildToParentField)var1).path);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).addValue(this.toSoql()).toString();
   }

   public int compareTo(ChildToParentField var1) {
      return this.toSoql().compareToIgnoreCase(var1.toSoql());
   }
}
