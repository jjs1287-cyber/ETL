/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifdef WIN32
#include <windows.h>	                                        
#endif

#include <sqlext.h>

#include "DbProcedureColumn.h"

using namespace konga;

bool DbProcedureColumn::isReturnValue() const {
	if ( IsReturnValue )
		return true;
	if ( ColumnType == SQL_RETURN_VALUE )
		return true;

	return false;
}

bool DbProcedureColumn::isInput() const {
	switch( ColumnType ) {
		case SQL_PARAM_TYPE_UNKNOWN:
		case SQL_PARAM_INPUT:
		case SQL_PARAM_INPUT_OUTPUT:
			return true;
		default:
			return false;
	};
}

bool DbProcedureColumn::isOutput() const {
	switch( ColumnType ) {
		case SQL_PARAM_INPUT_OUTPUT:
		case SQL_PARAM_OUTPUT:
			return true;
		default:
			return false;
	};
}

