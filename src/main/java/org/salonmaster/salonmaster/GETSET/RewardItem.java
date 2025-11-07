package org.salonmaster.salonmaster.GETSET;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RewardItem {

    private final StringProperty custnum;
    private final IntegerProperty reward;

    public RewardItem(String custnum, int reward) {
        this.custnum = new SimpleStringProperty(custnum);
        this.reward = new SimpleIntegerProperty(reward);
    }

    public String getCustnum() {
        return custnum.get();
    }

    public void setCustnum(String custnum) {
        this.custnum.set(custnum);
    }

    public int getReward() {
        return reward.get();
    }

    public void setReward(int reward) {
        this.reward.set(reward);
    }

    public StringProperty custnumProperty() {
        return custnum;
    }

    public IntegerProperty rewardProperty() {
        return reward;
    }
}
