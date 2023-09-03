package com.edddoubled.wikisearcher.service.index;

import com.edddoubled.wikisearcher.model.LightweightWikiPage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static com.edddoubled.wikisearcher.model.WikiFields.CONTENT;
import static com.edddoubled.wikisearcher.model.WikiFields.LINK;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class Indexer {
    Directory index;

    public void index(List<LightweightWikiPage> page) {
        SimpleAnalyzer analyzer = new SimpleAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try (final IndexWriter writer = new IndexWriter(index, config)) {

            page.stream().map(p -> {
                Document doc = new Document();
                doc.add(new StringField(LINK, p.getLink(), Field.Store.YES));
                doc.add(new TextField(CONTENT, p.getContent(), Field.Store.YES));
                return doc;
            }).forEach(document -> {
                try {
                    writer.addDocument(document);
                    writer.commit();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
