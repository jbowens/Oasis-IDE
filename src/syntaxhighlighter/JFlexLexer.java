package camel.syntaxhighlighter;

import java.io.Reader;
import java.util.List;
import java.io.IOException;

public abstract class JFlexLexer {

    protected int tokenStart;
    protected int tokenLength;
    protected int offset;

    protected Token token(TokenType type, int tStart, int tLength,
            int newStart, int newLength) {
        tokenStart = newStart;
        tokenLength = newLength;
        return new Token(type, tStart + offset, tLength);
    }

    protected Token token(TokenType type, int start, int length) {
        return new Token(type, start + offset, length);
    }

    protected Token token(TokenType type) {
        return new Token(type, yychar() + offset, yylength());
    }

    protected Token token(TokenType type, int pairValue) {
        return new Token(type, yychar() + offset, yylength(), (byte) pairValue);
    }

    public void parse(Reader reader, List<Token> tokens, int offset) {
        try {
            yyreset(reader);
            for (Token t = yylex(); t != null; t = yylex()) {
                t.setStart(t.getStart() + offset);
                tokens.add(t);
            }
        } catch (IOException ex) {
        }
    }

    public abstract void yyreset(Reader reader);

    public abstract Token yylex() throws java.io.IOException;

    public abstract char yycharat(int pos);

    public abstract int yylength();

    public abstract String yytext();

    public abstract int yychar();
}
