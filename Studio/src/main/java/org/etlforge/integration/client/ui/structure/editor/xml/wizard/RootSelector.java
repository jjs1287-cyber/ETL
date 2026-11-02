package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.option.Option;
import org.etlforge.xml.ExpandedName;

interface RootSelector extends UiProvider, Option {
   ExpandedName getSelectedRoot() throws InvalidRootException;

   void setSelectedRoot(ExpandedName var1);

   void setActive(boolean var1);
}
