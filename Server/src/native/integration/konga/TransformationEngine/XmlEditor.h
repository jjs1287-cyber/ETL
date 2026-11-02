/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef __XMLEDITOR_H_
#define __XMLEDITOR_H_

#include <string>
#include <vector>
#include <stack>
#include <map>

#include "XPathSearch.h"


namespace konga {

enum XCMD {							                       
                                                                         
	XCMD_open,						                                    
	XCMD_close,						   
	XCMD_define,					                
	XCMD_save_nodes,				     
	XCMD_save_values,				     
	XCMD_write_nodes,				                              
	XCMD_write_values,				                              
	XCMD_write_all,					                              
	XCMD_delete,					          
	XCMD_add,						                                          
	XCMD_update,					                                          
	XCMD_insert,					                                          
	XCMD_add_file,					                                    
	XCMD_update_file,				                                    
	XCMD_insert_file,				                                    
	XCMD_select,					        
	XCMD_end
};

                                                                                    
                                                                                     
                                                              
                                                                                                        
                                                              
                                                                                                                   
                                                                                  

          
  
               
                                                                    
                                                       
                                     
                                                
                                                                 
                                                  
                      
                                                                                                        
                                                   
                                                      
                                                         
                      
                                                             
                                                 
                      
                                                             
                                                 
                      
                                                          
                                                            
                                                         
                                                                
                                                     
                                                     
                                                        
                      
                    
  

class XmlEditorCommand {
	friend class XmlEditor;

protected:
	XCMD command;
	std::string xpath;
	std::string nodeString;
	XmlEditorCommand(XCMD command0, const char* xpath0, const std::string& nodeString0) :
		command(command0),  xpath(xpath0), nodeString(nodeString0)
	{}
};

class XmlEditor
{
	friend class XmlEditorCommand;
protected:
	XPathSearch*reader;
	std::vector<XmlEditorCommand*> command_list;
	std::stack<XPathSearch*>reader_stack;
	std::map<std::string, std::string> dataelement_map;
	std::stack<std::vector<JbNode*>*>node_list_stack;
	std::vector<JbNode*>* node_list;

public:
	XmlEditor();
	~XmlEditor();

	void exec(XCMD cmd, const char* xpath="", const std::string& nodeString="");

protected:
	void push(){
		if(reader!=0){
			reader_stack.push(reader);
			node_list_stack.push(node_list);
		}
	}
	void openReader(const std::string& nodeString){
		push();
		std::string fileName=eval(nodeString.c_str());
		reader=new XPathSearch(fileName);
		node_list=new std::vector<JbNode*>();
	}

	void closeReader(){
		delete reader;
		delete node_list;
		if(reader_stack.size()>0){
			reader=reader_stack.top();
			reader_stack.pop();
			node_list=node_list_stack.top();
			node_list_stack.pop();
		}
		else {
			reader=0;
			node_list=0;
		}
	}

private:
	void cleanup();
	std::string eval(const char*expr);
};

}                       

#endif                  
