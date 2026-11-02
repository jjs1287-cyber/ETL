package org.etlforge.util;

import java.beans.PropertyChangeListener;

public abstract class AbstractPropertyBasedModel {
   private final PropertyService service = new PropertyService(this);

   protected AbstractPropertyBasedModel() {
   }

   protected final <T> DefaultProperty<T> newProperty(String var1) {
      return this.service.newProperty(var1);
   }

   protected final <T> DefaultProperty<T> newProperty(String var1, T var2) {
      return this.service.newProperty(var1, (T)var2);
   }

   protected final <T> ListProperty<T> newListProperty(String var1) {
      return this.service.newListProperty(var1);
   }

   protected final StringProperty newStringProperty(String var1, String var2) {
      return this.service.newStringProperty(var1, var2);
   }

   protected final StringProperty newAutoTrimmedStringProperty(String var1, String var2) {
      StringProperty var3 = this.newStringProperty(var1, var2 == null ? null : var2.trim());
      var3.setAutoTrim(true);
      return var3;
   }

   protected final void sendChange(String var1, Object var2, Object var3) {
      this.service.sendChange(var1, var2, var3);
   }

   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      this.service.addPropertyChangeListener(var1);
   }

   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      this.service.removePropertyChangeListener(var1);
   }

   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.service.addPropertyChangeListener(var1, var2);
   }

   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.service.removePropertyChangeListener(var1, var2);
   }

   public final <T> Property<T> getProperty(String var1) {
      return this.service.getProperty(var1);
   }
}
