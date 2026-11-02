/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_DOMPROCESSINGINSTRUCTION_PTR_H
#define KONGA_XML_DOMPROCESSINGINSTRUCTION_PTR_H

#include "DOMNodePtr.h"

namespace XERCES_CPP_NAMESPACE {
	class DOMProcessingInstruction;
}

namespace konga {             
    namespace xml {

    	class DOMProcessingInstructionPtr : public DOMNodePtr
    	{
    	public:
            DOMProcessingInstructionPtr(XERCES_CPP_NAMESPACE::DOMProcessingInstruction* pi, const RCDocPtr& doc);
            DOMProcessingInstructionPtr(int dummy = 0);
    		virtual ~DOMProcessingInstructionPtr(void);
    
			const DOMProcessingInstructionPtr* operator->() const { return this; }
			DOMProcessingInstructionPtr* operator->() { return this; }
    
    	private:
    		XERCES_CPP_NAMESPACE::DOMProcessingInstruction* m_pxPI;
    	};
    }
}
#endif                                            

