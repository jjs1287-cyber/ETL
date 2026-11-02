package org.etlforge.integration.data;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.etlforge.base.ListenerReport;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.EnumWithId;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.PropertyChangeManager;
import org.etlforge.util.StringKeyValuePairsPersistor;
import org.etlforge.util.id.KongaGuid;
import org.etlforge.util.id.KongaID;
import org.etlforge.util.name.DefaultNameValidator;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.Name;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;
import org.etlforge.util.name.Nameable;
import org.etlforge.util.persist.Persistor;

public abstract class IntegrationData implements Nameable, Cloneable {
   public static final String PROPERTY_ID = "ID";
   public static final String PROPERTY_DELETED = "Deleted";
   public static final String PROPERTY_NAME = "Name";
   public static final String PROPERTY_DESCRIPTION = "Description";
   private static final AtomicLong temporaryName = new AtomicLong(System.currentTimeMillis());
   private Object dataLock = new Object();
   private transient boolean deleted;
   private PropertyChangeManager propChangeManager;
   private KongaID id;
   private Name nameObject;
   private StringKeyValuePairs properties = new StringKeyValuePairs();

   public IntegrationData() {
      this(null, getTemporaryName());
   }

   public IntegrationData(String var1) {
      this(null, checkName(var1));
   }

   public IntegrationData(IntegrationId var1, String var2) {
      this.id = var1 != null ? var1 : KongaGuid.createGuid();

      try {
         this.nameObject = new Name(checkName(var2), getNameValidator(this));
      } catch (IllegalNameException var4) {
         throw new IllegalArgumentException("The specified name is illegal: " + var4.getMessage());
      }
   }

   private static NameValidator getNameValidator(Nameable var0) {
      NameValidator var1 = NameValidatorStore.getInstance().get(var0);
      return var1 == null ? new DefaultNameValidator() : var1;
   }

   public final Object getDataLock() {
      return this.dataLock;
   }

   public KongaID getID() {
      return this.id;
   }

   protected void setID(KongaID var1) {
      KongaID var2 = null;
      synchronized (this.getDataLock()) {
         if (!KongaID.areEqual(var1, this.id)) {
            var2 = this.id;
            this.id = var1;
         }
      }

      if (var2 != null) {
         this.firePropertyChange("ID", var2, var1);
      }
   }

   @Override
   public String getName() {
      return this.nameObject.getName();
   }

   public void setName(String var1) throws IllegalNameException {
      if (var1 == null) {
         var1 = "";
      }

      var1 = var1.trim();
      Name var2 = new Name(this.nameObject);
      if (this.nameObject.setName(var1)) {
         this.firePropertyChange("Name", var2, this.nameObject);
      }
   }

   public final String getDescription() {
      synchronized (this.getDataLock()) {
         return this.getProperty("Description", "");
      }
   }

   public void setDescription(String var1) {
      this.setStringProperty("Description", var1);
   }

   public void delete() {
      synchronized (this.getDataLock()) {
         if (this.deleted) {
            return;
         }

         this.deleted = true;
      }

      this.firePropertyChange("Deleted", false, true);
      if (this.propChangeManager != null) {
         this.propChangeManager.removeAllPropertyChangeListeners();
      }
   }

   public final boolean isDeleted() {
      synchronized (this.getDataLock()) {
         return this.deleted;
      }
   }

   public void setDeleted(boolean var1) {
      synchronized (this.getDataLock()) {
         this.deleted = var1;
      }
   }

   public final String getProperty(String var1) {
      return this.properties.get(var1);
   }

   public final String getProperty(String var1, String var2) {
      return this.properties.get(var1, var2);
   }

   public final <E extends Enum<E>> E getEnumProperty(String var1, Class<E> var2, E var3) {
      return this.properties.getEnum(var1, var2, (E)var3);
   }

   public final <E extends Enum<E>> void setEnumProperty(String var1, Class<E> var2, E var3) {
      Enum var4;
      synchronized (this.getDataLock()) {
         var4 = this.getEnumProperty(var1, var2, null);
         this.properties.putEnum(var1, (E)var3);
      }

      if (var4 != var3) {
         this.firePropertyChange(var1, var4, var3);
      }
   }

