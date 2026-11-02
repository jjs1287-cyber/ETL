/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <cassert>
#include "InterchangeDataStructs.h"

using namespace konga;
using namespace konga::interchange_deploy;

using std::string;
using std::vector;

void konga::interchange_deploy::FillInterchangeDataTableStruct(const InterchangeDataTable& table,
															   InterchangeDataTableStruct& tableStruct)
{
	                                        
	if ( !table.columnNames || !table.columnNames->item )
		return;

	int nCols = -1;
	const xsd__string* arrColumnNames = table.columnNames->item->get(nCols);
	if ( !arrColumnNames || !nCols )
		return;

	                          
	for( int iCol = 0; iCol < nCols; ++iCol ) {
		                                     
		if ( arrColumnNames[iCol] != 0 ) {
			tableStruct.m_vColumnNames.push_back(arrColumnNames[iCol]);
		}
	}

	if ( !table.rows || !table.rows->item )
		return;

	                 
	int nRows = -1;
	InterchangeDataRow** arrRows = table.rows->item->get(nRows);
	for( int iRow = 0; iRow < nRows; ++iRow ) {
		if ( !arrRows[iRow] )
			continue;

		const InterchangeDataRow& row = *arrRows[iRow];
		if ( row.fields == 0 )
			continue;
		if ( row.fields->item == 0 )
			continue;	                                

		int nDataCols = -1;
		const xsd__string* arrData = row.fields->item->get(nDataCols);
		assert(nDataCols==nCols);
		if ( nDataCols != nCols ) {
			string stErr("Internal Error: Column names array and data array are of different sizes. " \
				"This is not permitted and most likely a bug. These are the data columns:\n");
			for( int i = 0; i < nCols; ++i ) {
				stErr += tableStruct.m_vColumnNames.at(i) + "\n";
			}
			throw CrException(stErr);
		}

		                                                                       
		tableStruct.m_vRows.push_back(InterchangeDataRowStruct());
		InterchangeDataRowStruct& rowStruct = *(tableStruct.m_vRows.end()-1);
				
		for( int iDataCol = 0; iDataCol < nDataCols; ++iDataCol ) {
			                                      
			rowStruct.m_vFields.push_back( ClientField(arrData[iDataCol]) );
		}
	}
}


