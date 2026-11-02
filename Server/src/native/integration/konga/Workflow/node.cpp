/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <sstream>	                             
#include <map>

#include "node.h"

using namespace std;
using namespace konga;

                    
namespace {
	const char* chOP_UNDEF		= "";
	const char* chOP_XOR		= "^";
	const char* chOP_NOT		= "~";
	const char* chOP_EQ			= "==";
	const char* chOP_LT			= "<";
	const char* chOP_LTEQ		= "<=";
	const char* chOP_GT			= ">";
	const char* chOP_GTEQ		= ">=";
	const char* chOP_MULTIPLY	= "*";
	const char* chOP_DIVIDE		= "/";
	const char* chOP_PLUS		= "+";
	const char* chOP_MINUS		= "-";
	const char* chOP_AND		= "&&";
	const char* chOP_OR			= "||";
	const char* chOP_ASSIGN		= "=";

	const char* chOP_PLUSASSIGN	= "+=";
	const char* chOP_MINUASSIGN	= "+=";
	const char* chOP_MULTASSIGN	= "*=";
	const char* chOP_DIVASSIGN	= "/=";

	                                         
	map<string, node::bool_op> initOperatorMap() {
		map<string, node::bool_op> m;
		m["&"] = node::op_and;
		m["&&"] = node::op_and;
		m["|"] = node::op_or;
		m["||"] = node::op_or;
		m["!"] = node::op_not;
		m["~"] = node::op_not;
		m["^"] = node::op_xor;

		m["=="] = node::op_eq;
		m["<"] = node::op_lt;
		m["<="] = node::op_lteq;
		m[">"] = node::op_gt;
		m[">="] = node::op_gteq;

		m["*"] = node::op_multiply;
		m["/"] = node::op_divide;
		m["+"] = node::op_plus;
		m["-"] = node::op_minus;

		m["="] = node::op_assign;
		m["+="] = node::op_plusassign;
		m["-="] = node::op_minusassign;
		m["*="] = node::op_multiplyassing;
		m["/="] = node::op_divideassign;

		return m;
	}

	                                         
	map<node::bool_op, const char*> initTokenMap() {
		map<node::bool_op, const char*> m;

		m[node::op_and] = chOP_AND;
		m[node::op_or] = chOP_OR;
		m[node::op_not] = chOP_NOT;
		m[node::op_xor] = chOP_XOR;

		m[node::op_eq] = chOP_EQ;
		m[node::op_lt] = chOP_LT;
		m[node::op_lteq] = chOP_LTEQ;
		m[node::op_gt] = chOP_GT;
		m[node::op_gteq] = chOP_GTEQ;

		m[node::op_multiply] = chOP_MULTIPLY;
		m[node::op_divide] = chOP_DIVIDE;
		m[node::op_plus] = chOP_PLUS;
		m[node::op_minus] = chOP_MINUS;

		m[node::op_assign] = chOP_ASSIGN;
		m[node::op_plusassign] = chOP_PLUSASSIGN;
		m[node::op_minusassign] = chOP_MINUASSIGN;
		m[node::op_multiplyassing] = chOP_MULTASSIGN;
		m[node::op_divideassign] = chOP_DIVASSIGN;

		return m;
	}

	map<string, node::bool_op> g_TokenOperatorMap = initOperatorMap();
	map<node::bool_op, const char*> g_OperatorTokenMap = initTokenMap();
}

node::bool_op node::get_bool_op(const std::string& stStringRep) {
	map<string, node::bool_op>::iterator it = g_TokenOperatorMap.find(stStringRep);
	if ( it != g_TokenOperatorMap.end() )
		return it->second;
	else
		return op_undefined;
}

bool node::is_unary(node::bool_op op) {
	switch(op) {
		case op_not:
			return true;
		default:
			return false;
	}
}

const char* node::op_str(bool_op op) {
	map<node::bool_op, const char*>::iterator it = g_OperatorTokenMap.find(op);
	if ( it != g_OperatorTokenMap.end() )
		return it->second;
	else
		return chOP_UNDEF;
}

bool node::is_complete() const {
	if ( m_op == op_undefined)
		return true;
	else if ( is_unary(m_op) )
		return m_children.size() == 1;
	else
		return m_children.size() == 2;
}


string node::toString(const string& indention           ) const {
	stringstream ret;
	ret << indention << "node(" << op_str() << "," << m_data << ")" << endl;
	for(int i = 0, n = m_children.size(); i < n; ++i ) {
		ret << m_children[i]->toString(indention + "  ");
	}
	return ret.str();
}

const char* node::op_str() const { 
	return op_str(m_op);
}

