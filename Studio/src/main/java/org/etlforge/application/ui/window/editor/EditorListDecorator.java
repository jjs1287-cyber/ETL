package org.etlforge.application.ui.window.editor;

import com.google.common.base.Preconditions;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public final class EditorListDecorator implements ListDecorator {
   private final Editor editor;

   public EditorListDecorator(Editor var1) {
      this.editor = Preconditions.checkNotNull(var1);
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.editor.getTitle());
      var1.setIcon(this.editor.getSmallIcon());
      var1.setToolTip(this.editor.getTitle());
   }

   public Editor getEditor() {
      return this.editor;
   }

   @Override
   public String toString() {
      return this.editor.getTitle();
   }
}
