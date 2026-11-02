package org.etlforge.integration.data.entity.target;

import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.HttpLocation;

final class HttpTargetResponseLink extends AbstractEntityLink {
   public HttpTargetResponseLink(Target var1, Target var2) {
      super(var1, var2);
      DataLocation var3 = var2.getLocation();
      if (!(var3 instanceof HttpLocation)) {
         throw new IllegalArgumentException("httpTarget must be of type HTTP");
      }
   }

   public Target getDependantObject() {
      return (Target)super.getDependantObject();
   }

   public Target getMainObject() {
      return (Target)super.getMainObject();
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   @Override
   protected void mainObjectDeleted() {
      DataLocation var1 = this.getDependantObject().getLocation();
      if (var1 instanceof HttpLocation) {
         ((HttpLocation)var1).setTargetForResponse(null);
      }

      super.mainObjectDeleted();
   }
}
