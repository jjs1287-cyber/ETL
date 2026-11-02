package org.etlforge.ui.util.find;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Action;
import org.etlforge.system.Disposable;

public final class TextSearchController implements Disposable {
   private final TextSearchModel model;
   private SearchTarget target;
   private final TextSearchController.Actions actions;
   private final List<TextSearchObserver> observers;
   private final TextSearchController.ModelListener modelListener;
   private SearchTarget.Match currentMatch;

   public TextSearchController(TextSearchModel var1) {
      this.model = var1;
      this.modelListener = new TextSearchController.ModelListener();
      var1.addPropertyChangeListener("searchString", this.modelListener);
      this.actions = new TextSearchController.Actions();
      this.observers = new CopyOnWriteArrayList<>();
   }

   public TextSearchModel getModel() {
      return this.model;
   }

   @Override
   public void dispose() {
      this.actions.dispose();
   }

   public void setTarget(SearchTarget var1) {
      if (var1 != this.target) {
         this.target = var1;
         this.currentMatch = null;
         this.actions.setTarget(var1);
      }
   }

   boolean hasTarget() {
      return this.target != null;
   }

   public void findNext() {
      TextSearchParameters var1 = this.model.getSearchParameters();
      SearchTarget.Match var2 = this.target.nextMatch(var1);
      this.handleNewMatch(var2, var1);
   }

   private void handleNewMatch(SearchTarget.Match var1, TextSearchParameters var2) {
      if (var1 == null) {
         this.nothingMatched();
      } else if (var1.equals(this.currentMatch)) {
         this.newMatchIsEqualToPreviousMatch(var1, var2);
      } else {
         this.newMatchFound(var1, var2);
      }
   }

   private void nothingMatched() {
      this.notifyObserversOnNoMatch();
      this.setCurrentMatch(null);
   }

   private void notifyObserversOnNoMatch() {
      for (TextSearchObserver var2 : this.observers) {
         var2.noMatch();
      }
   }

   private void newMatchIsEqualToPreviousMatch(SearchTarget.Match var1, TextSearchParameters var2) {
      if (var2.isWrap()) {
         this.notifyObserversOnWrap(var1);
      } else {
         this.notifyObserversOnNoMatch();
      }
   }

   private void notifyObserversOnWrap(SearchTarget.Match var1) {
      for (TextSearchObserver var3 : this.observers) {
         var3.searchWrappedAround(var1);
      }
   }

   private void newMatchFound(SearchTarget.Match var1, TextSearchParameters var2) {
      if (this.isNewMatchWrapped(var1, var2)) {
         this.notifyObserversOnWrap(var1);
      } else {
         this.notifyObserversAfterFindNext(var1);
      }

      this.setCurrentMatch(var1);
   }

   private boolean isNewMatchWrapped(SearchTarget.Match var1, TextSearchParameters var2) {
      if (this.currentMatch == null) {
         return false;
      } else {
         return var2.getDirection() == SearchDirection.DOWN ? var1.compareLocation(this.currentMatch) < 0 : var1.compareLocation(this.currentMatch) > 0;
      }
   }

   private void notifyObserversAfterFindNext(SearchTarget.Match var1) {
      assert var1 != null;

      for (TextSearchObserver var3 : this.observers) {
         var3.newMatch(var1);
      }
   }

   private void setCurrentMatch(SearchTarget.Match var1) {
      if (var1 != this.currentMatch) {
         this.currentMatch = var1;
         this.actions.updateEnabledStates();
      }
   }

   public void replace() {
      Preconditions.checkState(this.target instanceof SearchAndReplaceTarget, "replace is not supported by the current target");
      Preconditions.checkState(this.hasCurrentMatch(), "No current match to replace");
      String var1 = this.model.getReplaceString();
      ((SearchAndReplaceTarget)this.target).replace(this.currentMatch, var1);
      this.notifyObserversAfterReplace(this.currentMatch, var1);
      this.setCurrentMatch(null);
   }

   private void notifyObserversAfterReplace(SearchTarget.Match var1, String var2) {
      for (TextSearchObserver var4 : this.observers) {
         var4.matchReplaced(var1, var2);
      }
   }

