package org.etlforge.integration.data.location.file;

import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Set;
import org.etlforge.integration.data.location.DataLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;

public final class IgnoredLines {
   public static final ValidationIssue INVALID_IGNORE_FIRST = new DataLocationError("FileLocationValidator.IgnoreFirst");
   public static final ValidationIssue INVALID_IGNORE_LAST = new DataLocationError("FileLocationValidator.IgnoreLast");
   private final int first;
   private final int last;

   public IgnoredLines() {
      this(0, 0);
   }

   public IgnoredLines(int var1, int var2) {
      this.first = var1;
      this.last = var2;
   }

   public int getFirst() {
      return this.first;
   }

   public int getLast() {
      return this.last;
   }

   public Set<ValidationIssue> validate() {
      LinkedHashSet var1 = Sets.newLinkedHashSet();
      ValidationIssue var2 = this.validateFirst();
      if (var2 != null) {
         var1.add(var2);
      }

      var2 = this.validateLast();
      if (var2 != null) {
         var1.add(var2);
      }

      return var1;
   }

   public ValidationIssue validateFirst() {
      return this.first < 0 ? INVALID_IGNORE_FIRST : null;
   }

   public ValidationIssue validateLast() {
      return this.last < 0 ? INVALID_IGNORE_LAST : null;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof IgnoredLines)) {
         return false;
      }

      IgnoredLines var2 = (IgnoredLines)var1;
      return this.first == var2.first && this.last == var2.last;
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.first;
      return 31 * var1 + this.last;
   }

   @Override
   public String toString() {
      return "First: " + this.first + ", Last: " + this.last;
   }
}
