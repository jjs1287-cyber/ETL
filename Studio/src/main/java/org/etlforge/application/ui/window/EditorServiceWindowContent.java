package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public class EditorServiceWindowContent extends AbstractWindowElement implements WindowSectionContent {
   private static final String CONTENT_ID = "editor_service_content";
   private final WindowSection winSection;
   private final EditorService editors;

   public EditorServiceWindowContent(WindowSection var1, EditorService var2) {
      super("editor_service_content");
      Preconditions.checkNotNull(var1, "winSec");
      Preconditions.checkNotNull(var2, "editors");
      this.winSection = var1;
      this.editors = var2;
   }

   @Override
   public WindowSection getWindowSection() {
      return this.winSection;
   }

   @Override
   public String getTitle() {
      return "Editors";
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).center(this.editors.getUi());
   }

   @Override
   public boolean isDetachable() {
      return false;
   }

   @Override
   protected void activated() {
      this.editors.activate();
   }

   @Override
   protected void deactivated() {
      this.editors.deactivate();
   }
}
