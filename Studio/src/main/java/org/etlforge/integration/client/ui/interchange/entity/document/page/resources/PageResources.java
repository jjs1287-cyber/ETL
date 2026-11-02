package org.etlforge.integration.client.ui.interchange.entity.document.page.resources;

import java.util.ListResourceBundle;

public class PageResources extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {"NewField.Label", "New Field"},
      {"Action.AddNew", "Ne&w"},
      {"Action.Calculate", "Calculate"},
      {"Action.CopySegment", "&Copy Segment"},
      {"Action.CreateManually", "Create &Manually"},
      {"Action.CreateFromFile", "Create From &File..."},
      {"Action.DefineFields", "&Fields..."},
      {"Action.Delete", "&Delete Row"},
      {"Action.MoveDown", "Move Down"},
      {"Action.MoveUp", "Move Up"},
      {"Caption.Delimiter", "Delimiter & Qualifier"},
      {"Caption.Dialog.Main", "Define Segment Fields"},
      {"Caption.Dialog.Sub", "(an identifier must be defined in the list)"},
      {"DeleteField", "&Delete Field"},
      {"DeleteSegment", "&Delete Segment"},
      {"Error.DelimiterMissing", "Must specify a character delimiter."},
      {"Error.InvalidQualifier", "The defined qualifier is invalid."},
      {"Label.CharacterOption", "Character &Delimited"},
      {"Label.ComplexTextOption", "&Complex Text Document"},
      {"Label.Delimiter", "Delimiter"},
      {"Label.FixedWidthOption", "Fixed Field &Widths"},
      {"Label.SimpleTextOption", "&Simple Text Document"},
      {"Label.StringQualifier", "String Qualifier"},
      {"Message.EmptyName", "The document name cannot be empty."},
      {"NoName", "[No Name]"},
      {"Segment", "Segment"},
      {"Table.Column.BeginPos", "Begin"},
      {"Table.Column.DataType", "Type"},
      {"Table.Column.Default", "Default"},
      {"Table.Column.EndPos", "End"},
      {"Table.Column.FieldName", "Field Name"},
      {"Table.Column.Format", "Format"},
      {"Table.Column.Length", "Length"},
      {"Table.Column.SegmentName", "Segment Name"},
      {"Table.Column.Occurrence", "Occurrence"},
      {"Table.Column.Parent", "Segment Parent"},
      {"Title.DocumentFormat", "Select Document Format"},
      {"Title.DocumentType", "Select Document Type"},
      {"Title.FieldTable", "Define Segment Properties"}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
