package org.etlforge.integration.client.server.ui.user;

import com.google.common.base.Function;
import org.etlforge.integration.user.Group;

public interface GroupUi {
   Function<GroupUi, String> TO_NAME = new Function<GroupUi, String>() {
      public String apply(GroupUi var1) {
         return var1.getGroup().getName();
      }
   };

   Group getGroup();

   void update();
}
