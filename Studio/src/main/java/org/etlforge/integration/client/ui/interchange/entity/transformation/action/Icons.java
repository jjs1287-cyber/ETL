package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.util.ListResourceBundle;
import org.etlforge.application.ui.action.ActionDictionaryKeys;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {ActionDictionaryKeys.smallIcon("action.ic.tx.LoadSource"), ClientIcons.LOAD_SOURCE_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.tx.LoadSource"), ClientIcons.LOAD_SOURCE_24},
      {ActionDictionaryKeys.smallIcon("action.ic.tx.TestTransformation"), ClientIcons.TEST_TRANSFORMATION_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.tx.TestTransformation"), ClientIcons.TEST_TRANSFORMATION_24},
      {ActionDictionaryKeys.smallIcon("action.ic.tx.TestOperation"), ClientIcons.TEST_OPERATION_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.tx.TestOperation"), ClientIcons.TEST_OPERATION_24},
      {ActionDictionaryKeys.smallIcon("action.ic.tx.EditUpdateKeys"), DatabaseUiResources.ICONS.getIcon("PrimaryKey.16")},
      {ActionDictionaryKeys.smallIcon("action.ic.RefreshAllTransformations"), CommonIcons.REFRESH_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.RefreshAllTransformations"), CommonIcons.REFRESH_24},
      {ActionDictionaryKeys.smallIcon("action.ic.tx.Refresh"), CommonIcons.REFRESH_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.tx.Refresh"), CommonIcons.REFRESH_24},
      {ActionDictionaryKeys.smallIcon("action.ic.tx.Search"), CommonIcons.FIND_16},
      {ActionDictionaryKeys.mediumIcon("action.ic.tx.Search"), ApplicationResources.ICONS.getIcon("Find.24")}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
