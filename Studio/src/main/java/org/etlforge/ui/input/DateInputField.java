package org.etlforge.ui.input;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.input.undo.DateUndoableEdit;
import org.etlforge.ui.widget.text.KongaTextField;

public class DateInputField extends TextBasedInputField {
   private final DateFormat dateFormat;

   public DateInputField(String var1, DateFormat var2, int var3, JLabel var4) {
      this(var1, var2, new KongaTextField(var3), var4);
   }

   public DateInputField(String var1, DateFormat var2, JTextComponent var3, JLabel var4) {
      super(var1, var3, var4);
      Preconditions.checkNotNull(var2, "format");
      this.dateFormat = var2;
   }

   @Override
   public boolean isInputValid(String var1) {
      try {
         this.dateFormat.parse(var1);
         return true;
      } catch (ParseException var3) {
         this.setErrorMessage(this.getInvalidMessage(var1));
         return false;
      }
   }

   public Date getValue() {
      try {
         return this.getDate();
      } catch (ParseException var2) {
         return null;
      }
   }

   public Date getDate() throws ParseException {
      return this.dateFormat.parse(this.getText());
   }

   public void setDate(Date var1) {
      this.setTextUndoableEditsBlocked(true);

      try {
         Date var2 = this.getValue();
         if (var1 == null) {
            this.setText("");
         } else {
            this.setText(this.dateFormat.format(var1));
         }

         if (!Objects.equal(var1, var2)) {
            this.fireUndoableEditHappened(new DateUndoableEdit(this, var2, var1));
         }
      } finally {
         this.setTextUndoableEditsBlocked(false);
      }
   }

   public String getInvalidMessage(String var1) {
      return PackageResources.DateInputField.getInvalidFormat(this.dateFormat);
   }
}
