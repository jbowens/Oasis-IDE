package camel.interactions;

/*
 * Thrown when the program is unable to connect with the OCaml
 * REPL, and so Interactions are impossible to support. It would
 * be reasonable to kill execution when this exception is thrown.
 */
public class InteractionsUnavailableException extends Exception { }
