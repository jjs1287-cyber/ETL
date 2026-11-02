package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class S3Location extends FileLocation {
 public S3Location(){super(DataLocationType.AmazonS3);}
 public static String[] getPropertyKeys(){return org.etlforge.lang.Arrays2.append(FileLocation.getPropertyKeys(),new String[]{"locator","path","s3_region","s3_authentication","s3_access_key_protected","s3_secret_key_protected","s3_session_token_protected","s3_recursive","s3_overwrite"});}
 public String getBucket(){return getProperty("locator","");}
 public void setBucket(String value){setStringProperty("locator",value.trim());}
 public String getRegion(){return getProperty("s3_region","us-east-1");}
 public void setRegion(String value){setStringProperty("s3_region",value.trim());}
 public String getPrefix(){return getProperty("path","");}
 public void setPrefix(String value){setStringProperty("path",value);}
 public String getAuthentication(){return getProperty("s3_authentication","access_key");}
 public void setAuthentication(String value){setStringProperty("s3_authentication",value);}
 public String getCredential(String key){Optional<Password> p=PasswordUtils.getPassword(this,key);return p.isPresent()?p.get().clearText():"";}
 public void setCredential(String key,String value){PasswordUtils.setPassword(this,key,value.isEmpty()?Optional.<Password>absent():Optional.of(Password.encrypt(value)));}
 @Override public void collectValidationMessages(ValidationMessageCollector messages){
  super.collectValidationMessages(messages);
  if(!getBucket().matches("[a-z0-9][a-z0-9.-]{1,61}[a-z0-9]")||getBucket().contains("..")||getBucket().matches("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+"))messages.addMessage("Enter an Amazon S3 bucket name, without s3:// or a URL.",ValidationStatus.INVALID);
  if(!getRegion().matches("[a-z]{2}(-[a-z]+)+-[0-9]+"))messages.addMessage("Enter an AWS region, for example us-east-1.",ValidationStatus.INVALID);
  if(!getAuthentication().equals("access_key")&&!getAuthentication().equals("temporary"))messages.addMessage("Select an S3 authentication method.",ValidationStatus.INVALID);
  if(getCredential("s3_access_key_protected").isEmpty()||getCredential("s3_secret_key_protected").isEmpty())messages.addMessage("Enter the S3 access key ID and secret access key.",ValidationStatus.INVALID);
  if(getAuthentication().equals("temporary")&&getCredential("s3_session_token_protected").isEmpty())messages.addMessage("Enter the temporary AWS session token.",ValidationStatus.INVALID);
  if(!getRenameFileTo().getFileName().isEmpty()&&!willBeDeletedOnSuccess())messages.addMessage("S3 supports keeping or deleting source objects after success; renaming is not supported.",ValidationStatus.INVALID);
 }
}
