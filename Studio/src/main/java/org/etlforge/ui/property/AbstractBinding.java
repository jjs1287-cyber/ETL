package org.etlforge.ui.property;

import com.google.common.base.Supplier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public abstract class AbstractBinding<T> implements Binding {
   protected final Property<T> property;
   private final AbstractBinding<T>.ModelListener modelListener;
   private boolean uiToModelEnabled;

   protected AbstractBinding(AbstractPropertyBasedModel var1, String var2) {
      this(var1.getProperty(var2));
   }

   protected AbstractBinding(Property<T> var1) {
      this.property = var1;
      this.modelListener = new AbstractBinding.ModelListener();
      var1.addListener(this.modelListener);
      this.uiToModelEnabled = true;
   }

   @Override
   public final void dispose() {
      this.property.removeListener(this.modelListener);
      this.disposeUiListener();
   }

   protected void disposeUiListener() {
   }

   @Override
   public final void setUiToModelEnabled(boolean var1) {
      this.uiToModelEnabled = var1;
   }

   protected final boolean isUiToModelEnabled() {
      return this.uiToModelEnabled;
   }

   @Override
   public final void setModelToUiEnabled(boolean var1) {
      this.modelListener.enabled = var1;
   }

   @Override
   public final void syncModel() {
      Object var1 = this.getValueFromUi();
      this.setModelToUiEnabled(false);

      try {
         this.property.set((T)var1);
      } finally {
         this.setModelToUiEnabled(true);
      }
   }

   protected T getValueFromUi() {
      throw new UnsupportedOperationException();
   }

   @Override
   public final void syncUi() {
      Object var1 = this.property.get();
      this.pushValueToUi((T)var1);
   }

   private void pushValueToUi(T var1) {
      this.setUiToModelEnabled(false);

      try {
         this.updateUiWithValue((T)var1);
      } finally {
         this.setUiToModelEnabled(true);
      }
   }

   public final void syncUiIfNotNull() {
      Object var1 = this.property.get();
      if (var1 != null) {
         this.pushValueToUi((T)var1);
      }
   }

   protected abstract void updateUiWithValue(T var1);

   protected final ActionListener defaultActionListener() {
      return new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (AbstractBinding.this.isUiToModelEnabled()) {
               AbstractBinding.this.syncModel();
            }
         }
      };
   }

   private class ModelListener implements PropertyChangeListener {
      private boolean enabled = true;

      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (this.enabled) {
            UiThreadSupplier.call(new Supplier<Void>() {
               public Void get() {
                  AbstractBinding.this.syncUi();
                  return null;
               }
            });
         }
      }
   }
}
