package edu.kit.ipd.pronat.action_analyzer;

public enum BetweenRole {

	//From predicates to other predicates in order of presence in the sentence
	NEXT_ACTION,

	//From every predicate to its actor and parameter
	PREDICATE_TO_PARA,

	//Between the tokens in an predicate/actor/parameter
	INSIDE_CHUNK

}
