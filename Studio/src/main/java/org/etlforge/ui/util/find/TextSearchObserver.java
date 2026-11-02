package org.etlforge.ui.util.find;

import java.util.List;

public interface TextSearchObserver {
   void newMatch(SearchTarget.Match var1);

   void noMatch();

   void searchWrappedAround(SearchTarget.Match var1);

   void matchReplaced(SearchTarget.Match var1, String var2);

   void matchesReplaced(List<? extends SearchTarget.Match> var1, String var2);

   abstract class Adapter implements TextSearchObserver {
      @Override
      public void newMatch(SearchTarget.Match var1) {
      }

      @Override
      public void noMatch() {
      }

      @Override
      public void searchWrappedAround(SearchTarget.Match var1) {
      }

      @Override
      public void matchReplaced(SearchTarget.Match var1, String var2) {
      }

      @Override
      public void matchesReplaced(List<? extends SearchTarget.Match> var1, String var2) {
      }
   }
}
