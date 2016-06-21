package test.java;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import main.java.Process;
import main.java.ValueComparator;

public class TestProcess {

	private Process process;
	private ValueComparator<String,Float> valueComparator;
	private HashMap<String,Float> map ;
	@Before
	public void setUp() throws Exception {
		process = new Process();
		map = new HashMap<String,Float>();
		valueComparator = new ValueComparator<String,Float>(map);
	}

	@After
	public void tearDown() throws Exception {
		process = null;
		valueComparator = null;
	}

	@Test
	public void getzipCodes() {
		assertNotNull(process);
		assertNotNull( process.zipCodeMap());
	}

	@Test
	public void sortMap() {
		assertNotNull(process);
		map.put("first", 50.23f);
		map.put("second", 35.23f);
		valueComparator = new ValueComparator<String,Float>(map);
		int i = valueComparator.compare("first","second");
		assertEquals(1,i);
	}

	@Test
	public void readXML() {
		assertNotNull(process);
		process.readXML();
	}

}
