package org.etlforge.integration.client.ui.script.builder.dataelement;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.de.global.GlobalDataElementsBrowser;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.FunctionArgumentSuggestions;
import org.etlforge.integration.client.ui.script.builder.InsertionToken;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderMode;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSection;
import org.etlforge.integration.client.ui.script.resources.ScriptResources;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;

public final class DefaultDataElementSelector implements ScriptBuilderSection {
   private final GlobalDataElementsBrowser browser;
   private Receiver<InsertionToken> deInserter = NoReceiver.create();
   private final SelectionAction transferAction = new DefaultDataElementSelector.TransferAction();
   private final DataElementSelectorLayout layout;

   public DefaultDataElementSelector(GlobalDataElementsBrowser var1) {
      this.browser = var1;
      var1.setDefaultAction(this.transferAction);
      this.layout = new DataElementSelectorLayout(var1, this.transferAction);
   }

   @Override
   public void setReceiver(Receiver<InsertionToken> var1) {
      this.deInserter = Preconditions.checkNotNull(var1);
   }

   @Override
   public ScriptBuilderSection.Title getTitleDecoration() {
      return new ScriptBuilderSection.Title("&Data Elements", DataElementIcons.TITLE);
   }

   @Override
   public void collectArgumentSuggestions(FunctionArgumentSuggestions var1) {
   }

   @Override
   public void setMode(ScriptBuilderMode var1) {
   }

   public void setAllowBuiltInVariablesToBeHidden(boolean var1) {
      this.layout.setAllowBuiltInVariablesToBeHidden(var1);
   }

   @Override
   public void dispose() {
      this.browser.dispose();
   }

   @Override
   public void requestFocus() {
      this.browser.requestFocus();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout.decorate(var1);
   }

   private class TransferAction extends AbstractSingleObjectSelectionAction<String> {
      public TransferAction() {
         super(String.class, null, Icons.getPreferredTransferIcon());
         this.putValue("ShortDescription", ScriptResources.getString("DataElementSelector.Transfer"));
      }

      protected void run(String var1, ActionEvent var2) {
         DefaultDataElementSelector.this.deInserter.handle(new InsertionToken.DataElement(var1));
      }
   }
}
