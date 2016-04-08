import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 */

/**
 * @author kilt
 *
 */
public class MyHelloWorldTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Start");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("End");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("Before");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("After");
	}

	@Test
	public void test0() {
		int test = 3 + 4;
		assertEquals(7, test);
		
		assertEquals("Test whether numbers are equal", 7, test);
	}
	
	@Test
	public void test1() {
		int test = 3 + 4;
		assertEquals(7, test);
		
		assertEquals("Test whether numbers are equal", 7, test);
	}

}
