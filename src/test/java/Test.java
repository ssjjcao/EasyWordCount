import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.io.*;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        String s = "凌影一击之下，几乎是将整个云岚宗，搞得陷入了一种瘫痪状态，斗皇强者，居然强悍至此，恐怖如斯。";
        CRFLexicalAnalyzer a = new CRFLexicalAnalyzer();
        System.out.println(a.analyze(s));

        InputStream inputStream = WordCount.class.getResourceAsStream("/data/dictionary/stopwords.txt");        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            System.out.println(str.trim());
        }
    }
}
