package test.java;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.Process;

public class TestProcess {
	
private Process process;
	
	@Before
	public void setUp() throws Exception {
		process = new Process();
	}

	@After
	public void tearDown() throws Exception {
		process = null;
	}
	
	@Test
	public void getzipCodes() {
		assertNotNull(process);
		assertNotNull( process.zipCodeMap());
	}
	@Test
	public void readXML() {
		assertNotNull(process);
		 process.readXML();
	}
}
