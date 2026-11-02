package org.etlforge.util.persist;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.etlforge.util.KongaSerializationUtils;

public final class BinaryPersistor implements FilePersistor {
   private final BinaryPersistor.Node node;

   private BinaryPersistor(BinaryPersistor.Node var1) {
      this.node = var1;
   }

   public static BinaryPersistor newRoot(String var0) {
      BinaryPersistor.Node var1 = new BinaryPersistor.Node(KeyValidator.checkKey(var0));
      return new BinaryPersistor(var1);
   }

   public static BinaryPersistor load(File var0) throws IOException, ClassNotFoundException {
      BinaryPersistor.Node var1 = KongaSerializationUtils.deserializeFromFile(var0, BinaryPersistor.Node.class);
      return new BinaryPersistor(var1);
   }

   @Override
   public String getName() {
      return this.node.name;
   }

   @Override
   public Persistor createChild(String var1) {
      BinaryPersistor.Node var2 = this.node.createChild(var1);
      return new BinaryPersistor(var2);
   }

   @Override
   public Persistor getFirstChild(String var1) {
      Iterator var2 = this.getChildren(var1).iterator();
      return var2.hasNext() ? (Persistor)var2.next() : null;
   }

   @Override
   public Iterable<Persistor> getAllChildren() {
      ArrayList var1 = new ArrayList();

      for (BinaryPersistor.Node var3 : this.node.children.values()) {
         var1.add(new BinaryPersistor(var3));
      }

      return var1;
   }

   @Override
   public Iterable<Persistor> getChildren(String var1) {
      ArrayList var2 = new ArrayList();

      for (BinaryPersistor.Node var4 : this.node.getChildren(var1)) {
         var2.add(new BinaryPersistor(var4));
      }

      return var2;
   }

   @Override
   public void removeAllChildren() {
      this.node.removeAllChildren();
   }

   @Override
   public void removeChildren(String var1) {
      this.node.removeAllChildren(var1);
   }

   @Override
   public Boolean getBoolean(String var1) {
      String var2 = this.node.getValue(var1);
      return var2 != null ? Boolean.valueOf(var2) : null;
   }

   @Override
   public Double getDouble(String var1) {
      String var2 = this.node.getValue(var1);
      if (var2 != null) {
         try {
            return Double.valueOf(var2);
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   @Override
   public Integer getInteger(String var1) {
      String var2 = this.node.getValue(var1);
      if (var2 != null) {
         try {
            return Integer.valueOf(var2);
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   @Override
   public Long getLong(String var1) {
      String var2 = this.node.getValue(var1);
      if (var2 != null) {
         try {
            return Long.valueOf(var2);
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   @Override
   public String getString(String var1) {
      return this.node.getValue(var1);
   }

   @Override
   public String getText(String var1) {
      return this.getString(var1);
   }

   @Override
   public void putText(String var1, String var2) {
      this.putString(var1, var2);
   }

   @Override
   public void putBoolean(String var1, boolean var2) {
      this.node.values.put(KeyValidator.checkKeyRelaxed(var1), String.valueOf(var2));
   }

   @Override
   public void putDouble(String var1, double var2) {
      this.node.values.put(KeyValidator.checkKeyRelaxed(var1), String.valueOf(var2));
   }

   @Override
   public void putInteger(String var1, int var2) {
      this.node.values.put(KeyValidator.checkKeyRelaxed(var1), String.valueOf(var2));
   }

   @Override
   public void putLong(String var1, long var2) {
      this.node.values.put(KeyValidator.checkKeyRelaxed(var1), String.valueOf(var2));
   }

   @Override
   public void putString(String var1, String var2) {
      this.node.values.put(KeyValidator.checkKeyRelaxed(var1), var2);
   }

   @Override
   public void store(File var1) throws IOException {
      KongaSerializationUtils.serializeToFile(this.node, var1);
   }

   private static final class Node implements Serializable {
      private final String name;
      private final ListMultimap<String, BinaryPersistor.Node> children;
      private final HashMap<String, String> values;

      public Node(String var1) {
         this.name = var1;
         this.children = ArrayListMultimap.create();
         this.values = Maps.newHashMap();
      }

      public BinaryPersistor.Node createChild(String var1) {
         String var2 = var1.trim();
         Preconditions.checkArgument(!var2.isEmpty(), "name cannot be blank");
         Preconditions.checkArgument(!var2.startsWith("konga."), "name cannot start with konga.");
         BinaryPersistor.Node var3 = new BinaryPersistor.Node(var1);
         this.children.put(var1, var3);
         return var3;
      }

      public Iterable<BinaryPersistor.Node> getChildren(String var1) {
         return this.children.get(var1);
      }

      public String getValue(String var1) {
         return this.values.get(KeyValidator.checkKeyRelaxed(var1));
      }

      public void removeAllChildren() {
         this.children.clear();
      }

      public void removeAllChildren(String var1) {
         this.children.removeAll(var1);
      }
   }
}
