package org.etlforge.integration.client.ui.script.builder;

import java.awt.datatransfer.StringSelection;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.integration.client.ui.entity.TransferableIntegrationEntities;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.ui.dnd.DelegatingTransferHandler;

final class ExpressionAreaTransferHandler extends DelegatingTransferHandler {
   public ExpressionAreaTransferHandler(TransferHandler var1) {
      super(var1);
   }

   @Override
   public boolean canImport(TransferSupport var1) {
      return this.isProjectItems(var1) || super.canImport(var1);
   }

   private boolean isProjectItems(TransferSupport var1) {
      return var1.isDataFlavorSupported(TransferableIntegrationEntities.ENTITIES_FLAVOR);
   }

   @Override
   public boolean importData(TransferSupport var1) {
      if(var1.isDataFlavorSupported(SourcePathTransferable.FLAVOR)&&var1.getComponent() instanceof javax.swing.text.JTextComponent){
         try{
            javax.swing.text.JTextComponent editor=(javax.swing.text.JTextComponent)var1.getComponent();int position=editor.getCaretPosition();
            if(var1.isDrop()&&var1.getDropLocation() instanceof javax.swing.text.JTextComponent.DropLocation)position=((javax.swing.text.JTextComponent.DropLocation)var1.getDropLocation()).getIndex();
            if(org.etlforge.integration.data.script.JavaScriptFunctions.isInside(editor.getText(),position)){
               String path=org.etlforge.lang.KongaStringUtils.removeStartAndEnd((String)var1.getTransferable().getTransferData(SourcePathTransferable.FLAVOR),"[","]");
               editor.setCaretPosition(position);editor.replaceSelection("ETLForge.GetSource('"+org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(path)+"')");return true;
            }
         }catch(Exception failure){return false;}
      }
      return this.isProjectItems(var1) && this.importProjectItems(var1) ? true : super.importData(var1);
   }

   private boolean importProjectItems(TransferSupport var1) {
      try {
         List var2 = (List)var1.getTransferable().getTransferData(TransferableIntegrationEntities.ENTITIES_FLAVOR);
         if (var2.size() != 1) {
            return false;
         }

         IntegrationEntity var3 = (IntegrationEntity)var2.get(0);
         StringSelection var4 = new StringSelection(ScriptConstants.getProjectItemTagString(var3));
         super.importData((JComponent)var1.getComponent(), var4);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return true;
   }
}