   public void replaceAndFind() {
      this.replace();
      this.findNext();
   }

   public void replaceAll() {
      Preconditions.checkState(this.target instanceof SearchAndReplaceTarget, "replace is not supported by the current target");
      TextSearchParameters var1 = this.model.getSearchParameters();
      List var2 = ((SearchAndReplaceTarget)this.target).replaceAll(var1);
      this.notifyObserversAfterReplaceAll(var2, var1.getReplaceString());
      this.setCurrentMatch(null);
   }

   private void notifyObserversAfterReplaceAll(List<? extends SearchTarget.Match> var1, String var2) {
      if (var1.isEmpty()) {
         for (TextSearchObserver var4 : this.observers) {
            var4.noMatch();
         }
      } else {
         for (TextSearchObserver var6 : this.observers) {
            var6.matchesReplaced(var1, var2);
         }
      }
   }

   public void addObserver(TextSearchObserver var1) {
      this.observers.add(var1);
   }

   public void removeObserver(TextSearchObserver var1) {
      this.observers.remove(var1);
   }

   public Action getFindAction() {
      return this.actions.getFind();
   }

   public Action getReplaceAction() {
      return this.actions.getReplace();
   }

   public Action getReplaceAndFindAction() {
      return this.actions.getReplaceAndFind();
   }

   public Action getReplaceAllAction() {
      return this.actions.getReplaceAll();
   }

   boolean canReplace() {
      return this.hasCurrentMatch() && this.target instanceof SearchAndReplaceTarget;
   }

   boolean hasCurrentMatch() {
      return this.currentMatch != null;
   }

   private class Actions {
      private AbstractTextSearchAction find;
      private AbstractTextSearchAction replace;
      private AbstractTextSearchAction replaceAndFind;
      private AbstractTextSearchAction replaceAll;

      private Actions() {
      }

      public void dispose() {
         this.dispose(this.find);
         this.dispose(this.replace);
         this.dispose(this.replaceAndFind);
         this.dispose(this.replaceAll);
      }

      private void dispose(AbstractTextSearchAction var1) {
         if (var1 != null) {
            var1.dispose();
         }
      }

      public void updateEnabledStates() {
         this.updateEnabledState(this.find);
         this.updateEnabledState(this.replace);
         this.updateEnabledState(this.replaceAndFind);
         this.updateEnabledState(this.replaceAll);
      }

      private void updateEnabledState(AbstractTextSearchAction var1) {
         if (var1 != null) {
            var1.updateEnabledState();
         }
      }

      public Action getFind() {
         if (this.find == null) {
            this.find = new FindNextAction(TextSearchController.this.model, TextSearchController.this);
            this.find.setTarget(TextSearchController.this.target);
         }

         return this.find;
      }

      public Action getReplace() {
         if (this.replace == null) {
            this.replace = new ReplaceAction(TextSearchController.this.model, TextSearchController.this);
            this.replace.setTarget(TextSearchController.this.target);
         }

         return this.replace;
      }

      public Action getReplaceAndFind() {
         if (this.replaceAndFind == null) {
            this.replaceAndFind = new ReplaceAndFindAction(TextSearchController.this.model, TextSearchController.this);
            this.replaceAndFind.setTarget(TextSearchController.this.target);
         }

         return this.replaceAndFind;
      }

      public Action getReplaceAll() {
         if (this.replaceAll == null) {
            this.replaceAll = new ReplaceAllAction(TextSearchController.this.model, TextSearchController.this);
            this.replaceAll.setTarget(TextSearchController.this.target);
         }

         return this.replaceAll;
      }

      public void setTarget(SearchTarget var1) {
         this.setTarget(var1, this.find);
         this.setTarget(var1, this.replace);
         this.setTarget(var1, this.replaceAndFind);
         this.setTarget(var1, this.replaceAll);
      }

      private void setTarget(SearchTarget var1, AbstractTextSearchAction var2) {
         if (var2 != null) {
            var2.setTarget(var1);
         }
      }
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "searchString".equals(var1.getPropertyName());
         TextSearchController.this.setCurrentMatch(null);
      }
   }
}
