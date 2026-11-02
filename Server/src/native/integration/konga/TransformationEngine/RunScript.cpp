/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>
#include <memory>

#include <boost/thread/tss.hpp>				                       

#include <konga/WinUtils/WinErrorUtils.h>
#include <konga/WinUtils/KongaStackWalker.h>

#include <konga/Debugger/Debuggee.h>

#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/SystemVariables/SystemVariables.h>	                       

#include <konga/TransformationUtils/ScriptCache.h>
#include <konga/TransformationUtils/IntegrationBreakpointLocation.h>

#include "DETransform.h"	                           
#include "TreeMapper.h"
#include "RunScript.h"

using namespace konga;
using namespace std;

namespace {

	const string MAX_NESTING_LEVEL_DE_NAME("etlforge.scripting.nesting.max");
	const int DEFAULT_NESTING_LEVEL = 10;

	boost::thread_specific_ptr<int> tls_pNestingLevel;

	  
                      
                                                                                
                                                                                      

                       
                
                    

                        
   
   

#ifdef WIN32
    string getStackTrace(PCONTEXT context) {
        KongaStackWalker walker;
        walker.ShowCallstack(GetCurrentThread(), context);
        return walker.toString();
    }
	DWORD jbExcpFilter(DWORD code, LPEXCEPTION_POINTERS pExceptionInfo) {
		if( code != EXCEPTION_STACK_OVERFLOW )
			return EXCEPTION_CONTINUE_SEARCH;

		string s(prettyPrintExceptionInfo(pExceptionInfo));
		string stack(getStackTrace(pExceptionInfo->ContextRecord));
		cerr << stack << endl;
		cerr.flush();
		                                                                                                   
		return EXCEPTION_CONTINUE_EXECUTION;
	}
#endif

	                         
	  
                                                
              
       
   
        
                                                               
                                                               
              
     
                                                                         
   
                               
                 
                         
                                      
                             
   
                                                                
                                              
                                                                            

                                                                                     

                                          
                                                                                                 
                                                              

                                
                                                                                          
                                                                                                  
     
        
  
   

}

namespace {
	string getMaxNestingLevelDataElementName() {
		return MAX_NESTING_LEVEL_DE_NAME;
	}
}

int konga::getMaxNestingLevel(const OperationDataElementHolder& deHolder) {
	return GetSystemVariableAsLong(getMaxNestingLevelDataElementName(), &deHolder, DEFAULT_NESTING_LEVEL);
}

int konga::incrementScriptNestingLevel() {
	if (tls_pNestingLevel.get() == 0)
	  tls_pNestingLevel.reset(new int(1));
	else
		++(*tls_pNestingLevel);
	return *tls_pNestingLevel;
}

int konga::decrementScriptNestingLevel() {
	if (tls_pNestingLevel.get() == 0)
	  tls_pNestingLevel.reset(new int(-1));
	else
		--(*tls_pNestingLevel);
	assert(*tls_pNestingLevel>=0 && "negative script nesting level");
	return *tls_pNestingLevel;
}

void konga::setScriptNestingLevel(int i) {
	if (tls_pNestingLevel.get() == 0)
	  tls_pNestingLevel.reset(new int(i));
	else
		(*tls_pNestingLevel) = i;
	assert(*tls_pNestingLevel>=0 && "negative script nesting level");
}

namespace {

	struct AutoDecrementNestingLevel {
		~AutoDecrementNestingLevel() { decrementScriptNestingLevel(); }
	};

	DataElement* RunScriptImpl(TreeMapper* pTreeMapper, const string& script, const string& scriptName, long lScriptId, CTransform* pTrans) {
		const int maxNestingLevel = getMaxNestingLevel(*pTreeMapper->getOperationDataElementHolder());
		const int nestingLevel = incrementScriptNestingLevel();
		AutoDecrementNestingLevel auto_decrement;

		if( nestingLevel > maxNestingLevel )
			throw CrException("Execution of the script '" + scriptName + "' would cause the nesting level to exceed the maximum "
				" of " + toString(maxNestingLevel) + " specified by the data element " + getMaxNestingLevelDataElementName() + ".");

		DETransform deTrans(pTreeMapper, lScriptId, pTrans);

		Debuggee* pDebuggee = pTreeMapper->getOperationContext()->getDebuggee();
		if ( pDebuggee ) {
			pDebuggee->getCallStack().push(
				new IntegrationBreakpointLocation(-1, -1, lScriptId, 0, "")
				);
		}
		AutoPopCallStack pop_stack(pDebuggee);

		string sWarnings;
		auto_ptr<DataElement> de( deTrans.runScript("", script, sWarnings) );		
		if ( !sWarnings.empty() )
			throw CrException(sWarnings);

		return de.release();
	}
}

DataElement* konga::RunScript(long scriptId, ScriptCache& scriptCache, TreeMapper* pTreeMapper, CTransform* pTrans) {
	string scriptName, script;
	scriptCache.getScript(scriptId, scriptName, script);
	return RunScriptImpl(pTreeMapper, script, scriptName, scriptId, pTrans);
}

DataElement* konga::RunScript(const string& stScriptGuid, ScriptCache& scriptCache, TreeMapper* pTreeMapper, CTransform* pTrans) {
	string scriptName, script;
	long scriptId = -1;
	scriptCache.getScript(stScriptGuid, scriptName, script, scriptId);
	return RunScriptImpl(pTreeMapper, script, scriptName, scriptId, pTrans);
}

