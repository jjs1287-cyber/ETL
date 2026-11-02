package org.etlforge.application.ui.window.action;

import com.google.common.base.Function;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.Icon;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.functors.function.KongaFunctions;

public abstract class AbstractSingleActiveObjectAction<T> extends AbstractActiveObjectAction {
   private final Function<Object, T> transform;
   private T object;

   public AbstractSingleActiveObjectAction(ActiveObjectService var1, String var2, ActionDictionary var3, Class<T> var4) {
      super(var1, var2, var3);
      this.transform = KongaFunctions.cast(var4);
      this.updateEnabledState(this.getActiveObjects());
   }

   public AbstractSingleActiveObjectAction(String var1, ActiveObjectService var2, String var3, Icon var4, Class<T> var5) {
      super(var1, var2, var3, var4);
      this.transform = KongaFunctions.cast(var5);
      this.updateEnabledState(this.getActiveObjects());
   }

   @Override
   public final boolean accepts(Object var1) {
      return true;
   }

   @Override
   protected final void updateEnabledState(List<? extends Object> var1) {
      this.object = null;
      if (this.transform == null) {
         this.setEnabled(false);
      } else {
         if (var1.size() == 1) {
            this.object = this.transform.apply(var1.get(0));
         }

         this.setEnabled(this.object != null && this.isEnabledFor(this.object));
      }
   }

   @Override
   public final void execute(ActionEvent var1) {
      if (this.object != null) {
         this.runOnObject(this.object);
      }
   }

   protected abstract void runOnObject(T var1);

   protected boolean isEnabledFor(T var1) {
      return true;
   }
}
