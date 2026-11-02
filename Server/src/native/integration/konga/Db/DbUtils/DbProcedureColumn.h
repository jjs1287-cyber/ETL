/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_PROC_COLUMN_H
#define KONGA_DB_PROC_COLUMN_H

#include <string>

namespace konga
{

	class DbProcedureColumn {
	public:
		DbProcedureColumn() : ColumnType(0), DataType(0), ColumnSize(0), BufferLength(0), DecimalDigits(0),
			Radix(0), Nullable(0), HasDefaultValue(false), SqlDataType(0),
			DateTimeSub(0), OctetLength(0), OrdinalPosition(-1),
			IsNullable(false), IsReturnValue(false) {}

		std::string Catalog;
		std::string Schema;
		std::string Name;
		std::string ProcedureName;
		int			ColumnType;
		int         DataType;
		std::string DataTypeName;
		long        ColumnSize;
		long        BufferLength;
		int         DecimalDigits;
		int         Radix;
		int         Nullable;
		std::string Remarks;
		bool        HasDefaultValue;
		std::string DefaultValue;
		int         SqlDataType;
		std::string SqlDataTypeName;
		int         DateTimeSub;
		long        OctetLength;
		long        OrdinalPosition;
		bool		IsNullable;
		bool		IsReturnValue;

		bool isReturnValue() const;

		                                                                                      

		bool isInput() const;
		bool isOutput() const;
	};

}                   

#endif                          
