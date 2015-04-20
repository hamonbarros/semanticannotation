package br.com.sann.ows.parser;

import junit.framework.TestCase;

public class WFSParserTest extends TestCase{
	
	private WFSParser wfsParser;
	
	protected void setUp() throws Exception {
		super.setUp();
		wfsParser = new WFSParser();
	}

	public void testExtractUrlService() {
		
		String url = "aaaaaaaa?bbbbbbb";
		assertEquals("aaaaaaaa", wfsParser.extractUrlService(url));
		
		url = "aaaaaaaa?";
		assertEquals("aaaaaaaa", wfsParser.extractUrlService(url));
		
		url = "aaaaaaaa";
		assertEquals("aaaaaaaa", wfsParser.extractUrlService(url));
		
		url = "";
		assertEquals("", wfsParser.extractUrlService(url));
		
		assertNull(wfsParser.extractUrlService(null));
	}
}
