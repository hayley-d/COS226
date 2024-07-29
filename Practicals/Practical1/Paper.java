import java.util.Random;

public class Paper {
    private static int numQuestions; // Static variable for number of questions
    private static char[] memo; // Static variable for memo
    

    private char[] answers;
    private int mark;

    // Static initializer block to set up memo
    static {
        setNumQuestions(5); // Set default number of questions
    }

    public Paper() {
        answers = new char[numQuestions];
        Random random = new Random();
        for (int i = 0; i < numQuestions; i++) {
            answers[i] = (char) ('a' + random.nextInt(4));
        }
        this.mark=0;
    }

    public static void setNumQuestions(int n) {
        numQuestions = n;
        memo = new char[numQuestions];
        for (int i = 0; i < numQuestions; i++) {
            memo[i] = (char) ('a' + i % 4);
        }
    }

    public char[] getAnswers() {
        return answers;
    }

    // Method to mark the paper
    public void mark() {
        int score = 0;
        for (int i = 0; i < numQuestions; i++) {
            if (answers[i] == memo[i]) {
                score++;
            }
        }
        this.mark=score;
    }
}
