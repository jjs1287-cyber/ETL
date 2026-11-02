package org.etlforge.integration.data.script;

public final class ScriptStatus {
   public static final ScriptStatus NONE = new ScriptStatus(ScriptStatus.State.VALID, "");
   public static final ScriptStatus VALID = new ScriptStatus(ScriptStatus.State.VALID, "Script is valid");
   private final String message;
   private final ScriptStatus.State state;

   public static ScriptStatus invalid(String var0) {
      return new ScriptStatus(ScriptStatus.State.INVALID, var0);
   }

   public static ScriptStatus warning(String var0) {
      return new ScriptStatus(ScriptStatus.State.WARNING, var0);
   }

   private ScriptStatus(ScriptStatus.State var1, String var2) {
      this.state = var1;
      this.message = var2 != null ? var2 : "";
   }

   public String getMessage() {
      return this.message;
   }

   public ScriptStatus.State getState() {
      return this.state;
   }

   public boolean isInvalid() {
      return this.state == ScriptStatus.State.INVALID;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ScriptStatus)) {
         return false;
      }

      ScriptStatus var2 = (ScriptStatus)var1;
      return this.state == var2.state && this.message.equals(var2.message);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.state.hashCode();
      return 31 * var1 + this.message.hashCode();
   }

   public enum State {
      VALID,
      WARNING,
      INVALID;
   }
}
