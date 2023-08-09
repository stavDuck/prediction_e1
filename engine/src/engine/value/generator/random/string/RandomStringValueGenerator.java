package engine.value.generator.random.string;

import engine.value.generator.ValueGeneratorFactory;
import engine.value.generator.random.AbstractRandomValueGenerator;
import engine.value.generator.random.numeric.RandomIntegerGenerator;

public class RandomStringValueGenerator extends AbstractRandomValueGenerator<String> {
    private enum Category {LETTER, NUMBER, SPECIAL_CHAR,SPACE}
    private enum SpecialChars {QUESTION_MARK, EXCLAMATION_MARK, COMMA, UNDER_SCORE, HYPHEN, DOT, OPEN_BRACKET, CLOSE_BRACKET}
    @Override
    public String generateValue() {
      /*  int length = random.nextInt(50) + 1; //generate length 1-50
        String res = "";
        // for run lenght
            res = res + randomChar();*/
        return null;
    }

    private char randomChar(){
        Category randomCategory = Category.values()[random.nextInt(Category.values().length)];

        switch (randomCategory) {
            case LETTER:
                return randomLetter();
            case NUMBER:
                return (char) ((random.nextInt(9)) + '0');
            case SPECIAL_CHAR:
                return randomSpecialChar();
            case SPACE:
                return ' '; // For SPACE category
            default:
                return ' ';
        }
    }


    private char randomLetter(){
        // need to generate if small or big letter
        // random letter and return
        return 'A';
    }

    private char randomSpecialChar(){
        // need to random number for SpecialChars list and return the spesific char
        return 'A';
    }


}
