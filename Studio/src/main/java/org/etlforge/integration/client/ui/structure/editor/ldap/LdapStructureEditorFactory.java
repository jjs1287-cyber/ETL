package org.etlforge.integration.client.ui.structure.editor.ldap;

import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.LdapStructure;

public final class LdapStructureEditorFactory implements StructureEditorFactory {
   @Override
   public StructureEditor getEditor(Transformation var1, DataStructure var2, SourceTarget var3, EntityExplorerSupport var4) {
      return new LdapStructureEditor((LdapStructure)var2, var3, var4);
   }
}
