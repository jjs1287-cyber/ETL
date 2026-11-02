package org.etlforge.integration.client.ui.admin.core.console;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.swing.Action;

public final class ConsoleLauncherFactory {
   private final Iterable<Action> actions;

   public ConsoleLauncherFactory(Iterable<Action> var1) {
      this.actions = var1;
   }

   public Iterable<Action> getActions() {
      return this.actions;
   }

   public Iterable<ConsoleLauncher> getLaunchers() {
      ArrayList var1 = Lists.newArrayList();

      for (Action var3 : this.actions) {
         var1.add(new DefaultConsoleLauncher(var3));
      }

      return var1;
   }
}
