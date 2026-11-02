package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import org.etlforge.integration.data.Password;

public interface LoginProtectedResource {
   String getLogin();

   Optional<Password> getPassword();

   String getPasswordPropertyName();
}
