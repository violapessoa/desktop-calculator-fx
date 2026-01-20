/*
 * @topic T10173 Desktop Calculator v3
 * @brief class AstNode represents a building block for AST, the abstract syntax tree
 */
package prof_AST;

import java.util.HashMap;

public class AstNode {

    static public HashMap<String, Double> symbolTable = new HashMap<>();
    //---------------------------
    // constants
    //---------------------------

    public static final int NUMBER = 'N';
    public static final int END = 'Z';
    public static final int ERROR = 'E';
    public static final int IDENTIFIER = 'I';
    //---------------------------
    // data
    //---------------------------
    public int type;
    public String value;
    public AstNode leftNode;
    public AstNode rightNode;

    //---------------------------
    // constructors
    //---------------------------
    public AstNode(int type) {
        this.type = type;
        this.value = "";
        this.leftNode = null;
        this.rightNode = null;
    }//AstNode

    public AstNode(int type, String value) {
        this.type = type;
        this.value = value;
        this.leftNode = null;
        this.rightNode = null;
    }//AstNode

    //---------------------------
    // operations
    //---------------------------
    public void print(int level) {
        for (int idx = 0; idx < level; ++idx) {
            System.out.print(".");
        }
        System.out.println(value);
        if (leftNode != null) {
            leftNode.print(level + 1);
        }
        if (rightNode != null) {
            rightNode.print(level + 1);
        }
    }//print

    public double evaluate() {
        if (type == NUMBER) {
            return Double.parseDouble(value);
        } else if (type == '-' && leftNode == null) {
            //this is a unary minus
            return -rightNode.evaluate();
        } else if (type == '+' && leftNode == null) {
            //this is a unary plus
            return +rightNode.evaluate();
        } else if (type == '-') {
            //this is a binary minus
            return leftNode.evaluate() - rightNode.evaluate();
        } else if (type == '+') {
            //this is a binary plus
            return leftNode.evaluate() + rightNode.evaluate();
        } else if (type == '/') {
            double rightResult = rightNode.evaluate();
            //this is a binary division
            if (rightResult == 0.0) {
                throw new ArithmeticException("Cannot divide by zero!");
            }
            return leftNode.evaluate() / rightResult;
        } else if (type == '*') {
            //this is a binary multiplication
            return leftNode.evaluate() * rightNode.evaluate();
        } else if (type == '%') {
            //this is a binary modolo operation
            return leftNode.evaluate() % rightNode.evaluate();
        } else if (type == IDENTIFIER) {
            //checks if value for identifier exists 
            if (symbolTable.containsKey(value)) {
                return symbolTable.get(value);
            } else{
            throw new ArithmeticException ( "Undefined variable " + value + ".");
            }
        } else if (type == '=') {
            double rightValue = rightNode.evaluate();
            symbolTable.put(leftNode.value, rightValue);
            return rightValue;
        }
        return 0.0;
    }//evaluate

}//class AstNode
