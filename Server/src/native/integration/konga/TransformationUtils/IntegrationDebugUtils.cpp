/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Debugger/Debuggee.h>

#include "IntegrationBreakpointLocation.h"
#include "IntegrationDebugUtils.h"

using namespace konga;
using std::string;

int konga::IntegrationDebugBreakpointHit(Debuggee* pDebuggee,
										 const string& stTargetPath,
										 long lScriptId,
										 long line_number,
										 const string& stInstanceString,
										 const string& stGeneratorPath,
										 const std::set<DataElement*>& source_de,
										 const string& stResult)
{
	if ( pDebuggee == 0 || !pDebuggee->isDebugging()) return 1;

	IntegrationBreakpointLocation* pBreakpointLocation = dynamic_cast<IntegrationBreakpointLocation*>( pDebuggee->getCallStack().top() );
#ifdef _DEBUG
	assert(pBreakpointLocation);
#endif
	if ( pBreakpointLocation  == 0 ) {
		                                                                                       
		return 0;
	}
	if ( lScriptId > 0 ) {
		                              
		assert(pBreakpointLocation->getScriptId() == lScriptId );	                                                                    
	}
	else {
		                                           
		assert(pBreakpointLocation->getAttributePath() == stTargetPath);
	}
	pBreakpointLocation->setLineNumber(line_number);
	pBreakpointLocation->setResult(stResult);

	pBreakpointLocation->setProperty(IntegrationBreakpointLocation::INSTANCE_STRING, stInstanceString);
	pBreakpointLocation->setProperty(IntegrationBreakpointLocation::GENERATOR_PATH, stGeneratorPath);

	pBreakpointLocation->setSourceDataElements(source_de);

	return pDebuggee->DebugBreakpointHit(pBreakpointLocation);
}

