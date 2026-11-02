package org.etlforge.integration.data.structure.database;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import java.util.ArrayList;
import java.util.Collection;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.annotations.InternalMethod;

public final class DbFields {
   public static final int SOURCE_DIFF_KEYS = 1;
   public static final int SOURCE_UPDATE_VALUES = 2;
   public static final int UPDATE_KEYS = 3;
   public static final String FIELD_SEPARATOR = ",";
   public static final String DB_FIELD_ID = "dbfieldid";
   public static final String KEY_FIELDS = "keyfields";
   public static final String KEY_FIELDS_TYPE = "keyfieldstype";
   private final DbFields.Entry[] entries = new DbFields.Entry[3];

   public DbFields.Entry getEntry(int var1) {
      validateType(var1);
      return this.entries[var1 - 1];
   }

   public DbFields.Entry[] getAllEntries() {
      ArrayList var1 = Lists.newArrayList();

      for (int var2 = 0; var2 < this.entries.length; var2++) {
         if (this.entries[var2] != null) {
            var1.add(this.entries[var2]);
         }
      }

      return (DbFields.Entry[]) var1.toArray(new DbFields.Entry[var1.size()]);
   }

   @InternalMethod
   public void addEntry(DbFields.Entry var1) {
      this.entries[var1.getType() - 1] = var1;
   }

   public DbFields.Entry removeEntry(int var1) {
      validateType(var1);
      DbFields.Entry var2 = this.entries[var1 - 1];
      this.entries[var1 - 1] = null;
      return var2;
   }

   public void addSourceDiffKeys(Collection<DatabaseColumn> var1) {
      if (!var1.isEmpty()) {
         this.addEntry(new DbFields.Entry(UpdateKeyStringConverter.toString(var1), 1));
      }
   }

   public void addUpdateKeys(Collection<DatabaseColumn> var1) {
      if (!var1.isEmpty()) {
         this.addEntry(new DbFields.Entry(UpdateKeyStringConverter.toString(var1), 3));
      }
   }

   public DatabaseColumn[] getUpdateKeys(DatabaseObject var1) {
      Preconditions.checkNotNull(var1);
      DbFields.Entry var2 = this.getEntry(3);
      return var2 != null ? UpdateKeyStringConverter.fromString(var2.getKeyFields(), var1) : new DatabaseColumn[0];
   }

   public void addSourceUpdateValues(DatabaseColumn[] var1, String[] var2) {
      if (var1.length != var2.length) {
         throw new IllegalArgumentException("columns and values must have the same dimension.");
      }

      if (var1.length != 0) {
         String var3 = UpdateValuesStringConverter.toString(var1, var2);
         this.addEntry(new DbFields.Entry(var3, 2));
      }
   }

   public UpdateValue[] getUpdateValues(DatabaseObject var1) {
      Preconditions.checkNotNull(var1);
      DbFields.Entry var2 = this.getEntry(2);
      return var2 != null ? UpdateValuesStringConverter.fromString(var2.getKeyFields(), var1) : new UpdateValue[0];
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 instanceof DbFields) {
         DbFields var2 = (DbFields)var1;

         for (int var3 = 0; var3 < this.entries.length; var3++) {
            if (differs(this.entries[var3], var2.entries[var3])) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 17;

      for (DbFields.Entry var5 : this.entries) {
         var1 = 37 * var1 + (var5 == null ? 0 : var5.hashCode());
      }

      return var1;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DIFF:\n");
      var1.append("   ");
      var1.append(this.entries[0]);
      var1.append("\n");
      var1.append("Update Values:\n");
      var1.append("   ");
      var1.append(this.entries[1]);
      var1.append("\n");
      var1.append("Update Keys:\n");
      var1.append("   ");
      var1.append(this.entries[2]);
      var1.append("\n");
      return var1.toString();
   }

   public DbFields getCopy() {
      DbFields var1 = new DbFields();

      for (int var2 = 0; var2 < this.entries.length; var2++) {
         var1.entries[var2] = this.entries[var2] == null ? null : this.entries[var2].getCopy();
      }

      return var1;
   }

   private static boolean differs(Object var0, Object var1) {
      return var0 == null && var1 != null || var0 != null && !var0.equals(var1);
   }

   private static void validateType(int var0) {
      if (var0 != 1 && var0 != 2 && var0 != 3) {
         throw new IllegalArgumentException("type must be one of SOURCE_DIFF_KEYS, SOURCE_UPDATE_VALUES, orTARGET_UPDATE_KEYS.");
      }
   }

   public static final class Entry implements Comparable<DbFields.Entry>, Cloneable {
      private static long TEMPORARY_ID_SEED = -System.currentTimeMillis();
      private final long id;
      private final String keyFields;
      private final int type;

      public Entry(String var1, int var2) {
         this(getNextTemporaryId(), var1, var2);
      }

      public Entry(long var1, String var3, int var4) {
         DbFields.validateType(var4);
         this.id = var1;
         this.keyFields = var3;
         this.type = var4;
      }

      public long getId() {
         return this.id;
      }

      public String getKeyFields() {
         return this.keyFields;
      }

      public int getType() {
         return this.type;
      }

      public DbFields.Entry getCopy() {
         try {
            return (DbFields.Entry)this.clone();
         } catch (CloneNotSupportedException var2) {
            throw new RuntimeException(var2);
         }
      }

      public int compareTo(DbFields.Entry var1) {
         return Longs.compare(this.id, var1.id);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (var1 instanceof DbFields.Entry) {
            DbFields.Entry var2 = (DbFields.Entry)var1;
            if (this.id != var2.id) {
               return false;
            } else {
               return this.type != var2.type ? false : !DbFields.differs(this.keyFields, var2.keyFields);
            }
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 37 * var1 + Longs.hashCode(this.id);
         var1 = 37 * var1 + this.type;
         return 37 * var1 + (this.keyFields == null ? 0 : this.keyFields.hashCode());
      }

      public static String[] getColumnNames() {
         return new String[]{"transformation_id", "keyfields", "keyfieldstype"};
      }

      public static DbFields.Entry fromColumnValues(StringKeyValuePairs var0) {
         String var1 = var0.get("keyfields");
         int var2 = var0.getInt("keyfieldstype");
         return new DbFields.Entry(var1, var2);
      }

      public String[] getColumnValues(String var1) {
         return new String[]{var1, this.getKeyFields(), Integer.toString(this.getType())};
      }

      @Override
      public String toString() {
         return "Fields: " + this.keyFields + "   Type: " + this.type;
      }

      private static synchronized long getNextTemporaryId() {
         return --TEMPORARY_ID_SEED;
      }
   }
}
