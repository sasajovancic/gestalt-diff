package org.diff;


public class TextPart extends Diff {
	private final int start1;
	private final int start2;
	
	protected TextPart(int start1, int start2, char[] textChars, Operation operation, int realLength) {
		super(textChars, operation, realLength);
		this.start1 = start1;
		this.start2 = start2;
	}
	
	protected TextPart(int start1, int start2, char[] textChars, Operation operation) {
		super(textChars, operation);
		this.start1 = start1;
		this.start2 = start2;
	}
	
	public int getStart1() {
		return start1;
	}

	public int getStart2() {
		return start2;
	}

	public int getEnd1() {
		return start1 + getLength();
	}

	public int getEnd2() {
		return start2 + getLength();
	}
	
	public static TextPart createNewTextPart(int start1,
			int start2, char[] textChars, Operation operation) {
		return new TextPart(start1, start2, textChars, operation);
	}
	
	public static TextPart createNewTextPart(int start1,
			int start2, char[] textChars, Operation operation, int realLength) {
		return new TextPart(start1, start2, textChars, operation, realLength);
	}

	public static TextPart createNewEqualTextPart(int start1,
			int start2, char[] textChars) {
		return createNewTextPart(start1, start2, textChars, Operation.EQUAL);
	}
	
	public static TextPart createNewEqualTextPart(int start1,
			int start2, char[] textChars, int realLength) {
		return createNewTextPart(start1, start2, textChars, Operation.EQUAL, realLength);
	}

	public String toDetailString() {
		return "TextPart [start1=" + start1 + ", start2=" + start2
				+ ", text=" + getText() + ", operation="
				+ getOperation() + "]";
	}

	public boolean isLongerThan(TextPart other) {
		if (other == null) return true;
		return this.getRealLength() > other.getRealLength();
	}
}
