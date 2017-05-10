package org.diff.gestalt;

import static org.junit.Assert.*;
import static org.diff.DiffAssert.assertDiffList;
import static org.diff.DiffAssert.assertResults;
import static org.diff.DiffAssert.doubleCheckResults;


import java.util.List;

import org.diff.Context;
import org.diff.Diff;
import org.diff.TextPart;
import org.junit.Test;

public class GestaltDiffImplTest {

	@Test
	public void testFindLongesEqualStringWithImaginaryMatrix_Equal() {
		TextPart res = GestaltDiffImpl.findLongesEqualStringWithImaginaryMatrix(null, "1234".toCharArray(), "1234".toCharArray());
		assertNotNull(res);
		assertEquals("1234", res.getText());
	}
	
	@Test
	public void testFindLongesEqualStringWithImaginaryMatrix_Equal_2() {
		TextPart res = GestaltDiffImpl.findLongesEqualStringWithImaginaryMatrix(null, "AB567".toCharArray(), "567".toCharArray());
		assertNotNull(res);
		assertEquals("567", res.getText());
	}
	
	@Test
	public void testFindLongesEqualStringWithImaginaryMatrix_Equal_3() {
		TextPart res = GestaltDiffImpl.findLongesEqualStringWithImaginaryMatrix(null, "567".toCharArray(), "AB567".toCharArray());
		assertNotNull(res);
		assertEquals("567", res.getText());
	}
	
	@Test
	public void testFindLongesEqualStringWithImaginaryMatrix_Equal_4() {
		TextPart res = GestaltDiffImpl.findLongesEqualStringWithImaginaryMatrix(null, "567CD".toCharArray(), "567".toCharArray());
		assertNotNull(res);
		assertEquals("567", res.getText());
	}
	
	@Test
	public void testFindLongesEqualStringWithImaginaryMatrix_Equal_5() {
		TextPart res = GestaltDiffImpl.findLongesEqualStringWithImaginaryMatrix(null, "567".toCharArray(), "567CD".toCharArray());
		assertNotNull(res);
		assertEquals("567", res.getText());
	}
	
	@Test
	public void testFindLongesEqualStringWithImaginaryMatrix_Equal_6() {
		TextPart res = GestaltDiffImpl.findLongesEqualStringWithImaginaryMatrix(null, "AB567CD".toCharArray(), "567".toCharArray());
		assertNotNull(res);
		assertEquals("567", res.getText());
	}
	
	@Test
	public void testFindLongesEqualStringWithImaginaryMatrix_Equal_7() {
		TextPart res = GestaltDiffImpl.findLongesEqualStringWithImaginaryMatrix(null, "567".toCharArray(), "AB567CD".toCharArray());
		assertNotNull(res);
		assertEquals("567", res.getText());
	}
	
	///////////////////
	@Test
	public void testDiffListChars_Equals() {
		String text1 = "123456";
		String text2 = "123456";
		
		List<Diff> res = GestaltDiffImpl.diffListChars(text1, text2);
		assertNotNull(res);
		assertTrue(res.size() > 0);
		doubleCheckResults(res, text1, text2);
		assertDiffList("[Diff [text=123456, operation=EQUAL]]", res);
	}
	
	@Test
	public void testDiffListChars_Insert() {
		String text1 = "1234567";
		String text2 = "1234AB567";
		
		List<Diff> res = GestaltDiffImpl.diffListChars(text1, text2);
		assertNotNull(res);
		assertTrue(res.size() > 0);
		doubleCheckResults(res, text1, text2);
		assertDiffList("[Diff [text=1234, operation=EQUAL], Diff [text=AB, operation=INSERT], Diff [text=567, operation=EQUAL]]", res);
	}
	
	@Test
	public void testDiffListChars_Delete() {
		String text1 = "1234AB567";
		String text2 = "1234567";
		
		List<Diff> res = GestaltDiffImpl.diffListChars(text1, text2);
		assertNotNull(res);
		assertTrue(res.size() > 0);
		doubleCheckResults(res, text1, text2);
		assertDiffList("[Diff [text=1234, operation=EQUAL], Diff [text=AB, operation=DELETE], Diff [text=567, operation=EQUAL]]", res);
	}
	
	@Test
	public void testDiffListChars_Change() {
		String text1 = "1234AB567";
		String text2 = "1234CD567";
		
		List<Diff> res = GestaltDiffImpl.diffListChars(text1, text2);
		assertNotNull(res);
		assertTrue(res.size() > 0);
		doubleCheckResults(res, text1, text2);
		assertDiffList("[Diff [text=1234, operation=EQUAL], Diff [text=AB, operation=DELETE], Diff [text=CD, operation=INSERT], Diff [text=567, operation=EQUAL]]", res);
	}
	
	/////////////////////////////

	@Test
	public void testDiffStrings_1() {
		String text1 = "wollen.";
        String text2 = "wollllen.";
        
		String[] res = GestaltDiffImpl.diffStrings(text1, text2, Context.makeDefaultCharContext());
		doubleCheckResults(res, text1, text2);
		assertResults(res, "wollen.", "wo<i>ll</i>llen.");
	}
	
	@Test
	public void testDiffWords_1() {
		String text1 = "The quick brown fox jumped over the lazy dog.";
        String text2 = "The quick yellow fox jumped over the well-bred dog.";
        
		String[] res = GestaltDiffImpl.diffStrings(text1, text2, Context.makeDefaultWordContext());
		doubleCheckResults(res, text1, text2);
		assertResults(res, "The quick <d>brown</d> fox jumped over the <d>lazy</d> dog.", "The quick <i>yellow</i> fox jumped over the <i>well-bred</i> dog.");
	}
}
