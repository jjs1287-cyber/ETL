package org.etlforge.integration.client.ui.structure.editor.xml;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusListener;
import java.util.Comparator;
import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.KeyStroke;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.string.StringMatchers;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.property.SingleSelectionBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.list.FilteredKongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.StringMatcherListFilterFunction;

final class ExistingFileSelectionUi implements UiProvider, Focusable {
   private final KongaComboBox folderSelector = createFolderSelector();
   private final FilteredKongaList list;
   private Action refreshAction;

   public static ExistingFileSelectionUi forXml(StructureFileSelectionModel var0) {
      return new ExistingFileSelectionUi(var0, false);
   }

   public static ExistingFileSelectionUi forJson(StructureFileSelectionModel var0) {
      return new ExistingFileSelectionUi(var0, true);
   }

   private ExistingFileSelectionUi(StructureFileSelectionModel var1, boolean var2) {
      this.list = createList(var2);
      this.bindToModel(var1, var2);
   }

   private static KongaComboBox createFolderSelector() {
      KongaComboBox var0 = new KongaComboBox();
      var0.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var0.setRenderer(new ExistingFileSelectionUi.FolderRenderer());
      var0.setMaximumRowCount(16);
      return var0;
   }

   private static FilteredKongaList createList(boolean var0) {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setCellRenderer(new ExistingFileSelectionUi.FileRenderer(var0));
      var1.setVisibleRowCount(4);
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      return FilteredKongaList.builder(var1)
         .withFilterFunction(getListFilterFunction())
         .withFilterLabelStyle(WizardStyleSheet.SMALL_CAPTION_STYLE)
         .buildWithFilterBelow();
   }

   private void bindToModel(StructureFileSelectionModel var1, boolean var2) {
      ComboBoxBinding var3 = new ComboBoxBinding<>(this.folderSelector, var1.getFoldersProperty(), var1.getSelectedFolderProperty());
      ListBinding var4 = new ListBinding<>(var1.getAvailableFilesProperty(), this.list);
      var4.setUseFilteredModel(true);
      var4.setListOrder(new Comparator<ServerFile>() {
         public int compare(ServerFile var1, ServerFile var2x) {
            return var1.getFilePath().compareToIgnoreCase(var2x.getFilePath());
         }
      });
      var4.setItemWhenEmpty(String.format("(No %s structures available)", var2 ? "JSON" : "XML"));
      SingleSelectionBinding var5 = new SingleSelectionBinding<>(var1.getSelectedFileProperty(), this.list, ServerFile.class);
      new Bindings(var3, var4, var5).syncUi();
   }

   public void setRefreshAction(Action var1) {
      this.refreshAction = var1;
      this.list.addKeyBinding((KeyStroke)var1.getValue("AcceleratorKey"), "refresh", var1);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.list.addFocusListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.list.removeFocusListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = Horizontal.box().add(this.folderSelector);
      if (this.refreshAction != null) {
         var2.glue().add(Horizontal.Align.east(this.refreshAction));
      }

      CaptionLayout.of(var2, this.list).decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (this.list.getSelection().isEmpty()) {
         this.list.selectFirst();
      }

      this.list.requestFocus();
   }

   private static Function<String, ListFilter> getListFilterFunction() {
      Function var0 = new Function<Object, String>() {
         @Nullable
         public String apply(Object var1) {
            return var1 instanceof ServerFile ? ((ServerFile)var1).getDisplayName() : null;
         }
      };
      return StringMatcherListFilterFunction.of(StringMatchers.CONTAINS_IGNORE_CASE, var0);
   }

   private static class FileRenderer extends DefaultListCellRenderer {
      private final boolean json;

      public FileRenderer(boolean var1) {
         this.json = var1;
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof ServerFile) {
            this.configureFor((ServerFile)var2);
         }

         if (var4 && !var5) {
            this.setForeground(javax.swing.UIManager.getColor("Label.foreground"));
            this.setBackground(Color.LIGHT_GRAY);
         }

         return this;
      }

      private void configureFor(ServerFile var1) {
         String var3;
         if (this.json) {
            var3 = StringUtils.substringAfter(var1.getName(), "etlforge.json.");
            var3 = StringUtils.substringBeforeLast(var3, ".");
         } else {
            var3 = var1.getDisplayName();
         }

         this.setText(var3);
      }
   }

   private static class FolderRenderer extends DefaultListCellRenderer {
      private FolderRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof ServerFileFolder) {
            ServerFileFolder var6 = (ServerFileFolder)var2;
            this.setText(var6.getName());
            this.setToolTipText(Strings.emptyToNull(var6.getDescription()));
         }

         return this;
      }
   }
}

