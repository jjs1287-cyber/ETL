package org.etlforge.integration.client.database.ui.primarykeys;

import org.etlforge.ui.UiProvider;

public interface PrimaryKeyDefiner extends UiProvider {
   PrimaryKeys[] getPrimaryKeys();

   void setPrimaryKeys(PrimaryKeys[] var1);
}
