/*
 * @topic T10174 Desktop Calculator v3
 * @brief class Lexer parses input tokens and assembles the AST
 */
package prof_AST;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {

    HashMap<String, Double> symbolTable;
    //---------------------------
    // data
    //---------------------------
    TokenStream tokenStream;

    //---------------------------
    // constructors
    //---------------------------
    public Lexer(StringBuilder text, HashMap<String, Double> symbolTable) {
        tokenStream = new TokenStream(text);
        this.symbolTable = symbolTable;
    }//Lexer

    //---------------------------
    // operations
    //---------------------------
    // starting grammar rule
    public AstNode startRule() {
        tokenStream.tokenizeInput();
        //tokenStream.debugPrintTokens();
        tokenStream.begin();
        return expressionRule();
    }//startRule

    private AstNode expressionRule() {
        if (tokenStream.isEnd()) {
            // nothing to do
            return new AstNode(AstNode.END);
        }

        AstNode left = termRule();
        if (left.type == AstNode.ERROR) {
            return left;
        }
        // since Term advances to the next token, check for end-of-stream:
        if (tokenStream.isEnd()) {
            // end-of-stream is reached,
            // the input is fully consumed:
            return left;
        }

        while (tokenStream.isString("+") || tokenStream.isString("-")) {
            // construct the fork node
            AstNode fork = new AstNode(
                    tokenStream.getString().charAt(0), // + or -
                    tokenStream.getString()
            );
            tokenStream.next();
            if (tokenStream.isEnd()) {
                return new AstNode(
                        AstNode.ERROR,
                        "Error: (" + fork.value + ") is a binary operator"
                );
            }
            fork.leftNode = left;
            left = fork; // prepare to return the left node to the caller
            AstNode right = termRule();
            if (right.type == AstNode.ERROR) {
                return right;
            }
            fork.rightNode = right;
        }//while

        if (!tokenStream.isEnd() && !tokenStream.isString(")")) {
            // closing parenthesis: let caller consume this token:
            return new AstNode(
                    AstNode.ERROR,
                    "Error: (" + tokenStream.getString() + ") is unexpected"
            );
        }

        return left;
    }//expressionRule

    private AstNode termRule() {
        if (tokenStream.isEnd()) {
            // something is missing....
            return new AstNode(
                    AstNode.ERROR,
                    "Error: unexpected end of input"
            );
        }

        AstNode left = primaryRule();
        if (left.type == AstNode.ERROR) {
            return left;
        }

        // since Primary rule advances to next token, check for end-of-stream:
        if (tokenStream.isEnd()) {
            // end-of-stream is reached,
            // the input is fully consumed:
            return left;
        }

        while (tokenStream.isString("*")
                || tokenStream.isString("/")
                || tokenStream.isString("%")) {
            // construct the fork node
            AstNode fork = new AstNode(
                    tokenStream.getString().charAt(0), // * or / or %
                    tokenStream.getString()
            );
            tokenStream.next();
            if (tokenStream.isEnd()) {
                return new AstNode(
                        AstNode.ERROR,
                        "Error: (" + fork.value + ") is a binary operator"
                );
            }
            fork.leftNode = left;
            left = fork; // prepare to return the left node to the caller
            AstNode right = primaryRule();
            if (right.type == AstNode.ERROR) {
                return right;
            }
            fork.rightNode = right;
        }//while

        return left;
    }//termRule

    private AstNode primaryRule() {
        if (tokenStream.isEnd()) {
            return new AstNode(
                    AstNode.ERROR,
                    "Error: unexpected end of input"
            );
        }
        // New code to handle variables and variable assignment:
        if (tokenStream.isIdentifier()) {
            AstNode primary = new AstNode(AstNode.IDENTIFIER, tokenStream.getString());
            tokenStream.next();
            if (tokenStream.isEnd()) {
                // nothing else left in the input stream
                return primary;
            }
            if (tokenStream.isString("=")) {
                AstNode fork = new AstNode(
                        tokenStream.getString().charAt(0), // =
                        tokenStream.getString()
                );
                tokenStream.next();
                if (tokenStream.isEnd()) {
                    return new AstNode(
                            AstNode.ERROR,
                            "Error: bad (" + fork.value + ") operator syntax"
                    );
                }
                fork.leftNode = primary;
                fork.rightNode = expressionRule();
                return fork;
            }
            return primary;
        }
        if (tokenStream.isNumber()) {
            AstNode primary = new AstNode(AstNode.NUMBER, tokenStream.getString());
            tokenStream.next();
            return primary;

        }

        if (tokenStream.isString("-") || tokenStream.isString("+")) {
            // unary + or -
            // construct the fork node
            AstNode fork = new AstNode(
                    tokenStream.getString().charAt(0), // + or -
                    "unary" + tokenStream.getString()
            );
            tokenStream.next();
            if (tokenStream.isEnd()) {
                return new AstNode(
                        AstNode.ERROR,
                        "Error: bad (" + fork.value + ") operator"
                );
            }
            fork.rightNode = primaryRule();
            return fork;
        }

        if (tokenStream.isString("(")) {
            // this is a sub-expression: (expr)
            tokenStream.next();
            if (tokenStream.isEnd()) {
                return new AstNode(
                        AstNode.ERROR, "Error: bad grouping syntax (");
            }
            AstNode primary = expressionRule();
            if (primary.type == AstNode.ERROR) {
                return primary;
            }
            if (!tokenStream.isString(")")) {
                // make sure ) is present in the stream
                return new AstNode(
                        AstNode.ERROR, "Error: missing ) parenthesis");
            }
            tokenStream.next(); // consume the closing parenthesis )
            return primary;
        }
        return new AstNode(
                AstNode.ERROR, "Error: bad input (" + tokenStream.getString() + ")"
        );
    }//primaryRule
}//class Lexer

/*
 This lexer will recognize the following rules
 for interpreting arithmetic expressions:

 Expression:
    Term
    Term "+" Term
    Term "-" Term

 Term:
    Primary
    Primary "*" Primary
    Primary "/" Primary
    Primary "%" Primary

 Primary:
    Number
    "-" Number
    "+" Number
    "(" Expression ")"
    Name
    Name "=" Expression

 */
