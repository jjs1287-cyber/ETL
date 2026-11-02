package org.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public final class Picklist implements Iterable<PicklistEntry>, Serializable {
   private static final long serialVersionUID = -6548275987919864636L;
   public static final Picklist EMPTY = new Picklist();
   private final ImmutableList<PicklistEntry> entries;

   private Picklist() {
      this.entries = ImmutableList.of();
   }

   public Picklist(List<PicklistEntry> var1) {
      this.entries = ImmutableList.copyOf(var1);
   }

   @Override
   public Iterator<PicklistEntry> iterator() {
      return this.entries.iterator();
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }
}
