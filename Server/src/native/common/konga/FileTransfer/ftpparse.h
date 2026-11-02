#ifndef FTPPARSE_H
#define FTPPARSE_H

  
                                                             

                                                     
                                                                         

                                                  

                                                                
                                       
                                                                
                                    
  

struct ftpparse {
  char *name;                                   
  int namelen;
  int flagtrycwd;                                                    
  int flagtryretr;                                                     
  int sizetype;
  long size;                       
  int mtimetype;
  time_t mtime;                        
  int idtype;
  char *id;                                   
  int idlen;
} ;

#define FTPPARSE_SIZE_UNKNOWN 0
#define FTPPARSE_SIZE_BINARY 1                                             
#define FTPPARSE_SIZE_ASCII 2                                             

#define FTPPARSE_MTIME_UNKNOWN 0
#define FTPPARSE_MTIME_LOCAL 1                      
#define FTPPARSE_MTIME_REMOTEMINUTE 2                                     
#define FTPPARSE_MTIME_REMOTEDAY 3                                            
  
                                                                  
                                                                     
                                                                       
  

#define FTPPARSE_ID_UNKNOWN 0
#define FTPPARSE_ID_FULL 1                                                     

                                                                                                   
                                   
int kftpparse(struct ftpparse *,char *,int);

#endif

