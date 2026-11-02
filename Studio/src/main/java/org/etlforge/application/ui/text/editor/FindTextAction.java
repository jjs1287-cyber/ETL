package org.etlforge.application.ui.text.editor;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.ui.util.find.AbstractSearchDialog;
import org.etlforge.ui.util.find.SearchAndReplaceTarget;
import org.etlforge.ui.util.find.SearchAndReplaceTextComponentTarget;
import org.etlforge.ui.util.find.SearchAndReplaceTextDialog;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.util.find.SearchTextDialog;

public final class FindTextAction extends AbstractAction {
   private final JTextComponent text;
   private final String title;
   private final boolean withReplace;

   public FindTextAction(JTextComponent var1) {
      this(var1, null);
   }

   public FindTextAction(JTextComponent var1, String var2) {
      this(var1, var2, false);
   }

   public FindTextAction(JTextComponent var1, String var2, boolean var3) {
      Preconditions.checkNotNull(var1, "text");
      ActionDecorators.FIND.decorate(this);
      this.text = var1;
      this.title = var2;
      this.withReplace = var3;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.text.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            FindTextAction.this.openDialog();
         }
      });
   }

   private void openDialog() {
      SearchAndReplaceTextComponentTarget var1 = new SearchAndReplaceTextComponentTarget(this.text);
      if (this.withReplace) {
         this.searchAndReplace(var1, false);
      } else {
         this.searchOnly(var1);
      }
   }

   private void searchAndReplace(SearchAndReplaceTarget var1, boolean var2) {
      SearchAndReplaceTextDialog var3 = SearchAndReplaceTextDialog.sharedDialog(var1);
      this.configureDialog(var3);
      if (var2) {
         var3.showReplaceUi();
      }

      var3.show(null);
   }

   private void configureDialog(AbstractSearchDialog var1) {
      if (this.title != null) {
         var1.setTitle(this.title);
      }

      String var2 = this.text.getSelectedText();
      if (StringUtils.isNotEmpty(var2)) {
         var1.setSearchString(var2);
      }
   }

   private void searchOnly(SearchTarget var1) {
      SearchTextDialog var2 = SearchTextDialog.sharedDialog(var1);
      this.configureDialog(var2);
      var2.show(null);
   }

   public void openReplaceDialog() {
      SearchAndReplaceTextComponentTarget var1 = new SearchAndReplaceTextComponentTarget(this.text);
      this.searchAndReplace(var1, true);
   }
}
