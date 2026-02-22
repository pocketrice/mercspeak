package no.mincci.mercspeak;

public class UnreachableException extends Exception{
    public UnreachableException() {
        super("Unreachable switch branch");
    }
}
