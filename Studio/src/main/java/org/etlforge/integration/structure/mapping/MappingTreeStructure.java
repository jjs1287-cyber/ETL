package org.etlforge.integration.structure.mapping;

import java.io.IOException;
import java.io.Reader;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROMHeader;

public interface MappingTreeStructure {
   CROM getRoot();

   Reader getReader() throws IOException;

   String getString();

   CROMHeader getHeader();

   boolean outOfDate();
}
