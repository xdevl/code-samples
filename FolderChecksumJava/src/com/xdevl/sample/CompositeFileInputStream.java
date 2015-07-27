package com.xdevl.sample;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CompositeFileInputStream extends InputStream
{
	private File mFile ;
	private List<File> mFiles ;
	private InputStream mInputStream ;
	
	public CompositeFileInputStream(File file) throws FileNotFoundException
	{
		mFile=file ;
		if(file.isDirectory())
		{
			mFiles=new ArrayList<File>(Arrays.asList(file.listFiles())) ;
			Collections.sort(mFiles) ;
			mInputStream=nextInputStream() ;
		}
		else
		{
			mFiles=new ArrayList<File>() ;
			mInputStream=new FileInputStream(file) ;
		}
	}
	
	@Override
	public int read() throws IOException
	{
		int result=mInputStream==null?-1:mInputStream.read() ;
		if(result<0 && (mInputStream=nextInputStream())!=null)
			return read() ;
		else return result ;
	}
	
	protected String getRelativePath(File file)
	{
		return file.getAbsolutePath().substring(mFile.getAbsolutePath().length()) ;
	}
	
	protected InputStream nextInputStream() throws FileNotFoundException
	{
		if(!mFiles.isEmpty())
		{
			File nextFile=mFiles.remove(0) ;
			return new SequenceInputStream(
					new ByteArrayInputStream(getRelativePath(nextFile).getBytes()),
					new CompositeFileInputStream(nextFile)) ;
		}
		else return null ;
	}
	
	public static void main(String args[])
	{
		if(args.length!=1)
			System.out.println("Usage: checksum directory|file");
		else
		{
			System.out.println("Checksum of: "+args[0]) ;
			DigestInputStream digestInputStream=null ;
			try {
				MessageDigest messageDigest=MessageDigest.getInstance("MD5") ;
			    digestInputStream=new DigestInputStream(new CompositeFileInputStream(new File(args[0])),messageDigest) ;
			    while(digestInputStream.read()>=0) ;
			    
			    System.out.print("\nmd5 sum=") ;
			    for(byte b: messageDigest.digest())
			    	System.out.print(String.format("%02x",b)) ;
			    
			} catch(IOException | NoSuchAlgorithmException e) {
				e.printStackTrace() ;
			} finally {
				if(digestInputStream!=null) try {digestInputStream.close();} catch (IOException e) {}
			}
		}
	}
}
