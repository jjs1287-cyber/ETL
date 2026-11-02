package org.etlforge.integration.client.ui.admin.core.console;

import javax.swing.Icon;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.application.ui.window.editor.EditorService;

public abstract class ConsolePageHistoryPoint implements EditorHistoryPoint {
   private boolean available = true;
   private final String id;
   private final String title;
   private final Icon icon;

   protected ConsolePageHistoryPoint(Editor var1) {
      this.id = var1.getId();
      this.title = var1.getTitle();
      this.icon = var1.getSmallIcon();
   }

   @Override
   public void dispose() {
      this.available = false;
   }

   @Override
   public String getDisplayString() {
      return this.title;
   }

   @Override
   public Icon getIcon() {
      return this.icon;
   }

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public boolean isAvailable() {
      return this.available;
   }

   @Override
   public boolean isDisposed() {
      return !this.available;
   }

   @Override
   public void restore(EditorService var1) {
      if (this.isDisposed()) {
         throw new IllegalStateException("Cannot restore a disposed EditorHistoryPoint");
      }

      Editor var2 = this.recreateEditor();
      if (var2 != null) {
         var1.openEditor(var2);
      }
   }

   protected abstract Editor recreateEditor();
}
