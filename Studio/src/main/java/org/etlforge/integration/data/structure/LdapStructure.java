package org.etlforge.integration.data.structure;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.ldap.LdapSearchParameters;
import org.etlforge.integration.data.structure.ldap.LdapSearchParametersPersistor;
import org.etlforge.integration.data.structure.ldap.LdapStructureEntryPersistor;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.system.annotations.InternalConstructor;
import org.etlforge.system.annotations.InternalMethod;
import org.etlforge.util.persist.Persistor;

public final class LdapStructure extends DataStructure {
   public static final String CLASS_ENTRIES = "classEntries";
   public static final String SEARCH_PARAMS = "searchParams";
   public static final String CLASS_NAMES_PROPERTY = "classNames";
   public static final String SELECTED_ATTRIBUTES = "selectedAttributes";
   private LdapLocation location;
   private LdapStructureEntry[] entries;
   private Map<NumericOid, LdapSearchParameters> searchParams;

   @InternalConstructor
   public LdapStructure() {
      super(DataStructureType.LDAP);
      this.searchParams = Maps.newHashMap();
      this.setEntriesImpl(null);
   }

   public LdapStructure(LdapLocation var1) {
      this(var1, null);
   }

   public LdapStructure(LdapLocation var1, LdapStructureEntry[] var2) {
      super(DataStructureType.LDAP);
      Preconditions.checkNotNull(var1, "loc");
      this.location = var1;
      this.searchParams = Maps.newHashMap();
      this.setEntriesImpl(var2);
   }

   public LdapStructure withNewLocation(LdapLocation var1) {
      Preconditions.checkNotNull(var1);
      synchronized (this.getDataLock()) {
         if (var1 == this.location) {
            return this;
         }

         LdapStructure var3 = new LdapStructure(var1, this.entries);
         var3.searchParams = Maps.newHashMap(this.searchParams);
         return var3;
      }
   }

   @Override
   public String getName() {
      synchronized (this.getDataLock()) {
         if (this.location != null) {
            String var2 = this.location.getServer();
            return "LDAP - " + (var2.length() > 0 ? var2 : "[server not specified]");
         } else {
            return super.getName();
         }
      }
   }

   public LdapLocation getLdapLocation() {
      synchronized (this.getDataLock()) {
         return this.location == null ? null : this.location.clone();
      }
   }

   public LdapLocation getUnprotectedLdapLocation() {
      synchronized (this.getDataLock()) {
         return this.location;
      }
   }

   @InternalMethod
   public void setLdapLocation(LdapLocation var1) {
      Preconditions.checkNotNull(var1, "loc");
      synchronized (this.getDataLock()) {
         this.location = var1;
      }
   }

   public LdapStructureEntry[] getClassEntries() {
      synchronized (this.getDataLock()) {
         return (LdapStructureEntry[])this.entries.clone();
      }
   }

   public Multimap<NumericOid, LdapStructureAttribute> getExtendedAttributeInfo() {
      HashMultimap var1 = HashMultimap.create();
      synchronized (this.getDataLock()) {
         for (LdapStructureEntry var6 : this.entries) {
            Set var7 = var6.getExtendedAttributes();
            var1.putAll(var6.getOid(), var7);
         }

         return var1;
      }
   }

   public Set<LdapStructureAttribute> getExtendedAttributes() {
      HashSet var1 = Sets.newHashSet();
      synchronized (this.getDataLock()) {
         for (LdapStructureEntry var6 : this.entries) {
            var1.addAll(var6.getExtendedAttributes());
         }

         return var1;
      }
   }

   public Map<NumericOid, NumericOid[]> getAuxClassInfo() {
      HashMap var1 = Maps.newHashMap();

      for (LdapStructureEntry var5 : this.entries) {
         synchronized (this.getDataLock()) {
            var1.put(var5.getOid(), var5.getAuxiliaryClasses());
         }
      }

      return var1;
   }

   public Set<String> getSelectedAttributeNames() {
      HashSet var1 = null;
      synchronized (this.getDataLock()) {
         for (LdapStructureEntry var6 : this.entries) {
            String[] var7 = var6.getAttributeNames();
            if (var7 != null) {
               if (var1 == null) {
                  var1 = Sets.newHashSet();
               }

               var1.addAll(Sets.newHashSet(var7));
            }
         }

         return var1;
      }
   }

   public LdapStructureEntry getClassEntry(NumericOid var1) {
      synchronized (this.getDataLock()) {
         for (LdapStructureEntry var6 : this.entries) {
            if (var6.getOid().equals(var1)) {
               return var6;
            }
         }

         return null;
      }
   }

   public void setClassEntries(LdapStructureEntry[] var1) {
      LdapStructureEntry[] var2;
      synchronized (this.getDataLock()) {
         var2 = this.entries;
         this.setEntriesImpl(var1);
      }

      this.firePropertyChange("classEntries", var2, var1);
   }

   public NumericOid[] getObjectClassOids() {
      HashSet var1 = Sets.newHashSet();
      synchronized (this.getDataLock()) {
         for (LdapStructureEntry var6 : this.entries) {
            collectClassOids(var6, var1);
         }
      }

      return (NumericOid[]) var1.toArray(new NumericOid[var1.size()]);
   }

   public NumericOid[] getStructuralObjectClassOids() {
      HashSet var1 = Sets.newHashSet();
      synchronized (this.getDataLock()) {
         for (LdapStructureEntry var6 : this.entries) {
            var1.add(var6.getOid());
         }
      }

      return (NumericOid[]) var1.toArray(new NumericOid[var1.size()]);
   }

