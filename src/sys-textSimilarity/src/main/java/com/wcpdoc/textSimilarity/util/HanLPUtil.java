package com.wcpdoc.textSimilarity.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.wcpdoc.textSimilarity.entity.Article;
import com.wcpdoc.textSimilarity.entity.CompareTask;
import com.wcpdoc.textSimilarity.entity.WordFreq;


public class HanLPUtil {
	
	

	/**
	 * 快速计算2个句子的相似度
	 * 
	 * v1.0 wjj 2021年11月19日下午5:26:29
	 * @param sentence
	 * @param compareSentence
	 * @return
	 * @throws IOException
	 * @throws TikaException double
	 */
	public static double sentenceSimilarity(String sentence, String compareSentence) throws IOException, TikaException {

		List<Term> senList = HanLP.segment(sentence);
		List<Term> comSenList = HanLP.segment(compareSentence);

		Article a1 = HanLPUtil.getArticle(sentence, senList);
		Article a2 = HanLPUtil.getArticle(compareSentence, comSenList);
		
		CompareTask task = new CompareTask(a1, a2);
		double similarity = task.execute();
		return similarity;
	}

	/**
	 *  从文件中获取数据，封装成对象。会将词频排序，在多个任务的情况下性能得到优化
	 * 
	 * v1.0 wjj 2021年11月19日下午5:26:10
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws TikaException Article
	 */
	public static Article getArticle(File file) throws IOException, TikaException {
		String text = getString(file);
		List<Term> segmentList = getSegmentList(text);
		List<WordFreq> wordFreqList = getWordFrequency(segmentList);
		// 词频从高到低排序
		wordFreqList.sort((a, b) -> Integer.compare(b.getFreq(), a.getFreq()));

		// 封装Article
		Article article = new Article();
		article.setName(file.getName());
		article.setText(text);
		article.setSegmentList(segmentList);
		article.setWordFreqList(wordFreqList);
		return article;
	}

	
	/**
	 *  获取数据，封装成对象。会将词频排序，在多个任务的情况下性能得到优化
	 * 
	 * v1.0 wjj 2021年11月18日下午5:28:10
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws TikaException Article
	 */
	public static Article getArticle(String text, List<Term> segmentList) throws IOException, TikaException {
		List<WordFreq> wordFreqList = getWordFrequency(segmentList);
		// 词频从高到低排序
		wordFreqList.sort((a, b) -> Integer.compare(b.getFreq(), a.getFreq()));

		// 封装Article
		Article article = new Article();
		article.setName(text);
		article.setText(text);
		article.setSegmentList(segmentList);
		article.setWordFreqList(wordFreqList);
		return article;
	}

	/**
	 * 从指定文件读取一整个字符串
	 * 
	 * v1.0 wjj 2021年11月19日下午5:25:47
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws TikaException String
	 */
	private static String getString(File file) throws IOException, TikaException {
		Tika tika = new Tika();
		tika.setMaxStringLength((int) file.length());
		String str = tika.parseToString(file);
		return str;
	}

	/**
	 * 将输入的字符串分词处理。
	 * 
	 * v1.0 wjj 2021年11月19日下午5:25:26
	 * @param text 文本
	 * @return List<Term> 切分后的单词
	 */
	public static List<Term> getSegmentList(String text) {
		List<Term> segmentList = HanLP.segment(text);
		// 过滤器
		segmentList.removeIf(new Predicate<Term>() {
			/**
			 * 过滤掉：长度为1的分词、标点符号
			 */
			public boolean test(Term term) {
				boolean flag = false;
				// 长度
				String real = term.word.trim();
				if (real.length() <= 1) {
					flag = true;
				}
				// 类型
				// 词性以w开头的，为各种标点符号
				if (term.nature.startsWith('w')) {
					flag = true;
				}
				// 过滤掉代码
				if (term.nature.equals(Nature.nx)) {// 字母专名
					flag = true;
				}
				return flag;
			}
		});
		return segmentList;
	}

	/**
	 * 根据分词集合统计词频
	 * 
	 * v1.0 wjj 2021年11月19日下午5:24:45
	 * @param segmentList 统计词频集合
	 * @return List<WordFreq>
	 */
	public static List<WordFreq> getWordFrequency(List<Term> segmentList) {
		// 统计词频
		Multiset<String> wordSet = HashMultiset.create();
		for (Term term : segmentList) {// 放入词汇集合
			wordSet.add(term.word);
		}
		// 从词汇集合取出单词和频次,放入词频集合
		List<WordFreq> wfList = new ArrayList<>();
		for (Entry<String> entry : wordSet.entrySet()) {
			wfList.add(new WordFreq(entry.getElement(), entry.getCount()));
		}
		return wfList;
	}

	/**
	 * 最长公共子串。
	 * 
	 * v1.0 wjj 2021年11月19日下午5:07:07
	 * @param string1
	 * @param string2
	 * @return String
	 */
	public static String getLCString(String string1, String string2) {
		int len1, len2;
		char[] str1 = string1.toCharArray();
		char[] str2 = string2.toCharArray();

		len1 = str1.length;
		len2 = str2.length;
		int maxLen = len1 > len2 ? len1 : len2;

		int[] max = new int[maxLen];// 保存最长子串长度的数组
		int[] maxIndex = new int[maxLen];// 保存最长子串长度最大索引的数组
		int[] c = new int[maxLen];

		int i, j;
		for (i = 0; i < len2; i++) {
			for (j = len1 - 1; j >= 0; j--) {
				if (str2[i] == str1[j]) {
					if ((i == 0) || (j == 0))
						c[j] = 1;
					else
						c[j] = c[j - 1] + 1;// 此时C[j-1]还是上次循环中的值，因为还没被重新赋值
				} else {
					c[j] = 0;
				}

				// 如果是大于那暂时只有一个是最长的,而且要把后面的清0
				if (c[j] > max[0]) {
					max[0] = c[j];
					maxIndex[0] = j;

					for (int k = 1; k < maxLen; k++) {
						max[k] = 0;
						maxIndex[k] = 0;
					}
				}
				// 有多个是相同长度的子串
				else if (c[j] == max[0]) {
					for (int k = 1; k < maxLen; k++) {
						if (max[k] == 0) {
							max[k] = c[j];
							maxIndex[k] = j;
							break;
						}
					}
				}
			}
		}
		// 最长子字符串
		StringBuilder lcs = new StringBuilder();
		for (j = 0; j < maxLen; j++) {
			if (max[j] > 0) {
				for (i = maxIndex[j] - max[j] + 1; i <= maxIndex[j]; i++)
					lcs.append((str1[i]));
			}
		}
		return lcs.toString();
	}
}
