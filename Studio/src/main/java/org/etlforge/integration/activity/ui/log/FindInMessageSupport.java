package org.etlforge.integration.activity.ui.log;

import javax.swing.Action;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.text.editor.SearchableTextComponent;
import org.etlforge.ui.text.ConditionalTextEnabler;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;

final class FindInMessageSupport {
   private final JTextComponent textComponent;
   private final Action action;

   public FindInMessageSupport(JTextComponent var1) {
      this.textComponent = var1;
      this.action = this.createAction(var1);
      this.installActionEnabler();
   }

   private void installActionEnabler() {
      ConditionalTextEnabler var1 = ConditionalTextEnabler.install(this.textComponent);
      NonEmptyTextEnablerCondition var2 = new NonEmptyTextEnablerCondition();
      var1.addDependency(this.action, var2);
   }

   Action getFindAction() {
      return this.action;
   }

   private Action createAction(JTextComponent var1) {
      SearchableTextComponent var2 = new SearchableTextComponent(this.textComponent, PackageResources.Displayer.FIND_TITLE, true);
      var2.setActionDescription(PackageResources.Displayer.FIND_TOOLTIP);
      return var2.getFindAction();
   }
}
