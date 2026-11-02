package org.etlforge.application.ui.search.string;

public interface StringSearchModel {
   boolean test(String var1);

   String getSearchString();

   StringSearchRule getSearchRule();

   void addModelListener(StringSearchModelListener var1);

   void removeModelListener(StringSearchModelListener var1);
}
