/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include "DefaultErrorReporter.h"

using namespace konga;
using std::string;

string DefaultErrorReporter::getError() const {
	return m_sError;
}

string DefaultErrorReporter::getLastError() const {
	return m_sLastError;
}

                                                                   
void DefaultErrorReporter::addError(const string& sError) {
	if ( sError.empty() )
		return;
	if ( !m_sError.empty() && *(m_sError.end()-1) != '\n' )
		m_sError += '\n';
	m_sError += sError;
	m_sLastError = sError;
}