   protected void setProperty(String var1, String var2) {
      this.properties.putString(var1, var2);
   }

   protected final void setProperty(String var1, int var2) {
      this.properties.putInt(var1, var2);
   }

   protected final void setProperty(String var1, long var2) {
      this.properties.putLong(var1, var2);
   }

   public final void setProperty(String var1, Object var2) {
      this.setProperty(var1, var2, false);
   }

   public final void setProperty(String var1, Object var2, boolean var3) {
      String var4;
      synchronized (this.getDataLock()) {
         var4 = this.properties.get(var1);
         this.properties.putObject(var1, var2);
      }

      if (var3) {
         this.firePropertyChange(var1, var4, var2);
      }
   }

   public StringKeyValuePairs getCopyOfKeyValues() {
      synchronized (this.getDataLock()) {
         return new StringKeyValuePairs(this.properties);
      }
   }

   public final StringKeyValuePairs getKeyValues() {
      synchronized (this.getDataLock()) {
         return this.properties;
      }
   }

   public final void setProperty(String var1) {
      this.properties.putKeyValuePair(var1);
   }

   protected final StringKeyValuePairs getData() {
      return this.properties;
   }

   public final void setStringProperty(String var1, String var2) {
      String var3;
      synchronized (this.getDataLock()) {
         var3 = this.getProperty(var1);
         this.setProperty(var1, var2);
      }

      if (KongaStringUtils.stringsDiffer(var3, var2)) {
         this.firePropertyChange(var1, var3, var2);
      }
   }

   public final boolean getBooleanProperty(String var1, boolean var2) {
      return this.properties.getBoolean(var1, var2);
   }

   public final void setBooleanProperty(String var1, boolean var2) {
      this.setBooleanProperty(var1, var2, false);
   }

   protected final void setBooleanProperty(String var1, boolean var2, boolean var3) {
      boolean var4;
      synchronized (this.getDataLock()) {
         var4 = this.getBooleanProperty(var1, var3);
         this.properties.putBoolean(var1, var2);
      }

      if (var4 != var2) {
         this.firePropertyChange(var1, var4, var2);
      }
   }

   public final int getIntProperty(String var1, int var2) {
      return this.properties.getInt(var1, var2);
   }

   public final void setIntProperty(String var1, int var2) {
      int var3;
      synchronized (this.getDataLock()) {
         var3 = this.getIntProperty(var1, 0);
         this.setProperty(var1, var2);
      }

      if (var2 != var3) {
         this.firePropertyChange(var1, var3, var2);
      }
   }

   protected final <E extends Enum<E> & EnumWithId<Integer>> void setEnumWithIntIdProperty(String var1, E var2, E var3) {
      int var4;
      int var5;
      synchronized (this.getDataLock()) {
         var4 = this.getIntProperty(var1, (Integer)((EnumWithId)var3).getId());
         var5 = (Integer)((EnumWithId)var2).getId();
         this.setProperty(var1, Integer.toString(var5));
      }

      if (var4 != var5) {
         this.firePropertyChange(var1, EnumWithIdLookup.lookupOr(var4, (E)var3), var2);
      }
   }

   public final long getLongProperty(String var1, long var2) {
      return this.properties.getLong(var1, var2);
   }

