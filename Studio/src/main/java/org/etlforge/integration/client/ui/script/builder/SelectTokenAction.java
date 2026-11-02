package org.etlforge.integration.client.ui.script.builder;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.integration.client.ui.script.text.TokenSelector;

public final class SelectTokenAction extends AbstractAction {
   private final TokenSelector selector;

   public SelectTokenAction(JTextComponent var1, ScriptPartitioner var2) {
      this.selector = new TokenSelector(var1, var2);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.selector.selectTokenAtCaretPosition();
   }
}
