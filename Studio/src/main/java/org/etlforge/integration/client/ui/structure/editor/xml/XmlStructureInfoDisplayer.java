package org.etlforge.integration.client.ui.structure.editor.xml;

import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.ui.UiProvider;

public interface XmlStructureInfoDisplayer extends UiProvider {
   void display(XmlStructure var1);

   XmlStructure getDisplayedStructure();
}
