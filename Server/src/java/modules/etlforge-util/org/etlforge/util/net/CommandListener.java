package org.etlforge.util.net;

import java.io.IOException;

public interface CommandListener {
   void registerCommandHandler(String var1, CommandHandler var2);

   void startListening() throws IOException;

   void stopListening() throws IOException;
}
