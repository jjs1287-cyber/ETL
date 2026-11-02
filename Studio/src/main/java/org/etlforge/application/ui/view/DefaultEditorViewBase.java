package org.etlforge.application.ui.view;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorService;

public abstract class DefaultEditorViewBase implements View {
   private final ApplicationWindow appWin;
   private boolean active;

   protected DefaultEditorViewBase(ApplicationWindow var1) {
      this.appWin = var1;
   }

   @Override
   public final ApplicationWindow getWindow() {
      return this.appWin;
   }

   public final EditorService getEditorService() {
      return this.appWin.getEditorService();
   }

   @Override
   public void setActive(boolean var1) {
      this.active = var1;
   }

   public final boolean isActive() {
      return this.active;
   }
}
