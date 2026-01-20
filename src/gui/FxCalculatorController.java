package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.GridPane;
import prof_AST.AstNode;
import static prof_AST.AstNode.symbolTable;
import prof_AST.Lexer;

/**
 * FXML Controller class
 *
 * @author viola
 */
public class FxCalculatorController implements Initializable {

    private Button btnEvaluate;
    @FXML
    private TextField txtExpression;
    @FXML
    private TextField txtResult;
    @FXML
    private Label txtMessage;
    @FXML
    private Button btnDeleteVar;
    @FXML
    private Button btn7;
    @FXML
    private Button btn8;
    @FXML
    private Button btn9;
    @FXML
    private Button btnDivide;
    @FXML
    private Button btn4;
    @FXML
    private Button btn5;
    @FXML
    private Button btn6;
    @FXML
    private Button btnMultiply;
    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    private Button btn3;
    @FXML
    private Button btnSubtract;
    @FXML
    private Button btn0;
    @FXML
    private Button btnEquals;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDecimal;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnBackspace;
    @FXML
    private Button btnModulo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtMessage.setVisible(false);

        Button[] buttons = {
            btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
            btnDivide, btnMultiply, btnSubtract, btnAdd, btnEquals, btnDecimal,
            btnModulo
        };

        for (Button btn : buttons) {
            btn.setOnAction(event -> {
                String buttonText = ((Button) event.getSource()).getText();
                if (buttonText.equals("X")) {
                    buttonText = "*";
                }
                addNumber(buttonText);
            });
        }
    }

    @FXML
    private void onclickEvaluate(ActionEvent event) {
        txtMessage.setVisible(false);
        StringBuilder input = new StringBuilder(txtExpression.getText());
        Lexer lexer = new Lexer(input, AstNode.symbolTable);
        AstNode result = lexer.startRule();

        if (result.type == AstNode.ERROR) {
            txtMessage.setText(result.value);
            txtMessage.setVisible(true);
            txtResult.setText("");
        } else {
            try {
                double evalResult = result.evaluate();
                txtResult.setText(Double.toString(evalResult));
            } catch (ArithmeticException ex) {
                txtMessage.setVisible(true);
                txtMessage.setText("Error: " + ex.getMessage());
            }
        }
    }//onclickEvaluate

    @FXML
    private void deleteVariable() {
        txtMessage.setVisible(true);
        String variable = txtExpression.getText().trim();
        if (symbolTable.isEmpty()) {
            txtMessage.setText("Error: No variables have been set.");
        } else if (symbolTable.containsKey(variable)) {
            symbolTable.remove(variable);
            txtMessage.setText("Removed variable " + variable + ".");
        } else if (variable.equalsIgnoreCase("all")) {
            symbolTable.clear();
            txtMessage.setText("All clear!");
        } else {
        }
    }//deleteVariable

    private void addNumber(String input) {
        String currentText = txtExpression.getText();
        txtExpression.setText(currentText + input);
        maintainFocusAndCaret(txtExpression);;
    }//addNumber

    @FXML
    private void onclickClearEntry(ActionEvent event) {
        txtExpression.setText("");
        maintainFocusAndCaret(txtExpression);
    }//onclickClearEntry

    @FXML
    private void onclickBackspace(ActionEvent event) {
        String currentText = txtExpression.getText();

        if (currentText.length() > 0) {
            txtExpression.setText(currentText.substring(0, currentText.length() - 1));
            maintainFocusAndCaret(txtExpression);
        }
    }//onclickBackspace

    private void maintainFocusAndCaret(TextField textField) {
        textField.requestFocus();
        textField.selectPositionCaret(textField.getLength());
    }//maintainFocusandCaret

}
