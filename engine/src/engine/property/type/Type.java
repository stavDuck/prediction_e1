package engine.property.type;

public enum Type {
   DECIMAL {

       public Integer convert(Object value) {
           if (!(value instanceof Integer)) {
               throw new IllegalArgumentException("value " + value + " is not of a DECIMAL type (expected Integer class)");
           }
           return (Integer) value;
       }
   }, BOOLEAN {

        public Boolean convert(Object value) {
            if (!(value instanceof Boolean)) {
                throw new IllegalArgumentException("value " + value + " is not of a BOOLEAN type (expected Boolean class)");
            }
            return (Boolean) value;
        }
    }, FLOAT {

        public Float convert(Object value) {
            if (!(value instanceof Float)) {
                throw new IllegalArgumentException("value " + value + " is not of a FLOAT type (expected Float class)");
            }
            return (Float) value;
        }
    }, STRING {

        public String convert(Object value) {
            if (!(value instanceof String)) {
                throw new IllegalArgumentException("value " + value + " is not of a STRING type (expected String class)");
            }
            return (String) value;
        }
    };
    public abstract <T> T convert(Object value);
}
