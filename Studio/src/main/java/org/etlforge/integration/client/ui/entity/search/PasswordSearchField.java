package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Predicate;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.password.PasswordField;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class PasswordSearchField implements PropertySearchField, TwoRowSearchField {
   private final SearchableProperty<Password> property;
   private final PasswordField field = new PasswordField();
   private final KongaCheckBox blankedOut = new KongaCheckBox("Blanked out");
   private TextChangeHandler changeHandler;
   private Action searchAction;

   public PasswordSearchField(SearchableProperty<Password> var1) {
      this.property = var1;
      this.field.setValidationEnabled(false);
      this.blankedOut.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            PasswordSearchField.this.field.setEnabled(!var1);
            if (PasswordSearchField.this.searchAction != null) {
               ActionUtils.run(PasswordSearchField.this.searchAction, PasswordSearchField.this, "search");
            }
         }
      });
   }

   public PasswordSearchField forReplace() {
      this.blankedOut.setText("Blank out");
      return this;
   }

   @Override
   public void decorate(UiCanvas var1) {
      new OneColumnPanel().stretchHorizontally().add(this.field.layoutWithoutLabel()).add(this.blankedOut).decorate(var1);
   }

   @Override
   public JComponent getTopRow() {
      return this.field.layoutWithoutLabel();
   }

   @Override
   public JComponent getBottomRow() {
      return this.blankedOut;
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   @Override
   public SearchableProperty getProperty() {
      return this.property;
   }

   public Password getValue() {
      if (this.blankedOut.isSelected()) {
         return null;
      }

      String var1 = this.field.getPassword();
      return Password.encrypt(var1);
   }

   @Override
   public Predicate<IntegrationEntity> getSearcher(SearchOptions var1) {
      if (!this.isEnabled()) {
         return SearchFieldUtils.ALL;
      } else {
         return this.blankedOut.isSelected() ? new PasswordSearchField.BlankedOutSearcher() : new PasswordSearchField.ValueSearcher(var1);
      }
   }

   @Override
   public void setEnabled(boolean var1) {
      this.field.setEnabled(var1 && !this.blankedOut.isSelected());
      this.blankedOut.setEnabled(var1);
   }

   @Override
   public boolean isEnabled() {
      return this.blankedOut.isEnabled();
   }

   @Override
   public void setSearchAction(Action var1) {
      this.searchAction = var1;
      if (this.changeHandler != null) {
         this.field.removeDocumentListener(this.changeHandler.asDocumentListener());
      }

      if (var1 != null) {
         this.changeHandler = new TextChangeHandler(var1);
         this.field.addDocumentListener(this.changeHandler.asDocumentListener());
      } else {
         this.changeHandler = null;
      }
   }

   private final class BlankedOutSearcher implements Predicate<IntegrationEntity> {
      private BlankedOutSearcher() {
      }

      public boolean apply(IntegrationEntity var1) {
         return PasswordSearchField.this.property.getValue(var1) == null;
      }
   }

   private final class ValueSearcher implements Predicate<IntegrationEntity> {
      private final Predicate<String> clearTextPredicate;

      public ValueSearcher(SearchOptions var2) {
         String var3 = PasswordSearchField.this.field.getPassword();
         this.clearTextPredicate = var2.getPredicate(var3);
      }

      public boolean apply(IntegrationEntity var1) {
         Password var2 = PasswordSearchField.this.property.getValue(var1);
         return var2 != null && this.clearTextPredicate.apply(var2.clearText());
      }
   }
}
