package org.etlforge.integration.client.ui.structure.editor.db.rels;

import org.etlforge.ui.UiProvider;

interface LinkKeysDefiner extends UiProvider {
   boolean checkInput();

   void apply();
}
