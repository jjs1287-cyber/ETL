package org.etlforge.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public final class PropertyChangeManager implements Serializable {
   private final Object source;
   private final PropertyChangeSupport propChangeSupport;
   private boolean blockEvents;

   public PropertyChangeManager(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("src cannot be null");
      }

      this.source = var1;
      this.propChangeSupport = new PropertyChangeSupport(this.source);
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.addPropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.addPropertyChangeListener(var1, var2);
   }

   public void removeAllPropertyChangeListeners() {
      PropertyChangeListener[] var1 = this.propChangeSupport.getPropertyChangeListeners();

      for (int var2 = 0; var2 < var1.length; var2++) {
         this.propChangeSupport.removePropertyChangeListener(var1[var2]);
      }
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.removePropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.removePropertyChangeListener(var1, var2);
   }

   public void block() {
      this.blockEvents = true;
   }

   public void unblock() {
      this.blockEvents = false;
   }

   public boolean hasListeners(String var1) {
      return this.propChangeSupport.hasListeners(var1);
   }

   public PropertyChangeListener[] getListeners() {
      return this.propChangeSupport.getPropertyChangeListeners();
   }

   public void firePropertyChange(String var1, Object var2, Object var3) {
      if (!this.blockEvents) {
         if (this.propChangeSupport.hasListeners(var1)) {
            this.propChangeSupport.firePropertyChange(var1, var2, var3);
         }
      }
   }

   public void firePropertyChange(String var1, int var2, int var3) {
      this.firePropertyChange(var1, (Object)var2, (Object)Integer.valueOf(var3));
   }

   public void firePropertyChange(String var1, long var2, long var4) {
      this.firePropertyChange(var1, (Object)var2, (Object)Long.valueOf(var4));
   }

   public void firePropertyChange(String var1, boolean var2, boolean var3) {
      this.firePropertyChange(var1, (Object)var2, (Object)Boolean.valueOf(var3));
   }

   public void firePropertyChange(String var1, double var2, double var4) {
      this.firePropertyChange(var1, new Double(var2), new Double(var4));
   }

   public void firePropertyChange(String var1, float var2, float var3) {
      this.firePropertyChange(var1, new Float(var2), new Float(var3));
   }

   public void firePropertyChange(String var1, char var2, char var3) {
      this.firePropertyChange(var1, (Object)var2, (Object)Character.valueOf(var3));
   }
}
