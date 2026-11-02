package org.etlforge.integration.client.ui.interchange.component.launcher;

import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.style.TextStyle;

public abstract class AbstractLaunchOption implements LaunchOption {
   private final String name;
   private final Icon icon;
   private LaunchOptionContainer container;
   private final TextStyle captionStyle;

   public AbstractLaunchOption(String var1, Icon var2) {
      this.name = var1;
      this.icon = var2;
      TextStyle var3 = WizardStyleSheet.CAPTION_STYLE;
      this.captionStyle = TextStyle.adjustSize(var3, 2);
   }

   @Override
   public boolean canBeDefaultChoice() {
      return false;
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public final Icon getIcon() {
      return this.icon;
   }

   @Override
   public final void setContainer(LaunchOptionContainer var1) {
      this.container = var1;
   }

   @Override
   public boolean needsExternalScroll() {
      return true;
   }

   protected final void launchProject() {
      this.container.launchProject(this);
   }

   protected final JLabel createCaption() {
      String var1 = UiResourceBundle.MNEMONIC_FACTORY.asText(this.name);
      return this.captionStyle.makeLabel(var1);
   }

   @Override
   public void hibernate() {
   }

   @Override
   public void wakeUp() {
   }
}
