package com.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import org.etlforge.integration.salesforce.SObject;

final class ObjectList implements Iterable<SObject>, Serializable {
   private static final long serialVersionUID = -1400207921702891687L;
   private final SortedSet<SObject> objects = Sets.newTreeSet(new ObjectList.ObjectNameComparator());

   public ObjectList(Collection<SObject> var1) {
      this.objects.addAll(var1);
   }

   @Override
   public Iterator<SObject> iterator() {
      return Collections.unmodifiableSortedSet(this.objects).iterator();
   }

   private static final class ObjectNameComparator implements Comparator<SObject>, Serializable {
      private static final long serialVersionUID = -8778658334837400430L;

      private ObjectNameComparator() {
      }

      public int compare(SObject var1, SObject var2) {
         return var1.getLocalName().compareToIgnoreCase(var2.getLocalName());
      }
   }
}
