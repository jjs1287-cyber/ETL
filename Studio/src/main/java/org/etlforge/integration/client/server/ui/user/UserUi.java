package org.etlforge.integration.client.server.ui.user;

import com.google.common.base.Function;
import org.etlforge.integration.user.User;

public interface UserUi {
   Function<UserUi, String> TO_NAME = new Function<UserUi, String>() {
      public String apply(UserUi var1) {
         return var1.getUser().getName();
      }
   };

   User getUser();

   void update();
}
