package org.etlforge.integration.client.ui.structure.editor.db;

import java.text.MessageFormat;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.database.info.DatabaseObject;

final class PackageResources {
   private PackageResources() {
   }

   private static String getString(String var0) {
      return StructureEditorResources.STRINGS.getString(var0);
   }

   private static Icon getIcon(String var0) {
      return StructureEditorResources.ICONS.getIcon(var0);
   }

   static class MultipleTableWhereClausePage {
      static final String SUB_TITLE = PackageResources.getString("Db.MultipleTableWhereClausePage.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.MultipleTableWhereClausePage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.MultipleTableWhereClausePage.SubCaption");
   }

   static class ObjectSelectionPage {
      static final String SOURCE_SUB_TITLE = PackageResources.getString("Db.ObjectSelectionPage.Source.SubTitle");
      static final String TARGET_SUB_TITLE = PackageResources.getString("Db.ObjectSelectionPage.Target.SubTitle");

      static class Labels {
         static final String AVAILABLE_ITEMS = PackageResources.getString("Db.ObjectSelectionPage.Label.AvailableItems");
         static final String SELECTED_ITEMS = PackageResources.getString("Db.ObjectSelectionPage.Label.SelectedItems");
      }

      static class List {
         static final String CAPTION = PackageResources.getString("Db.ObjectSelectionPage.List.Caption");
         static final String SUB_CAPTION = PackageResources.getString("Db.ObjectSelectionPage.List.SubCaption");
      }

      static class Schema {
         static final String SWITCH_TEXT = PackageResources.getString("Db.ObjectSelectionPage.Schema.SwitchText");
      }

      static class Search {
         static final String LABEL = "";
         static final Icon ICON = PackageResources.getIcon("DatabaseSearch.16");
      }
   }

   static class ObjectUsePage {
      static final String SOURCE_SUB_TITLE = PackageResources.getString("Db.ObjectUsePage.Source.SubTitle");
      static final String TARGET_SUB_TITLE = PackageResources.getString("Db.ObjectUsePage.Target.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.ObjectUsePage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.ObjectUsePage.SubCaption");

      static class InvalidCount {
         static final String MESSAGE = PackageResources.getString("Db.ObjectUsePage.InvalidCount.Message");
         static final String EXCEPTION = PackageResources.getString("Db.ObjectUsePage.InvalidCount.Exception");
      }

      static class TableHeaders {
         static final String MAIN = PackageResources.getString("Db.ObjectUsePage.TableHeaders.Main");
         static final String COUNT = PackageResources.getString("Db.ObjectUsePage.TableHeaders.Count");
      }
   }

   static class PrimaryKeySelectionPage {
      static final String SUB_TITLE = PackageResources.getString("Db.PrimaryKeySelectionPage.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.PrimaryKeySelectionPage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.PrimaryKeySelectionPage.SubCaption");
      static final String EXPLANATION = PackageResources.getString("Db.PrimaryKeySelectionPage.Explanation");
   }

   static class SingleTableWhereClausePage {
      static final String SUB_TITLE = PackageResources.getString("Db.SingleTableWhereClausePage.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.SingleTableWhereClausePage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.SingleTableWhereClausePage.SubCaption");

      static String getColumnCaption(DatabaseObject var0) {
         return MessageFormat.format(PackageResources.getString("Db.SingleTableWhereClausePage.ColumnCaption"), var0.getName());
      }

      static class Labels {
         static final String WHERE_CLAUSE = PackageResources.getString("Db.SingleTableWhereClausePage.Label.WhereClause");
      }
   }

   static class SourceStartPage {
      static final String SUB_TITLE = PackageResources.getString("Db.SourceStartPage.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.SourceStartPage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.SourceStartPage.SubCaption");
      static final String COMBO_LABEL = PackageResources.getString("Db.SourceStartPage.ComboLabel");

      static class Errors {
         static final String NO_SOURCE = PackageResources.getString("Db.SourceStartPage.Error.SourceNotDefined");
      }

      static class Labels {
         static final String WRITE_SQL = PackageResources.getString("Db.SourceStartPage.Label.WriteSQL");
      }
   }

   static class TargetInsertUpdateModePage {
      static final String SUB_TITLE = PackageResources.getString("Db.TargetInsertUpdateModePage.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.TargetInsertUpdateModePage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.TargetInsertUpdateModePage.SubCaption");
      static final String EMPTY_SELETION_ERROR = PackageResources.getString("Db.TargetInsertUpdateModePage.Error.EmptySelection");

      static class Labels {
         static final String SELECT_MODE = PackageResources.getString("Db.TargetInsertUpdateModePage.Label.SelectMode");
      }

      static class TableHeader {
         static final String ITEM = PackageResources.getString("Db.TargetInsertUpdateModePage.TableHeader.Item");
         static final String KEY = PackageResources.getString("Db.TargetInsertUpdateModePage.TableHeader.Key");
      }
   }

   static class TargetStartPage {
      static final String SUB_TITLE = PackageResources.getString("Db.TargetStartPage.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.TargetStartPage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.TargetStartPage.SubCaption");
      static final String COMBO_LABEL = PackageResources.getString("Db.TargetStartPage.ComboLabel");

      static class Errors {
         static final String NO_TARGET = PackageResources.getString("Db.TargetStartPage.Error.TargetNotDefined");
      }
   }

   static class UpdateSourcePage {
      static final String SUB_TITLE = PackageResources.getString("Db.UpdateSourcePage.SubTitle");
      static final String MAIN_CAPTION = PackageResources.getString("Db.UpdateSourcePage.MainCaption");
      static final String SUB_CAPTION = PackageResources.getString("Db.UpdateSourcePage.SubCaption");
      static final String DESCRIPTION = PackageResources.getString("Db.UpdateSourcePage.Description");
      static final String DISCLAIMER = PackageResources.getString("Db.UpdateSourcePage.Disclaimer");
   }

   static class WhereClauseValidator {
      static final String NAME = PackageResources.getString("Db.WhereClauseValidator.Name");
      static final String STARTED = PackageResources.getString("Db.WhereClauseValidator.Started");
      static final String ERROR_MESSAGE = PackageResources.getString("Db.WhereClauseValidator.Error.Message");
   }
}
