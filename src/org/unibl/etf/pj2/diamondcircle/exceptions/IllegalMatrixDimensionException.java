package org.unibl.etf.pj2.diamondcircle.exceptions;

public class IllegalMatrixDimensionException extends Exception{
    public IllegalMatrixDimensionException(){
        this("Nekorektna dimenzija matrice.");
    }
    public IllegalMatrixDimensionException(String msg){
        super(msg);
    }
}
