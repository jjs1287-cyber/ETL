package org.etlforge.integration.data;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.ObjectStreamException;
import java.io.Serializable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class Password implements Serializable {
   private final String clearText;
   private final String encrypted;

   public static Password encrypt(String var0) {
      return new Password(var0, CC.hide(var0));
   }

   public static Password decrypt(String var0) {
      return new Password(CC.reveal(var0), var0);
   }

   private Password(String var1, String var2) {
      this.clearText = Preconditions.checkNotNull(var1);
      this.encrypted = Preconditions.checkNotNull(var2);
   }

   public String clearText() {
      return this.clearText;
   }

   public String encrypted() {
      return this.encrypted;
   }

   @Override
   public int hashCode() {
      return this.clearText.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof Password && this.clearText.equals(((Password)var1).clearText);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("clearText", this.clearText).add("encrypted", this.encrypted).toString();
   }

   private Object writeReplace() throws ObjectStreamException {
      return new Password.SerializationProxy(this);
   }

   private static class SerializationProxy implements Serializable {
      private final String encryptedPassword;

      public SerializationProxy(Password var1) {
         this.encryptedPassword = var1.encrypted;
      }

      private Object readResolve() throws ObjectStreamException {
         return Password.decrypt(this.encryptedPassword);
      }
   }
}
