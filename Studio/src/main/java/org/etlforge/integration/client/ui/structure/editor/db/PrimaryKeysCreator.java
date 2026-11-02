package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.client.database.ui.primarykeys.PrimaryKeys;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.lang.KongaStringUtils;

public final class PrimaryKeysCreator {
   private final Map<String, PrimaryKeysCreator.DbObjectMap> objects;
   private final PrimaryKeysCreator.KeyMap keyMap;

   public PrimaryKeysCreator(TransformationDatabaseInfo var1) {
      DatabaseObject[] var2 = var1.getDatabaseObjects();
      this.objects = this.createObjectMap(var2);
      this.keyMap = new PrimaryKeysCreator.KeyMap(var2);
      BeginEndQuote var3 = this.getBeginEndQuote(var1);

      for (String var7 : this.getKeysAsStrings(var1)) {
         DatabaseColumn var8 = this.getColumn(var7, var3);
         this.keyMap.addKey(var8);
      }
   }

   public PrimaryKeys[] getPrimaryKeys() {
      return this.keyMap.getKeys();
   }

   private Map<String, PrimaryKeysCreator.DbObjectMap> createObjectMap(DatabaseObject[] var1) {
      HashMap var2 = Maps.newHashMap();

      for (DatabaseObject var6 : var1) {
         var2.put(var6.getName(), new PrimaryKeysCreator.DbObjectMap(var6));
      }

      return var2;
   }

   private BeginEndQuote getBeginEndQuote(TransformationDatabaseInfo var1) {
      DatabaseLocation var2 = var1.getStructure().getDbLocation();
      return var2.getBeginEndQuote();
   }

   private String[] getKeysAsStrings(TransformationDatabaseInfo var1) {
      return this.getKeysAsStrings(var1.getStructure().getDbFields());
   }

   private String[] getKeysAsStrings(DbFields var1) {
      DbFields.Entry var2 = var1.getEntry(3);
      if (var2 == null) {
         return new String[0];
      }

      String var3 = var2.getKeyFields();
      return var3.split(",");
   }

   private DatabaseColumn getColumn(String var1, BeginEndQuote var2) {
      String var3 = this.unescape(var1, var2);
      String[] var4 = var3.split("\\.");
      String var5;
      if (var4.length == 2) {
         var5 = var4[0];
      } else {
         var5 = var4[0] + "." + var4[1];
      }

      PrimaryKeysCreator.DbObjectMap var6 = this.objects.get(var5);
      return var6 != null ? var6.getColumn(var4[var4.length - 1]) : null;
   }

   private String unescape(String var1, BeginEndQuote var2) {
      return var2.isEnclosingName(var1) ? KongaStringUtils.removeStartAndEnd(var1, var2.begin, var2.end) : var1;
   }

   private static final class DbObjectMap {
      private final Map<String, DatabaseColumn> columns = Maps.newHashMap();

      public DbObjectMap(DatabaseObject var1) {
         for (DatabaseColumn var3 : var1.getAllColumns()) {
            this.columns.put(var3.getName(), var3);
         }
      }

      public DatabaseColumn getColumn(String var1) {
         return this.columns.get(var1);
      }
   }

   private static final class Key implements PrimaryKeys {
      private final DatabaseObject object;
      private final Set<DatabaseColumn> columns;

      public Key(DatabaseObject var1, Collection<DatabaseColumn> var2) {
         this.object = var1;
         this.columns = new LinkedHashSet<>(var2);
      }

      @Override
      public DatabaseObject getDatabaseObject() {
         return this.object;
      }

      @Override
      public DatabaseColumn[] getPrimaryKeys() {
         return this.columns.toArray(new DatabaseColumn[this.columns.size()]);
      }
   }

   private static final class KeyMap {
      private final Multimap<DatabaseObject, DatabaseColumn> keys = ArrayListMultimap.create();

      public KeyMap(DatabaseObject[] var1) {
      }

      public void addKey(DatabaseColumn var1) {
         if (var1 != null) {
            this.keys.put(var1.getDatabaseObject(), var1);
         }
      }

      public PrimaryKeys[] getKeys() {
         ArrayList var1 = Lists.newArrayList();

         for (DatabaseObject var3 : this.keys.keySet()) {
            var1.add(new PrimaryKeysCreator.Key(var3, this.keys.get(var3)));
         }

         Collections.sort(var1, new Comparator<PrimaryKeys>() {
            public int compare(PrimaryKeys var1, PrimaryKeys var2) {
               return var1.getDatabaseObject().getName().compareTo(var2.getDatabaseObject().getName());
            }
         });
         return (PrimaryKeys[]) var1.toArray(new PrimaryKeys[var1.size()]);
      }
   }
}
