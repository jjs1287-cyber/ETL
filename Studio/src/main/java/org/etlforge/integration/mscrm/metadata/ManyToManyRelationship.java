package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.etlforge.system.ArgCheck;

public final class ManyToManyRelationship implements Relationship, Serializable {
   private static final long serialVersionUID = -4719533633277293205L;
   private final LinkAttribute from;
   private final LinkAttribute to;
   public final String intersectEntityName;

   public ManyToManyRelationship(LinkAttribute var1, LinkAttribute var2, String var3) {
      this.from = Preconditions.checkNotNull(var1, "from");
      this.to = Preconditions.checkNotNull(var2, "to");
      this.intersectEntityName = ArgCheck.checkNotEmpty(var3, "intersectEntityName");
   }

   public LinkAttribute getFrom() {
      return this.from;
   }

   public LinkAttribute getTo() {
      return this.to;
   }

   @Override
   public String getLinkEntityName() {
      return this.intersectEntityName;
   }

   @Override
   public String getFromAttribute() {
      return this.from.getAttributeName();
   }

   @Override
   public String getToAttribute() {
      return this.to.getAttributeName();
   }

   @Override
   public boolean isIntersect() {
      return true;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.from, this.to, this.intersectEntityName);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ManyToManyRelationship)) {
         return false;
      }

      ManyToManyRelationship var2 = (ManyToManyRelationship)var1;
      return this.from.equals(var2.from) && this.to.equals(var2.to) && this.intersectEntityName.equals(var2.intersectEntityName);
   }

   @Override
   public String toString() {
      return String.format("%s: %s - %s", this.intersectEntityName, this.from, this.to);
   }
}
