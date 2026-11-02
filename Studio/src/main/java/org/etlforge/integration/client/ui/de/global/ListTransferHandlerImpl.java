package org.etlforge.integration.client.ui.de.global;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.integration.dataelement.DefaultDataElementTemplate;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferable;
import org.etlforge.ui.widget.list.ListUtils;

final class ListTransferHandlerImpl extends TransferHandler {
   @Override
   public boolean canImport(TransferSupport var1) {
      return false;
   }

   @Override
   public int getSourceActions(JComponent var1) {
      return 1;
   }

   @Override
   protected Transferable createTransferable(JComponent var1) {
      JList var2 = (JList)var1;
      int[] var3 = var2.getSelectedIndices();
      switch (var3.length) {
         case 0:
            return null;
         case 1:
            String var4 = (String)var2.getModel().getElementAt(var3[0]);
            DefaultDataElementTemplate var5 = new DefaultDataElementTemplate(var4);
            return new DeTemplateTransferable(var5);
         default:
            String var6 = StringUtils.join(ListUtils.toArray(var2.getModel(), var3), SystemUtils.LINE_SEPARATOR);
            return new StringSelection(var6);
      }
   }
}
