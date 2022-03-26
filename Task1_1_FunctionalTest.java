package st;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Before;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Task1_1_FunctionalTest {
	private Parser parser;
	@Before
	public void initialize() {
		parser = new Parser();
	}
	
	// [Bug #2 - Easy, 1PT]
	@Test
	public void test2() {
		parser.addOption(new Option("abc", Type.BOOLEAN), "a");
		parser.parse("--abc=0");
		assertFalse(parser.getBoolean("abc"));
	}
	
	// [Bug #10 - Easy, 1PT]
	@Test
	public void test3() {
		parser.addOption(new Option("a", Type.CHARACTER));
		assertEquals(parser.getCharacter("a"),('\0'));
	}
	// [Bug #4 - Medium, 2PTS]
	@Test
	public void test4() {
		parser.addOption(new Option("abc", Type.INTEGER), "aaaaaaaaaaaaaaaaaaaa");
		assertTrue(parser.shortcutExists("aaaaaaaaaaaaaaaaaaaa"));
	}
	
	// [Bug #17 - Hard, 3PTS]
	@Test
	public void test5() {
		parser.addOption(new Option("abcdefghijklmnopqrstuvwxyzabcdefghijk", Type.STRING));
		assertEquals(parser.getString("abcdefghijklmnopqrstuvwxyzabcdefghijk"),"");
	}
	// [Bug #1 - Easy, 1PT] & [Bug #7 - Hard, 3PTS]
	@Test
	public void test6() {
		parser.addOption(new Option("abc", Type.STRING));
		parser.parse("--abc=5");
		assertEquals(parser.getInteger("abc"), 5);
	}
	
	// [Bug #11 - Hard, 3PTS], [Bug #1 - Easy, 1PT], [Bug #7 - Hard, 3PTS]
	@Test(expected = IllegalArgumentException.class)
	public void test7() {
		parser.addOption(new Option("abc#", Type.STRING));
	}
	
	// [Bug #1 - Easy, 1PT], [Bug #7 - Hard, 3PTS], [Bug #15 - Medium, 2PTS]
	@Test
	public void test8() {
		parser.addOption(new Option("abc", Type.STRING));
		parser.parse("--abc=21474836475");
		assertEquals(parser.getInteger("abc"), 0);
	}
	
	// [Bug #1 - Easy, 1PT] [Bug #3 - Medium, 2PTS]
	@Test
	public void test9() {
		parser.addOption(new Option("abc", Type.BOOLEAN));
		parser.parse("--abc=qwertyuiop");
		assertEquals(parser.getInteger("abc"), 1);
	}
	
	// [Bug #1 - Easy, 1PT], [Bug #14 -  Hard, 3PTS]
	@Test
	public void test10() {
		parser.addOption(new Option("abc", Type.BOOLEAN));
		parser.parse("--abc=\\n");
		assertEquals(parser.getString("abc"), "\\n");
	}
	
	// [Bug #1 - Easy, 1PT] [Bug #18 - Easy, 1PT]
	@Test
	public void test11() {
		parser.addOption(new Option("abc", Type.STRING));
		parser.parse("--abc= Old");
		parser.replace("abc       ", "Old", "New");
		assertEquals(parser.getString("abc"), "New");
	}
	
	// [Bug #12 - Hard, 3PTS]
	@Test
	public void test12() {
		parser.addOption(new Option("opt", Type.STRING), "opt");
		parser.parse("--opt= Old");
		parser.replace("-opt ", "Old", "New");
		assertEquals(parser.getString("opt"), "New");
	}
	
	// [Bug #1 - Easy, 1PT] [Bug #9 - Easy, 1PT]
	@Test
	public void test13() {
		parser.addOption(new Option("w", Type.INTEGER));
		assertEquals(parser.parse(" "), 0);
	}
	
	// [Bug #13 - Medium, 2PTS], [Bug #20 - Hard, 3PTS]
	@Test
	public void test14() {
		parser.addOption(new Option("z", Type.STRING));
		parser.parse("--z == '='");
		assertEquals(parser.getString("z"), "==");
	}
	
	
	// [Bug #19 - Medium, 2PTS]
	@Test
	public void test16() {
		parser.addOption(new Option("b", Type.STRING),"b");
		parser.parse("b=\" -\"");
		assertEquals(parser.getString("b"), " -");
	}
	
	// [Bug #8 - Medium, 2PTS]
	@Test
	public void test17() {
		parser.addOption(new Option("d", Type.BOOLEAN), "d");
		parser.addOption(new Option("d", Type.INTEGER), "e");
		assertTrue(parser.shortcutExists("e"));
	}
	
	// [Bug #6 - Easy, 1PT]
	@Test
	public void test18() {
		Option opt1 = new Option("f", Type.BOOLEAN);
		Option opt2 = new Option("h", Type.BOOLEAN);
		assertFalse(opt1.equals(opt2));
	}
	
	// [Bug #5 - Medium, 2PTS]
	@Test
	public void test19() {
		parser.addOption(new Option("neg", Type.INTEGER));
		parser.parse("--neg=-1");
		assertEquals(parser.getInteger("neg"), -1);
	}
	
	// [Bug #16 - Medium, 2PTS]
	@Test(expected = NullPointerException.class)
	public void test20() {
		parser.getString(null);
	}

}
