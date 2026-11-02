package org.etlforge.ui.property;

import com.google.common.collect.Lists;
import java.util.List;

public final class Bindings implements Binding {
   private final List<Binding> bindings;

   public static Bindings of(Binding... var0) {
      return new Bindings(var0);
   }

   public Bindings(Binding... var1) {
      this.bindings = Lists.newArrayList(var1);
   }

   public Bindings add(Binding var1) {
      this.bindings.add(var1);
      return this;
   }

   @Override
   public void dispose() {
      for (Binding var2 : this.bindings) {
         var2.dispose();
      }
   }

   @Override
   public void setUiToModelEnabled(boolean var1) {
      for (Binding var3 : this.bindings) {
         var3.setUiToModelEnabled(var1);
      }
   }

   @Override
   public void setModelToUiEnabled(boolean var1) {
      for (Binding var3 : this.bindings) {
         var3.setModelToUiEnabled(var1);
      }
   }

   @Override
   public void syncModel() {
      for (Binding var2 : this.bindings) {
         var2.syncModel();
      }
   }

   @Override
   public void syncUi() {
      for (Binding var2 : this.bindings) {
         var2.syncUi();
      }
   }
}
