package org.etlforge.integration.uidelegate.entity;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class IconKey {
   private final EntityIcons.Size size;
   private final EntityIcons.Category category;

   public static final IconKey of(EntityIcons.Size var0, EntityIcons.Category var1) {
      return new IconKey(var0, var1);
   }

   public IconKey(EntityIcons.Size var1, EntityIcons.Category var2) {
      this.size = Preconditions.checkNotNull(var1, "size");
      this.category = Preconditions.checkNotNull(var2, "category");
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof IconKey)) {
         return false;
      }

      IconKey var2 = (IconKey)var1;
      return this.size == var2.size && this.category == var2.category;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.size, this.category);
   }
}
