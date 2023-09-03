package com.edddoubled.wikisearcher.service.search;

import com.edddoubled.wikisearcher.model.LightweightWikiPage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.edddoubled.wikisearcher.model.WikiFields.CONTENT;
import static com.edddoubled.wikisearcher.model.WikiFields.LINK;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class Searcher {
    Directory index;
    StandardAnalyzer analyzer;

    public List<LightweightWikiPage> search(String queryString) {
        try (IndexReader reader = DirectoryReader.open(index)) {
            Query query = new QueryParser(CONTENT, analyzer).parse(queryString);
            int hitsPerPage = 10;
            final IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(query, hitsPerPage);

            Stream<LightweightWikiPage> documents = Stream.of(docs.scoreDocs).map(hits -> {
                int docId = hits.doc;
                LightweightWikiPage page = new LightweightWikiPage();
                try {
                    Document document = searcher.storedFields().document(docId);
                    page.setLink(document.get(LINK));
                    page.setContent(document.get(CONTENT));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }

                return page;
            });

            return documents.toList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return Collections.emptyList();
    }
}
