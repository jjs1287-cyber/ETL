package org.etlforge.util.name;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class Name implements Nameable {
   public static final String PROPERTY_NAME = "Name";
   private String name;
   private boolean allowEmptyName;
   private String illegalCharacters;
   private List<PropertyChangeListener> propChangeListeners;
   private NameValidator nameValidator;
   private final Object lock = new Object();

   public Name() {
      this.name = "";
      this.allowEmptyName = true;
   }

   public Name(String var1) {
      this.name = var1;
      this.allowEmptyName = true;
   }

   public Name(boolean var1) {
      this.name = "";
      this.allowEmptyName = var1;
   }

   public Name(String var1, boolean var2) throws IllegalNameException {
      this.name = var1;
      this.allowEmptyName = var2;
      this.checkName(this.name, true);
   }

   public Name(Name var1) {
      this.name = var1.getName();
      this.allowEmptyName = var1.getAllowEmptyName();
      this.illegalCharacters = var1.getIllegalCharacters();
   }

   public Name(NameValidator var1) throws IllegalNameException {
      this("", var1);
   }

   public Name(String var1, NameValidator var2) throws IllegalNameException {
      if (var2 == null) {
         throw new IllegalArgumentException("validator cannot be null.");
      }

      this.name = var1;
      this.nameValidator = var2;
      this.nameValidator.validateName(var1);
   }

   public void setIllegalCharacters(String var1) throws IllegalNameException {
      this.illegalCharacters = var1;
      this.checkName(this.getName(), false);
   }

   @Override
   public String getName() {
      synchronized (this.lock) {
         return this.name;
      }
   }

   public String getIllegalCharacters() {
      return this.illegalCharacters;
   }

   public boolean getAllowEmptyName() {
      return this.allowEmptyName;
   }

   public boolean setName(String var1) throws IllegalNameException {
      String var2 = this.getName();
      boolean var3 = false;
      synchronized (this.lock) {
         this.checkName(var1, true);
         if (!StringUtils.equals(var1, this.name)) {
            this.name = var1;
            var3 = true;
         }
      }

      if (var3) {
         this.fireNameChanged(var2, var1);
      }

      return var3;
   }

   private void checkName(String var1, boolean var2) throws IllegalNameException {
      if (this.nameValidator != null) {
         this.nameValidator.validateName(var1);
      } else {
         if (var2 && !this.allowEmptyName && StringUtils.isBlank(var1)) {
            throw new IllegalNameException("The name cannot be empty.");
         }

         if (StringUtils.isNotBlank(this.illegalCharacters)) {
            for (int var3 = 0; var3 < this.illegalCharacters.length(); var3++) {
               if (var1.indexOf(this.illegalCharacters.charAt(var3)) >= 0) {
                  throw new IllegalNameException(null, var1, this.illegalCharacters);
               }
            }
         }
      }
   }

   @Override
   public String toString() {
      return this.getName();
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      if (this.propChangeListeners == null) {
         this.propChangeListeners = new ArrayList<>();
      }

      if (!this.propChangeListeners.contains(var1)) {
         this.propChangeListeners.add(var1);
      }
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      if (var1 != null && this.propChangeListeners != null) {
         this.propChangeListeners.remove(var1);
      }
   }

   protected void fireNameChanged(String var1, String var2) {
      if (this.propChangeListeners != null) {
         PropertyChangeEvent var3 = null;

         for (PropertyChangeListener var5 : this.propChangeListeners) {
            if (var3 == null) {
               var3 = new PropertyChangeEvent(this, "Name", var1, var2);
            }

            var5.propertyChange(var3);
         }
      }
   }
}
