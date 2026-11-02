package org.etlforge.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Map;

public class PropertyService implements Serializable {
   private final PropertyChangeSupport changeSupport;
   private final Map<String, Property> properties;

   public PropertyService(Object var1) {
      Preconditions.checkNotNull(var1, "client");
      this.changeSupport = new PropertyChangeSupport(var1);
      this.properties = Maps.newHashMap();
   }

   public <T> DefaultProperty<T> newProperty(String var1) {
      DefaultProperty var2 = new DefaultProperty(var1, this.changeSupport);
      return this.storeProperty(var1, var2);
   }

   private <T> DefaultProperty<T> storeProperty(String var1, DefaultProperty<T> var2) {
      this.properties.put(var1, var2);
      return var2;
   }

   public <T> DefaultProperty<T> newProperty(String var1, T var2) {
      DefaultProperty var3 = new DefaultProperty<>(var1, var2, this.changeSupport);
      return this.storeProperty(var1, var3);
   }

   public <T> ListProperty<T> newListProperty(String var1) {
      ListProperty var2 = new ListProperty(var1, this.changeSupport);
      this.properties.put(var1, var2);
      return var2;
   }

   public StringProperty newStringProperty(String var1, String var2) {
      StringProperty var3 = new StringProperty(var1, var2, this.changeSupport);
      this.properties.put(var1, var3);
      return var3;
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.changeSupport.removePropertyChangeListener(var1, var2);
   }

   public void sendChange(String var1, Object var2, Object var3) {
      this.changeSupport.firePropertyChange(var1, var2, var3);
   }

   public Property getProperty(String var1) {
      Property var2 = this.properties.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("No such property: " + var1);
      } else {
         return var2;
      }
   }
}
