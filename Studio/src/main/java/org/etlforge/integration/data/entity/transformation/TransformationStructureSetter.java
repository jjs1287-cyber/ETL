package org.etlforge.integration.data.entity.transformation;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.system.callback.Receiver;

public abstract class TransformationStructureSetter {
   public static TransformationStructureSetter inputSetter() {
      return new TransformationStructureSetter() {
         @Override
         public void setStructure(Transformation var1, DataStructure var2) {
            var1.setInputStructure(var2);
         }
      };
   }

   public static TransformationStructureSetter outputSetter() {
      return new TransformationStructureSetter() {
         @Override
         public void setStructure(Transformation var1, DataStructure var2) {
            var1.setOutputStructure(var2);
         }
      };
   }

   public abstract void setStructure(Transformation var1, DataStructure var2);

   public final Receiver<DataStructure> asReceiver(final Transformation var1) {
      return new Receiver<DataStructure>() {
         public void handle(DataStructure var1x) {
            TransformationStructureSetter.this.setStructure(var1, var1x);
         }
      };
   }
}
