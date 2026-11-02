package org.etlforge.application.ui.window;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderEvent;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListeners;

public final class DefaultActiveObjectService implements ActiveObjectService {
   private final DefaultActiveObjectService.EventLogger eventLogger;
   private final DefaultActiveObjectService.ExternalListeners externalListeners = new DefaultActiveObjectService.ExternalListeners();
   private final ActiveObjectProviderListener eventDispatcher = new DefaultActiveObjectService.EventDispatcher();
   private final Map<String, ActiveObjectProvider> providers = Maps.newHashMap();
   private String currentProvider;
   private List<?> activeObjects = new ArrayList();
   private final Object lock;

   public DefaultActiveObjectService() {
      this.eventLogger = DefaultActiveObjectService.EventLogger.getLogger();
      this.lock = new Object();
   }

   @Override
   public void addActiveObjectProvider(ActiveObjectProvider var1) {
      String var2 = var1.getProviderId();
      synchronized (this.lock) {
         ActiveObjectProvider var4 = this.providers.remove(var2);
         if (var4 != null) {
            this.removeActiveObjectProvider(var1);
         }

         this.providers.put(var2, var1);
      }

      var1.addActiveObjectListener(this.eventDispatcher);
   }

   @Override
   public void removeActiveObjectProvider(ActiveObjectProvider var1) {
      synchronized (this.lock) {
         this.providers.remove(var1.getProviderId());
      }

      var1.removeActiveObjectListener(this.eventDispatcher);
   }

   @Override
   public List<?> getActiveObjects() {
      synchronized (this.activeObjects) {
         return new ArrayList(this.activeObjects);
      }
   }

   @Override
   public void setActiveObjects(ActiveObjectProvider var1, List<?> var2) {
      ActiveObjectProviderEvent var3 = new ActiveObjectProviderEvent(var1, var2);
      this.eventDispatcher.activeObjectsChanged(var3);
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.externalListeners.add(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.externalListeners.remove(var1);
   }

   @Override
   public void addActiveObjectListener(String var1, ActiveObjectProviderListener var2) {
      this.externalListeners.add(var1, var2);
   }

   @Override
   public void removeActiveObjectListener(String var1, ActiveObjectProviderListener var2) {
      this.externalListeners.remove(var1, var2);
   }

   private static class ConcreteEventLogger extends DefaultActiveObjectService.EventLogger {
      private final Logger logger = ApplicationLog.getLogger(ActiveObjectService.class, true);

      private ConcreteEventLogger() {
      }

      @Override
      public void log(ActiveObjectProviderEvent var1) {
         StringBuilder var2 = new StringBuilder("Active object change reported from provider ");
         var2.append(var1.getSource().getProviderId());
         var2.append("\n");
         var2.append("Active objects:");
         var2.append("\n");
         List var3 = var1.getActiveObjects();
         if (var3.isEmpty()) {
            var2.append("[none]");
         } else {
            for (Object var5 : var3) {
               var2.append(var5.toString());
               var2.append("\n");
            }
         }

         this.logger.info(var2.toString());
      }
   }

   private class EventDispatcher implements ActiveObjectProviderListener {
      private EventDispatcher() {
      }

      @Override
      public void activeObjectsChanged(ActiveObjectProviderEvent var1) {
         ActiveObjectProvider var2 = var1.getSource();
         if (var2.isActive()) {
            List var3 = var1.getActiveObjects();
            synchronized (DefaultActiveObjectService.this.lock) {
               boolean var5 = this.equals(DefaultActiveObjectService.this.currentProvider, var2.getProviderId())
                  && this.equals(var3, DefaultActiveObjectService.this.activeObjects);
               if (var5) {
                  return;
               }

               DefaultActiveObjectService.this.currentProvider = var2.getProviderId();
               DefaultActiveObjectService.this.eventLogger.log(var1);
               DefaultActiveObjectService.this.activeObjects = var3;
            }

            DefaultActiveObjectService.this.externalListeners.notifyListeners(var1);
         }
      }

      private boolean equals(List<?> var1, List<?> var2) {
         return var1.size() != var2.size() ? false : new HashSet(var1).equals(new HashSet(var2));
      }

      private boolean equals(String var1, String var2) {
         return var1 == null ? var2 == null : var1.equals(var2);
      }
   }

   private abstract static class EventLogger {
      private EventLogger() {
      }

      public abstract void log(ActiveObjectProviderEvent var1);

      public static DefaultActiveObjectService.EventLogger getLogger() {
         return isLoggingEnabled() ? new DefaultActiveObjectService.ConcreteEventLogger() : new DefaultActiveObjectService.EventLogger() {
            @Override
            public void log(ActiveObjectProviderEvent var1) {
            }
         };
      }

      private static boolean isLoggingEnabled() {
         return Boolean.getBoolean("Debug.LogActiveObjectEvents");
      }
   }

   private static class ExternalListeners {
      private final ActiveObjectProviderListeners globalListeners = new ActiveObjectProviderListeners();
      private final Map<String, ActiveObjectProviderListeners> individualListeners = Maps.newHashMap();

      public ExternalListeners() {
      }

      public void add(ActiveObjectProviderListener var1) {
         this.globalListeners.add(var1);
      }

      public void remove(ActiveObjectProviderListener var1) {
         this.globalListeners.remove(var1);
      }

      public void add(String var1, ActiveObjectProviderListener var2) {
         ActiveObjectProviderListeners var3 = this.individualListeners.get(var1);
         if (var3 == null) {
            var3 = new ActiveObjectProviderListeners();
            this.individualListeners.put(var1, var3);
         }

         var3.add(var2);
      }

      public void remove(String var1, ActiveObjectProviderListener var2) {
         ActiveObjectProviderListeners var3 = this.individualListeners.get(var1);
         if (var3 != null) {
            var3.remove(var2);
         }
      }

      public void notifyListeners(ActiveObjectProviderEvent var1) {
         this.globalListeners.call().activeObjectsChanged(var1);
         String var2 = var1.getSource().getProviderId();
         ActiveObjectProviderListeners var3 = this.individualListeners.get(var2);
         if (var3 != null) {
            var3.call().activeObjectsChanged(var1);
         }
      }
   }
}
