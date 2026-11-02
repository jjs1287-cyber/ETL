package org.etlforge.ui.util.file;

import java.awt.event.ActionListener;
import java.io.File;
import javax.annotation.Nullable;
import javax.swing.filechooser.FileFilter;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanelSite;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.util.Property;

public interface FileSelectorPanel extends UiProvider, InputPanelSite, Focusable {
   FileSelectorPanel.Mode OPEN = FileSelectorPanel.Mode.OPEN;
   FileSelectorPanel.Mode SAVE = FileSelectorPanel.Mode.SAVE;

   void setSelectionMode(FileChooser.SelectionMode var1);

   void displayLastFile();

   void setSelectedFile(File var1);

   void setPathValidator(PathValidator var1);

   @Nullable
   File getSelectedFile();

   void setFileFilter(FileFilter var1);

   void setUseTitleLabel(boolean var1);

   void decorateTitle(LabelDecorator var1);

   @Override
   boolean isInputValid();

   void setEnabled(boolean var1);

   void addFileSelectionListener(FileSelectionListener var1);

   void removeFileSelectionListener(FileSelectionListener var1);

   void addActionListener(ActionListener var1);

   void removeActionListener(ActionListener var1);

   void setBrowseButtonText(String var1);

   Binding bindWith(Property<File> var1);

   void layoutIn(TwoColumnLayout var1);

   enum Mode {
      OPEN,
      SAVE;
   }
}
