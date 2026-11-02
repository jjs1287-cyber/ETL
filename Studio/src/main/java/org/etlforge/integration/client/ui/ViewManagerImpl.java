package org.etlforge.integration.client.ui;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.util.Map;
import java.util.Set;
import org.etlforge.application.Application;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.view.View;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.application.ui.view.ViewManagerEvent;
import org.etlforge.application.ui.view.ViewManagerListener;
import org.etlforge.application.ui.view.ViewManagerListeners;
import org.etlforge.application.ui.view.shell.ViewShell;
import org.etlforge.ui.UiThreadSupplier;

final class ViewManagerImpl implements ViewManager {
   private final Map<String, View> viewMap = Maps.newHashMap();
   private final Set<String> visibleViews = Sets.newHashSet();
   private ImmutableMap<String, ViewShell> viewIdToShell;
   private final ViewManagerListeners mgrListeners = new ViewManagerListeners();

   public void initialize(Iterable<ViewShell> var1) {
      this.viewIdToShell = Maps.uniqueIndex(var1, new Function<ViewShell, String>() {
         public String apply(ViewShell var1) {
            return var1.getID();
         }
      });
      if (this.viewIdToShell.isEmpty()) {
         ApplicationLog.getMainLogger().warning("The application configuration does not contain any views.");
      }
   }

   @Override
   public boolean hasView(String var1) {
      return this.viewMap.containsKey(var1);
   }

   @Override
   public void showView(String var1) {
      ViewManagerImpl.ShowViewJob var2 = new ViewManagerImpl.ShowViewJob(var1);
      var2.run();
   }

   @Override
   public boolean hasVisibleViews() {
      return !this.visibleViews.isEmpty();
   }

   @Override
   public void removeView(String var1) {
      if (this.visibleViews.contains(var1)) {
         View var2 = this.viewMap.get(var1);
         this.removeViewImpl(var2);
      }
   }

   private void removeViewImpl(View var1) {
      if (var1.allowsRemoval()) {
         var1.setActive(false);
         var1.prepareForRemoval();
         this.visibleViews.remove(var1.getID());
         this.fireViewRemoved(var1);
      }
   }

   @Override
   public void addViewManagerListener(ViewManagerListener var1) {
      this.mgrListeners.add(var1);
   }

   @Override
   public void removeViewManagerListener(ViewManagerListener var1) {
      this.mgrListeners.remove(var1);
   }

   private void fireViewLoaded(View var1) {
      this.mgrListeners.call().viewIsLoaded(new ViewManagerEvent(this, var1));
   }

   private void fireViewDisplayed(View var1) {
      this.mgrListeners.call().viewIsDisplayed(new ViewManagerEvent(this, var1));
   }

   private void fireViewRemoved(View var1) {
      this.mgrListeners.call().viewWasRemoved(new ViewManagerEvent(this, var1));
   }

   private final class ShowViewJob {
      private final String viewId;

      public ShowViewJob(String var2) {
         this.viewId = var2;
      }

      public void run() {
         View var1 = this.showViewPreReqs(this.viewId);
         this.showViewImpl(var1);
      }

      private View showViewPreReqs(String var1) {
         View var2 = ViewManagerImpl.this.viewMap.get(var1);
         if (var2 == null) {
            var2 = this.initializeView(var1);
            if (var2 == null) {
               throw new RuntimeException("Unknown view ID: " + var1);
            }

            ViewManagerImpl.this.fireViewLoaded(var2);
         }

         return var2;
      }

      private View initializeView(String var1) {
         View var2 = UiThreadSupplier.call(new ViewManagerImpl.ViewSupplier(var1));
         ViewManagerImpl.this.viewMap.put(var1, var2);
         return var2;
      }

      private void showViewImpl(View var1) {
         if (this.isViewReady(var1)) {
            ViewManagerImpl.this.visibleViews.add(var1.getID());
            var1.setActive(true);
            this.notifyListeners(var1);
         }
      }

      private boolean isViewReady(View var1) {
         return ViewManagerImpl.this.visibleViews.contains(var1.getID()) || var1.prepareForDisplay();
      }

      private void notifyListeners(final View var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewManagerImpl.this.fireViewDisplayed(var1);
            }
         });
      }
   }

   private static final class ViewSupplier implements Supplier<View> {
      private final String id;

      public ViewSupplier(String var1) {
         this.id = var1;
      }

      public View get() {
         String var1 = this.id + ".View";
         if (Application.containsBean(var1)) {
            return Application.getBean(var1, View.class);
         }

         String var2 = "The application configuration does not contain a definition for the View with ID \"" + this.id + "\".";
         FatalConfigurationError.throwError(9, ViewManagerImpl.class, new Exception(var2), null);
         return null;
      }
   }
}
