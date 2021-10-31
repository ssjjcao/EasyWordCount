import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundaryBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.palette.ColorPalette;

public class WordCount {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final IntWritable one = new IntWritable(1);
        private final Text word = new Text();
        private final static HashSet<String> stopWords = new HashSet<String>();

        static {
            try {
                InputStream inputStream = WordCount.class.getResourceAsStream("/data/dictionary/stopwords.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stopWords.add(str.trim());
                }
            } catch (IOException ioException) {
                System.exit(-1);
            }
        }


        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            Sentence sentence = new CRFLexicalAnalyzer().analyze(line);
            List<Word> wordList = sentence.toSimpleWordList();

            for (Word thisWord : wordList) {
                String wordValue = thisWord.getValue();
                if (!stopWords.contains(wordValue)) {
                    word.set(wordValue);
                    context.write(word, one);
                }
            }
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum += value.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "wordcount");
        job.setJarByClass(WordCount.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        Path inputPath = new Path(args[0]);
        Path resultPath = new Path(args[1]);
        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, resultPath);

        job.waitForCompletion(true);

        List<WordFrequency> wordFrequencies = new ArrayList<WordFrequency>();
        InputStream inputStream = new FileInputStream(args[1] + "/part-r-00000");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            String[] splitResult = str.split("\t");
            if (splitResult.length >= 2) {
                StringBuilder thisWord = new StringBuilder();
                for (int i = 0; i < splitResult.length - 1; i++) {
                    thisWord.append(splitResult[i]);
                }
                int count = Integer.parseInt(splitResult[splitResult.length - 1]);
                if (thisWord.toString().length() >= 2 && count > 250) {
                    wordFrequencies.add(new WordFrequency(thisWord.toString(), count));
                }
            }
        }

        Dimension dimension = new Dimension(1000, 630);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setColorPalette(new ColorPalette(Color.RED, Color.MAGENTA, Color.PINK, Color.GREEN, Color.ORANGE, Color.BLUE, Color.CYAN));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        Font font = Font.createFont(Font.TRUETYPE_FONT, WordCount.class.getResourceAsStream("/HuaWenXingKai.ttf")).deriveFont(9f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackgroundColor(new Color(255, 255, 255));
        wordCloud.setBackground(new PixelBoundaryBackground(WordCount.class.getResourceAsStream("/whale.png")));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile(args[1] + "/word_freq.png");
    }
}