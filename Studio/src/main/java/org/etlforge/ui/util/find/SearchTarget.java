package org.etlforge.ui.util.find;

public interface SearchTarget {
   SearchTarget.Match nextMatch(TextSearchParameters var1);

   interface Match {
      int compareLocation(SearchTarget.Match var1);

      @Override
      boolean equals(Object var1);
   }
}
