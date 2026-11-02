package org.etlforge.application.ui.window.event;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public final class ActiveObjectProviderHelper {
   private final ActiveObjectProvider provider;
   private final ActiveObjectProviderListeners listeners;
   private List<?> activeObjects;

   public ActiveObjectProviderHelper(ActiveObjectProvider var1) {
      this.provider = var1;
      this.listeners = new ActiveObjectProviderListeners();
      this.activeObjects = Lists.newArrayList();
   }

   public void add(ActiveObjectProviderListener var1) {
      this.listeners.add(var1);
   }

   public void remove(ActiveObjectProviderListener var1) {
      this.listeners.remove(var1);
   }

   public List<?> getActiveObjects() {
      return this.activeObjects;
   }

   public void notifyListeners(List<?> var1) {
      this.activeObjects = var1;
      ActiveObjectProviderEvent var2 = new ActiveObjectProviderEvent(this.provider, var1);
      this.listeners.call().activeObjectsChanged(var2);
   }

   public void notifyListeners(Object var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var1 != null) {
         var2.add(var1);
      }

      this.notifyListeners(var2);
   }

   public void clear() {
      this.activeObjects = Lists.newArrayList();
   }
}
