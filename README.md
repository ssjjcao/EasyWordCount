# Word Count Project Using MapReduce
## Prerequisite
Download and unzip [HanLP data](https://file.hankcs.com/hanlp/data-for-1.7.5.zip) to the directory src/main/resources.

## Workflow
Given a file --**Map**-> (Split by line) for each line(i.e., sentence), tokenize the sentence using *CRFLexicalAnalyzer* in [HanLP](https://github.com/hankcs/HanLP), and count the non-stopwords --**Reduce**-> Sum up the number of each non-stopword --> Generate word-cloud using [Kumo](https://github.com/kennycason/kumo).

## Example
TO ADD

## Usage
TO ADD
