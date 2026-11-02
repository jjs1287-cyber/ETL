package org.etlforge.integration.client.ui.structure.editor.text;

import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DataStructure;

public final class TextStructureEditorFactory implements StructureEditorFactory {
   @Override
   public StructureEditor getEditor(Transformation var1, DataStructure var2, SourceTarget var3, EntityExplorerSupport var4) {
      TextDocumentId var5 = var3 == SourceTarget.Source ? var1.getSourceTextDocumentId() : var1.getTargetTextDocumentId();
      IntegrationProject var6 = ProjectUtils.getProject(var1);
      TextDocument var7 = var6.getItemLookup().getEntity(var5, TextDocument.class);
      return new TextStructureEditor(var7, var3, var4);
   }
}
