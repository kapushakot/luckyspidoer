package com.spiderluck.fingercut.game.model.meta;



public class MetaDataMsg {
	public enum Fields {
		Reason,
		Data
	}

	public enum Reason {
		Init,
		LivesDecreased,
		ScoreChanged,
		LevelUp,
		GameOver
	}
}
