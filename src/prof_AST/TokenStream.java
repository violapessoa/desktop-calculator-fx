/*
 * @topic T10172 Desktop Calculator v3
 * @brief class TokenStream tokenizes the input
 */
package prof_AST;

public class TokenStream {

    //---------------------------
    // data
    //---------------------------
    int tokenIdx;
    StringBuilder input;
    String[] tokens;

    //---------------------------
    // constructors
    //---------------------------
    public TokenStream(StringBuilder text) {
        input = text;
    }//TokenStream

    //---------------------------
    // operations
    //---------------------------
    public void tokenizeInput() {
        // insert spaces where necessary
        for (int idx = 0; idx < input.length(); ++idx) {
            char oneChar = input.charAt(idx);
            if (Character.isDigit(oneChar)) {
                while (Character.isDigit(oneChar) || oneChar == '.') {
                    ++idx;
                    if (idx == input.length()) {
                        break;
                    }
                    oneChar = input.charAt(idx);
                }//while
                input.insert(idx, " ");
                continue;

            } else if (Character.isLetter(oneChar)) {
                while (Character.isLetter(oneChar) || Character.isDigit(oneChar) || oneChar == '_') {
                    ++idx;
                    if (idx == input.length()) {
                        break;
                    }
                    oneChar = input.charAt(idx);
                }//while
                input.insert(idx, " ");

            } else if (oneChar == '(' || oneChar == ')') {
                // we need space after each parenthesis
                input.insert(++idx, " ");
                continue;

            } else if (Character.isWhitespace(oneChar)) {
                continue;

            } else if (isPunctuation(oneChar)) {
                while (isPunctuation(oneChar)) {
                    if (oneChar == '(' || oneChar == ')') {
                        // we need space before each parenthesis
                        input.insert(idx, " ");
                        break;
                    }
                    ++idx;
                    if (idx == input.length()) {
                        break;
                    }
                    oneChar = input.charAt(idx);
                }//while
                input.insert(idx, " ");
                continue;
            }
        }

        // tokenize by splitting:
        String temp = new String(input);
        tokens = temp.split(" ");

        tokenIdx = 0;
    }//tokenizeInput

    public void debugPrintTokens() {
        System.out.println("[" + input + "]");
        for (String str : tokens) {
            System.out.println("[" + str + "]");
        }
    }//debugPrintTokens

    public static boolean isPunctuation(char ch) {
        if (Character.isDigit(ch)) {
            return false;
        } else if (Character.isLetter(ch)) {
            return false;
        } else if (Character.isWhitespace(ch)) {
            return false;
        }
        return true;
    }//isPunctuation

    private void skipEmptyTokens() {
        // skip empty tokens:
        while (tokenIdx < tokens.length) {
            // is token tokens[ tokenIdx ] empty?
            if (tokens[tokenIdx].length() > 0) {
                // not empty, stop the loop:
                break;
            }
            // yes, empty, skip it:
            ++tokenIdx;
        }//while
    }//skipEmptyTokens

    public void begin() {
        tokenIdx = 0;
        skipEmptyTokens();
    }//begin

    public void next() {
        ++tokenIdx;
        skipEmptyTokens();
    }//next

    public boolean isEnd() {
        return (tokenIdx >= tokens.length);
    }//isEnd

    public String getString() {
        if (isEnd()) {
            return "";
        }
        return tokens[tokenIdx];
    }//getString

    public boolean isString(String text) {
        if (isEnd()) {
            return false;
        }
        return text.equals(tokens[tokenIdx]);
    }//isString

    public boolean isNumber() {
        if (isEnd()) {
            return false;
        }
        return Character.isDigit(tokens[tokenIdx].charAt(0));
    }//isNumber

    public boolean isIdentifier() {
        if (isEnd()) {
            return false;
        }
        return Character.isAlphabetic(tokens[tokenIdx].charAt(0));
    }//isIdentifier
    
}//class TokenStream
