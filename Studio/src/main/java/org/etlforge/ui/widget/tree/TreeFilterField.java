package org.etlforge.ui.widget.tree;

import java.awt.event.ActionListener;
import javax.swing.border.Border;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;

public interface TreeFilterField extends UiProvider {
   void setBorder(Border var1);

   void setCloseable(ActionListener var1);

   void setRemoveFilterOnClose(boolean var1);

   void clear();

   void setDescription(String var1);

   void setInterceptor(Receiver<KongaTreeFilter> var1);
}
