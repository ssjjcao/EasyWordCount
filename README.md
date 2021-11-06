# Word Count Project Using MapReduce
## Prerequisite
Download and unzip [HanLP data](https://file.hankcs.com/hanlp/data-for-1.7.5.zip) to the directory *src/main/resources*.

## Workflow
Given a file --**Map**-> (Split by line) for each line(i.e., sentence), tokenize the sentence using *CRFLexicalAnalyzer* in [HanLP](https://github.com/hankcs/HanLP), and then count the non-stopwords --**Reduce**-> Sum up the number of each non-stopword --> Generate word-cloud using [Kumo](https://github.com/kennycason/kumo).

## Usage
```hadoop  jar  jar_path_of_the_project  WordCount  file_path(text)/to/process  result_path(dir)/to/store```

## Example
### wordcloud of lotus
![lotus_wordcloud](https://github.com/ssjjcao/EasyWordCount/blob/master/src/main/resources/word_freq_500_lotus.png)

### wordcloud of whale
![whale_wordcloud](https://github.com/ssjjcao/EasyWordCount/blob/master/src/main/resources/word_freq_500.png)