package org.diff;

import java.util.Arrays;


public class Diff {
	
	private final char[] textChars;
	private final Operation operation;
	
	// optinal value if char represents word
	private final int realLength;

	protected Diff(char[] textChars, Operation operation, int realLength) {
		this.textChars = textChars;
		this.operation = operation;
		this.realLength = realLength;
	}
	
	protected Diff(char[] textChars, Operation operation) {
		this(textChars, operation, textChars.length);
	}
	
	public String getText() {
		return String.copyValueOf(textChars);
	}

	public char[] getTextChars() {
		return Arrays.copyOfRange(textChars, 0, textChars.length);
	}
	
	public Operation getOperation() {
		return operation;
	}

	public int getLength() {
		return textChars.length;
	}
	
	public int getRealLength() {
		return realLength;
	}

	public static Diff createNewSimpleDiff(char[] chars1, Operation operation) {
		return new Diff(chars1, operation);
	}

	@Override
	public String toString() {
		return "Diff [text=" + getText() + ", operation="
				+ getOperation() + "]";
	}
}
