package org.etlforge.integration.structure.reader;

import java.io.IOException;
import java.util.List;

public interface DocParser {
   long getBeginRecordPos();

   long getEndRecordPos();

   String getIdentifier() throws IOException;

   long readValue(long var1) throws IOException;

   long readRecord(long var1) throws IOException;

   String getStr(long var1, int var3) throws IOException;

   List<String> splitValues(JbNode var1, boolean var2) throws IOException;

   String getValue(String var1, char var2);
}
