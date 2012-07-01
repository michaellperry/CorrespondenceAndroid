package com.facetedworlds.factual.compiler.manifest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;

public class FactualManifest {

	private LinkedHashSet<URL> manfiestFactualFiles = new LinkedHashSet<URL>();
	
	public FactualManifest() {
		
	}
	
	private void addFactFile( URL newURL ) {
		manfiestFactualFiles.add(newURL);
	}
	
	public int getFactFileCount() { 
		return manfiestFactualFiles.size();
	}
	
	public Iterable<URL> getFactFileIterable() {
		return manfiestFactualFiles;
	}
	
	public static FactualManifest loadManifest( URL manifestURL ) throws IOException {
		
		FactualManifest returnManifest = new FactualManifest();
		
		InputStream inStream = null;
		URLConnection urlConnection = null;
		try {
			urlConnection = manifestURL.openConnection();
			
			urlConnection.setConnectTimeout(30000);
			urlConnection.setReadTimeout(30000);
			urlConnection.connect();
			
			inStream = urlConnection.getInputStream();
			BufferedReader inReader = new BufferedReader( new InputStreamReader(inStream) );
			
			boolean versionLineFound = false;
			String inputLine = null;
			while( ( inputLine = inReader.readLine() ) != null ) {
				
				inputLine = normalizeInputString(inputLine);
				
				// Skip lines that are empty
				if( inputLine.length() == 0 ) {
					continue;
				}
				
				if( inputLine.startsWith( "#" ) ) {
					// comment line...skip it.
					continue;
				}
				
				if( versionLineFound == false ) {
					// This must be the version information line.
					// "version 1.0"
					if( inputLine.startsWith( "version" ) ) {
						// Nothing to do with version yet.
						versionLineFound = true;
						continue;
					}
					
					// This is not a version line...set the flag so that the assumed url
					// will be handled
					versionLineFound = true;
				}
				
				if( versionLineFound == true ) {
					
					String nextFactFile = inputLine;
					
					URL nextFactFileURL = null; 
					if( nextFactFile.indexOf( ':' ) != -1 ) {
						nextFactFileURL = new URL( nextFactFile );
					}
					else {
						nextFactFileURL = toURL( URLUtilities.parentDirectory(manifestURL), nextFactFile );
					}
					
					returnManifest.addFactFile(nextFactFileURL);
				}
			}
		}
		finally {
			if( inStream != null ) {
				try { inStream.close(); } catch( IOException e ) {}
			}
		}
		
		return returnManifest;
	}
	
	public static FactualManifest loadManifest( File manifestFile ) throws IOException {
		try {
			return loadManifest(manifestFile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String normalizeInputString( String inputString ) {
		
		inputString = inputString.replace( '\t', ' ' );
		inputString = inputString.replace( '\r' , ' ' );
		inputString = inputString.replace( '\n' , ' ' );
		inputString = inputString.trim();
		
		return inputString;
	}
	
	private static URL toURL( URL parentURL , String nextFactFileReference ) throws MalformedURLException {
		
		URL returnURL = URLUtilities.buildURL(parentURL, nextFactFileReference);
		
		return returnURL;
	}
}
