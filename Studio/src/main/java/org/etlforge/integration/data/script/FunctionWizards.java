package org.etlforge.integration.data.script;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;

final class FunctionWizards {
   private final Object lock = new Object();
   private final Map<Function, FunctionInserterDescriptor> inserters = Maps.newHashMap();

   public FunctionInserter getFunctionWizard(Function var1) {
      try {
         FunctionInserterDescriptor var2 = this.getFunctionInserterDescriptor(var1);
         return var2.instantiate(var1);
      } catch (Exception var3) {
         ErrorLog.log(this.getClass(), "Failed to create a FunctionInserter for the function \"" + var1.getName() + "\".", var3);
         return null;
      }
   }

   private FunctionInserterDescriptor getFunctionInserterDescriptor(Function var1) {
      synchronized (this.lock) {
         FunctionInserterDescriptor var3 = this.inserters.get(var1);
         if (var3 == null) {
            var3 = this.createFunctionInserter(var1);
         }

         return var3;
      }
   }

   private FunctionInserterDescriptor createFunctionInserter(Function var1) {
      FunctionInserterDescriptor var2 = FunctionInserterDescriptor.locate(var1);
      this.inserters.put(var1, var2);
      return var2;
   }

   public boolean hasFunctionWizard(Function var1) {
      synchronized (this.lock) {
         FunctionInserterDescriptor var3 = this.inserters.get(var1);
         if (var3 == null) {
            var3 = this.createFunctionInserter(var1);
         }

         return var3 != FunctionInserterDescriptor.NONE;
      }
   }
}
