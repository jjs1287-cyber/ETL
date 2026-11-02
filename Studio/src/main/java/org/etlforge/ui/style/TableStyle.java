package org.etlforge.ui.style;

import javax.swing.JTable;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public interface TableStyle {
   void makeOver(JTable var1);

   void addRendererPreparer(TableRendererPreparer var1);

   void setGridMode(TableGridMode var1);
}
