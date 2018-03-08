import java.util.Dictionary;
import java.util.Enumeration;

public class ShortSentence {

    static Dictionary dictionary = new Dictionary() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Enumeration keys() {
            return null;
        }

        @Override
        public Enumeration elements() {
            return null;
        }

        @Override
        public Object get(Object key) {
            return null;
        }

        @Override
        public Object put(Object key, Object value) {
            return null;
        }

        @Override
        public Object remove(Object key) {
            return null;
        }
    };
    static String rule;

    String content;
    String primaryLocation;
    String figure;

    public void setContent(String value){
        content = value;
    }

    public void match(){

    }
}
