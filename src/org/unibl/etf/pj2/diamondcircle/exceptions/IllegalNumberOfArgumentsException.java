package org.unibl.etf.pj2.diamondcircle.exceptions;

public class IllegalNumberOfArgumentsException extends Exception {
    public IllegalNumberOfArgumentsException() {
        this("Nekorektan broj argumenata programa.");
    }

    public IllegalNumberOfArgumentsException(String msg) {
        super(msg);
    }
}
