package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.SystemTarget;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;

public final class TransformationTargetValidator {
   private final Transformation transformation;
   private final Target target;

   public TransformationTargetValidator(Transformation var1, Target var2) {
      this.transformation = Preconditions.checkNotNull(var1);
      this.target = var2;
   }

   public boolean isValid() {
      DataStructure var1 = this.transformation.getOutputStructure();
      return var1 != null ? this.canTargetAcceptOutput(var1) : false;
   }

   private boolean canTargetAcceptOutput(DataStructure var1) {
      assert var1 != null;
      DataStructureType var2 = var1.getStructureType();
      if (this.target == null) {
         return this.acceptsNullTarget(var2);
      }

      if (this.target == SystemTarget.WEB_SERVICE_CALL) {
         return var2 == DataStructureType.WebServiceResponse;
      }

      TargetId var3 = this.transformation.getTargetId();
      if (var3 != null) {
         return var3.equals(this.target.getID());
      }

      DataLocation var4 = this.target.getLocation();
      return var4 != null && var1.isCompatibleWith(var4);
   }

   private boolean acceptsNullTarget(DataStructureType var1) {
      return var1 == DataStructureType.None || var1.isFileBased();
   }
}
