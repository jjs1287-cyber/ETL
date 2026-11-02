package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.system.ArgCheck;

public final class FunctionTransformationDecorator<T extends Function> extends UiEntityFactoryCallback.Adapter {
   private final T functionCall;
   private final FunctionStructureGetter<T> structureGetter;
   private final TransformationStructureSetter structureSetter;
   private final String namePattern;

   public static <T extends Function> UiEntityFactoryCallback forRequest(T var0) {
      return new FunctionTransformationDecorator<>(var0, FunctionStructureGetter.inputGetter(), TransformationStructureSetter.outputSetter(), "%s - Request");
   }

   public static <T extends Function> UiEntityFactoryCallback forResponse(T var0) {
      return new FunctionTransformationDecorator<>(var0, FunctionStructureGetter.outputGetter(), TransformationStructureSetter.inputSetter(), "%s - Response");
   }

   private FunctionTransformationDecorator(T var1, FunctionStructureGetter<T> var2, TransformationStructureSetter var3, String var4) {
      this.functionCall = Preconditions.checkNotNull((T)var1, "functionCall");
      this.structureGetter = Preconditions.checkNotNull(var2, "structureGetter");
      this.structureSetter = Preconditions.checkNotNull(var3, "structureSetter");
      this.namePattern = ArgCheck.checkNotEmpty(var4, "namePattern");
   }

   @Override
   public final Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      Transformation var3 = (Transformation)var1;
      this.setSuggestedName(var3);
      this.setStructure(var3);
      return Result.continueProcess(var2);
   }

   private void setSuggestedName(Transformation var1) {
      IntegrationProject var2 = ProjectUtils.getProject((IntegrationEntity)this.functionCall);
      if (var2 != null) {
         RootFolder var3 = var2.getRootFolder(EntityType.Transformation);
         String var4 = String.format(this.namePattern, this.functionCall.getName());
         String var5 = EntityUtils.getAvailableChildName(var3, var4);
         var1.setName(var5);
      }
   }

   private void setStructure(Transformation var1) {
      DataStructure var2 = this.structureGetter.getStructure(this.functionCall);
      this.structureSetter.setStructure(var1, var2);
   }
}
