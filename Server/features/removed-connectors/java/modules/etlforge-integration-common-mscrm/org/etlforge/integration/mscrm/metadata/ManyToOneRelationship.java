package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;

public final class ManyToOneRelationship implements Relationship, Serializable {
   private static final long serialVersionUID = -3501003694699204348L;
   private final LinkAttribute many;
   private final LinkAttribute one;

   public ManyToOneRelationship(LinkAttribute var1, LinkAttribute var2) {
      this.many = Preconditions.checkNotNull(var1, "many");
      this.one = Preconditions.checkNotNull(var2, "one");
   }

   public LinkAttribute getOne() {
      return this.one;
   }

   public LinkAttribute getMany() {
      return this.many;
   }

   @Override
   public String getLinkEntityName() {
      return this.one.getEntityName();
   }

   @Override
   public String getFromAttribute() {
      return this.one.getAttributeName();
   }

   @Override
   public String getToAttribute() {
      return this.many.getAttributeName();
   }

   @Override
   public boolean isIntersect() {
      return false;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.many, this.one);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ManyToOneRelationship)) {
         return false;
      }

      ManyToOneRelationship var2 = (ManyToOneRelationship)var1;
      return this.many.equals(var2.many) && this.one.equals(var2.one);
   }

   @Override
   public String toString() {
      return this.many + " - " + this.one;
   }
}
