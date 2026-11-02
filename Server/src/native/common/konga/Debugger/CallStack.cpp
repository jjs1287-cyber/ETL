/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/AutoPtrCleanup.h>

#include "CallStack.h"

using namespace konga;
using std::vector;

CallStack::~CallStack(void) {
	while( top() ) { pop(); }
	AutoPtrDeleteVector<BreakpointLocation> delete_these(m_trash);
}

const vector<BreakpointLocation*>& CallStack::getBreakpointLocations() const {
	return m_stack;
}

void CallStack::push(BreakpointLocation* bl) {
	if ( bl == 0 ) return;
	m_stack.push_back(bl);
}

BreakpointLocation* CallStack::top() const {
	if ( m_stack.empty() ) return 0;
	return *(m_stack.end()-1);
}

void CallStack::pop() {
	if ( m_stack.empty() ) return;

	vector<BreakpointLocation*>::iterator it = m_stack.end()-1;
	m_trash.push_back(*it);
	m_stack.erase(it);
}

