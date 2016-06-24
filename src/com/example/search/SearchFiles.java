package com.example.search;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** Simple command-line based search demo. */
public class SearchFiles {
	private SearchFiles() {
	}

	/** Simple command-line based search demo. */

	public static final int HitsPerPage = 10;
    private  static   String     compressContent(String content){
    	   int pos=0;
    	   int n=0;
    	   while((pos=content.indexOf("\\n",pos))!=-1){
    		     n++;
    		     if(n==5)
    		    	 return content.substring(0, pos);
    	   }
    	   return  content;  	   
    }
	public static ArrayList<SearchResult> find(String input, int startPage)
			throws Exception {
		//String index = ".\\WebContent\\index";//D:\Users\admin\workspace\TestJsp\WebContent\index
		String index="./index";
	//	System.out.println("index:"+index);
		String field = "content";
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		String queryString = null;
		int hitsPerPage = HitsPerPage;
		IndexReader reader = IndexReader
				.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
		Analyzer analyzer = new ICTCLASAnalyzer();
		BufferedReader in = null;
		// in = new BufferedReader(input);
		int pos;
		ArrayList<SearchResult> result = new ArrayList<SearchResult>();
		if (((pos = input.indexOf('\r')) != -1)
				|| (pos = input.indexOf('\n')) != -1)
			input = input.substring(0, pos);
		QueryParser parser = new QueryParser(Version.LUCENE_31, field, analyzer);
		// while (true) {
		// if (queries == null && queryString == null) { // prompt the user
		// System.out.println("Enter query: ");
		// }
		String line = queryString != null ? queryString : input;
		if (line == null || line.length() == -1) {
			searcher.close();
			reader.close();
			return result;
		}

		line = line.trim();
		if (line.length() == 0) {
			searcher.close();
			reader.close();
			return result;
		}

		Query query = parser.parse(line);
		System.out.println("Searching for: " + query.toString(field));

		if (repeat > 0) { // repeat & time as benchmark
			Date start = new Date();
			for (int i = 0; i < repeat; i++) {
				searcher.search(query, null, 100);
			}
			Date end = new Date();
			System.out.println("Time: " + (end.getTime() - start.getTime())
					+ "ms");
		}

		doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null
				&& queryString == null, result, startPage);
		searcher.close();
		reader.close();
		return result;
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 * @throws InvalidTokenOffsetsException
	 * 
	 */
	public static void doPagingSearch(BufferedReader in,
			IndexSearcher searcher, Query query, int hitsPerPage, boolean raw,
			boolean interactive, ArrayList<SearchResult> result, int startPage)
			throws IOException, InvalidTokenOffsetsException {
		startPage = Math.max(0, startPage);
		// Collect enough docs to show 5 pages 每行10条
//		System.out.println("need " + startPage);
		TopDocs results = searcher.search(query, startPage + hitsPerPage);// 5 *
																			// hitsPerPage
		ScoreDoc[] hits = results.scoreDocs;
		if (startPage > hits.length)
			return;
		int numTotalHits = results.totalHits;
//		System.out.println("judge ：" + hits.length + " " + numTotalHits);
		System.out.println(numTotalHits + " total matching documents");

		int start = startPage;
		// int end = Math.min(numTotalHits, hitsPerPage);

		/*
		 * if (end > hits.length) { System.out .println("Only results 1 - " +
		 * hits.length + " of " + numTotalHits +
		 * " total matching documents collected.");
		 * System.out.println("Collect more (y/n) ?"); String line =
		 * in.readLine(); if (line.length() == 0 || line.charAt(0) == 'n') {
		 * break; }
		 * 
		 * hits = searcher.search(query, numTotalHits).scoreDocs; }
		 */
		int end = Math.min(hits.length, start + hitsPerPage);
//		 ICTCLASAnalyzer    analyzer = new ICTCLASAnalyzer();
		for (int i = start; i < end; i++) {
			if (raw) { // output raw format
				// System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
				System.out.println(" score=" + hits[i].score);
				// continue;
			}

			Document doc = searcher.doc(hits[i].doc);
			
			String path = doc.get("url");
			if (path != null) {
				// System.out.println((i + 1) + ". " + path);
				String title = doc.get("title");// 控制输出的形式
				// if (title != null) {
				// System.out.println("   Title: " + title);
				String content = doc.get("content");
				// if (content != null)
				// System.out.println("Content: " + content);
				SearchResult item = new SearchResult();

				item.title = i + " " + title;
				item.url = path;
				item.score = hits[i].score;
/*SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
						"<font color='red'>", "</font>");
				 
				Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
						new QueryScorer(query));
				highlighter.setTextFragmenter(new SimpleFragmenter(100));*/
			//	if (content != null) {
		/*	TokenStream tokenStream = analyzer.tokenStream("content",
							new StringReader(content));
					String highLightText = highlighter.getBestFragment(
							tokenStream, content);
					System.out.println("★高亮显示第 " + (i + 1) + " 条检索结果如下所示：");
					System.out.println(highLightText);
				item.content=highLightText;*/
				if(content.length()>403)
					content=content.substring(0,399)+"...";
				item.content=content;
					result.add(item);
				//}
			} else {
				System.out
						.println((i + 1) + ". " + "No path for this document");
			}

		}// end for
//     analyzer.close();
		// if (numTotalHits >= end) {
		// boolean quit = false;
		// while (true) {
		// System.out.print("Press ");
		// if (start - hitsPerPage >= 0) {
		// System.out.print("(p)revious page, ");
		SearchResult.hasPrePage = (start - hitsPerPage >= 0);
		// }
		// if (start + hitsPerPage < numTotalHits) {
		// System.out.print("(n)ext page, ");
		// }
		SearchResult.hasNextPage = (start + hitsPerPage < numTotalHits);
		System.out.println("hasNextPage" + SearchResult.hasNextPage);
		// System.out
		// .println("(q)uit or enter number to jump to a page.");

		// String line = in.readLine();
		/*
		 * String line=new String(); if (line.length() == 0 || line.charAt(0) ==
		 * 'q') { quit = true; break; }
		 */
		/*
		 * if (line.charAt(0) == 'p') { start = Math.max(0, start -
		 * hitsPerPage); break; } else if (line.charAt(0) == 'n') { if (start +
		 * hitsPerPage < numTotalHits) { start += hitsPerPage; } break; } else {
		 * int page = Integer.parseInt(line); if ((page - 1) * hitsPerPage <
		 * numTotalHits) { start = (page - 1) * hitsPerPage; break; } else {
		 * System.out.println("No such page"); } } // } if (quit) break;
		 */
		// end = Math.min(numTotalHits, start + hitsPerPage);

	}
}
