package com.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.etlforge.integration.salesforce.EncryptedCredentials;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import org.etlforge.integration.salesforce.LoginCredentials;

public final class KnownWsdlsContentsList implements Serializable {
   private static final long serialVersionUID = -5861192802961706147L;
   private final LinkedHashMap<String, LinkedList<EncryptedCredentials>> contents = new LinkedHashMap<>(10, 0.75F, true);

   public boolean contains(String var1) {
      return this.contents.containsKey(var1);
   }

   public boolean remove(String var1) {
      return this.contents.remove(var1) != null;
   }

   public boolean addWsdl(String var1, LoginCredentials var2) {
      EncryptedCredentials var3 = new EncryptedCredentials(var2);
      LinkedList var4 = this.contents.get(var1);
      if (var4 == null) {
         var4 = Lists.newLinkedList();
         var4.add(var3);
         this.contents.put(var1, var4);
         return true;
      } else {
         return this.insertCredentials(var4, var3);
      }
   }

   private boolean insertCredentials(LinkedList<EncryptedCredentials> var1, EncryptedCredentials var2) {
      boolean var3 = true;
      boolean var4 = true;
      ListIterator var5 = var1.listIterator();

      while (var5.hasNext()) {
         EncryptedCredentials var6 = (EncryptedCredentials)var5.next();
         if (var6.username.equals(var2.username)) {
            if (var4 && var6.equals(var2)) {
               var3 = false;
            }

            var5.remove();
            break;
         }
      }

      var1.addFirst(var2);
      return var3;
   }

   public LinkedHashSet<String> getWsdls() {
      ArrayList var1 = Lists.newArrayList(this.contents.keySet());
      Collections.reverse(var1);
      return Sets.newLinkedHashSet(var1);
   }

   public LinkedHashSet<WsdlWithCredentials> getWsdlsWithCredentials() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.contents.entrySet()) {
         ArrayList var4 = Lists.newArrayList();

         for (Object __etlItem75 : (LinkedList)var3.getValue()) {
            EncryptedCredentials var6 = (EncryptedCredentials) __etlItem75;
            var4.add(var6.decrypt());
         }

         var1.add(new WsdlWithCredentials((String)var3.getKey(), var4));
      }

      Collections.reverse(var1);
      return Sets.newLinkedHashSet(var1);
   }

   public List<LoginCredentials> getCredentials(String var1) {
      ArrayList var2 = Lists.newArrayList();
      if (this.contents.containsKey(var1)) {
         for (EncryptedCredentials var4 : this.contents.get(var1)) {
            var2.add(var4.decrypt());
         }
      }

      return var2;
   }

   @Override
   public String toString() {
      return this.contents.keySet().toString();
   }
}
