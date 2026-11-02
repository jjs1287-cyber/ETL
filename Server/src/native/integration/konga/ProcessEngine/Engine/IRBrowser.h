/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */


#if !defined(AFX_IRBROWSER_H__9D7B1F43_54EE_11D3_9A12_006008CE14ED__INCLUDED_)
#define AFX_IRBROWSER_H__9D7B1F43_54EE_11D3_9A12_006008CE14ED__INCLUDED_

#if 0 && defined(DS_USE_CORBA) && !defined(TAO)

#include <vector>

#include "corba_util.h"

namespace CORBA_IR
{

class Object;
class Container;

class Module;
class Interface;

class Operation;
class Attribute;
class Typedef;
class Exception;

typedef std::vector<Object*> ContentsVector;
typedef ContentsVector::iterator ContentsVector_it;
typedef ContentsVector::const_iterator ContentsVector_cit;

class Object
{
public:
	Object() {}
	  
	                                                                            
	                                         
	  
	Object(CORBA_Identifier title, CORBA_RepositoryId repo_id, CORBA_IRObject_ptr obj) :
		m_title(title), m_repo_id(repo_id), m_object(obj) {}

	virtual ~Object()
	{
		CORBA_release( m_object );
	}

	virtual std::string Title() { return m_title; }
	virtual std::string RepoId() { return m_repo_id; }

	virtual void Display(CTreeCtrl*ctrl, HTREEITEM*h_parent) = 0;

protected:
	CORBA_IRObject_ptr m_object;                                     

private:
	std::string m_title;
	std::string m_repo_id;
};

class Container : public Object
{
public:
	  
	                                                                                  
	  
	Container(CORBA_Identifier title, CORBA_RepositoryId repo_id, CORBA_IRObject_ptr obj) : 
	  Object(title, repo_id, obj) {}

	virtual ~Container() { ReleaseContents(); }

	virtual ContentsVector Contents();
	virtual void Display(CTreeCtrl*ctrl, HTREEITEM*h_parent);

protected:
	virtual void LoadContents();
	virtual void ReleaseContents();

	ContentsVector m_contents;

private:
};

  
                                                                          
  
class IRBrowser : public Container
{
public:
	IRBrowser(const std::string& repo_ior_location);
	virtual ~IRBrowser();

	                                                           
	void Connect(const std::string& repo_ior_location);

	                                                   
	Container* Root();

	bool IsConnected() { return m_connected; }

	std::string Title() { return Object::Title() + " -- repository"; }
	                                                              

protected:

private:
	bool m_connected;
	CORBA_Repository_ptr m_repo;
};


class Module : public Container
{														
public:
	Module(CORBA_Identifier title, CORBA_RepositoryId repo_id, CORBA_IRObject_ptr obj) :
	  Container(title, repo_id, obj) {}
	virtual ~Module() {}

	                                                                 
	                                                                                                     
};

class Interface : public Container
{
public:
	Interface(CORBA_Identifier title, CORBA_RepositoryId repo_id, CORBA_IRObject_ptr obj) :
	  Container(title, repo_id, obj) {}
	virtual ~Interface() {}

	                                                                    
	                                                                                                        
};

class Operation : public Object
{
public:
	Operation(CORBA_Identifier title, CORBA_RepositoryId repo_id, CORBA_IRObject_ptr obj) :
	  Object(title, repo_id, obj) {}

	virtual ~Operation() {}

	                                                                    
	virtual void Display(CTreeCtrl*ctrl, HTREEITEM*h_parent);
};



};                      

#endif                         
#endif                                                                              
