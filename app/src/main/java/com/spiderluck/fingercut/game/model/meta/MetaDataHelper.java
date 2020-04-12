package com.spiderluck.fingercut.game.model.meta;



public class MetaDataHelper {

	public interface MetaDataObserver {
		void onMetaDataChanged(MetaDataMsg.Reason reason, MetaData metaData);
	}

	private MetaData metaData;

	private MetaDataObserver observer;

	public MetaDataHelper() {
		this.metaData = new MetaData();
	}

	public MetaDataHelper(MetaData metaData) {
		this.metaData = metaData;
	}

	public void setObserver(MetaDataObserver observer) {
		this.observer = observer;

		observer.onMetaDataChanged(MetaDataMsg.Reason.Init, metaData);
	}

	public void levelUp() {
		metaData = metaData.levelUp();

		observer.onMetaDataChanged(MetaDataMsg.Reason.LevelUp, metaData);
	}

	public void addScore(int add) {
		metaData = metaData.addScore(add);

		observer.onMetaDataChanged(MetaDataMsg.Reason.ScoreChanged, metaData);
	}

	public void die() {
		metaData = metaData.die();

		if (metaData.isFinished()) {
			observer.onMetaDataChanged(MetaDataMsg.Reason.GameOver, metaData);
		} else {
			observer.onMetaDataChanged(MetaDataMsg.Reason.LivesDecreased, metaData);
		}
	}

	public MetaData getMetaData() {
		return metaData;
	}
}