   public boolean hasAuxiliaryClasses() {
      synchronized (this.getDataLock()) {
         for (LdapStructureEntry var5 : this.entries) {
            if (var5.hasAuxiliaryClasses()) {
               return true;
            }
         }

         return false;
      }
   }

   public void addSearchParameters(NumericOid var1, LdapSearchParameters var2) {
      LdapSearchParameters var3;
      synchronized (this.getDataLock()) {
         this.checkSourceStructure("addSearchParameters");
         var3 = this.searchParams.put(var1, var2);
      }

      if (!equals(var3, var2)) {
         this.firePropertyChange("searchParams", var3, var2);
      }
   }

   public LdapSearchParameters getSearchParameters(NumericOid var1) {
      synchronized (this.getDataLock()) {
         this.checkSourceStructure("getSearchParameters");
         return this.searchParams.get(var1);
      }
   }

   public void clearSourceParameters() {
      int var1;
      synchronized (this.getDataLock()) {
         this.checkSourceStructure("clearSourceParameters");
         var1 = this.searchParams.size();
         this.searchParams.clear();
      }

      if (var1 != 0) {
         this.firePropertyChange("searchParams", "0", "1");
      }
   }

   @Override
   public StructureNature getStructureNature() {
      return StructureNature.Tree;
   }

   @Override
   public boolean isCompatibleWith(DataLocation var1) {
      return var1 instanceof LdapLocation;
   }

   @Override
   public boolean isChunkable() {
      return this.getClassEntries().length == 1;
   }

   public LdapStructure clone() {
      synchronized (this.getDataLock()) {
         LdapStructure var2 = (LdapStructure)super.clone();
         var2.searchParams = Maps.newHashMap(this.searchParams);
         var2.entries = new LdapStructureEntry[this.entries.length];
         System.arraycopy(this.entries, 0, var2.entries, 0, this.entries.length);
         return var2;
      }
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      synchronized (this.getDataLock()) {
         return super.equals(var1) ? this.equals((LdapStructure)var1) : false;
      }
   }

   private boolean equals(LdapStructure var1) {
      return !equals(this.location, var1.location)
         ? false
         : Sets.newHashSet(this.entries).equals(Sets.newHashSet(var1.entries)) && this.searchParams.equals(var1.searchParams);
   }

   @Override
   public int hashCode() {
      synchronized (this.getDataLock()) {
         int var2 = 17;
         var2 = 31 * var2 + super.hashCode();
         var2 = 31 * var2 + (this.location != null ? this.location.hashCode() : 0);
         var2 = 31 * var2 + Sets.newHashSet(this.entries).hashCode();
         return 31 * var2 + this.searchParams.hashCode();
      }
   }

   @Override
   public void writeTo(Persistor var1) {
      synchronized (this.getDataLock()) {
         this.persistEntries(var1);
         this.persistSearchParams(var1);
         super.writeTo(var1);
      }
   }

   private void persistEntries(Persistor var1) {
      LdapStructureEntryPersistor var2 = new LdapStructureEntryPersistor();

      for (LdapStructureEntry var6 : this.entries) {
         var2.writeTo(var6, var1.createChild("LdapStructureEntry"));
      }
   }

   private void persistSearchParams(Persistor var1) {
      LdapSearchParametersPersistor var2 = new LdapSearchParametersPersistor();

      for (Entry var4 : this.searchParams.entrySet()) {
         Persistor var5 = var1.createChild("SearchParams");
         var5.putString("oid", ((NumericOid)var4.getKey()).toString());
         Persistor var6 = var5.createChild("Data");
         var2.writeTo((LdapSearchParameters)var4.getValue(), var6);
      }
   }

   @Override
   public void restoreFrom(Persistor var1) {
      synchronized (this.getDataLock()) {
         super.restoreFrom(var1);
         this.restoreEntries(var1);
         this.restoreSearchParams(var1);
      }
   }

   private void restoreEntries(Persistor var1) {
      ArrayList var2 = Lists.newArrayList();
      LdapStructureEntryPersistor var3 = new LdapStructureEntryPersistor();

      for (Persistor var5 : var1.getChildren("LdapStructureEntry")) {
         var2.add(var3.restoreFrom(var5));
      }

      this.entries = (LdapStructureEntry[]) var2.toArray(new LdapStructureEntry[var2.size()]);
   }

   private void restoreSearchParams(Persistor var1) {
      LdapSearchParametersPersistor var2 = new LdapSearchParametersPersistor();
      HashMap var3 = Maps.newHashMap();

      for (Persistor var5 : var1.getChildren("SearchParams")) {
         NumericOid var6 = new NumericOid(var5.getString("oid"));
         Persistor var7 = var5.getFirstChild("Data");
         var3.put(var6, var2.restoreFrom(var7));
      }

      this.searchParams = new HashMap<>(var3);
   }

   private void setEntriesImpl(LdapStructureEntry[] var1) {
      if (var1 == null) {
         this.entries = new LdapStructureEntry[0];
      } else {
         this.entries = (LdapStructureEntry[])var1.clone();
      }
   }

   private static void collectClassOids(LdapStructureEntry var0, Set<NumericOid> var1) {
      var1.add(var0.getOid());

      for (NumericOid var5 : var0.getAuxiliaryClasses()) {
         var1.add(var5);
      }
   }

   private static boolean equals(Object var0, Object var1) {
      return var0 != null ? var0.equals(var1) : var1 == null;
   }

   private void checkSourceStructure(String var1) {
      if (this.location != null && this.location.getSourceId() == null) {
         throw new UnsupportedOperationException(var1 + " is applicable for source structures only");
      }
   }
}
