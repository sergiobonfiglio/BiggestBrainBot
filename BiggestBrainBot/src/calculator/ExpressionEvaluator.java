package calculator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.StringTokenizer;

public class ExpressionEvaluator {
    // initialize two stacks: operator and operand
    private static Stack operatorStack = new Stack();
    private static Stack operandStack = new Stack();

    public static int evaluateInfix(String expr) {
	try {
	    return evaluate(toPostfix(expr));
	} catch (Exception e) {
//	    e.printStackTrace();
	    return -1;
	}
    }

    // method converts infix expression to postfix notation
    private static String toPostfix(String infix) {
	StringTokenizer s = new StringTokenizer(infix);
	// divides the input into tokens for input
	String symbol, postfix = "";
	while (s.hasMoreTokens())
	// while there is input to be read
	{
	    symbol = s.nextToken();
	    // if it's a number, add it to the string
	    if (Character.isDigit(symbol.charAt(0)))
		postfix = postfix + " " + (Integer.parseInt(symbol));
	    else if (symbol.equals("("))
	    // push "("
	    {
		Character operator = new Character('(');
		operatorStack.push(operator);
	    } else if (symbol.equals(")"))
	    // push everything back to "("
	    {
		while (((Character) operatorStack.peek()).charValue() != '(') {
		    postfix = postfix + " " + operatorStack.pop();
		}
		operatorStack.pop();
	    } else
	    // print operatorStack occurring before it that have greater
	    // precedence
	    {
		while (!operatorStack.empty()
			&& !(operatorStack.peek()).equals("(")
			&& prec(symbol.charAt(0)) <= prec(((Character) operatorStack
				.peek()).charValue()))
		    postfix = postfix + " " + operatorStack.pop();
		Character operator = new Character(symbol.charAt(0));
		operatorStack.push(operator);
	    }
	}
	while (!operatorStack.empty())
	    postfix = postfix + " " + operatorStack.pop();
	return postfix;
    }

    // method evaulates postfix expression
    private static int evaluate(String postfix) {
	StringTokenizer s = new StringTokenizer(postfix);
	// divides the input into tokens for input
	int value;
	String symbol;
	while (s.hasMoreTokens()) {
	    symbol = s.nextToken();
	    if (Character.isDigit(symbol.charAt(0)))
	    // if it's a number, push it onto stack
	    {
		Integer operand = new Integer(Integer.parseInt(symbol));
		operandStack.push(operand);
	    } else // if it's an operator, operate on the previous two popped
	    // operandStack items
	    {
		int op2 = ((Integer) operandStack.pop()).intValue();
		int op1 = ((Integer) operandStack.pop()).intValue();
		int result = 0;
		switch (symbol.charAt(0)) {
		case 'x':
		case '*': {
		    result = op1 * op2;
		    break;
		}
		case '+': {
		    result = op1 + op2;
		    break;
		}
		case '-': {
		    result = op1 - op2;
		    break;
		}
		case ':':
		case '/': {
		    result = op1 / op2;
		    break;
		}
		case '%': {
		    result = op1 % op2;
		    break;
		}
		}
		Integer operand = new Integer(result);
		operandStack.push(operand);
	    }
	}
	value = ((Integer) operandStack.pop()).intValue();
	return value;
    }

    // method compares operators to establish precedence
    private static int prec(char x) {
	if (x == '+' || x == '-')
	    return 1;
	if (x == '*' || x == '/' || x == '%')
	    return 2;
	return 0;
    }

    // main method
    public static void main(String argv[]) throws IOException {
	String infix;

	// inputs stream for string of characters
	BufferedReader keyboard = new BufferedReader(new InputStreamReader(
		System.in));

	// inputs expression in infix notation
	System.out.print("Expression in infix: ");
	infix = keyboard.readLine();

	// displays postfix notation
	System.out.println("Expression in postfix:" + toPostfix(infix));

	// displays evaluated expression
	System.out.println("Evaluated expression: "
		+ evaluate(toPostfix(infix)));
    }
}
