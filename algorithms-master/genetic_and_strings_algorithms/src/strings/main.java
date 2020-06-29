package strings;

public class main {

    static int maxMultipleSubstr = 0;

    public static String createStringOnlyWithSpaces(int length) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    // простой поиск образца в строке
    // возвращает индекс начала образца в строке или -1, если не найден
    public static int find(int M, String txt) {
        String substr = createStringOnlyWithSpaces(M);
        // i-с какого места строки  ищем
        // j-с какого места образца ищем
        for (int i=0, j = 0; i < txt.length(); ++i) {
            for ( j=0; j < substr.length() ;++j) {
                if( txt.charAt(i+j) != substr.charAt(j) ) break;
            }
            if( j == substr.length() ) return i; // образец найден
            // пока не найден, продолжим внешний цикл
        }
        // образца нет
        return txt.length();
    }

    public static void findMultipleSubstr(String substr,  String txt){
        findMultipleSubstr( substr,  txt, 0);
    }

    public static void findMultipleSubstr(String substr, String txt, int countSubstr) {
        if(maxMultipleSubstr < countSubstr) maxMultipleSubstr = countSubstr;

        // i-с какого места строки  ищем
        // j-с какого места образца ищем
        for (int i=0, j = 0; i < txt.length(); ++i) {
            for ( j=0; j < substr.length() ;++j) {
                if( txt.charAt(i+j) != substr.charAt(j) )
                {
                    if (countSubstr != 0) countSubstr=0;
                    break;
                }
            }
            if( j == substr.length()) {
                countSubstr++;
                if (i+j < txt.length()) {
                    if (txt.charAt(i + j) == substr.charAt(0)) {
                        findMultipleSubstr(substr, txt.substring(i + j), countSubstr);
                    }
                    findMultipleSubstr(substr, txt.substring(i + j), countSubstr);
                }

            }  // образец найден
            // пока не найден, продолжим внешний цикл
        }
        // образца нет
    }



    public static void main( String[] args ){
        String str = "olo 45   olo21";
        int M = 3;
        //System.out.println( find( M, str) );
        findMultipleSubstr("john", "dkfjnsjohnooojohnjohnjohn gmkl-johnjohn-johnjohnjohnjohn-");
        System.out.println( maxMultipleSubstr);
    }

}
