package org.etlforge.integration.data.entity.transformation;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.NodePath;

public final class TransformationVariables {
   private final GlobalDataElements des;

   public TransformationVariables(GlobalDataElements var1) {
      this.des = var1;
   }

   public void scanMapping(Transformation var1, Mapping var2) {
      this.scanMapping(var1, var2.getTargetPath(), var2.getExpression());
   }

   public void scanMapping(Transformation var1, NodePath var2, String var3) {
      try {
         TransformationExpressionSource var4 = new TransformationExpressionSource(var1, var2);
         this.des.parseExpression(var4, var3);
      } catch (Exception var5) {
         ErrorLog.internal(this.getClass(), "Error parsing the mapping expression for the target node " + var2 + " in the Transformation " + var1, var5);
      }
   }

   public void removeMapping(Transformation var1, Mapping var2) {
      this.des.remove(new TransformationExpressionSource(var1, var2.getTargetPath()));
   }

   public void scanTransformation(Transformation var1) {
      for (String var3 : var1.getMapList()) {
         try {
            Mapping var4 = MapStringFormat.parse(var3);
            this.scanMapping(var1, var4.getTargetPath(), var4.getExpression());
         } catch (RuntimeException var5) {
         }
      }
   }
}
