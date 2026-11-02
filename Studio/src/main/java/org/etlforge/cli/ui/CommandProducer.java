package org.etlforge.cli.ui;

import com.google.common.base.Function;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;

public interface CommandProducer extends UiProvider {
   void setCommandConsumer(Receiver<String> var1);

   void setCompleter(Function<String, String> var1);
}
