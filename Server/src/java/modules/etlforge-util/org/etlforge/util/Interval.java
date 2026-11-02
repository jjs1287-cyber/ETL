package org.etlforge.util;

import com.google.common.primitives.Ints;
import java.util.Comparator;

public interface Interval {
   Comparator<Interval> START_POINT_ORDER = new Comparator<Interval>() {
      public int compare(Interval var1, Interval var2) {
         return Ints.compare(var1.startsAt(), var2.startsAt());
      }
   };

   int startsAt();

   int length();

   boolean contains(int var1);

   @Override
   boolean equals(Object var1);
}
