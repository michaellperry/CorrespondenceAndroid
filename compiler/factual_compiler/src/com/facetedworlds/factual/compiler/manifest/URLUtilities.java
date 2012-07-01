package com.facetedworlds.factual.compiler.manifest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLUtilities {

	public static URL parentDirectory( URL url ) throws MalformedURLException {
		
		String path = url.getPath();
		if( path.endsWith("/")) {
			path = path.substring(0,path.length()-1);
		}
		
		// Find the last slash
		int slashPos = path.lastIndexOf( '/' );
		if( slashPos == -1 ) {
			return null; // no parent available.
		}
		
		path = path.substring(0,slashPos);
		
		URL returnURL = new URL(url.getProtocol(), url.getHost(), url.getPort(), path );
		return returnURL;
	}
	
	public static URL createURL( String fileOrURLString ) {
		
		if( fileOrURLString == null || fileOrURLString.length() == 0 ) {
			throw new IllegalArgumentException();
		}
		
		int colonPosition = fileOrURLString.indexOf(':');
		if( colonPosition != -1 ) {
			try {
				URL returnURL = new URL( fileOrURLString );
				return returnURL;
			}
			catch( MalformedURLException e ) {
				
			}
		}
		
		File tempFile = new File( fileOrURLString );
		if( tempFile.exists() == true ) {
			
			try {
				return tempFile.toURI().toURL();
			} catch (MalformedURLException e) {
				return null;
			}
		}
		
		return null;
	}
	
	public static URL buildURL( URL parentURL , String absOrRelativeLocation ) throws MalformedURLException {
		
		if( absOrRelativeLocation.startsWith( "./" ) == true ) {
			absOrRelativeLocation = absOrRelativeLocation.substring(2);
			return buildURL(parentURL, absOrRelativeLocation);
		}
		
		if( absOrRelativeLocation.startsWith( "../" ) == true ) {
			absOrRelativeLocation = absOrRelativeLocation.substring(3);
			return buildURL(parentDirectory(parentURL), absOrRelativeLocation);
		}
		
		if( absOrRelativeLocation.startsWith( "/" ) == true ) {
			
			String tempPath = parentURL.getPath();
			int bangIndex = tempPath.indexOf('!');
			if( bangIndex != -1 ) {
				// Chop at the bang
				tempPath = tempPath.substring(0,bangIndex+1);
				tempPath += absOrRelativeLocation;
			}
			else {
				tempPath = absOrRelativeLocation;
			}
			
			URL returnURL = new URL( parentURL.getProtocol() , parentURL.getHost() , parentURL.getPort() , tempPath );
			return returnURL;
		}
				
		String newPath = parentURL.getPath() + "/" + absOrRelativeLocation;
		
		URL returnURL = new URL( parentURL.getProtocol() , parentURL.getHost() , parentURL.getPort() , newPath );
		return returnURL;
	}
	
	public static String loadURLData( URL factFileReference ) throws IOException {
		
		InputStream inStream = null;
		try {
			URLConnection urlConnection = factFileReference.openConnection();
			urlConnection.setConnectTimeout(30000);
			urlConnection.setReadTimeout(30000);
			urlConnection.connect();
			inStream = urlConnection.getInputStream();
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			
			int readCount = 0;
			while( ( readCount = inStream.read(buffer) ) > 0 ) {
				outStream.write( buffer , 0 , readCount );
			}
			
			return outStream.toString("UTF-8");
		}
		catch( IOException e ) {
			return null;
		}
		finally {
			if( inStream != null ) {
				try { inStream.close(); } catch( IOException e ) {}
			}
		}
	}
}
