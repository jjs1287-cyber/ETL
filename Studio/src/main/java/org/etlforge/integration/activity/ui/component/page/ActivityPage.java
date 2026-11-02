package org.etlforge.integration.activity.ui.component.page;

import javax.swing.Icon;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.activity.ActivitySubject;

public interface ActivityPage extends Editor {
   void retrieveRecords();

   ActivitySubject getObject();

   void setSubjectIcon(Icon var1);
}
