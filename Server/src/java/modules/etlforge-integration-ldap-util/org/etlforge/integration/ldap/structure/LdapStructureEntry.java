package org.etlforge.integration.ldap.structure;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public final class LdapStructureEntry {
   public static final String TRANSFORMATION_ID = "transformation_id";
   public static final String TYPE = "type";
   public static final String OID = "oid";
   public static final String ATTRIBUTES = "attributes";
   public static final String AUXILIARY_CLASSES = "auxiliary_classes";
   private static final char ATTRIBUTE_STRING_REP_SEPARATOR = '\t';
   private final NumericOid oid;
   private final String[] attributes;
   private final NumericOid[] auxiliary;
   private final Set<LdapStructureAttribute> extendedAttributes;

   public static String[] getLdapStructureEntriesColumnNames() {
      return new String[]{"transformation_id", "type", "oid", "attributes", "auxiliary_classes"};
   }

   public LdapStructureEntry(NumericOid var1) {
      this(var1, null, null);
   }

   public LdapStructureEntry(NumericOid var1, String[] var2) {
      this(var1, var2, null);
   }

   public LdapStructureEntry(NumericOid var1, String[] var2, NumericOid[] var3) {
      this(var1, var2, var3, null);
   }

   public LdapStructureEntry(NumericOid var1, String[] var2, NumericOid[] var3, LdapStructureAttribute[] var4) {
      Preconditions.checkNotNull(var1, "oid");
      this.oid = var1;
      if (var2 != null) {
         this.attributes = (String[])var2.clone();
      } else {
         this.attributes = null;
      }

      if (var3 != null) {
         this.auxiliary = (NumericOid[])var3.clone();
      } else {
         this.auxiliary = new NumericOid[0];
      }

      if (var4 != null) {
         this.extendedAttributes = Sets.newHashSet(var4);
      } else {
         this.extendedAttributes = Sets.newHashSet();
      }
   }

   public NumericOid getOid() {
      return this.oid;
   }

   public String[] getAttributeNames() {
      return this.attributes != null ? (String[])this.attributes.clone() : null;
   }

   public Set<LdapStructureAttribute> getExtendedAttributes() {
      return Sets.newHashSet(this.extendedAttributes);
   }

   public boolean isExtendedAttribute(LdapStructureAttribute var1) {
      return this.extendedAttributes.contains(var1);
   }

   private static final String toStringRep(Object[] var0) {
      StringBuilder var1 = new StringBuilder();
      if (var0 != null) {
         for (Object var5 : var0) {
            var1.append(var5.toString());
            var1.append('\t');
         }
      }

      if (var1.length() > 0) {
         var1.deleteCharAt(var1.length() - 1);
      }

      return var1.toString();
   }

   public static final String[] attributesFromString(String var0) {
      return var0 != null && !var0.isEmpty() ? StringUtils.split(var0, '\t') : new String[0];
   }

   public String getAttributesAsString() {
      return toStringRep(this.getAttributeNames());
   }

   public static final NumericOid[] auxiliaryClassesFromString(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         String[] var1 = StringUtils.split(var0, '\t');
         NumericOid[] var2 = new NumericOid[var1.length];

         for (int var3 = 0; var3 < var1.length; var3++) {
            var2[var3] = new NumericOid(var1[var3]);
         }

         return var2;
      } else {
         return new NumericOid[0];
      }
   }

   public NumericOid[] getAuxiliaryClasses() {
      return (NumericOid[])this.auxiliary.clone();
   }

   public String getAuxiliaryClassesAsString() {
      return toStringRep(this.getAuxiliaryClasses());
   }

   public boolean hasAuxiliaryClasses() {
      return this.auxiliary.length > 0;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof LdapStructureEntry ? this.equals((LdapStructureEntry)var1) : false;
      }
   }

   private boolean equals(LdapStructureEntry var1) {
      if (!this.oid.equals(var1.oid)) {
         return false;
      } else if (!equals(this.attributes, var1.attributes)) {
         return false;
      } else {
         return !equals(this.auxiliary, var1.auxiliary) ? false : Objects.equal(this.extendedAttributes, var1.extendedAttributes);
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.oid.hashCode();
      var1 = 31 * var1 + (this.attributes != null ? Sets.newHashSet(this.attributes).hashCode() : 0);
      var1 = 31 * var1 + Sets.newHashSet(this.auxiliary).hashCode();
      return 31 * var1 + this.extendedAttributes.hashCode();
   }

   private static <T> boolean equals(T[] var0, T[] var1) {
      if (var0 == null) {
         return var1 == null;
      }

      if (var1 == null) {
         return false;
      }

      HashSet var2 = Sets.newHashSet(var0);
      HashSet var3 = Sets.newHashSet(var1);
      return var2.equals(var3);
   }
}
