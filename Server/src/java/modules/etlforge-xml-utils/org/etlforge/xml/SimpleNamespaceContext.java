package org.etlforge.xml;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Pair;

public final class SimpleNamespaceContext implements NamespaceContext {
   private final Map<String, String> prefixToUri = Maps.newHashMap();
   private final Multimap<String, String> uriToPrefixes = ArrayListMultimap.create();

   public SimpleNamespaceContext() {
      this.addUnprotected("xml", "http://www.w3.org/XML/1998/namespace");
      this.addUnprotected("xmlns", "http://www.w3.org/2000/xmlns/");
      this.addUnprotected("", "");
   }

   public SimpleNamespaceContext(String... var1) {
      this(Sets.newHashSet(var1));
   }

   public SimpleNamespaceContext(Iterable<String> var1) {
      this();

      for (String var3 : var1) {
         this.add(var3);
      }
   }

   public SimpleNamespaceContext(Pair<String, String>... var1) {
      this();

      for (Pair var5 : var1) {
         this.add(var5);
      }
   }

   public void add(Pair<String, String> var1) {
      this.add((String)var1.first, (String)var1.second);
   }

   public void add(String var1) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "Namespace mapping may not be blank");
      String[] var2 = var1.split(":", 2);
      Preconditions.checkArgument(var2.length == 2, "Mapping must be on the form <prefix>:<uri> (was " + var1 + ")");
      this.add(var2[0], var2[1]);
   }

   public void add(String var1, String var2) {
      Preconditions.checkNotNull(var1, "prefix");
      checkReservedPrefixes(var1, var2);
      this.addUnprotected(var1, var2);
   }

   private void addUnprotected(String var1, String var2) {
      this.prefixToUri.put(var1, var2);
      this.uriToPrefixes.put(var2, var1);
   }

   private static void checkReservedPrefixes(String var0, String var1) {
      checkReservedPrefix("xml", "http://www.w3.org/XML/1998/namespace", var0, var1);
      checkReservedPrefix("xmlns", "http://www.w3.org/2000/xmlns/", var0, var1);
   }

   private static void checkReservedPrefix(String var0, String var1, String var2, String var3) {
      if (var0.equals(var2)) {
         Preconditions.checkArgument(var1.equals(var3), var0 + " is a reserved prefix, it cannot be bound to any other namespace than " + var1);
      }
   }

   @Override
   public String getNamespaceURI(String var1) {
      Preconditions.checkArgument(var1 != null, "prefix cannot be null");
      String var2 = this.prefixToUri.get(var1);
      return var2 != null ? var2 : "";
   }

   @Override
   public String getPrefix(String var1) {
      Preconditions.checkArgument(var1 != null, "namespaceURI cannot be null");
      if (Objects.equal(var1, this.prefixToUri.get(""))) {
         return "";
      } else {
         return this.uriToPrefixes.containsKey(var1) ? this.uriToPrefixes.get(var1).iterator().next() : null;
      }
   }

   @Override
   public Iterator getPrefixes(String var1) {
      Preconditions.checkArgument(var1 != null, "namespaceURI cannot be null");
      return this.uriToPrefixes.containsKey(var1)
         ? Collections.unmodifiableCollection(this.uriToPrefixes.get(var1)).iterator()
         : Collections.emptyList().iterator();
   }
}
