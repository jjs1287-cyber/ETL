package org.etlforge.integration.activity.serverapi;

import com.google.common.base.Preconditions;
import org.etlforge.integration.activity.ActivitySubjectType;
import org.etlforge.lang.Objects;
import org.etlforge.util.id.KongaID;

public final class ActivitySubjectId {
   private final ActivitySubjectType type;
   private final KongaID guid;

   public ActivitySubjectId(ActivitySubjectType var1, KongaID var2) {
      this.type = Preconditions.checkNotNull(var1);
      this.guid = Preconditions.checkNotNull(var2);
   }

   public KongaID getGuid() {
      return this.guid;
   }

   public ActivitySubjectType getType() {
      return this.type;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.guid, this.type);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ActivitySubjectId)) {
         return false;
      }

      ActivitySubjectId var2 = (ActivitySubjectId)var1;
      return this.guid.equals(var2.guid) && this.type == var2.type;
   }

   @Override
   public String toString() {
      return this.type + "[" + this.guid + "]";
   }
}
