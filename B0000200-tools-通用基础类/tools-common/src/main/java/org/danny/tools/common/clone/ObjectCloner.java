package org.danny.tools.common.clone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectCloner {
	
	/**
	 * 深度clone，把对象以及它的关系对象全部复制
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Object deepClone(Object obj) throws Exception{   
	       ByteArrayOutputStream  byteOut = new ByteArrayOutputStream();     
	       ObjectOutputStream out = new ObjectOutputStream(byteOut);     
	       out.writeObject(obj);            
	       ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());     
	       ObjectInputStream in =new ObjectInputStream(byteIn);           
	       return in.readObject(); 
	}

}
