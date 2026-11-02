package org.etlforge.integration.client.ui.project.info;

import javax.swing.Icon;
import org.etlforge.ui.UiProvider;

public interface ProjectConsoleSubView extends UiProvider {
   String getId();

   String getTitle();

   Icon getIcon();
}
