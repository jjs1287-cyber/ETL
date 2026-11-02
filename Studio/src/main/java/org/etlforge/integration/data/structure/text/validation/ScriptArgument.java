package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.ScriptId;

public final class ScriptArgument implements OperatorArgument {
   private final ScriptId guid;

   public ScriptArgument(ScriptId var1) {
      this.guid = Preconditions.checkNotNull(var1);
   }

   public ScriptId getId() {
      return this.guid;
   }

   @Override
   public String toStorageForm() {
      return this.guid.toString();
   }

   public static ScriptArgument fromStorageForm(String var0) {
      return new ScriptArgument(new ScriptId(var0));
   }

   @Override
   public int hashCode() {
      return this.guid.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof ScriptArgument && this.guid.equals(((ScriptArgument)var1).guid);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).addValue(this.guid).toString();
   }
}
