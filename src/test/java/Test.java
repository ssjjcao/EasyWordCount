import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.bg.PixelBoundaryBackground;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.palette.LinearGradientColorPalette;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException, FontFormatException {
        String s = "凌影一击之下，几乎是将整个云岚宗，搞得陷入了一种瘫痪状态，斗皇强者，居然强悍至此，恐怖如斯。";
        CRFLexicalAnalyzer a = new CRFLexicalAnalyzer();
        System.out.println(a.analyze(s));

        InputStream inputStream = Test.class.getResourceAsStream("/data/dictionary/stopwords.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            System.out.println(str.trim());
        }

        List<WordFrequency> wordFrequencies = new ArrayList<WordFrequency>();
        wordFrequencies.add(new WordFrequency("复旦大学", 6666));
        wordFrequencies.add(new WordFrequency("上海交通大学", 2333));
        wordFrequencies.add(new WordFrequency("计算机", 5555));
        for (int i = 0; i < 1024; i++) {
            wordFrequencies.add(new WordFrequency("安全", (int) (Math.random() * 1000)));
        }

        Dimension dimension = new Dimension(1598, 1064);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.MAGENTA, Color.PINK, Color.GREEN, Color.ORANGE, Color.BLUE, Color.CYAN));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
//        Font font = new java.awt.Font("华文行楷", Font.PLAIN, 9);
        Font font = Font.createFont(Font.TRUETYPE_FONT, Test.class.getResourceAsStream("/HuaWenXingKai.ttf")).deriveFont(9f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackgroundColor(new Color(255, 255, 255));
        wordCloud.setBackground(new PixelBoundaryBackground(Test.class.getResourceAsStream("/lotus.png")));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("src/test/test_lotus.png");
    }
}
