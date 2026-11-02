package org.etlforge.application.ui.window.action;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.event.ActiveObjectProviderEvent;

public abstract class AbstractActiveObjectAction extends AbstractApplicationAction implements ActiveObjectsAction {
   private ActiveObjectService activeObjectService;
   private List<? extends Object> activeObjects;

   public AbstractActiveObjectAction(ActiveObjectService var1, String var2, ActionDictionary var3) {
      super(var2, var3);
      this.init(var1);
   }

   public AbstractActiveObjectAction(String var1, ActiveObjectService var2, String var3, Icon var4) {
      super(var1, var3, var4);
      this.init(var2);
   }

   private void init(ActiveObjectService var1) {
      var1.addActiveObjectListener(this);
      this.activeObjectService = var1;
      this.setActiveObjects(var1.getActiveObjects());
   }

   @Override
   public void dispose() {
      this.activeObjectService.removeActiveObjectListener(this);
   }

   @Override
   public final void activeObjectsChanged(ActiveObjectProviderEvent var1) {
      this.setActiveObjects(var1.getActiveObjects());
   }

   @Override
   public void setActiveObjects(List<?> var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         for (Object var4 : var1) {
            if (this.accepts(var4)) {
               var2.add(var4);
            }
         }
      }

      this.activeObjects = var2;
      this.updateEnabledState(this.activeObjects);
   }

   @Override
   public boolean accepts(Object var1) {
      return false;
   }

   protected <T> List<T> getActiveObjects(Class<T> var1) {
      ArrayList var2 = new ArrayList();
      if (this.activeObjects != null) {
         for (Object var4 : this.activeObjects) {
            if (var1.isInstance(var4)) {
               var2.add(var1.cast(var4));
            }
         }
      }

      return var2;
   }

   protected abstract void updateEnabledState(List<? extends Object> var1);

   protected ActiveObjectService getActiveObjectService() {
      return this.activeObjectService;
   }

   protected final List<? extends Object> getActiveObjects() {
      return this.activeObjects;
   }
}
