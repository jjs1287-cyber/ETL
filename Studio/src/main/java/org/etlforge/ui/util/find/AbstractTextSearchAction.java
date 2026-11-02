package org.etlforge.ui.util.find;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.etlforge.system.Disposable;

public abstract class AbstractTextSearchAction extends AbstractAction implements Disposable {
   private final TextSearchModel model;
   protected final TextSearchController controller;
   protected SearchTarget target;
   private final boolean isReplace;
   private final PropertyChangeListener modelListener;

   protected AbstractTextSearchAction(TextSearchModel var1, TextSearchController var2, boolean var3) {
      this.model = var1;
      this.controller = var2;
      this.isReplace = var3;
      this.modelListener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            assert "searchString".equals(var1.getPropertyName());
            AbstractTextSearchAction.this.updateEnabledState();
         }
      };
      var1.addPropertyChangeListener("searchString", this.modelListener);
      this.updateEnabledState();
   }

   public final void updateEnabledState() {
      boolean var1 = this.isActionSupported();
      this.setEnabled(var1);
   }

   protected boolean isActionSupported() {
      if (this.target == null) {
         return false;
      } else if (!this.hasSearchString()) {
         return false;
      } else {
         return this.isReplace ? this.controller.canReplace() : true;
      }
   }

   protected final boolean hasSearchString() {
      String var1 = this.model.getSearchString();
      return !var1.isEmpty();
   }

   public final void setTarget(SearchTarget var1) {
      this.target = var1;
      this.updateEnabledState();
   }

   @Override
   public void dispose() {
      this.model.removePropertyChangeListener("searchString", this.modelListener);
   }
}
