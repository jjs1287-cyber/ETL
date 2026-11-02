package org.etlforge.integration.server.backend.webservice.scriptengine;

import com.google.common.base.Function;
import javax.script.Bindings;
import javax.script.SimpleBindings;

final class BindingsConverter implements Function<Binding[], Bindings> {
   public static Bindings convert(Binding[] var0) {
      return new BindingsConverter().apply(var0);
   }

   public Bindings apply(Binding[] var1) {
      SimpleBindings var2 = new SimpleBindings();

      for (Binding var6 : var1) {
         Object var7 = this.getBindingValue(var6);
         var2.put(var6.getName(), var7);
      }

      return var2;
   }

   private Object getBindingValue(Binding var1) {
      VariableType var2 = VariableType.getType(var1.getType());
      return var2.getValue(var1.getValue());
   }
}
