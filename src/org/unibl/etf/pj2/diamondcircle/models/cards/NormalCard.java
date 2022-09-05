package org.unibl.etf.pj2.diamondcircle.models.cards;

public class NormalCard extends Card{
    private static final  String CARD_IMAGE_PATH_PREFIX =  "src/resources/img/card";
    private final int numOfFields;

    public NormalCard(int numOfFields){
        super(String.format(CARD_IMAGE_PATH_PREFIX+"%d.png",numOfFields));
        this.numOfFields = numOfFields;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + " "+numOfFields;
    }

    public int getNumOfFields() {
        return numOfFields;
    }
}
