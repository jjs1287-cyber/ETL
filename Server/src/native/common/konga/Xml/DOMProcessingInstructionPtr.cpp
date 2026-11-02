/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <xercesc/dom/DOMProcessingInstruction.hpp>

#include "DOMProcessingInstructionPtr.h"

XERCES_CPP_NAMESPACE_USE

using namespace konga;

xml::DOMProcessingInstructionPtr::DOMProcessingInstructionPtr(DOMProcessingInstruction* pi, const RCDocPtr& doc)
	: DOMNodePtr(pi, doc), m_pxPI(pi) { }

xml::DOMProcessingInstructionPtr::DOMProcessingInstructionPtr(int dummy)
    : DOMNodePtr(0), m_pxPI(0) { }

xml::DOMProcessingInstructionPtr::~DOMProcessingInstructionPtr(void) { }

