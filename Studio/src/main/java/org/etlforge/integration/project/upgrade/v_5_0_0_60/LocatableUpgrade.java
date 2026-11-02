package org.etlforge.integration.project.upgrade.v_5_0_0_60;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.CC;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LoginProtectedResource;
import org.etlforge.integration.data.project.IntegrationProject;

final class LocatableUpgrade {
   public static void run(IntegrationProject var0) {
      for (Locatable var3 : collectLocatables(var0)) {
         DataLocation var4 = var3.getLocation();
         if (var4 instanceof LoginProtectedResource) {
            boolean var5;
            synchronized (var4.getDataLock()) {
               var5 = upgrade((LoginProtectedResource)var4);
            }

            if (var5) {
               ((IntegrationEntity)var3).setDeployDirty(true);
            }
         }
      }
   }

   private static List<Locatable> collectLocatables(IntegrationProject var0) {
      ArrayList var1 = Lists.newArrayList();
      Iterables.addAll(var1, var0.getAllEntitiesOfClass(Source.class));
      Iterables.addAll(var1, var0.getAllEntitiesOfClass(Target.class));
      return var1;
   }

   private static boolean upgrade(LoginProtectedResource var0) {
      DataLocation var1 = (DataLocation)var0;
      String var2 = var0.getPasswordPropertyName();
      String var3 = var1.getProperty(var2);
      if (var3 == null) {
         return false;
      }

      String var4 = CC.hide(var3);
      var1.setProperty(var2, var4);
      return true;
   }

   private LocatableUpgrade() {
   }
}
