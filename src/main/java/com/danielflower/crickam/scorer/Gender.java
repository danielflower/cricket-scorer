package com.danielflower.crickam.scorer;


public enum Gender {

	FEMALE("female", "she", "her", "hers"),
	MALE("male", "he", "him", "his"),
	OTHER("other", "they", "them", "theirs");


	private final String possessivePronoun;
	private final String name;
	private final String subjectPronoun;
	private final String objectPronoun;

	Gender(String name, String subjectPronoun, String objectPronoun, String possessivePronoun) {
		this.possessivePronoun = possessivePronoun;
		this.name = name;
		this.subjectPronoun = subjectPronoun;
		this.objectPronoun = objectPronoun;
	}

	public String noun() {
		return name;
	}

	public String subjectPronoun() {
		return subjectPronoun;
	}

	public String objectPronoun() {
		return objectPronoun;
	}

	public String possessivePronoun() {
		return possessivePronoun;
	}
}
