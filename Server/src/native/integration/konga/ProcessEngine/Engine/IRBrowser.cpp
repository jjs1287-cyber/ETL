/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                        
  
                                                                      

#if 0 && defined(DS_USE_CORBA) && !defined(TAO)

#include "IRBrowser.h"
#include <iostream>
#include <fstream>

namespace CORBA_IR {

                                                                      
                           
                                                                      

IRBrowser::IRBrowser(const std::string& repo_ior_location) : m_connected(false),
	Container("","",0)
{
	if( repo_ior_location.length() )
		Connect(repo_ior_location);
}

IRBrowser::~IRBrowser() {}

void IRBrowser::Connect(const std::string& repo_ior_location)
{
	if( m_connected ) return;

	CORBA_Object_var obj;
	try {
		std::ifstream ins;
		ins.open( repo_ior_location.c_str() );
		if( ins.fail() ) return;
		std::string ior;
		ins >> ior;
		ins.close();

		if( ior.length() == 0 )
		{
			                                                                                                     
			return;
		}

		obj = g_orb->string_to_object(ior.c_str());

		if( CORBA_is_nil(obj) )
		{
			                                                                                                                           
			return;
		}
	}
	catch (CORBA_SystemException&          ) {
		                                                                                                                                     
		return;
	} catch(...) {
		                                                                                                                             
		return;
	}

	try
	{
		m_object = CORBA_Repository::_duplicate( CORBA_Repository::_narrow(obj) );
		if( CORBA_is_nil(m_object) )
		{
			m_object = 0;
			return;
		}
	}
	catch (CORBA_SystemException& ) {
		return;
	} catch(...) {
		return;
	}

	m_connected = true;
}

Container* IRBrowser::Root()
{
	return this;
}

void Container::ReleaseContents()
{
	for(ContentsVector_it it=m_contents.begin(); it!=m_contents.end(); ++it)
		delete *it;
}

void Container::LoadContents()
{
	CORBA_Container_ptr container = CORBA_Container::_narrow(m_object);
	if( CORBA_is_nil(container) )
	{
		                             
		return;
	}
		
	CORBA_ContainedSeq* contSeq = container->contents(CORBA_dk_all, false);

	std::string type;

	for(CORBA_ULong i=0; i<(*contSeq).length(); ++i)
	{
		switch ((*contSeq)[i]->def_kind()) {
		case CORBA_dk_Attribute: {
			type = "attribute";
			break; }
		case CORBA_dk_Constant: {
			type = "constant";
			break; }
		case CORBA_dk_Exception: {
			type = "exception";
			break; }
		case CORBA_dk_Interface: {
			type = "interface";
			Interface* p = 
				new Interface((*contSeq)[i]->name(),
				(*contSeq)[i]->id(), CORBA_Contained::_duplicate((*contSeq)[i]) );
			m_contents.push_back( p );
			break; }
		case CORBA_dk_Module: {
			type = "module";
			Module* p = 
				new Module((*contSeq)[i]->name(),
					(*contSeq)[i]->id(), CORBA_Contained::_duplicate((*contSeq)[i]) );
			m_contents.push_back( p );
			break; }
		case CORBA_dk_Operation: {
			type = "operation";
			Operation* p = 
				new Operation((*contSeq)[i]->name(),
					(*contSeq)[i]->id(), CORBA_Contained::_duplicate((*contSeq)[i]) );
			m_contents.push_back( p );
			break; }
		case CORBA_dk_Typedef: {
			type = "typedef";
			break; }
		case CORBA_dk_Alias: {
			type = "alias";
			break; }
		case CORBA_dk_Struct: {
			type = "struct";
			break; }
		case CORBA_dk_Union: {
			type = "union";
			break; }
		case CORBA_dk_Enum: {
			type = "enum"; 
			break; }
		case CORBA_dk_Primitive: {
			type = "primitive";
			break; }
		case CORBA_dk_String: {
			type = "string";
			break; }
		case CORBA_dk_Sequence: {
			type = "sequence";
			break; }
		case CORBA_dk_Array: {
			type = "array";
			break; }
		case CORBA_dk_Repository: {
			type = "repository";
			break; }
		default: {
			continue; }
		}

		TRACE("type = %s\n", type.c_str());
	}
}

ContentsVector Container::Contents()
{
	if( m_contents.size() <= 0 )
		LoadContents();

	return m_contents;
}

void Container::Display(CTreeCtrl*ctrl, HTREEITEM*h_parent)
{
	if( m_contents.size() <= 0 )
		LoadContents();

	for(ContentsVector_cit it=m_contents.begin(); it!=m_contents.end(); ++it)
	{
		  
		                             
		  

		TRACE("\tName = %s\n", (*it)->Title().c_str());
		HTREEITEM h;
		if( h_parent ) h = ctrl->InsertItem((*it)->Title().c_str(), *h_parent);
		else h = ctrl->InsertItem((*it)->Title().c_str(), 0);
		(*it)->Display(ctrl,&h);
		ctrl->Expand(h,TVE_EXPAND);
	}
}

void Operation::Display(CTreeCtrl*ctrl, HTREEITEM*h_parent)
{
	CORBA_OperationDef_ptr op = CORBA_OperationDef::_narrow(m_object);
	if( CORBA_is_nil(op) )
	{
		                             
		return;
	}
	
	std::string result_type = CORBA_TypeKindToString(op->result()->kind());
	if( op->mode() == CORBA_OP_ONEWAY ) result_type += " [one way]";

	CORBA_ParDescriptionSeq* params = op->params();
	if( !params )
		return;

	std::string args;
	for(CORBA_ULong i=0; i<params->length(); ++i)
	{
		if( i )	args += ",";
		switch( (*params)[i].mode )
		{
		case CORBA_PARAM_IN: args += " [in] "; break;
		case CORBA_PARAM_OUT: args += " [out] "; break;
		case CORBA_PARAM_INOUT: args += " [in out] "; break;
		}
		args += CORBA_TypeKindToString((*params)[i].type->kind());
		args += ' ';
		args += (*params)[i].name;		
	}
	if( !params->length() ) args = "";

	std::string func_desc = result_type + "  " + Title() + '(' + args + ')' ;

	TRACE("%s\n", func_desc.c_str()); 
	                                                                            

	HTREEITEM h;
	if( h_parent ) h = ctrl->InsertItem(func_desc.c_str(), *h_parent);
	else h = ctrl->InsertItem(func_desc.c_str(), 0);
	ctrl->Expand(h,TVE_EXPAND);
}


};                      

#endif                
