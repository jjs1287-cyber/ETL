package org.etlforge.util.exception;

public class IllegalValueException extends Exception {
   private final Object value;

   public IllegalValueException(Object var1) {
      this.value = var1;
   }

   public IllegalValueException(Object var1, String var2) {
      super(var2);
      this.value = var1;
   }

   public IllegalValueException(Object var1, Throwable var2) {
      super(var2);
      this.value = var1;
   }

   public IllegalValueException(Object var1, String var2, Throwable var3) {
      super(var2, var3);
      this.value = var1;
   }

   public Object getValue() {
      return this.value;
   }
}
