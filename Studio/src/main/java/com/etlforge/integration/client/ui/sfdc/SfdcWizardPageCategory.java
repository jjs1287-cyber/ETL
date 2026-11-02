package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public final class SfdcWizardPageCategory {
   public static final EditorCategory CATEGORY = new EditorCategory(
      "SFDC Wizards", EntityIcons.forType(EntityType.SalesforceQuery).getDefaultIcon(EntityIcons.Size.SMALL)
   );
}
