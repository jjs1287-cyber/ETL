package org.etlforge.integration.loadsource.client;

import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;

public interface LoadSourceDataInputProvider {
   LoadSourceDataInput getInput();
}
