package org.etlforge.integration.client.ui.interchange.component.launcher;

import javax.swing.Icon;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiProvider;

public interface LaunchOption extends UiProvider {
   String getName();

   Icon getIcon();

   IntegrationProject getProject() throws Exception;

   void hibernate();

   void wakeUp();

   void setContainer(LaunchOptionContainer var1);

   void activated();

   boolean needsExternalScroll();

   boolean canBeDefaultChoice();
}
