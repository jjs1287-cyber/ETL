package org.etlforge.integration.etlforgeconnector;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.data.Password;

@ParametersAreNonnullByDefault
public final class UserCredentials {
   private final String username;
   private final Optional<Password> password;

   public static UserCredentials usernameOnly(String var0) {
      return new UserCredentials(var0, Optional.absent());
   }

   public UserCredentials(String var1, @Nullable Password var2) {
      this(var1, Optional.fromNullable(var2));
   }

   public UserCredentials(String var1, Optional<Password> var2) {
      this.username = Preconditions.checkNotNull(var1, "username");
      this.password = Preconditions.checkNotNull(var2, "password");
   }

   public String getUsername() {
      return this.username;
   }

   public Optional<Password> getPassword() {
      return this.password;
   }

   String encryptedPassword() {
      return this.password.isPresent() ? this.password.get().encrypted() : null;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.username, this.password);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof UserCredentials)) {
         return false;
      }

      UserCredentials var2 = (UserCredentials)var1;
      return this.username.equals(var2.username) && Objects.equal(this.password, var2.password);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("user", this.username).add("password", "*not shown*").toString();
   }
}
