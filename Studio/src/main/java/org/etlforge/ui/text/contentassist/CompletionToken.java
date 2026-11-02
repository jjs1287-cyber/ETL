package org.etlforge.ui.text.contentassist;

import javax.swing.Icon;
import javax.swing.text.Document;
import org.etlforge.util.Interval;

public interface CompletionToken {
   void complete(Document var1);

   Interval getInterval(Document var1);

   String getDisplayValue();

   String getInfo();

   Icon getIcon();
}