   public final void setLongProperty(String var1, long var2) {
      long var4;
      synchronized (this.getDataLock()) {
         var4 = this.getLongProperty(var1, 0L);
         this.setProperty(var1, var2);
      }

      if (var2 != var4) {
         this.firePropertyChange(var1, var4, var2);
      }
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + "[" + this.getName() + "]";
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      if (this.getClass().equals(var1.getClass())) {
         synchronized (this.getDataLock()) {
            StringKeyValuePairs var3 = this.properties;
            StringKeyValuePairs var4 = ((IntegrationData)var1).properties;
            if (var3.size() != var4.size()) {
               return false;
            }

            for (String var6 : var3.getKeys()) {
               String var7 = var3.get(var6);
               String var8 = var4.get(var6);
               if (KongaStringUtils.stringsDiffer(var7, var8)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      synchronized (this.getDataLock()) {
         StringKeyValuePairs var3 = this.properties;

         for (String var5 : var3.getKeys()) {
            String var6 = var3.get(var5);
            if (var6 != null) {
               var1 = var1 * 31 + var6.hashCode();
            } else {
               var1 *= 31;
            }
         }

         return var1;
      }
   }

   private static String getTemporaryName() {
      return String.valueOf(temporaryName.incrementAndGet());
   }

   private static String checkName(String var0) {
      return var0 != null && var0.length() != 0 ? var0 : getTemporaryName();
   }

   protected void collectValidationMessages(ValidationMessageCollector var1) {
   }

   public IntegrationData clone() {
      synchronized (this.getDataLock()) {
         IntegrationData var10000;
         try {
            IntegrationData var2 = (IntegrationData)super.clone();
            var2.dataLock = new Object();
            if (this.propChangeManager != null) {
               var2.propChangeManager = new PropertyChangeManager(var2);
            }

            var2.nameObject = new Name(this.nameObject);
            var2.properties = new StringKeyValuePairs(this.properties);
            var10000 = var2;
         } catch (CloneNotSupportedException var4) {
            throw new RuntimeException(var4);
         }

         return var10000;
      }
   }

   public void writeTo(Persistor var1) {
      synchronized (this.getDataLock()) {
         Set var3 = this.getKeysToSkipWhenPersisting();
         Predicate var4 = var3.isEmpty() ? Predicates.alwaysTrue() : Predicates.not(Predicates.in(var3));
         StringKeyValuePairsPersistor.save(this.properties, var1.createChild("Properties"), var4);
      }
   }

   public void restoreFrom(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Properties");
      if (var2 == null) {
         throw new IllegalArgumentException("Invalid persistor: no Properties root found");
      }

      synchronized (this.getDataLock()) {
         this.properties = StringKeyValuePairsPersistor.restoreFrom(var2);
      }
   }

   protected Set<String> getKeysToSkipWhenPersisting() {
      return Collections.emptySet();
   }

   public ListenerReport reportListeners() {
      ListenerReport var1 = new ListenerReport(this);
      var1.addPropertyChangeListeners(this.propChangeManager);
      return var1;
   }

   protected final PropertyChangeManager getPropertyChangeManager() {
      return this.propChangeManager;
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      if (this.propChangeManager == null) {
         this.propChangeManager = new PropertyChangeManager(this);
      }

      this.propChangeManager.addPropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      if (this.propChangeManager == null) {
         this.propChangeManager = new PropertyChangeManager(this);
      }

      this.propChangeManager.addPropertyChangeListener(var1, var2);
   }

   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      if (this.propChangeManager != null) {
         this.propChangeManager.removePropertyChangeListener(var1);
      }
   }

   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      if (this.propChangeManager != null) {
         this.propChangeManager.removePropertyChangeListener(var1, var2);
      }
   }

   public final void firePropertyChange(String var1, Object var2, Object var3) {
      if (var1 != null) {
         if (this.propChangeManager != null && this.propChangeManager.hasListeners(var1)) {
            this.propChangeManager.firePropertyChange(var1, var2, var3);
         }
      }
   }

   protected final void firePropertyChange(String var1, int var2, int var3) {
      this.firePropertyChange(var1, (Object)var2, (Object)Integer.valueOf(var3));
   }

   protected final void firePropertyChange(String var1, long var2, long var4) {
      this.firePropertyChange(var1, (Object)var2, (Object)Long.valueOf(var4));
   }

   protected final void firePropertyChange(String var1, boolean var2, boolean var3) {
      this.firePropertyChange(var1, (Object)var2, (Object)Boolean.valueOf(var3));
   }

   protected final void firePropertyChange(String var1, double var2, double var4) {
      this.firePropertyChange(var1, new Double(var2), new Double(var4));
   }

   protected final void firePropertyChange(String var1, float var2, float var3) {
      this.firePropertyChange(var1, new Float(var2), new Float(var3));
   }

   protected final void firePropertyChange(String var1, char var2, char var3) {
      this.firePropertyChange(var1, (Object)var2, (Object)Character.valueOf(var3));
   }

   protected final void firePropertyChange(PropertyChangeEvent var1) {
      this.firePropertyChange(var1.getPropertyName(), var1.getOldValue(), var1.getNewValue());
   }
}
