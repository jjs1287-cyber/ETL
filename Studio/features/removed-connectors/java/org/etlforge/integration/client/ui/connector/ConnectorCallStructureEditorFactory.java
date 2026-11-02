package org.etlforge.integration.client.ui.connector;

import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class ConnectorCallStructureEditorFactory implements StructureEditorFactory {
   @Override
   public StructureEditor getEditor(Transformation var1, DataStructure var2, SourceTarget var3, EntityExplorerSupport var4) {
      XsdBasedStructure var5 = (XsdBasedStructure)var2;
      IntegrationEntityId var6 = var5.getFunctionCallId();
      EntityType var7 = var6.getEntityType();
      Class var8 = getConnectorClass(var6);
      if (var8 == null) {
         throw new NotImplementedYetException("We do not have a structure editor for connectors of type " + var7);
      } else {
         return ConnectorCallStructureEditor.create(var8, var5, var3, var4);
      }
   }

   private static Class<? extends AbstractConnectorCall> getConnectorClass(IntegrationEntityId var0) {
      EntityType var1 = var0.getEntityType();
      if (var1.canBeInstantiated()) {
         String var2 = var1.getImplementingClassName().orNull();
         if (var2 != null) {
            try {
               Class var3 = Class.forName(var2);
               if (AbstractConnectorCall.class.isAssignableFrom(var3)) {
                  return var3.asSubclass(AbstractConnectorCall.class);
               }
            } catch (ClassNotFoundException var4) {
               var4.printStackTrace();
            }
         }
      }

      return null;
   }
}
