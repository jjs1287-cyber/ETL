package org.etlforge.integration.client.ui.de.global.tree;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.dataelement.DefaultDataElementTemplate;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferable;
import org.etlforge.ui.widget.tree.KongaTree;

final class TransferHandlerImpl extends TransferHandler {
   private final Function<Object, String> nameExtractor = Functions.toStringFunction();

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
      KongaTree var2 = (KongaTree)var1;
      List var3 = KongaListUtils.filter(var2.getSelectedNodes(), Predicates.instanceOf(DataElementNode.class));
      switch (var3.size()) {
         case 0:
            return null;
         case 1:
            String var4 = this.nameExtractor.apply(var3.get(0));
            DefaultDataElementTemplate var5 = new DefaultDataElementTemplate(var4);
            return new DeTemplateTransferable(var5);
         default:
            String var6 = Joiner.on(SystemUtils.LINE_SEPARATOR).join(Iterables.transform(var3, this.nameExtractor));
            return new StringSelection(var6);
      }
   }
}
