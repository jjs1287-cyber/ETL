package org.etlforge.ui.util.find;

import java.util.List;

public interface SearchAndReplaceTarget extends SearchTarget {
   void replace(SearchTarget.Match var1, String var2);

   List<? extends SearchTarget.Match> replaceAll(TextSearchParameters var1);
}
