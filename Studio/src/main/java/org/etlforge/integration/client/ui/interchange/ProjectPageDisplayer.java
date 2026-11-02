package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.entity.ProjectPageGroup;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ProjectPageDisplayer implements EditorDisplayer {
   private final CurrentProjectService currentProjectService;
   private final EditorService editorService;

   public ProjectPageDisplayer(CurrentProjectService var1, EditorService var2) {
      this.currentProjectService = Preconditions.checkNotNull(var1, "currentProjectService");
      this.editorService = Preconditions.checkNotNull(var2, "editorService");
   }

   @Override
   public void openEditor(Editor var1) {
      IntegrationProject var2 = this.currentProjectService.getProject();
      this.editorService.openEditor(var1, ProjectPageGroup.get(var2));
   }

   @Override
   public Editor[] getOpenEditors() {
      return this.editorService.getOpenEditors();
   }
}
