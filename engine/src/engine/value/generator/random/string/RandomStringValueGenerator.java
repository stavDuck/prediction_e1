package engine.value.generator.random.string;

import engine.value.generator.ValueGeneratorFactory;
import engine.value.generator.random.AbstractRandomValueGenerator;
import engine.value.generator.random.numeric.RandomIntegerGenerator;

public class RandomStringValueGenerator extends AbstractRandomValueGenerator<String> {
    public enum Category {LETTER, NUMBER, SPECIAL_CHAR,SPACE}
    public enum SpecialChars {QUESTION_MARK, EXCLAMATION_MARK, COMMA, UNDER_SCORE, HYPHEN, DOT, OPEN_BRACKET, CLOSE_BRACKET}
    @Override
    public String generateValue() {
        int length = random.nextInt(50) + 1; //generate length 1-50
        String res = "";
        for(int i = 0; i < length; i++){
            res = res + randomChar();
        }
         return res;
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
        boolean isSmallLetter = ValueGeneratorFactory.createRandomBoolean().generateValue();
        int whatLetter = ValueGeneratorFactory.createRandomInteger(0,25).generateValue(); // English has 26 letters

        if(isSmallLetter){
            return (char)(whatLetter + 'a');
        }
        else {
            return (char)(whatLetter + 'A');
        }
    }

    private char randomSpecialChar(){
        SpecialChars randomSpecialChars = SpecialChars.values()[random.nextInt(SpecialChars.values().length)];

        switch (randomSpecialChars){
            case QUESTION_MARK: return '?';
            case EXCLAMATION_MARK: return '!';
            case COMMA: return',';
            case UNDER_SCORE: return '_';
            case HYPHEN: return '-';
            case DOT: return '.';
            case OPEN_BRACKET: return '(';
            case CLOSE_BRACKET: return  ')';
            default: return ' ';
        }
    }
}
