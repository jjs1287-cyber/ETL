package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.db.ColumnData;

public final class TransformationsTabRow implements TranDbTableRow {
   private final ColumnData<Integer> transformation_id = ColumnData.newIntColumn("transformation_id");
   private final ColumnData<String> transformation_name = ColumnData.newStringColumn("transformation_name");
   private final ColumnData<Integer> source_type_id = ColumnData.newIntColumn("source_type_id");
   private final ColumnData<Integer> target_type_id = ColumnData.newIntColumn("target_type_id");
   private final ColumnData<String> source_text_doc = ColumnData.newStringColumn("source_text_doc");
   private final ColumnData<String> source_edi = ColumnData.newStringColumn("source_edi");
   private final ColumnData<String> source_sourceid = ColumnData.newStringColumn("source_sourceid");
   private final ColumnData<String> target_text_doc = ColumnData.newStringColumn("target_text_doc");
   private final ColumnData<String> target_edi = ColumnData.newStringColumn("target_edi");
   private final ColumnData<String> target_targetid = ColumnData.newStringColumn("target_targetid");
   private final ColumnData<String> source_xml = ColumnData.newStringColumn("source_xml");
   private final ColumnData<String> target_xml = ColumnData.newStringColumn("target_xml");
   private final ColumnData<String> natureofsource = ColumnData.newStringColumn("natureofsource");
   private final ColumnData<String> natureoftarget = ColumnData.newStringColumn("natureoftarget");
   private final ColumnData<Integer> insert_mode = ColumnData.newIntColumn("insert_mode");
   private final ColumnData<String> target_dtd_url = ColumnData.newStringColumn("target_dtd_url");
   private final ColumnData<Integer> source_edi_default = ColumnData.newIntColumn("source_edi_default");
   private final ColumnData<Integer> target_edi_default = ColumnData.newIntColumn("target_edi_default");
   private final ColumnData<Integer> isvalid = ColumnData.newIntColumn("isvalid");
   private final ColumnData<String> sourcedtd_root = ColumnData.newStringColumn("sourcedtd_root");
   private final ColumnData<String> targetdtd_root = ColumnData.newStringColumn("targetdtd_root");
   private final ColumnData<Integer> sourcejmsmessageid = ColumnData.newIntColumn("sourcejmsmessageid");
   private final ColumnData<Integer> targetjmsmessageid = ColumnData.newIntColumn("targetjmsmessageid");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.transformation_id.fetchValue(var1);
      this.transformation_name.fetchValue(var1);
      this.source_type_id.fetchValue(var1);
      this.target_type_id.fetchValue(var1);
      this.source_text_doc.fetchValue(var1);
      this.source_edi.fetchValue(var1);
      this.source_sourceid.fetchValue(var1);
      this.target_text_doc.fetchValue(var1);
      this.target_edi.fetchValue(var1);
      this.target_targetid.fetchValue(var1);
      this.source_xml.fetchValue(var1);
      this.target_xml.fetchValue(var1);
      this.natureofsource.fetchValue(var1);
      this.natureoftarget.fetchValue(var1);
      this.insert_mode.fetchValue(var1);
      this.target_dtd_url.fetchValue(var1);
      this.source_edi_default.fetchValue(var1);
      this.target_edi_default.fetchValue(var1);
      this.isvalid.fetchValue(var1);
      this.sourcedtd_root.fetchValue(var1);
      this.targetdtd_root.fetchValue(var1);
      this.sourcejmsmessageid.fetchValue(var1);
      this.targetjmsmessageid.fetchValue(var1);
   }

   public Integer get_transformation_id() {
      return this.transformation_id.getValue();
   }

   public String get_transformation_name() {
      return this.transformation_name.getValue();
   }

   public Integer get_source_type_id() {
      return this.source_type_id.getValue();
   }

   public Integer get_target_type_id() {
      return this.target_type_id.getValue();
   }

   public String get_source_text_doc() {
      return this.source_text_doc.getValue();
   }

   public String get_source_edi() {
      return this.source_edi.getValue();
   }

   public String get_source_sourceid() {
      return this.source_sourceid.getValue();
   }

   public String get_target_text_doc() {
      return this.target_text_doc.getValue();
   }

   public String get_target_edi() {
      return this.target_edi.getValue();
   }

   public String get_target_targetid() {
      return this.target_targetid.getValue();
   }

   public String get_source_xml() {
      return this.source_xml.getValue();
   }

   public String get_target_xml() {
      return this.target_xml.getValue();
   }

   public String get_natureofsource() {
      return this.natureofsource.getValue();
   }

   public String get_natureoftarget() {
      return this.natureoftarget.getValue();
   }

   public Integer get_insert_mode() {
      return this.insert_mode.getValue();
   }

   public String get_target_dtd_url() {
      return this.target_dtd_url.getValue();
   }

   public Integer get_source_edi_default() {
      return this.source_edi_default.getValue();
   }

   public Integer get_target_edi_default() {
      return this.target_edi_default.getValue();
   }

   public Integer get_isvalid() {
      return this.isvalid.getValue();
   }

   public String get_sourcedtd_root() {
      return this.sourcedtd_root.getValue();
   }

   public String get_targetdtd_root() {
      return this.targetdtd_root.getValue();
   }

   public Integer get_sourcejmsmessageid() {
      return this.sourcejmsmessageid.getValue();
   }

   public Integer get_targetjmsmessageid() {
      return this.targetjmsmessageid.getValue();
   }
}
