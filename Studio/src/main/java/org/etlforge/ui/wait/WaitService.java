package org.etlforge.ui.wait;

public interface WaitService {
   WaitService NONE = new NullWaitService();

   WaitLock startWait();

   WaitLock startWait(int var1);
}
