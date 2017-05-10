package org.diff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

public class DiffAssert {

	public static void assertDiffList(String expected, List<Diff> res) {
		assertNotNull(res);
		assertEquals(expected, res.toString());
	}

	public static void assertResults(String[] res, String text1, String text2) {
		if (!res[0].equals(text1)) {
			System.out.println(res[0]);
			System.out.println(text1);
		}
		if (!res[1].equals(text2)) {
			System.out.println(res[1]);
			System.out.println(text2);
		}
		assertEquals("Wrong results for original text!", text1, res[0]);
		assertEquals("Wrong results for changed text!", text2, res[1]);
	}
	
	public static void doubleCheckResults(List<Diff> res, String original, String changed) {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		for (Diff d : res) {
			if (d.getOperation() == Operation.EQUAL) {
				sb1.append(d.getText());
				sb2.append(d.getText());
			} else if (d.getOperation() == Operation.DELETE) {
				sb1.append(d.getText());
			} else if (d.getOperation() == Operation.INSERT) {
				sb2.append(d.getText());
			}
			
		}

		assertEquals(original, sb1.toString());
		assertEquals(changed, sb2.toString());
	}
	
	public static void doubleCheckResults(String[] res, String original, String changed) {
		doubleCheckResults(res, original, changed, Context.makeDefaultCharContext());
	}
	
	public static void doubleCheckResults(String[] res, String original, String changed, Context context) {
		// is original == res original - bi
		String newOrg = res[0].replaceAll(context.getStartEqual(), "").replaceAll(context.getEndEqual(), "")
			.replaceAll(context.getStartInsert(), "").replaceAll(context.getEndInsert(), "")
			.replaceAll(context.getStartDelete(), "").replaceAll(context.getEndDelete(), "");
		
		if (!original.equals(newOrg)) {
			System.out.println(original);
			System.out.println(newOrg);
		}		
		assertEquals(original, newOrg);
		
		String newChn = res[1].replaceAll(context.getStartEqual(), "").replaceAll(context.getEndEqual(), "")
				.replaceAll(context.getStartInsert(), "").replaceAll(context.getEndInsert(), "")
				.replaceAll(context.getStartDelete(), "").replaceAll(context.getEndDelete(), "");
		
		if (!changed.equals(newChn)) {
			System.out.println(changed);
			System.out.println(newChn);
		}
		assertEquals(changed, newChn);
		
	}
}
