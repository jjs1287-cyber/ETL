package com.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.xml.ExpandedName;

public final class SalesforceWsdlInfo implements Iterable<SObject>, Serializable {
   private static final long serialVersionUID = -4325572146554734883L;
   public static final SalesforceWsdlInfo NOT_SALESFORCE = new SalesforceWsdlInfo(false, "", new ObjectList(new ArrayList<>()), new HashSet<>());
   private final boolean salesforce;
   private final String url;
   private final ObjectList objects;
   private final Set<String> namespaceMappings;
   private String wsdlLocator;
   private transient Map<ExpandedName, SObject> nameLookup;
   private final Multimap<String, String> requestHeaders;

   SalesforceWsdlInfo(boolean var1, String var2, ObjectList var3, Set<String> var4) {
      this(var1, var2, var3, var4, emptyMap());
   }

   private static Multimap<String, String> emptyMap() {
      return HashMultimap.create();
   }

   SalesforceWsdlInfo(boolean var1, String var2, ObjectList var3, Set<String> var4, Multimap<String, String> var5) {
      this.salesforce = var1;
      this.url = var2;
      this.objects = var3;
      this.namespaceMappings = Collections.unmodifiableSet(var4);
      this.requestHeaders = HashMultimap.create(var5);
      this.createNameLookup();
   }

   private void createNameLookup() {
      this.nameLookup = Maps.newHashMap();

      for (SObject var2 : this.objects) {
         this.nameLookup.put(var2.getName(), var2);
      }
   }

   public String getWsdlLocator() {
      return this.wsdlLocator;
   }

   public void setWsdlLocator(String var1) {
      this.wsdlLocator = var1;
   }

   public WsdlFile getWsdl() {
      return new WsdlFile(this.wsdlLocator);
   }

   public boolean isSalesforce() {
      return this.salesforce;
   }

   public String getUrl() {
      return this.url;
   }

   @Override
   public Iterator<SObject> iterator() {
      return this.objects.iterator();
   }

   public SObject lookupObject(ExpandedName var1) {
      return this.nameLookup.get(var1);
   }

   public SortedSet<ExpandedName> getObjectNames() {
      TreeSet var1 = Sets.newTreeSet(new Comparator<ExpandedName>() {
         public int compare(ExpandedName var1, ExpandedName var2) {
            return var1.getLocalName().compareToIgnoreCase(var2.getLocalName());
         }
      });

      for (SObject var3 : this.objects) {
         var1.add(var3.getName());
      }

      return var1;
   }

   public ExpandedName findObjectName(String var1) {
      for (SObject var3 : this.objects) {
         if (var3.getLocalName().equalsIgnoreCase(var1)) {
            return var3.getName();
         }
      }

      return null;
   }

   public Set<String> getNamespaceMappings() {
      return this.namespaceMappings;
   }

   public Set<String> getRequestHeaderElements(String var1) {
      return Sets.newHashSet(this.requestHeaders.get(var1));
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.createNameLookup();
   }
}
