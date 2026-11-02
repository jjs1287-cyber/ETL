package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.text.Document;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.text.KongaTextField;

public abstract class AbstractStringBasedSearchField implements PropertySearchField {
   private final KongaTextField textField = new KongaTextField(20);
   private DelayedDocumentContentChangeReaction changeHandler;

   public AbstractStringBasedSearchField() {
      this.textField.setSelectAllWhenFocused(true);
   }

   public AbstractStringBasedSearchField(Document var1) {
      this();
      this.textField.setDocument(var1);
   }

   protected final String getSearchString() {
      return this.textField.getText();
   }

   protected final Predicate<String> getPredicate(SearchOptions var1) {
      String var2 = this.getSearchString();
      return var1.getPredicate(var2);
   }

   @Override
   public final boolean isEnabled() {
      return this.textField.isEnabled();
   }

   @Override
   public final void setEnabled(boolean var1) {
      this.textField.setEnabled(var1);
   }

   @Override
   public final void setSearchAction(Action var1) {
      if (this.changeHandler != null) {
         this.changeHandler.uninstall(this.textField);
         this.changeHandler = null;
      }

      if (var1 != null) {
         this.changeHandler = new TextChangeHandler(var1);
         this.changeHandler.install(this.textField);
      }
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(this.textField);
   }

   @Override
   public final void requestFocus() {
      this.textField.requestFocusInWindow();
   }

   public void addActionListener(ActionListener var1) {
      this.textField.addActionListener(var1);
   }
}
