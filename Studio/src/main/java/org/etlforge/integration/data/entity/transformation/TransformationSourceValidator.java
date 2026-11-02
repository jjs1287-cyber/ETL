package org.etlforge.integration.data.entity.transformation;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;

public final class TransformationSourceValidator {
   private final Transformation transformation;
   private final Source source;

   public TransformationSourceValidator(Transformation var1, Source var2) {
      this.transformation = Preconditions.checkNotNull(var1);
      this.source = var2;
   }

   public boolean isValid() {
      DataStructure var1 = this.transformation.getInputStructure();
      return var1 != null ? this.canSourceProduceInput(var1) : false;
   }

   private boolean canSourceProduceInput(DataStructure var1) {
      assert var1 != null;
      DataStructureType var2 = var1.getStructureType();
      if (this.source == null) {
         return !var2.isSourceDataRequired() || var2 == DataStructureType.WebServiceRequest;
      }

      if (this.source == SystemSource.SOURCE_FROM_PREVIOUS_OPERATION || this.source == SystemSource.FROM_HTTP_ENDPOINT) {
         var2.isExternalSourceAccepted();
         return true;
      }

      if (this.source == SystemSource.WEB_SERVICE_CALL) {
         return var2 == DataStructureType.WebServiceRequest;
      }

      SourceId var3 = this.transformation.getSourceId();
      if (var3 != null) {
         return var3.equals(this.source.getID());
      }

      DataLocation var4 = this.source.getLocation();
      return var4 != null && var1.isCompatibleWith(var4);
   }
}
