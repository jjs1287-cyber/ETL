package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.NetSuiteRecordRef;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldMultiSelect;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueMultiSelect;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.util.ListProperty;

public final class RecordRefListValueUi extends ValueUi {
   private final List<String> types;
   private final ListProperty<NetSuiteRecordRef> recordRefs = new ListProperty<>("values", new PropertyChangeSupport(this));
   private final KongaList list;
   private final RecordRefListValueUi.AddEntryAction addAction = new RecordRefListValueUi.AddEntryAction();
   private final RecordRefListValueUi.EditEntryAction editAction = new RecordRefListValueUi.EditEntryAction();
   private final RecordRefListValueUi.RemoveEntriesAction removeAction = new RecordRefListValueUi.RemoveEntriesAction();
   private final boolean isCustomField;

   public RecordRefListValueUi(SearchFieldMultiSelect var1) {
      this(var1.getPredefinedList(), var1.isCustomField());
   }

   public RecordRefListValueUi(List<String> var1, boolean var2) {
      this.types = var2 ? null : Preconditions.checkNotNull(var1);
      this.list = this.createList();
      this.isCustomField = var2;
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(4);
      var1.setCellRenderer(new RecordRefListValueUi.Renderer());
      KongaList var2 = var1.newList();
      ListBinding var3 = new ListBinding<>(this.recordRefs, var2);
      var3.syncUi();
      var2.setDefaultAction(this.editAction);
      this.removeAction.setSelectionSource(var2);
      return var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = "Click Add to add new RecordRefs to the list.";
      BorderLayoutBuilder var3 = Horizontal.Align.east(Horizontal.grid(5).of(this.addAction, this.editAction, this.removeAction));
      new BorderLayoutBuilder(0, 8).north(var2).center(this.list).south(var3).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.selectFirst();
      this.list.requestFocus();
   }

   protected SearchFieldValueMultiSelect constructValue(NetSuiteSearchOperator var1) {
      SearchFieldValueMultiSelect var2 = new SearchFieldValueMultiSelect(var1, this.isCustomField);

      for (NetSuiteRecordRef var4 : this.recordRefs) {
         var2.add(var4);
      }

      return var2;
   }

   @Override
   public void setValue(SearchFieldValue var1) {
      SearchFieldValueMultiSelect var2 = ArgCheck.checkInstance(var1, SearchFieldValueMultiSelect.class);
      this.recordRefs.set(var2.getSearchValue());
   }

   @Nullable
   private NetSuiteRecordRef openRecordRefDialog(ActionEvent var1, final RecordRefValueUi var2, String var3) {
      Window var4 = Objects.firstNonNull(UiUtils.getWindowAncestor(var1).orNull(), ApplicationUi.getAppWinFrame());
      KongaDialog var5 = new KongaDialog(var4, var3, ModalityType.APPLICATION_MODAL) {
         @Override
         protected void onOk() {
            ValueUi.ValidationResult var1x = var2.validateInput();
            if (var1x.isValid()) {
               super.onOk();
            } else {
               Alert.error(this, var1x.whyNot(), "Invalid input");
            }
         }
      };
      var5.standardLayout(var2, KongaDialog.OK_CANCEL);
      var5.setOkButtonAsDefault();
      var5.manageLocation(RecordRefListValueUi.class);
      var5.setVisible(true);
      return var5.wasOkPressed() ? var2.getRecordRef() : null;
   }

   private class AddEntryAction extends AbstractAction {
      public AddEntryAction() {
         super("Add");
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddEntryAction.this.add(var1);
            }
         });
      }

      private void add(ActionEvent var1) {
         NetSuiteRecordRef var2 = this.getNewValue(var1);
         if (var2 != null) {
            RecordRefListValueUi.this.recordRefs.add(var2);
         }
      }

      @Nullable
      private NetSuiteRecordRef getNewValue(ActionEvent var1) {
         RecordRefValueUi var2 = new RecordRefValueUi(RecordRefListValueUi.this.types, RecordRefListValueUi.this.isCustomField);
         return RecordRefListValueUi.this.openRecordRefDialog(var1, var2, "Add a Record Ref");
      }
   }

   private class EditEntryAction extends AbstractSingleObjectSelectionAction<NetSuiteRecordRef> {
      public EditEntryAction() {
         super(NetSuiteRecordRef.class, "Edit");
      }

      protected void run(final NetSuiteRecordRef var1, final ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditEntryAction.this.edit(var1, var2);
            }
         });
      }

      private void edit(NetSuiteRecordRef var1, ActionEvent var2) {
         RecordRefValueUi var3 = new RecordRefValueUi(RecordRefListValueUi.this.types, RecordRefListValueUi.this.isCustomField);
         var3.setRecordRef(var1);
         NetSuiteRecordRef var4 = RecordRefListValueUi.this.openRecordRefDialog(var2, var3, "Edit Record Ref");
         if (var4 != null) {
            RecordRefListValueUi.this.recordRefs.identityReplace(var1, var4);
         }
      }
   }

   private class RemoveEntriesAction extends AbstractSingleTypeSelectionAction<NetSuiteRecordRef> {
      public RemoveEntriesAction() {
         super(NetSuiteRecordRef.class, "Remove");
      }

      @Override
      protected void run(final List<NetSuiteRecordRef> var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RecordRefListValueUi.this.recordRefs.removeAll(var1);
            }
         });
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof NetSuiteRecordRef) {
            NetSuiteRecordRef var6 = (NetSuiteRecordRef)var2;
            this.render(var6);
         } else {
            this.setToolTipText(null);
         }

         return this;
      }

      private void render(NetSuiteRecordRef var1) {
         String var2 = String.format(
            "Type: %s, Name: %s, Internal ID: %s, External ID: %s",
            Strings.nullToEmpty(var1.getType()),
            Strings.nullToEmpty(var1.getName()),
            Strings.nullToEmpty(var1.getInternalId()),
            Strings.nullToEmpty(var1.getExternalId())
         );
         this.setText(var2);
         this.setToolTipText(var2);
      }
   }
}
