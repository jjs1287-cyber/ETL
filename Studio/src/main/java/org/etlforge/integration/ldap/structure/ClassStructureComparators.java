package org.etlforge.integration.ldap.structure;

import java.util.Comparator;
import org.etlforge.util.name.NameSorter;

public final class ClassStructureComparators {
   public static final Comparator<ObjectClassStructure> BY_NAME = new NameSorter<>(false);
   public static final Comparator<ObjectClassStructure> BY_NAME_IGNORE_CASE = NameSorter.ignoreCase();

   private ClassStructureComparators() {
   }
}
