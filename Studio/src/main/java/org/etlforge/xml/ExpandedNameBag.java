package org.etlforge.xml;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Set;

public final class ExpandedNameBag {
   private final Multimap<String, ExpandedName> uriToExpandedName = HashMultimap.create();
   private final Multimap<String, ExpandedName> localNameToExpandedName = HashMultimap.create();

   public void add(ExpandedName var1) {
      this.uriToExpandedName.put(var1.getNamespace(), var1);
      this.localNameToExpandedName.put(var1.getLocalName(), var1);
   }

   public void addAll(ExpandedName[] var1) {
      for (ExpandedName var5 : var1) {
         this.add(var5);
      }
   }

   public void add(String var1, XmlNameFactory var2) {
      ExpandedName var3 = var2.newExpandedName(var1);
      this.add(var3);
   }

   public void addAll(Iterable<String> var1, XmlNameFactory var2) {
      for (String var4 : var1) {
         this.add(var4, var2);
      }
   }

   public String[] getNamespaces() {
      Set var1 = this.uriToExpandedName.keySet();
      return (String[]) var1.toArray(new String[var1.size()]);
   }

   public int getNumberOfNamespaces() {
      return this.uriToExpandedName.keySet().size();
   }

   public int getNumberOfNames() {
      return this.uriToExpandedName.size();
   }

   public ExpandedName[] getNamesFromNamespace(String var1) {
      Collection var2 = this.uriToExpandedName.get(var1);
      return (ExpandedName[]) var2.toArray(new ExpandedName[var2.size()]);
   }

   public boolean contains(ExpandedName var1) {
      String var2 = var1.getNamespace();
      return this.uriToExpandedName.containsEntry(var2, var1);
   }

   public ExpandedName[] getNamesFromLocalName(String var1) {
      Collection var2 = this.localNameToExpandedName.get(var1);
      return (ExpandedName[]) var2.toArray(new ExpandedName[var2.size()]);
   }

   public boolean containsLocalName(String var1) {
      return !this.localNameToExpandedName.get(var1).isEmpty();
   }
}
