/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <boost/scoped_array.hpp>

#include <konga/Utils/Settings.h>

#include <konga/Crypto/Crypto.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "SpecialHandlingDeployBase64Col.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;

SpecialHandlingDeployBase64Col::SpecialHandlingDeployBase64Col(int colpos) : m_iColPos(colpos) {}

bool SpecialHandlingDeployBase64Col::HandleRow(InterchangeDataRowStruct& row) {
	bool bRowModified = false;

	const NullableString& tran = row.m_vFields.at(m_iColPos);
	if ( tran != 0 ) {
		const string& s = tran.str();
		if ( !s.empty() ) {
			                                     
			try {
				int len = 0;
				boost::scoped_array<unsigned char> p( konga::DecodeBase64(s, len) );

				                                  
				const string utf8_string((const char*)p.get(), len);
				row.m_vFields.at(m_iColPos) = utf8_string;
				bRowModified = true;
			}
			catch( const CrException& e ) {
				UIBACKEND_TRACE(e.GetErrorString());
			}
		}
	}

	return bRowModified;
}

