package org.etlforge.application.ui.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import org.etlforge.application.appdir.LogDirectory;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.log.LogLevelSelector;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class ApplicationLogLevelConsole implements UiProvider {
   private final ApplicationLogLevelsTable table;
   private final ApplicationLogLevelConsole.GlobalSelector globalLevelSelector;
   private final Collection<Logger> loggers;

   public ApplicationLogLevelConsole() {
      this(ApplicationLog.getExistingLoggers());
   }

   public ApplicationLogLevelConsole(Collection<Logger> var1) {
      this.loggers = var1;
      this.table = new ApplicationLogLevelsTable(var1);
      this.globalLevelSelector = new ApplicationLogLevelConsole.GlobalSelector();
      this.globalLevelSelector.setEnabled(!var1.isEmpty());
   }

   public void apply() {
      if (this.globalLevelSelector.isActive()) {
         this.applyGlobalLevel();
      } else {
         this.table.apply();
      }
   }

   private void applyGlobalLevel() {
      Level var1 = this.globalLevelSelector.getLevel();
      if (var1 != null) {
         for (Logger var3 : this.loggers) {
            var3.setLevel(var1);
         }

         this.table.reset();
      }
   }

   public void saveLogLevels(LogDirectory var1) {
      Logger[] var2 = this.table.getDisplayedLoggers();
      var1.saveLogLevels(var2);
   }

   public void reset() {
      this.table.reset();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.center(this.table).south(this.globalLevelSelector.getUi());
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   private final class GlobalSelector implements ActionListener {
      private final JCheckBox activator = new KongaCheckBox("&Global setting:");
      private final LogLevelSelector selector;

      public GlobalSelector() {
         this.activator.addActionListener(this);
         this.selector = new LogLevelSelector();
         this.selector.setEnabled(false);
      }

      public UiProvider getUi() {
         return BoxBuilder.horizontal().addAllWithSpace(10, this.activator, this.selector);
      }

      public void setEnabled(boolean var1) {
         this.activator.setEnabled(var1);
         this.selector.setEnabled(var1 && this.activator.isSelected());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         boolean var2 = this.activator.isSelected();
         this.selector.setEnabled(var2);
         ApplicationLogLevelConsole.this.table.setEnabled(!var2);
      }

      public boolean isActive() {
         return this.activator.isEnabled() && this.activator.isSelected();
      }

      public Level getLevel() {
         return this.selector.getSelectedLevel();
      }
   }
}
