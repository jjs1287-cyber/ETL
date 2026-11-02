package org.etlforge.integration.data.function;

import javax.annotation.Nullable;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallRequestStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;

public abstract class FunctionStructureGetter<T extends Function> {
   public abstract DataStructure getStructure(@Nullable T var1);

   public static FunctionStructureGetter<Function> getter(FunctionCallStructure var0) {
      return var0 instanceof FunctionCallRequestStructure ? inputGetter() : outputGetter();
   }

   public static <T extends Function> FunctionStructureGetter<T> inputGetter() {
      return new FunctionStructureGetter<T>() {
         @Override
         public DataStructure getStructure(@Nullable T var1) {
            return var1 == null ? null : var1.getInputStructure();
         }
      };
   }

   public static <T extends Function> FunctionStructureGetter<T> outputGetter() {
      return new FunctionStructureGetter<T>() {
         @Override
         public DataStructure getStructure(@Nullable T var1) {
            return var1 == null ? null : var1.getOutputStructure();
         }
      };
   }

   private FunctionStructureGetter() {
   }
}
