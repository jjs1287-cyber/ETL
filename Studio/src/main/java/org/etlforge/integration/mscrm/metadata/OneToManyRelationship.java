package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;

public final class OneToManyRelationship implements Relationship, Serializable {
   private static final long serialVersionUID = -4768185913303091807L;
   private final LinkAttribute one;
   private final LinkAttribute many;

   public OneToManyRelationship(LinkAttribute var1, LinkAttribute var2) {
      this.one = Preconditions.checkNotNull(var1, "one");
      this.many = Preconditions.checkNotNull(var2, "many");
   }

   public LinkAttribute getOne() {
      return this.one;
   }

   public LinkAttribute getMany() {
      return this.many;
   }

   @Override
   public boolean isIntersect() {
      return false;
   }

   @Override
   public String getLinkEntityName() {
      return this.many.getEntityName();
   }

   @Override
   public String getFromAttribute() {
      return this.many.getAttributeName();
   }

   @Override
   public String getToAttribute() {
      return this.one.getAttributeName();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.one, this.many);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof OneToManyRelationship)) {
         return false;
      }

      OneToManyRelationship var2 = (OneToManyRelationship)var1;
      return this.one.equals(var2.one) && this.many.equals(var2.many);
   }

   @Override
   public String toString() {
      return this.one + " - " + this.many;
   }
}
