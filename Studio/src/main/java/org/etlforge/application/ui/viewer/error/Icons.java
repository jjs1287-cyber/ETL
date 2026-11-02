package org.etlforge.application.ui.viewer.error;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.Empty;

final class Icons {
   static final Icon ERROR_ICON = CommonIcons.ERROR_24;
   static final Icon EMPTY_ICON = Empty.iconOfSameSize(ERROR_ICON);

   private Icons() {
   }
}
